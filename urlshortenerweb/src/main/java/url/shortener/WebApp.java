package url.shortener;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication()
@PropertySource("classpath:application.properties")
public class WebApp {

    public static void main(String[] a){
        SpringApplication.run(WebApp.class, a);
    }
}


