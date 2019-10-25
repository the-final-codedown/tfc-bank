package fr.polytech.al.tfc.account.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.polytech.al.tfc.account.exception.NoExistingAccountException;
import fr.polytech.al.tfc.account.model.Account;
import fr.polytech.al.tfc.account.model.Transaction;
import fr.polytech.al.tfc.account.repository.AccountRepository;
import fr.polytech.al.tfc.account.repository.TransactionRepository;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.*;

@EnableScheduling
@Component
public class TransactionControllerQueue {

    private static final String app = "kafka-account";
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    KafkaConsumer<String, String> consumerTransaction;

    @Autowired
    public TransactionControllerQueue(TransactionRepository transactionRepository, AccountRepository accountRepository, @Value("${kafkabroker}") String broker) {
        String receivingTransactionQueue = "kafka-transaction";
        this.consumerTransaction = subscribe(broker, receivingTransactionQueue);
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    public KafkaConsumer<String, String> subscribe(String kafkaBrokers, String receivingQueue) {

        String topic = String.format("%s", receivingQueue);
        String groupId = String.format("%s", receivingQueue, app);

        Map<String, Object> config = new HashMap<>();
        config.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, kafkaBrokers);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);

        StringDeserializer deserializer = new StringDeserializer();

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(config, deserializer, deserializer);
        List<String> topics = new ArrayList<>();
        topics.add(topic);
        consumer.subscribe(topics);
        return consumer;
    }

    @Scheduled(fixedDelay = 5000)
    public void listenTransaction() {
        try {
            ConsumerRecords<String, String> records = consumerTransaction.poll(Duration.ofSeconds(1));
            ObjectMapper objectMapper = new ObjectMapper();
            for (ConsumerRecord<String, String> record : records) {
                String key = record.key();
                String value = record.value();
                Transaction transaction = objectMapper.readValue(value, Transaction.class);
                Optional<Account> optionalAccount = accountRepository.findById(transaction.getSource());
                if (optionalAccount.isPresent()) {
                    transactionRepository.save(transaction);
                    Account account = optionalAccount.get();
                    account.setMoney(account.getMoney() - transaction.getValue());
                    account.addPayment(transaction);
                    accountRepository.save(account);
                } else {
                    throw new NoExistingAccountException();
                }
            }
        } catch (KafkaException | IOException e) {
            System.out.println(e);
        }
    }
}