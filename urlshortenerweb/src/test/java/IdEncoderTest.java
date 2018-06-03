import org.junit.Assert;
import org.junit.Test;
import url.shortener.core.IdEncoder;

public class IdEncoderTest {

    @Test
    public void test1(){
        Assert.assertEquals("", IdEncoder.encode(0));
        Assert.assertEquals(0, IdEncoder.decode(""));
        Assert.assertEquals("b", IdEncoder.encode(1));
        Assert.assertEquals(1, IdEncoder.decode("b"));
        Assert.assertEquals("c", IdEncoder.encode(2));
        Assert.assertEquals("@", IdEncoder.encode(63));
        Assert.assertEquals("@", IdEncoder.encode(-63));
        Assert.assertEquals("ba", IdEncoder.encode(64));
        Assert.assertEquals(64, IdEncoder.decode("ba"));
    }
}
