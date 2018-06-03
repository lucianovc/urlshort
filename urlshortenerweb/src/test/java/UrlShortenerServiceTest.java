import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import url.shortener.core.IUrlShortener;
import url.shortener.messaging.KafkaClientConsumer;
import url.shortener.messaging.KafkaClientProducer;
import url.shortener.messaging.Message;
import url.shortener.service.UrlShortenerService;

@RunWith(SpringJUnit4ClassRunner.class)
public class UrlShortenerServiceTest {

    @InjectMocks
    UrlShortenerService service;

    @Mock
    KafkaClientConsumer consumer;

    @Mock
    KafkaClientProducer producer;

    @Mock
    StringRedisTemplate redis;

    @Mock
    IUrlShortener encoder;



    @Test
    public void testEmpty() throws Exception {
        Mockito.when(redis.opsForValue()).thenReturn(null);
        String result = service.create("");
        Mockito.verifyZeroInteractions(producer);
        Mockito.verifyZeroInteractions(redis);
        Mockito.verifyZeroInteractions(encoder);
        Assert.assertEquals("", result);
    }

    @Test
    public void testNonEmptyCreate() throws Exception {
        Mockito.when(redis.opsForValue()).thenReturn(Mockito.mock(ValueOperations.class));
        String result = service.create("www.google.es");
        Mockito.verify(producer).sendMessage(Mockito.any(Message.class));
        Mockito.verify(redis).opsForValue();
        Mockito.verify(encoder).create("www.google.es");
    }

    @Test
    public void testLookup() throws Exception {
        Mockito.when(redis.opsForValue()).thenReturn(Mockito.mock(ValueOperations.class));
        service.lookup("short");
        Mockito.verifyZeroInteractions(producer);
        Mockito.verify(redis).opsForValue();
        Mockito.verifyZeroInteractions(encoder);
        Mockito.verify(consumer);
    }
}
