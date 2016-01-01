package vertx.handlers.http.examples.foo.ext.statik.impl;

import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;

import java.io.File;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class Statik<A> implements
        BiFunction<BiConsumer<HttpServerRequest, Throwable>, BiConsumer<HttpServerRequest, A>, BiConsumer<HttpServerRequest, A>> {


    private final String appRoot;
    private String indexPage;

    public Statik(String appFolder) {
        this.appRoot = String.format("%s%s", System.getProperty("vertx.cwd"), appFolder);
        this.indexPage = "index.html";
    }

    @Override
    public BiConsumer<HttpServerRequest, A> apply(BiConsumer<HttpServerRequest, Throwable> fail,
                                                  BiConsumer<HttpServerRequest, A> next) {
        return (req, arg) -> {
            if (req.method() != HttpMethod.GET
                    && req.method() != HttpMethod.HEAD) {
                next.accept(req, arg);
            } else if (req.path().equals("/")) {

                req.response().sendFile(
                        String.format("%s%s%s", appRoot, File.separator, indexPage), event -> {
                            if (event.failed()) {
                                fail.accept(req, event.cause());
                            } else {
                                next.accept(req, arg);
                            }
                        });
            } else if (!req.path().contains("..")) {
                String filePath = String.format("%s%s%s", appRoot, File.separator, req.path());
                req.response().sendFile(filePath, event -> {
                    if (event.failed()) {
                        fail.accept(req, event.cause());
                    } else {
                        next.accept(req, arg);
                    }
                });
            } else {
                fail.accept(req, new Exception("404"));
            }
        };
    }
}

