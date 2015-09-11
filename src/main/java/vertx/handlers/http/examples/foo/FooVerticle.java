package vertx.handlers.http.examples.foo;

import com.github.spriet2000.handlers.Handlers;
import com.github.spriet2000.vertx.handlers.http.server.ext.impl.ExceptionHandler;
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
import vertx.handlers.http.examples.foo.ext.error.impl.ErrorHandler;
import vertx.handlers.http.examples.foo.ext.statik.impl.Statik;

import static com.github.spriet2000.handlers.Handlers.compose;
import static com.github.spriet2000.vertx.httprouter.Router.router;

public class FooVerticle extends AbstractVerticle {

    Logger logger = LoggerFactory.getLogger(FooVerticle.class);

    public static void main(String[] args) {
        Hosting.run(FooVerticle.class, new VertxOptions());
    }

    @Override
    public void start() throws Exception {

        Router router = router();

        Handlers<HttpServerRequest, FooContext> common = compose(
                new ExceptionHandler<>(),
                new TimeOutHandler<>(vertx),
                new ResponseTimeHandler<>());

        Handlers.Composition<HttpServerRequest, FooContext> statik = new Handlers.Composition<>(common)
                .andThen(new Statik("/app"))
                .exceptionHandler(new ErrorHandler())
                .successHandler((e, a) -> logger.info(a));

        router.get("/*filepath", (req, params) -> {
            statik.accept(req, null);
        });

        Handlers.Composition<HttpServerRequest, FooContext> bodyParser = compose(common)
                .andThen(new JsonBodyParser(FooBar.class), new FooFormHandler())
                .exceptionHandler(new ErrorHandler())
                .successHandler((e, a) -> logger.info(a));

        router.post("/foobar", (req, params) -> {
            bodyParser.accept(req, new FooContext(params));
        });

        vertx.createHttpServer(new HttpServerOptions().setPort(8080))
                .requestHandler(router).listen();

    }

}
