package vertx.handlers.http.examples.foo;

import com.github.spriet2000.vertx.handlers.http.server.ext.bodyParser.Body;
import com.github.spriet2000.vertx.handlers.http.server.ext.parameters.Parameters;
import io.vertx.core.buffer.Buffer;

import java.util.Map;

public class Context implements Parameters, Body<Form> {

    private final Map<String, String> parameters;

    private Form body;

    public Context(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    @Override
    public void body(Form body) {

        this.body = body;
    }

    @Override
    public Form body() {

        return this.body;
    }

    @Override
    public Map<String, String> parameters() {
        return parameters;
    }


}
