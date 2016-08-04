package vertx.handlers.http.examples.serverjs.impl;

import com.github.spriet2000.vertx.handlers.http.handlers.impl.ExceptionHandler;
import com.github.spriet2000.vertx.handlers.http.handlers.impl.LogHandler;
import com.github.spriet2000.vertx.handlers.http.handlers.timeout.impl.TimeoutHandler;
import com.github.spriet2000.vertx.handlers.http.impl.ServerRequestHandlers;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import vertx.handlers.http.examples.handlers.impl.ErrorHandler;
import vertx.handlers.http.examples.handlers.impl.JsHandler;
import vertx.handlers.http.examples.handlers.impl.SuccessHandler;
import vertx.handlers.http.examples.Runner;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.function.BiConsumer;

import static com.github.spriet2000.vertx.handlers.http.impl.ServerRequestHandlers.use;

@SuppressWarnings("unchecked")
public class App extends AbstractVerticle {

    private HttpServer server;
    private ScriptEngine scriptEngine;

    public static void main(String[] args) {
        Runner.run(vertx.handlers.http.examples.serverjs.impl.App.class, new VertxOptions());
    }

    @Override
    public void start(Future<Void> future) {
        scriptEngine = new ScriptEngineManager().getEngineByName("nashorn");
        try {
            scriptEngine.eval(vertx.fileSystem().readFileBlocking("static/js/nashorn-polyfill.js").toString());
        } catch (ScriptException ex) {
            future.fail(ex);
            return;
        }
        vertx.fileSystem().readFile("static/js/app.js", fileResult -> {
            try {
                scriptEngine.eval(fileResult.result().toString());
                app(future);
            } catch (ScriptException ex) {
                future.fail(ex);
            }
        });
    }

    @Override
    public void stop(Future<Void> stopFuture) {
        server.close();
        stopFuture.complete();
    }

    private void app(Future<Void> future) {

        // define error and success handlers
        ErrorHandler errorHandler = new ErrorHandler();
        SuccessHandler successHandler = new SuccessHandler();

        // common handlers
        ServerRequestHandlers<Void> common = use(
                new ExceptionHandler<>(),
                new TimeoutHandler<>(vertx),
                new LogHandler<>());

        // js handler
        BiConsumer<HttpServerRequest, Void> jsConsumer = use(common)
                .andThen(new JsHandler(scriptEngine))
                .apply(errorHandler, successHandler);

        // setup server
        server = vertx.createHttpServer()
                .requestHandler(e -> jsConsumer.accept(e, null))
                .listen(8080);

        // yeps
        future.complete();
    }
}
