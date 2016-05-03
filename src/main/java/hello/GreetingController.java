package hello;

import java.util.concurrent.atomic.AtomicLong;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RefreshScope
@RestController
@Slf4j
public class GreetingController {

    private final AtomicLong counter = new AtomicLong();

    @Value("${hello.GreetingController.message:Default Greetings from Spring}")
    private String message;

    private final String template = "%s %s!";

    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {

        log.info("GreetingController handling request");

        return new Greeting(counter.incrementAndGet(),
                String.format(template, message, name));
    }
}