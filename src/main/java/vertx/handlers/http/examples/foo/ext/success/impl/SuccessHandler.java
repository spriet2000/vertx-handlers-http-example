package vertx.handlers.http.examples.foo.ext.success.impl;

import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.util.function.BiConsumer;

public class SuccessHandler<A> implements BiConsumer<HttpServerRequest, A> {

    Logger logger = LoggerFactory.getLogger(SuccessHandler.class);

    @Override
    public void accept(HttpServerRequest request, A throwable) {
        logger.info(String.format("Success %s %s \n", request.uri(), request.method()));
    }
}
