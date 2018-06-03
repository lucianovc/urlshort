package url.shortener.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import url.shortener.messaging.KafkaClientConsumer;
import url.shortener.messaging.KafkaClientProducer;
import url.shortener.messaging.Message;
import url.shortener.service.UrlShortenerService;

import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.CompletableFuture;

import static url.shortener.messaging.JacksonConfiguration.OBJ_MAPPER;

/**
 * Definition of REST services : create and lookup
 */
@RestController
public class UrlShortenerWebService {

    private static Logger log = LoggerFactory.getLogger(UrlShortenerWebService.class);
    private final UrlShortenerService service;

    public UrlShortenerWebService(UrlShortenerService service){
        this.service = service;
    }

    @RequestMapping("/createUrl")
    public String create(@RequestParam String url) throws Exception {
        return service.create(url);
    }

    @RequestMapping("/lookupUrl")
    public String lookup(@RequestParam String url) throws Exception {
        return service.lookup(url);
    }

    @RequestMapping(value = "/")
    public String home() {
        return "ping";
    }
}


