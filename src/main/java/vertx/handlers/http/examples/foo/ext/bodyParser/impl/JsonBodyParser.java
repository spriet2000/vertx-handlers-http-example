package vertx.handlers.http.examples.foo.ext.bodyParser.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.spriet2000.vertx.handlers.http.server.ServerController;
import com.github.spriet2000.vertx.handlers.http.server.ServerHandler;
import io.netty.handler.codec.http.HttpHeaders;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import vertx.handlers.http.examples.foo.ext.bodyParser.Body;


@SuppressWarnings("unchecked")
public class JsonBodyParser<T extends Body> implements ServerController {

    private final Class<Object> clazz;

    public JsonBodyParser(Class<Object> clazz) {
        this.clazz = clazz;
    }

    @Override
    public ServerHandler<T> handle(Handler fail, Handler next) {
        return (req, res, args) -> {
            if (!req.headers().get(HttpHeaders.Names.CONTENT_TYPE).equals("application/json")) {
                next.handle(args);
                return;
            }
            if (req.method() == HttpMethod.GET
                    || req.method() == HttpMethod.HEAD
                    || args == null) {
                next.handle(args);
            } else {
                Buffer body = Buffer.buffer();
                req.handler(body::appendBuffer);
                req.endHandler(e -> {
                    ObjectMapper mapper = new ObjectMapper();
                    try {
                        Object result = mapper.readValue(body.toString(), clazz);
                        ((T) args).body(result);
                        next.handle(args);

                    } catch (Exception exception) {
                        fail.handle(exception);
                    }
                });
            }
        };
    }
}
