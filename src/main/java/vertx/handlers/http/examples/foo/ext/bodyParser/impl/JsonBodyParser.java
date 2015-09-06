package vertx.handlers.http.examples.foo.ext.bodyParser.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.handler.codec.http.HttpHeaders;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
import vertx.handlers.http.examples.foo.ext.bodyParser.Body;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class JsonBodyParser<A extends Body> implements BiFunction<Consumer<Throwable>, Consumer<Object>, BiConsumer<HttpServerRequest, A>> {

    private final Class clazz;

    public JsonBodyParser(Class clazz) {
        this.clazz = clazz;
    }

    @Override
    public BiConsumer<HttpServerRequest, A> apply(Consumer<Throwable> fail, Consumer<Object> next) {
        return (req, arg) -> {
            if (req.headers().contains(HttpHeaders.Names.CONTENT_TYPE)
                    && !req.headers().get(HttpHeaders.Names.CONTENT_TYPE).equals("application/json")) {
                next.accept(arg);
                return;
            }
            if (req.method() == HttpMethod.GET
                    || req.method() == HttpMethod.HEAD) {
                next.accept(arg);
            } else {
                Buffer body = Buffer.buffer();
                req.handler(body::appendBuffer);
                req.endHandler(e -> {
                    ObjectMapper mapper = new ObjectMapper();
                    try {
                        String input = body.toString();
                        Object result = mapper.readValue(input, clazz);
                        arg.body(result);
                        next.accept(arg);
                    } catch (Exception exception) {
                        fail.accept(exception);
                    }
                });
            }
        };
    }
}
