package url.shortener.core;

import com.google.common.hash.Hashing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;

@Component
public class UrlShortener implements IUrlShortener {

    private static Logger log = LoggerFactory.getLogger(UrlShortener.class);


    @Override
    public String create(String lurl){
        log.info("create: " + lurl);
        lurl = lurl.toLowerCase();

        int id = Hashing.murmur3_32().hashString(lurl, Charset.defaultCharset()).asInt();
        String surl = IdEncoder.encode(id);
        return surl;
    }

    @Override
    public void add(String surl, String lurl) {
        log.info("add: " + surl + " - " + lurl);
    }
}
