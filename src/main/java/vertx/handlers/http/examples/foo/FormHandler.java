package vertx.handlers.http.examples.foo;


import io.vertx.core.http.HttpServerRequest;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class FormHandler<A> implements BiFunction<BiConsumer<HttpServerRequest, Throwable>, BiConsumer<HttpServerRequest, Object>,
        BiConsumer<HttpServerRequest, Context>> {

    @Override
    public BiConsumer<HttpServerRequest, Context> apply(BiConsumer<HttpServerRequest, Throwable> fail, BiConsumer<HttpServerRequest, Object> next) {
        return (req, arg) -> {
            req.response().end(String.format("Result from server. \nParsed body to type %s.\nfoo: %s",
                    arg.getClass().getSimpleName(),
                    arg.body().foo));
            next.accept(req, arg);
        };
    }
}
