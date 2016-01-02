package vertx.handlers.http.examples.foo;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.util.function.BiConsumer;

public class Error implements BiConsumer<HttpServerRequest, Throwable> {

    Logger logger = LoggerFactory.getLogger(Error.class);

    @Override
    public void accept(HttpServerRequest request, Throwable throwable) {
        String message = String.format("Error in %s message %s %s",
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