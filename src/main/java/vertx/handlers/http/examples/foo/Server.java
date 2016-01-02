package vertx.handlers.http.examples.foo;

import com.github.spriet2000.handlers.BiHandlers;
import com.github.spriet2000.vertx.handlers.http.server.ext.bodyParser.impl.JsonBodyParser;
import com.github.spriet2000.vertx.handlers.http.server.ext.impl.ExceptionHandler;
import com.github.spriet2000.vertx.handlers.http.server.ext.impl.LogHandler;
import com.github.spriet2000.vertx.handlers.http.server.ext.impl.ResponseTimeHandler;
import com.github.spriet2000.vertx.handlers.http.server.ext.statik.impl.StatikFileHandler;
import com.github.spriet2000.vertx.handlers.http.server.ext.timeout.impl.TimeoutHandler;
import com.github.spriet2000.vertx.handlers.http.server.utils.Runner;
import com.github.spriet2000.vertx.httprouter.Router;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerRequest;


import java.util.function.BiConsumer;

import static com.github.spriet2000.handlers.BiHandlers.compose;
import static com.github.spriet2000.vertx.httprouter.Router.router;

public class Server extends AbstractVerticle {

    public static void main(String[] args) {
        Runner.run(Server.class, new VertxOptions());
    }

    @Override
    public void start() throws Exception {

        Router router = router();

        BiHandlers<HttpServerRequest, Context> common = compose(
                new ExceptionHandler<>(),
                new TimeoutHandler<>(vertx),
                new LogHandler<>());

        String appFolder =  String.format("%s%s", System.getProperty("vertx.cwd"), "/app");

        BiConsumer<HttpServerRequest, Context> statik = new BiHandlers<>(common)
                .andThen(new StatikFileHandler<>(appFolder))
                .apply(new Error(), new Success<>());

        router.get("/*filepath", (req, params) -> {
            statik.accept(req, new Context(params));
        });

        BiConsumer<HttpServerRequest, Context> bodyParser = compose(common)
                .andThen(new JsonBodyParser(Form.class), new FormHandler())
                .apply(new Error(), new Success<>());

        router.post("/foobar", (req, params) -> {
            bodyParser.accept(req, new Context(params));
        });

        vertx.createHttpServer(new HttpServerOptions().setPort(8080))
                .requestHandler(router)
                .listen();
    }
}
