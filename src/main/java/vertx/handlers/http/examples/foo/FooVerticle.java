package vertx.handlers.http.examples.foo;

import com.github.spriet2000.handlers.BiHandlers;
import com.github.spriet2000.vertx.handlers.http.server.ext.impl.ExceptionHandler;
import com.github.spriet2000.vertx.handlers.http.server.ext.impl.ResponseTimeHandler;
import com.github.spriet2000.vertx.handlers.http.server.ext.impl.TimeOutHandler;
import com.github.spriet2000.vertx.httprouter.Router;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerRequest;
import vertx.handlers.http.examples.foo.ext.bodyParser.impl.JsonBodyParser;
import vertx.handlers.http.examples.foo.ext.error.impl.ErrorHandler;
import vertx.handlers.http.examples.foo.ext.log.impl.LogHandler;
import vertx.handlers.http.examples.foo.ext.statik.impl.Statik;
import vertx.handlers.http.examples.foo.ext.success.impl.SuccessHandler;

import java.util.function.BiConsumer;

import static com.github.spriet2000.handlers.BiHandlers.compose;
import static com.github.spriet2000.vertx.httprouter.Router.router;

public class FooVerticle extends AbstractVerticle {

    public static void main(String[] args) {
        Runner.run(FooVerticle.class, new VertxOptions());
    }

    @Override
    public void start() throws Exception {

        Router router = router();

        BiHandlers<HttpServerRequest, FooContext> common = compose(
                new ExceptionHandler<>(),
                new TimeOutHandler<>(vertx),
                new LogHandler<>(),
                new ResponseTimeHandler<>());

        BiConsumer<HttpServerRequest, FooContext> statik = new BiHandlers<>(common)
                .andThen(new Statik("/app"))
                .apply(new ErrorHandler(), new SuccessHandler<>());

        router.get("/*filepath", (req, params) -> {
            statik.accept(req, null);
        });

        BiConsumer<HttpServerRequest, FooContext> bodyParser = compose(common)
                .andThen(new JsonBodyParser(FooBar.class), new FooFormHandler())
                .apply(new ErrorHandler(), new SuccessHandler<>());

        router.post("/foobar", (req, params) -> {
            bodyParser.accept(req, new FooContext(params));
        });

        vertx.createHttpServer(new HttpServerOptions().setPort(8080))
                .requestHandler(router).listen();
    }
}
