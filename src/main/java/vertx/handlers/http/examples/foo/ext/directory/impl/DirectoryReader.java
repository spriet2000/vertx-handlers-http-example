package vertx.handlers.http.examples.foo.ext.directory.impl;

import com.github.spriet2000.vertx.handlers.http.server.ServerController;
import com.github.spriet2000.vertx.handlers.http.server.ServerHandler;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import vertx.handlers.http.examples.foo.ext.directory.Directory;

import java.nio.file.Paths;


public class DirectoryReader implements ServerController {

    private final Vertx vertx;
    private final String dirPath;

    public DirectoryReader(Vertx vertx, String dirPath) {
        this.vertx = vertx;
        this.dirPath = String.format("%s%s", Paths.get("").toAbsolutePath().toString(), dirPath);
    }

    @Override
    public ServerHandler<Directory> handle(Handler fail, Handler next) {
        return (req, res, args) -> vertx.fileSystem().readDir(dirPath, async -> {
            if (async.succeeded()) {
                args.contents().addAll(async.result());
                next.handle(args);
            } else {
                fail.handle(async.cause());
            }
        });
    }
}
