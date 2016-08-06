package vertx.handlers.http.examples.serverjs.impl;

import com.github.spriet2000.vertx.handlers.http.handlers.impl.ExceptionHandler;
import com.github.spriet2000.vertx.handlers.http.handlers.impl.LogHandler;
import com.github.spriet2000.vertx.handlers.http.handlers.timeout.impl.TimeoutHandler;
import com.github.spriet2000.vertx.handlers.http.impl.ServerRequestHandlers;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpServer;
import vertx.handlers.http.examples.Runner;
import vertx.handlers.http.examples.handlers.impl.ErrorHandler;
import vertx.handlers.http.examples.handlers.impl.SuccessHandler;

import static com.github.spriet2000.vertx.handlers.http.impl.ServerRequestHandlers.use;

@SuppressWarnings("unchecked")
public class App extends AbstractVerticle {

    private HttpServer server;
    private ErrorHandler errorHandler = new ErrorHandler();
    private SuccessHandler successHandler = new SuccessHandler();

    public static void main(String[] args) {
        Runner.run(vertx.handlers.http.examples.serverjs.impl.App.class, new VertxOptions());
    }

    @Override
    public void start(Future<Void> future) {

        // setup server
        server = vertx.createHttpServer()
                .requestHandler(e -> common().andThen(new JsHandler(vertx),
                        (f, n) -> (req, a) -> {
                            // js handler, just printing hello world from javascript
                            req.response().end(a.toString());
                        })
                        .apply(errorHandler, successHandler).accept(e, null))
                .listen(8080);

        // yeps
        future.complete();
    }

    @Override
    public void stop(Future<Void> stopFuture) {
        server.close();
        stopFuture.complete();
    }


    private ServerRequestHandlers<Object> common(){
        return use(
                new ExceptionHandler<>(),
                new TimeoutHandler<>(vertx),
                new LogHandler<>());
    }
}
