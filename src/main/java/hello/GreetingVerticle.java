package hello;

import io.vertx.core.*;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Slf4j
@Component
public class GreetingVerticle extends AbstractVerticle {

    @Resource
    Vertx vertx;

    HttpServer httpServer;

    Router router;

    @Resource
    GreetingService greetingResponder;

    @Value("${hello.GreetingVerticle.httpServerPort:8081}")
    int httpServerPort;

    @Override
    public void start(Future<Void> startFuture) throws Exception {

        createRequestHandler();

        createHttpServer();

        startServer(startFuture);
    }

    private void createHttpServer() {

        httpServer = vertx.createHttpServer(new HttpServerOptions().setPort(httpServerPort));

        httpServer.requestHandler(router::accept);
    }

    private void createRequestHandler() {

        router = Router.router(vertx);

        Route route = router.route().path("/greeting");

        route.handler(routingContext -> {

            HttpServerRequest request = routingContext.request();

            request.endHandler(event -> {

                log.info("GreetingVerticle sending request to GreetingService");

                greetingResponder.send(new JsonObject().put("name", request.getParam("name")), (Handler<AsyncResult<Message<String>>>) reply -> {

                    request.response().end(reply.result().body());
                });
            });
        });
    }

    private void startServer(Future<Void> startFuture) {

        httpServer.listen(res -> {

            if (res.succeeded()) {

                log.info("GreetingVerticle id {}, is now listening on port {}!", context.deploymentID(), httpServerPort);
                startFuture.complete();

            } else {

                startFuture.fail(res.cause());
            }
        });

    }

    @PostConstruct
    public void postConstruct() {

        log.info("Deploying GreetingVerticle");

        vertx.deployVerticle(this);
    }
}
