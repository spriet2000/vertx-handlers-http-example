package vertx.handlers.http.examples.foo.ext.log.impl;

import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class LogHandler implements BiFunction<Consumer<Throwable>, Consumer<Object>, BiConsumer<HttpServerRequest, Object>> {

    Logger logger = LoggerFactory.getLogger(LogHandler.class);

    @Override
    public BiConsumer<HttpServerRequest, Object> apply(Consumer<Throwable> fail, Consumer<Object> next) {
        return (req, arg) -> {
            StringBuilder builder = new StringBuilder();
            builder.append(String.format("Uri %s %s \n", req.uri(), req.method()));
            builder.append("Request \n");
            req.response().headers().entries().forEach(e ->
                    builder.append(String.format("- hdr %s : %s \n",
                            e.getKey(), e.getValue())));
            req.response().headersEndHandler(x -> {
                builder.append("Response \n");
                if (!req.response().headWritten()) {

                    /*
                    req.response().headers().entries().forEach(e ->
                            builder.append(String.format("- hdr %s : %s \n",
                                    e.getKey(), e.getValue())));
                    */
                    builder.append(req.response().getStatusCode());
                }
                logger.info(builder.toString());
            });
            next.accept(arg);
        };
    }
}
