package vertx.handlers.http.examples.serverjs.impl;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerRequest;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class JsHandler<A> implements BiFunction<BiConsumer<HttpServerRequest, Throwable>,
        BiConsumer<HttpServerRequest, A>, BiConsumer<HttpServerRequest, A>> {

    private final Vertx vertx;

    public JsHandler(Vertx vertx) {

        this.vertx = vertx;
    }

    @Override
    public BiConsumer<HttpServerRequest, A> apply(BiConsumer<HttpServerRequest, Throwable> fail, BiConsumer<HttpServerRequest, A> next) {

        ScriptEngine scriptEngine = new ScriptEngineManager().getEngineByName("nashorn");
        try {
            scriptEngine.eval(vertx.fileSystem().readFileBlocking(
                    getClass()
                            .getClassLoader()
                            .getResource("app.js")
                            .getPath())
                    .toString());
        } catch (ScriptException ex) {
            throw new RuntimeException(ex);
        }
        return (req, arg) -> {
            try {

                // just a test of how this works..
                Object result = ((Invocable) scriptEngine).invokeFunction("say");
                next.accept(req, (A) result);

            } catch (ScriptException | NoSuchMethodException exception) {
                fail.accept(req, exception);
            }
        };
    }
}
