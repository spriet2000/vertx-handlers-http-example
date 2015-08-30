package vertx.handlers.http.examples.foo;

import com.github.spriet2000.handlers.Handlers;
import com.github.spriet2000.vertx.handlers.http.server.ext.impl.ResponseTimeHandler;
import com.github.spriet2000.vertx.handlers.http.server.ext.impl.TimeOutHandler;
import com.github.spriet2000.vertx.httprouter.Router;
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

        // handling
        BiConsumer<Object, Throwable> exception = (e, a) -> logger.error(a);
        BiConsumer<HttpServerRequest, Object> success = (e, a) -> logger.info(a);

        // common
        Handlers<HttpServerRequest> common = new Handlers<>(
                new TimeOutHandler(vertx), new ResponseTimeHandler());

        // configure router
        Router router = router();

        // statik serving
        Handlers<HttpServerRequest> statik =  new Handlers<>();
        statik.andThen(new Statik("/app"));

        router.get("/*filepath", (req, params) -> {
            common.accept(req, null, exception, success);
            statik.accept(req, null, exception, success);
        });

        // body parser
        Handlers<HttpServerRequest> bodyParser = new Handlers<HttpServerRequest>(
                new JsonBodyParser(FooBar.class), new FooFormHandler());

        router.post("/foobar", (req, params) -> {
            common.accept(req, null, exception, success);
            bodyParser.accept(req, new FooContext(params), exception, success);
        });

        vertx.createHttpServer(new HttpServerOptions().setPort(8080))
                .requestHandler(router)
                .listen();

    }
}
