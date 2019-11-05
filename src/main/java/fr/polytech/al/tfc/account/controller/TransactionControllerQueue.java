package fr.polytech.al.tfc.account.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.polytech.al.tfc.account.exception.NonExistentAccountException;
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

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private KafkaConsumer<String, String> consumerTransaction;

    @Autowired
    public TransactionControllerQueue(TransactionRepository transactionRepository,
                                      AccountRepository accountRepository,
                                      KafkaConsumer<String,String> consumerTransaction) {
        String receivingQueue = "kafka-transaction";
        List<String> topics = new ArrayList<>();
        String topic = String.format("%s", receivingQueue);
        topics.add(topic);
        consumerTransaction.subscribe(topics);
        this.consumerTransaction = consumerTransaction;
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
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
                Optional<Account> optionalSourceAccount = accountRepository.findById(transaction.getSource());
                Optional<Account> optionalDestinationAccount = accountRepository.findById(transaction.getReceiver());
                if (optionalSourceAccount.isPresent() && optionalDestinationAccount.isPresent()) {
                    transactionRepository.save(transaction);

                    Account sourceAccount = optionalSourceAccount.get();
                    sourceAccount.processTransaction(transaction, true);
                    accountRepository.save(sourceAccount);

                    Account destinationAccount = optionalDestinationAccount.get();
                    destinationAccount.processTransaction(transaction, false);
                    accountRepository.save(destinationAccount);

                } else {
                    throw new NonExistentAccountException();
                }
            }
        } catch (KafkaException | IOException e) {
            System.out.println(e);
        }
    }
}