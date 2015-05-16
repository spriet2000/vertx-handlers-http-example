package vertx.handlers.http.examples.foo;

import com.github.spriet2000.vertx.handlers.http.server.ServerHandlers;
import com.github.spriet2000.vertx.handlers.http.server.ext.impl.ResponseTimeHandler;
import com.github.spriet2000.vertx.handlers.http.server.ext.impl.TimeOutHandler;
import com.github.spriet2000.vertx.httprouter.Router;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpServerOptions;
import vertx.handlers.http.examples.foo.ext.bodyParser.impl.JsonBodyParser;
import vertx.handlers.http.examples.foo.ext.directory.impl.DirectoryContext;
import vertx.handlers.http.examples.foo.ext.directory.impl.DirectoryHandler;
import vertx.handlers.http.examples.foo.ext.error.impl.ErrorHandler;
import vertx.handlers.http.examples.foo.ext.log.impl.LogHandler;
import vertx.handlers.http.examples.foo.ext.statik.impl.Statik;

import static com.github.spriet2000.vertx.handlers.http.server.ServerHandlers.handlers;
import static com.github.spriet2000.vertx.httprouter.Router.router;

public class FooVerticle extends AbstractVerticle {

    public static void main(String[] args) {
        Hosting.run(FooVerticle.class, new VertxOptions());
    }

    @Override
    public void start() throws Exception {

        Router router = router();

        ServerHandlers common = handlers(new TimeOutHandler(vertx),
                new ResponseTimeHandler(), new LogHandler())
                .exceptionHandler(new ErrorHandler());

        ServerHandlers directory = handlers(common)
                .then(new DirectoryHandler(vertx, "/app/dir"));

        router.get("/dir/*filepath", (request, params) -> {
            directory.handle(request, new DirectoryContext());
        });

        ServerHandlers statik = handlers(common).then(new Statik("/app"));

        router.get("/*filepath", statik);

        ServerHandlers foo = handlers(common)
                .then(new JsonBodyParser(FooBar.class),  new FooFormHandler());

        router.post("/foobar", (request, params) -> {
            foo.handle(request, new FooContext(params));
        });

        vertx.createHttpServer(new HttpServerOptions().setPort(8080))
                .requestHandler(router).listen();
    }
}
