package vertx.handlers.http.examples.foo.ext.log.impl;

import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class LogHandler<A> implements BiFunction<Consumer<Throwable>, Consumer<Object>, BiConsumer<HttpServerRequest, A>> {

    Logger logger = LoggerFactory.getLogger(LogHandler.class);

    @Override
    public BiConsumer<HttpServerRequest, A> apply(Consumer<Throwable> fail, Consumer<Object> next) {
        return (req, arg) -> {
            StringBuilder builder = new StringBuilder();

            builder.append(String.format("Uri %s %s \n", req.uri(), req.method()));
            builder.append("Request \n");

            req.response().headers().entries().forEach(e ->
                    builder.append(String.format("- hdr %s : %s \n",
                            e.getKey(), e.getValue())));

            HttpServerRequest rew = req;

            req.response().headersEndHandler(x -> {


                logger.info("test " + rew.toString());

                builder.append("Response \n");

                if (!req.response().headWritten()) {
                    req.response().headers().entries().forEach(e ->
                            builder.append(String.format("- hdr %s : %s \n",
                                    e.getKey(), e.getValue())));
                    builder.append(req.response().getStatusCode());
                }

                logger.info(builder.toString());

            });
            next.accept(arg);
        };
    }
}
