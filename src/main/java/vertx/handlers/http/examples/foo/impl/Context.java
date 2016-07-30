package vertx.handlers.http.examples.foo.impl;


import com.github.spriet2000.vertx.handlers.extensions.bodyParser.Body;
import vertx.handlers.http.examples.foo.Parameters;

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
