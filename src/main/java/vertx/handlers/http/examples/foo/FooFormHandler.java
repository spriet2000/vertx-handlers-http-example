package vertx.handlers.http.examples.foo;


import io.vertx.core.http.HttpServerRequest;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class FooFormHandler<A> implements BiFunction<Consumer<Throwable>, Consumer<Object>,
        BiConsumer<HttpServerRequest, FooContext>> {

    @Override
    public BiConsumer<HttpServerRequest, FooContext> apply(Consumer<Throwable> fail, Consumer<Object> next) {
        return (req, arg) -> {
            req.response().end(String.format("Result from server. \nParsed body to type %s.\nfoo: %s",
                    arg.getClass().getSimpleName(),
                    arg.body().foo));
            next.accept(arg);
        };
    }
}
