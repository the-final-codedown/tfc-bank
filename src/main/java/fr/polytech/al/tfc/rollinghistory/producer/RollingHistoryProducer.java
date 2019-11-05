package fr.polytech.al.tfc.rollinghistory.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.polytech.al.tfc.account.model.Cap;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class RollingHistoryProducer {

    private String topic;
    private Producer<String, String> producer;

    public RollingHistoryProducer(@Value("${kafkabroker}") String broker) {
        //todo warning kafka broker here
        String customer = "kafka-cap";
        this.topic = String.format("%s", customer);

        Properties props = new Properties();
        props.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, broker);
        props.put("acks", "all");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        this.producer = new KafkaProducer<>(props);
    }

    public void sendCap(String accountId, Cap updatedCap) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        producer.send(new ProducerRecord<>(topic, accountId, objectMapper.writeValueAsString(updatedCap)));
        System.out.println("statistic sent by statisticService to fraudService");
        producer.flush();
        //producer.close();
    }
}
