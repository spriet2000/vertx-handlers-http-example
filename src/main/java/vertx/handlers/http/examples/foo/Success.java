package vertx.handlers.http.examples.foo;

import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.util.function.BiConsumer;

public class Success<A> implements BiConsumer<HttpServerRequest, A> {

    Logger logger = LoggerFactory.getLogger(Success.class);

    @Override
    public void accept(HttpServerRequest req, A throwable) {
        logger.info(String.format("Success %s %s \n", req.uri(), req.method()));
    }
}
