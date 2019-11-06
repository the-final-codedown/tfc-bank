package fr.polytech.al.tfc.rollinghistory.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.polytech.al.tfc.account.model.Cap;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;


@Component
public class RollingHistoryProducer {

    private final Producer<String, String> producer;
    private String topic = "kafka-cap";

    public RollingHistoryProducer(Producer<String, String> producer) {
        this.producer = producer;
    }

    public void sendCap(String accountId, Cap updatedCap) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            producer.send(new ProducerRecord<>(topic, accountId, objectMapper.writeValueAsString(updatedCap))).get();
            System.out.println("Sent " + accountId + " " + updatedCap.toString());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        producer.flush();
    }
}
