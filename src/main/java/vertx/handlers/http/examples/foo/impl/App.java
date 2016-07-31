package vertx.handlers.http.examples.foo.impl;

import com.github.spriet2000.vertx.handlers.http.handlers.bodyParser.impl.JsonBodyParser;
import com.github.spriet2000.vertx.handlers.http.handlers.impl.ExceptionHandler;
import com.github.spriet2000.vertx.handlers.http.handlers.impl.LogHandler;
import com.github.spriet2000.vertx.handlers.http.handlers.statik.impl.StatikFileHandler;
import com.github.spriet2000.vertx.handlers.http.handlers.timeout.impl.TimeoutHandler;
import com.github.spriet2000.vertx.handlers.http.impl.ServerRequestHandlers;
import com.github.spriet2000.vertx.httprouter.Router;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import vertx.handlers.http.examples.foo.utils.Runner;

import java.util.function.BiConsumer;

import static com.github.spriet2000.vertx.handlers.http.impl.ServerRequestHandlers.use;
import static com.github.spriet2000.vertx.httprouter.Router.router;

public class App extends AbstractVerticle {

    private String appFolder = String.format("%s%s", System.getProperty("vertx.cwd"), "/app");

    public static void main(String[] args) {
        Runner.run(App.class, new VertxOptions());
    }

    @Override
    public void start() throws Exception {

        // define error and success handlers
        ErrorHandler errorHandler = new ErrorHandler();
        SuccessHandler successHandler = new SuccessHandler();

        // common handlers
        ServerRequestHandlers<Context> common = use(
                new ExceptionHandler<>(),
                new TimeoutHandler<>(vertx),
                new LogHandler<>());

        // handler for static files
        BiConsumer<HttpServerRequest, Context> statik = use(common)
                .andThen(new StatikFileHandler<>(appFolder))
                .apply(errorHandler, successHandler);

        // handler for form processing
        BiConsumer<HttpServerRequest, Context> bodyParser = use(common)
                .andThen(new JsonBodyParser(Form.class), new FormHandler())
                .apply(errorHandler, successHandler);

        // setup router
        Router router = router()
                .get("/*filepath", (req, params) ->
                        statik.accept(req, new Context(params)))
                .post("/foobar", (req, params) ->
                        bodyParser.accept(req, new Context(params)));

        // setup server
        vertx.createHttpServer(new HttpServerOptions().setPort(8080))
                .requestHandler(router)
                .listen();
    }

    private class ErrorHandler implements BiConsumer<HttpServerRequest, Throwable> {

        Logger logger = LoggerFactory.getLogger(ErrorHandler.class);

        @Override
        public void accept(HttpServerRequest request, Throwable throwable) {
            String message = String.format("ErrorHandler in %s message %s %s",
                    request.path(),
                    throwable.toString(),
                    request.response().ended());

            String html = String.format("<html><head></head><body>%s</body></html>", message);

            request.response().headers().set(HttpHeaders.CONTENT_TYPE, "text/html");
            request.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
            request.response().end(html);

            logger.error(message);
        }
    }

    private class SuccessHandler implements BiConsumer<HttpServerRequest, Context> {

        Logger logger = LoggerFactory.getLogger(SuccessHandler.class);

        @Override
        public void accept(HttpServerRequest req, Context context) {
            logger.info(String.format("SuccessHandler %s %s \n", req.uri(), req.method()));
        }
    }
}
