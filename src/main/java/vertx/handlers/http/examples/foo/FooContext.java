package vertx.handlers.http.examples.foo;

import vertx.handlers.http.examples.foo.ext.bodyParser.Body;
import vertx.handlers.http.examples.foo.ext.parameters.Parameters;

import java.util.Map;

public class FooContext implements Parameters, Body<FooBar> {

    private final Map<String, String> parameters;

    private FooBar body;

    public FooContext(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    @Override
    public void body(FooBar body) {

        this.body = body;
    }

    @Override
    public FooBar body() {

        return this.body;
    }

    @Override
    public Map<String, String> parameters() {
        return parameters;
    }
}
