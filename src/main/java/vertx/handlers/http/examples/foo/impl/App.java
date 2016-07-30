package vertx.handlers.http.examples.foo.impl;

import com.github.spriet2000.vertx.handlers.core.http.ServerRequestHandlers;
import com.github.spriet2000.vertx.handlers.extensions.basic.impl.ExceptionHandler;
import com.github.spriet2000.vertx.handlers.extensions.basic.impl.LogHandler;
import com.github.spriet2000.vertx.handlers.extensions.bodyParser.impl.JsonBodyParser;
import com.github.spriet2000.vertx.handlers.extensions.statik.impl.StatikFileHandler;
import com.github.spriet2000.vertx.handlers.extensions.timeout.impl.TimeoutHandler;
import com.github.spriet2000.vertx.httprouter.Router;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerRequest;
import vertx.handlers.http.examples.foo.utils.Runner;

import java.util.function.BiConsumer;

import static com.github.spriet2000.vertx.handlers.core.http.ServerRequestHandlers.build;
import static com.github.spriet2000.vertx.httprouter.Router.router;

public class App extends AbstractVerticle {

    public static void main(String[] args) {
        Runner.run(App.class, new VertxOptions());
    }

    @Override
    public void start() throws Exception {

        String appFolder = String.format("%s%s", System.getProperty("vertx.cwd"), "/app");

        Router router = router();

        ServerRequestHandlers<Context> common = build(
                new ExceptionHandler<>(),
                new TimeoutHandler<>(vertx),
                new LogHandler<>());

        BiConsumer<HttpServerRequest, Context> statik = build(common)
                .andThen(new StatikFileHandler<>(appFolder))
                .apply(new ErrorResponse(), new SuccessHandler<>());

        router.get("/*filepath", (req, params) -> {
            statik.accept(req, new Context(params));
        });

        BiConsumer<HttpServerRequest, Context> bodyParser = build(common)
                .andThen(new JsonBodyParser(Form.class), new FormHandler())
                .apply(new ErrorResponse(), new SuccessHandler<>());

        router.post("/foobar", (req, params) -> {
            bodyParser.accept(req, new Context(params));
        });

        vertx.createHttpServer(new HttpServerOptions().setPort(8080))
                .requestHandler(router)
                .listen();
    }
}
