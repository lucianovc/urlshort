import org.junit.Assert;
import org.junit.Test;
import url.shortener.core.IUrlShortener;
import url.shortener.core.UrlShortener;

import java.util.Optional;

public class UrlTest {

    @Test
    public void test(){
        IUrlShortener url = new UrlShortener();

        String s1 = url.create("www.google.com");
        String s2 = url.create("www.facebook.com");
        String s3 = url.create("www.facebook.com");
        String s4 = url.create("www.GOOGLE.com");
        String s5 = url.create("");

        Assert.assertEquals(s1, s4);
        Assert.assertEquals(s2, s3);
        Assert.assertEquals("", s5);

    }
}
