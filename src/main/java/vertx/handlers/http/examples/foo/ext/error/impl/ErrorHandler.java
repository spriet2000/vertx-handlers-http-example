package vertx.handlers.http.examples.foo.ext.error.impl;

import com.github.spriet2000.vertx.handlers.http.server.ServerHandler;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;

public class ErrorHandler implements ServerHandler<Object> {

    @Override
    public void handle(HttpServerRequest req, HttpServerResponse res, Object args) {
        if (args instanceof Integer) {
            res.setStatusCode((Integer) args);
        } else {
            res.setStatusCode(500);
        }
        String body = String.format("<html><head></head><body><h1>Ooops</h1> <p>%s</p> </body></html>", args.toString());
        res.headers().set(HttpHeaders.CONTENT_LENGTH, String.valueOf(body.length()));
        res.write(body);
        res.end();
    }
}
