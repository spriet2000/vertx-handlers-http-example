package vertx.handlers.http.examples.handlers.impl;

import io.vertx.core.http.HttpServerRequest;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.util.Date;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class JsHandler<A> implements BiFunction<BiConsumer<HttpServerRequest, Throwable>,
        BiConsumer<HttpServerRequest, A>, BiConsumer<HttpServerRequest, A>> {

    private final ScriptEngine scriptEngine;

    public JsHandler(ScriptEngine scriptEngine){

        this.scriptEngine = scriptEngine;
    }

    @Override
    public BiConsumer<HttpServerRequest, A> apply(
            BiConsumer<HttpServerRequest, Throwable> fail, BiConsumer<HttpServerRequest, A> next) {
        return (req, arg) -> {
            Date date = new Date();
            try {
                Object result = ((Invocable) scriptEngine)
                        .invokeFunction("renderOnServer", "SERVER:" + date.toString());
                next.accept(req, (A) result);

            } catch (ScriptException | NoSuchMethodException exception) {
                fail.accept(req, exception);
            }
        };
    }
}
