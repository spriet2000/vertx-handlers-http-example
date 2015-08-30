package vertx.handlers.http.examples.foo.ext.directory.impl;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerRequest;
import vertx.handlers.http.examples.foo.ext.directory.Directory;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class DirectoryHandler implements BiFunction<Consumer<Throwable>, Consumer<Object>, BiConsumer<HttpServerRequest, Directory>> {

    private final DirectoryReader reader;
    private final DirectoryWriter writer;

    public DirectoryHandler(Vertx vertx, String dirPath) {
        this.reader = new DirectoryReader(vertx, dirPath);
        this.writer = new DirectoryWriter();
    }

    @Override
    public BiConsumer<HttpServerRequest, Directory> apply(Consumer<Throwable> fail, Consumer<Object> next) {
        return (req, arg) -> reader.apply(fail, o -> writer.apply(fail, next)).accept(req, new DirectoryContext());
    }
}
