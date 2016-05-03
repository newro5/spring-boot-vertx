package hello;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.*;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.MessageCodec;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Component
public class GreetingService extends AbstractVerticle {

    public final String greetingDestination = "hello.GreetingVerticle";

    @Value("${hello.GreetingService.initId:1000}")
    private long initId;

    private AtomicLong responseId;

    private final String template = "%s %s!";

    @Value("${hello.GreetingService.message: Default Greetings from Vertx}")
    private String message;

    @Resource
    Vertx vertx;

    public <T> void send(Object message, Handler<AsyncResult<Message<T>>> replyHandler) {

        vertx.eventBus().send(greetingDestination, message, replyHandler);
    }


    @Override
    public void start(Future<Void> startFuture) throws Exception {

        createConsumer();
    }

    private void createConsumer() {

        vertx.eventBus().registerDefaultCodec(Greeting.class, new GreetingCodec());

        vertx.eventBus().consumer(greetingDestination, new Handler<Message<JsonObject>>() {

            @Override
            public void handle(Message<JsonObject> event) {

                log.info("GreetingService handling message");

                event.reply(new Greeting(responseId.incrementAndGet(),
                        String.format(template, message, event.body().getString("name", "UNKNOWN"))));
            }
        });

    }

    @PostConstruct
    public void postConstruct() {

        responseId = new AtomicLong(initId);

        log.info("Deploying GreetingService");

        vertx.deployVerticle(this, new DeploymentOptions().setWorker(true));
    }

    class GreetingCodec implements MessageCodec<Greeting, String> {

        ObjectMapper mapper = new ObjectMapper();

        @Override
        public void encodeToWire(Buffer buffer, Greeting greeting) {

            try {

                buffer.appendBytes(mapper.writeValueAsBytes(greeting));

            } catch (JsonProcessingException e) {

                throw new RuntimeException("Error encoding Greeting message", e);
            }

        }

        @Override
        public String decodeFromWire(int pos, Buffer buffer) {

            try {

                return transform(mapper.readValue(buffer.getBytes(), Greeting.class));

            } catch (IOException e) {

                throw new RuntimeException("Error decoding Greeting message", e);
            }
        }

        @Override
        public String transform(Greeting greeting) {

            try {

                return mapper.writeValueAsString(greeting);

            } catch (JsonProcessingException e) {

                throw new RuntimeException("Error trandforming Greeting message", e);
            }
        }

        @Override
        public String name() {

            return "Greeting Codec";
        }

        @Override
        public byte systemCodecID() {

            return -1;
        }
    }
}
