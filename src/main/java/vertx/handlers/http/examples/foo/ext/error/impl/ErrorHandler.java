package vertx.handlers.http.examples.foo.ext.error.impl;

import io.vertx.core.http.HttpServerRequest;

import java.util.function.BiConsumer;

public class ErrorHandler  implements BiConsumer<HttpServerRequest, Throwable> {

    @Override
    public void accept(HttpServerRequest request, Throwable throwable) {

    }
}
