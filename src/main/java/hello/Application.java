package hello;

import io.vertx.core.Vertx;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@EnableAutoConfiguration
//@EnableDiscoveryClient
@Configuration
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public Vertx vertx() {

        Vertx vertx = Vertx.vertx();
        return vertx;
    }

}
