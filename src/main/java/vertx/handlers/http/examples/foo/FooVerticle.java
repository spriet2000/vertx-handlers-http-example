package vertx.handlers.http.examples.foo;

import com.github.spriet2000.vertx.handlers.http.server.RequestHandlers;
import com.github.spriet2000.vertx.handlers.http.server.ext.impl.ResponseTimeHandler;
import com.github.spriet2000.vertx.handlers.http.server.ext.impl.TimeOutHandler;
import com.github.spriet2000.vertx.httprouter.Router;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import vertx.handlers.http.examples.foo.ext.bodyParser.Body;
import vertx.handlers.http.examples.foo.ext.bodyParser.impl.JsonBodyParser;
import vertx.handlers.http.examples.foo.ext.directory.Directory;
import vertx.handlers.http.examples.foo.ext.directory.impl.DirectoryContext;
import vertx.handlers.http.examples.foo.ext.directory.impl.DirectoryHandler;
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
        BiConsumer<Object, Object> success = (e, a) -> logger.info(a);

        // common
        RequestHandlers<HttpServerRequest, Object> common =
                new RequestHandlers<>(exception, success);
        common.andThen(new TimeOutHandler(vertx), new ResponseTimeHandler());

        // directory todo
        RequestHandlers<HttpServerRequest, Directory> directory =
                new RequestHandlers<>(exception, success);
        directory.andThen(new DirectoryHandler(vertx, "/app/dir"));

        // configure router
        Router router = router();

        // directory listing
        router.get("/dir/*filepath", (req, params) -> {
            common.handle(req, null);
            directory.handle(req, new DirectoryContext());
        });

        // statik serving
        RequestHandlers<HttpServerRequest, Object> statik =
                new RequestHandlers<>(exception, success);
        statik.andThen(new Statik("/app"));

        router.get("/*filepath", (req, params) -> {
            common.handle(req, null);
            statik.handle(req, null);
        });

        // body parser
        RequestHandlers<HttpServerRequest, Body<FooBar>> bodyParser =
                new RequestHandlers<>(exception, success);
        bodyParser.andThen(new JsonBodyParser(FooBar.class), new FooFormHandler());

        router.post("/foobar", (req, params) -> {
            common.handle(req, null);
            bodyParser.handle(req, new FooContext(params));
        });

        vertx.createHttpServer(new HttpServerOptions().setPort(8080))
                .requestHandler(router)
                .listen();

    }
}
