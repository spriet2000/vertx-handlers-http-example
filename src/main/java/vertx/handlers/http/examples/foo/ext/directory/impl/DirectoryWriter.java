package vertx.handlers.http.examples.foo.ext.directory.impl;

import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServerRequest;
import vertx.handlers.http.examples.foo.ext.directory.Directory;

import java.nio.file.Paths;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class DirectoryWriter implements BiFunction<Consumer<Throwable>, Consumer<Object>, BiConsumer<HttpServerRequest, Directory>> {

    private String root;

    public DirectoryWriter() {
        root = Paths.get("").toAbsolutePath().toString();
    }

    @Override
    public BiConsumer<HttpServerRequest, Directory> apply(Consumer<Throwable> fail, Consumer<Object> next) {
        return (req, arg) -> {
            StringBuilder builder = new StringBuilder();
            arg.contents().forEach(i -> builder.append(String.format("content %s",
                    i.replace(root, ""))));
            req.response().headers().set(HttpHeaders.CONTENT_LENGTH, String.valueOf(builder.length()));
            req.response().end(builder.toString());
            next.accept(arg);
        };
    }
}
