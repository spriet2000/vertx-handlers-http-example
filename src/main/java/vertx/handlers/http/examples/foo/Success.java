package vertx.handlers.http.examples.foo;

import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.util.function.BiConsumer;

public class Success<A> implements BiConsumer<HttpServerRequest, A> {

    Logger logger = LoggerFactory.getLogger(Success.class);

    @Override
    public void accept(HttpServerRequest req, A throwable) {
        if (req != null && !req.isEnded()) {
            req.response().end();
        }
        logger.info(String.format("Success %s %s \n", req.uri(), req.method()));
    }
}
