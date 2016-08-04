package vertx.handlers.http.examples.foo.impl;


import com.github.spriet2000.vertx.handlers.http.handlers.bodyParser.Body;
import vertx.handlers.http.examples.handlers.Parameters;

import java.util.Map;

public class Context implements Parameters, Body<FormData> {

    private final Map<String, String> parameters;

    private FormData data;

    Context(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    @Override
    public void body(FormData data) {

        this.data = data;
    }

    @Override
    public FormData body() {

        return this.data;
    }

    @Override
    public Map<String, String> parameters() {
        return parameters;
    }
}
