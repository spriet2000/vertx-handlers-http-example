package vertx.handlers.http.examples.foo;

import com.github.spriet2000.handlers.Handlers;
import com.github.spriet2000.vertx.handlers.http.server.ext.impl.ResponseTimeHandler;
import com.github.spriet2000.vertx.handlers.http.server.ext.impl.TimeOutHandler;
import com.github.spriet2000.vertx.httprouter.Router;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import vertx.handlers.http.examples.foo.ext.bodyParser.impl.JsonBodyParser;
import vertx.handlers.http.examples.foo.ext.statik.impl.Statik;

import java.util.function.BiConsumer;

import static com.github.spriet2000.vertx.httprouter.Router.router;

public class FooVerticle extends AbstractVerticle {

    Logger logger = LoggerFactory.getLogger(FooVerticle.class);

    public static void main(String[] args) {
        Hosting.run(FooVerticle.class, new VertxOptions());
    }

    @Override
    public void start() throws Exception {

        // configure router
        Router router = router();

        // error handling
        BiConsumer<Object, Throwable> exception = (e, a) -> {
            ((HttpServerRequest) e).response().setStatusCode(
                    HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
            ((HttpServerRequest) e).response().end(a.toString());
            logger.error(a);
        };

        // success handling
        BiConsumer<HttpServerRequest, Object> success = (e, a) -> logger.info(a);

        // common handlers
        Handlers<HttpServerRequest> common = new Handlers<>(
                 new TimeOutHandler(vertx),
                 new ResponseTimeHandler()
        );

        // statik serving
        Handlers<HttpServerRequest> statik =  new Handlers<>(
                new Statik("/app"));

        // body parser
        Handlers<HttpServerRequest> bodyParser = new Handlers<>(
                new JsonBodyParser(FooBar.class),
                new FooFormHandler());

        router.get("/*filepath", (req, params) -> {
            common.accept(req, null, exception, (event, arg) -> {
                statik.accept(event, arg, exception, success);
            });
        });

        router.post("/foobar", (req, params) -> {
            common.accept(req, null, exception, (event, arg) -> {
                bodyParser.accept(event, new FooContext(params), exception, success);
            });
        });

        vertx.createHttpServer(new HttpServerOptions().setPort(8080))
                .requestHandler(router)
                .listen();

    }
}
