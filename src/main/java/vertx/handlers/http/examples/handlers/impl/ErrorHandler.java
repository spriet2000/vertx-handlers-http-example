package vertx.handlers.http.examples.handlers.impl;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.util.function.BiConsumer;

public class ErrorHandler implements BiConsumer<HttpServerRequest, Throwable> {

    private Logger logger = LoggerFactory.getLogger(ErrorHandler.class);

    @Override
    public void accept(HttpServerRequest request, Throwable throwable) {
        String message = String.format("ErrorHandler in %s message %s",
                request.path(), throwable.toString());

        String html = String.format("<html><head></head><body>%s</body></html>", message);

        request.response().headers().set(HttpHeaders.CONTENT_TYPE, "text/html");
        request.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
        request.response().end(html);

        logger.error(message);
    }
}
