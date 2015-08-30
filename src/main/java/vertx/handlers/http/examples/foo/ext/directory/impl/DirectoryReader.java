package vertx.handlers.http.examples.foo.ext.directory.impl;


import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerRequest;
import vertx.handlers.http.examples.foo.ext.directory.Directory;

import java.nio.file.Paths;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;


public class DirectoryReader implements BiFunction<Consumer<Throwable>, Consumer<Object>, BiConsumer<HttpServerRequest, Directory>> {

    private final Vertx vertx;
    private final String dirPath;

    public DirectoryReader(Vertx vertx, String dirPath) {
        this.vertx = vertx;
        this.dirPath = String.format("%s%s", Paths.get("").toAbsolutePath().toString(), dirPath);
    }

    public BiConsumer<HttpServerRequest, Directory> apply(Consumer<Throwable> fail, Consumer<Object> next) {
        return (req, arg) -> vertx.fileSystem().readDir(dirPath, async -> {
            if (async.succeeded()) {
                arg.contents().addAll(async.result());
                next.accept(arg);
            } else {
                fail.accept(async.cause());
            }
        });
    }
}
