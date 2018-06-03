package url.shortener.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import url.shortener.core.IUrlShortener;
import url.shortener.messaging.KafkaClientConsumer;
import url.shortener.messaging.KafkaClientProducer;
import url.shortener.messaging.Message;

import static url.shortener.messaging.JacksonConfiguration.OBJ_MAPPER;

/**
 * URL Shortener service.
 *
 * Communicates with the URL enconding unit, Redis and Kafka.
 */
@Service
public class UrlShortenerService {

    private static Logger log = LoggerFactory.getLogger(UrlShortenerService.class);
    private final KafkaClientConsumer consumer;
    private final KafkaClientProducer producer;
    private final StringRedisTemplate redis;

    private final IUrlShortener urlShortener;

    public UrlShortenerService(KafkaClientConsumer consumer, KafkaClientProducer producer, IUrlShortener urlShortener, StringRedisTemplate redis){
        this.consumer = consumer;
        this.producer = producer;
        this.urlShortener = urlShortener;
        this.redis = redis;
        this.consumer.start((s) -> {
            try {
                add(OBJ_MAPPER.readValue(s, Message.class));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void add(Message message) throws Exception {
        log.info("add ", message);
        redis.opsForValue().set(message.getShortUrl(), message.getLongUrl());
    }

    public String create(String url) throws Exception {
        log.info("create ", url);
        if (url.equalsIgnoreCase("")) return "";
        String shortUrl = urlShortener.create(url);
        redis.opsForValue().set(shortUrl, url);
        producer.sendMessage(new Message(shortUrl, url));
        return shortUrl;
    }

    public String lookup(String url) throws Exception {
        log.info("lookup ", url);
        if (url.equalsIgnoreCase("")) return "";
        return redis.opsForValue().get(url);
    }

}
