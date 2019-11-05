package fr.polytech.al.tfc.rollinghistory.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.polytech.al.tfc.account.model.Cap;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;


@Component
public class RollingHistoryProducer {

    private String topic = "kafka-cap";
    private final Producer<String, String> producer;

    public RollingHistoryProducer(Producer<String, String> producer) {
        this.producer = producer;
    }

    public void sendCap(String accountId, Cap updatedCap) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        producer.send(new ProducerRecord<>(topic, accountId, objectMapper.writeValueAsString(updatedCap)));
        producer.flush();
    }
}
