package vertx.handlers.http.examples.foo.ext.statik.impl;

import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;

import java.io.File;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class Statik<A> implements BiFunction<Consumer<Throwable>, Consumer<Object>, BiConsumer<HttpServerRequest, A>> {


    private final String appRoot;
    private String indexPage;

    public Statik(String appFolder) {
        this.appRoot = String.format("%s%s", System.getProperty("vertx.cwd"), appFolder);
        this.indexPage = "index.html";
    }

    @Override
    public BiConsumer<HttpServerRequest, A> apply(Consumer<Throwable> fail, Consumer<Object> next) {
        return (req, arg) -> {
            if (req.method() != HttpMethod.GET
                    && req.method() != HttpMethod.HEAD) {
                next.accept(arg);
            } else if (req.path().equals("/")) {
                req.response().sendFile(
                        String.format("%s%s%s", appRoot, File.separator, indexPage), event -> {
                            if (event.failed()) {
                                fail.accept(new Exception("404"));
                            } else {
                                next.accept(arg);
                            }
                        });
            } else if (!req.path().contains("..")) {
                req.response().sendFile(
                        String.format("%s%s%s", appRoot, File.separator, req.path()), event -> {
                            if (event.failed()) {
                                fail.accept(new Exception("404"));
                            } else {
                                next.accept(arg);
                            }
                        });
            } else {
                fail.accept(new Exception("404"));
            }
        };
    }
}

