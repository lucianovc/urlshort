package url.shortener.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Properties;
import java.util.concurrent.Future;

import static url.shortener.messaging.JacksonConfiguration.OBJ_MAPPER;

@Component
public class KafkaClientProducer {

    private final static String TOPIC = "url-shortener";
    private final static String BOOTSTRAP_SERVERS =
            "localhost:9092,localhost:9093";
    private final Producer<Long, String> producer;

    @Value("${spring.application.name}")
    private String name;

    public KafkaClientProducer(){
        this.producer = createProducer();
    }

    private Producer<Long, String> createProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                BOOTSTRAP_SERVERS);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, KafkaClientProducer.class.getName());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                LongSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class.getName());
        return new KafkaProducer<>(props);
    }

    public Future<RecordMetadata> sendMessage(final Message msg) throws Exception {
        final ProducerRecord<Long, String> record =
                new ProducerRecord(TOPIC, null, null, OBJ_MAPPER.writeValueAsString(msg));

        return producer.send(record);
    }
}
