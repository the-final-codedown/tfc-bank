package fr.polytech.al.tfc.account.config;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class KafkaConfig {
    private static final String app = "kafka-account";
    @Bean
    public KafkaConsumer<String, String> consumer(@Value("${kafkabroker}") String kafkaBrokers){
        String receivingQueue = "kafka-transaction";
        String topic = String.format("%s", receivingQueue);
        String groupId = String.format("%s %s", receivingQueue, app);

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
}
