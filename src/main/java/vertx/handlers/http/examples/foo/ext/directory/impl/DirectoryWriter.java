package vertx.handlers.http.examples.foo.ext.directory.impl;

import com.github.spriet2000.vertx.handlers.http.server.ServerController;
import com.github.spriet2000.vertx.handlers.http.server.ServerHandler;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpHeaders;
import vertx.handlers.http.examples.foo.ext.directory.Directory;

import java.nio.file.Paths;


public class DirectoryWriter implements ServerController {

    private String root;

    public DirectoryWriter() {
        root = Paths.get("").toAbsolutePath().toString();
    }

    @Override
    public ServerHandler<Directory> handle(Handler fail, Handler next) {
        return (req, res, args) -> {
            StringBuilder builder = new StringBuilder();
            args.contents().forEach(i -> builder.append(String.format("content %s",
                    i.replace(root, ""))));
            res.headers().set(HttpHeaders.CONTENT_LENGTH, String.valueOf(builder.length()));
            res.end(builder.toString());
            next.handle(args);
        };
    }
}
