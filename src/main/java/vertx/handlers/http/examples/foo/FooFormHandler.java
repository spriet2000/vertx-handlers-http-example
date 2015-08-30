package vertx.handlers.http.examples.foo;


import io.vertx.core.http.HttpServerRequest;
import vertx.handlers.http.examples.foo.ext.bodyParser.Body;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class FooFormHandler<T> implements BiFunction<Consumer<Throwable>, Consumer<Object>, BiConsumer<HttpServerRequest, Body>> {

    @Override
    public BiConsumer<HttpServerRequest, Body> apply(Consumer<Throwable> fail, Consumer<Object> next) {
        return (req, arg) -> {
            req.response().end(String.format("Result from server. \nParsed body to type %s.\nfoo: %s",
                    arg.getClass().getSimpleName(),
                    "something"));
            next.accept(arg);
        };
    }
}
