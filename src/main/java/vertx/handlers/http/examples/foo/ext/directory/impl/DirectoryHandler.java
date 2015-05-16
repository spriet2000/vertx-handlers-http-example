package vertx.handlers.http.examples.foo.ext.directory.impl;

import com.github.spriet2000.vertx.handlers.http.server.ServerController;
import com.github.spriet2000.vertx.handlers.http.server.ServerHandler;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import vertx.handlers.http.examples.foo.ext.directory.Directory;

public class DirectoryHandler implements ServerController {

    private final DirectoryReader reader;
    private final DirectoryWriter writer;

    public DirectoryHandler(Vertx vertx, String dirPath) {
        this.reader = new DirectoryReader(vertx, dirPath);
        this.writer = new DirectoryWriter();
    }

    @Override
    public ServerHandler<Directory> handle(Handler fail, Handler next) {
        return (req, res, args) -> reader.handle(fail,
                o -> writer.handle(fail, next)
                        .handle(req, res, args)).handle(req, res, args);
    }
}
