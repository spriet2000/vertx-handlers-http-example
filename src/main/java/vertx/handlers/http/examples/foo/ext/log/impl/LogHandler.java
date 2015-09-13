package vertx.handlers.http.examples.foo.ext.log.impl;

import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class LogHandler<A> implements BiFunction<Consumer<Throwable>, Consumer<A>, BiConsumer<HttpServerRequest, A>> {

    Logger logger = LoggerFactory.getLogger(LogHandler.class);

    @Override
    public BiConsumer<HttpServerRequest, A> apply(Consumer<Throwable> fail, Consumer<A> next) {
        return (req, arg) -> {
            StringBuilder builder1 = new StringBuilder();
            builder1.append(String.format("Request uri %s %s \n", req.uri(), req.method()));
            req.headers().entries().forEach(e ->
                    builder1.append(String.format("- hdr %s : %s \n", e.getKey(), e.getValue())));
            logger.info(builder1.toString());
            req.endHandler(x -> {
                StringBuilder builder2 = new StringBuilder();
                builder2.append(String.format("Response %s \n", req.response().getStatusCode()));
                req.response().headers().entries().forEach(e ->
                        builder2.append(String.format("- hdr %s : %s \n", e.getKey(), e.getValue())));
                logger.info(builder2.toString());
            });
            next.accept(arg);
        };
    }
}
