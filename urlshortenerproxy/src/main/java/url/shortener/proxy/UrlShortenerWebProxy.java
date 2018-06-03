package url.shortener.proxy;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Load Balancer :
 * Forwards requests to servers based on round robin scheme for the sake of scalability.
 */
@SpringBootApplication
@RestController
@RibbonClient(name = "url-shortener", configuration = UrlShortenerWebProxyConfiguration.class)
public class UrlShortenerWebProxy {

    @LoadBalanced
    @Bean
    RestTemplate restTemplate(){
        return new RestTemplate();
    }

    @Autowired
    RestTemplate restTemplate;

    @RequestMapping("/create")
    public ResponseEntity<String> create(@RequestParam String url) {
        return getForEntity(url, "http://url-shortener/createUrl");
    }

    @RequestMapping(value = "/lookup")
    public ResponseEntity<String> lookup(@RequestParam String url, HttpServletResponse resp) throws IOException {
        ResponseEntity<String> response = getForEntity(url, "http://url-shortener/lookupUrl");
        if (response != null && !response.getBody().isEmpty()) {
            resp.sendRedirect(response.getBody());
        } else {
            resp.sendError(HttpStatus.SC_NOT_FOUND, "Short URL not found");
        }
        return response;
    }

    private ResponseEntity<String> getForEntity(@RequestParam String url, String httpUrl) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(httpUrl).queryParam("url", url);
        return this.restTemplate.getForEntity(builder.build().encode().toUri(), String.class);
    }

}
