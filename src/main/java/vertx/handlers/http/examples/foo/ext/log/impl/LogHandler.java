package vertx.handlers.http.examples.foo.ext.log.impl;

import com.github.spriet2000.vertx.handlers.http.server.ServerController;
import com.github.spriet2000.vertx.handlers.http.server.ServerHandler;
import io.vertx.core.Handler;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.impl.LoggerFactory;

public class LogHandler implements ServerController {

    Logger logger = LoggerFactory.getLogger(LogHandler.class);

    @Override
    public ServerHandler<Object> handle(Handler fail, Handler next) {
        return (req, res, args) -> {
            StringBuilder builder = new StringBuilder();
            builder.append(String.format("Uri %s %s \n", req.uri(), req.method()));
            if (args != null) {
                builder.append(String.format("Args %s \n", args.getClass().getSimpleName()));
            } else {
                builder.append("Args NULL \n");
            }
            builder.append("Request \n");
            req.headers().entries().forEach(e ->
                    builder.append(String.format("- hdr %s : %s \n",
                            e.getKey(), e.getValue())));
            res.headersEndHandler(x -> {
                builder.append("Response \n");
                res.headers().entries().forEach(e ->
                        builder.append(String.format("- hdr %s : %s \n",
                                e.getKey(), e.getValue())));
                builder.append(res.getStatusCode());
                logger.info(builder.toString());
            });
            next.handle(args);
        };
    }
}
