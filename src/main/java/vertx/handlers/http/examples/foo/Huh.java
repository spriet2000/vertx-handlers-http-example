package vertx.handlers.http.examples.foo;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

@SuppressWarnings("unchecked")
public class Huh extends AbstractVerticle {

    Logger logger = LoggerFactory.getLogger(Huh.class);

    public static void main(String[] args) {
        Hosting.run(Huh.class, new VertxOptions());
    }

    @Override
    public void start() throws Exception {

        vertx.createHttpServer(new HttpServerOptions().setPort(8080))
                .requestHandler(req -> {
                    req.response().sendFile("/not/existing/path", event -> {
                        if (event.failed()) {
                            req.response().end("failed");
                        }
                    });
                }).listen();

    }




}
