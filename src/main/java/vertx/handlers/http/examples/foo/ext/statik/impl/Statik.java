package vertx.handlers.http.examples.foo.ext.statik.impl;

import com.github.spriet2000.vertx.handlers.http.server.ServerController;
import com.github.spriet2000.vertx.handlers.http.server.ServerHandler;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;

import java.io.File;

public class Statik implements ServerController {

    private final String appRoot;
    private String indexPage;

    public Statik(String appFolder) {
        this.appRoot = String.format("%s%s", System.getProperty("vertx.cwd"), appFolder);
        this.indexPage = "index.html";
    }

    @Override
    public ServerHandler<Object> handle(Handler fail, Handler next) {
        return (req, res, args) -> {
            if (req.method() != HttpMethod.GET && req.method() != HttpMethod.HEAD) {
                next.handle(args);
            } else if (req.path().equals("/")) {
                res.sendFile(String.format("%s%s%s", appRoot, File.separator, indexPage), event -> {
                    if (event.failed()) {
                        fail.handle(404);
                    } else {
                        next.handle(args);
                    }
                });
            } else if (!req.path().contains("..")) {
                res.sendFile(String.format("%s%s%s", appRoot, File.separator, req.path()), event -> {
                    if (event.failed()) {
                        fail.handle(404);
                    } else {
                        next.handle(args);
                    }
                });
            } else {
                fail.handle(404);
            }
        };
    }
}

