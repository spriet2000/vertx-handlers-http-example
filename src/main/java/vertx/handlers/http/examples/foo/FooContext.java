package vertx.handlers.http.examples.foo;

import vertx.handlers.http.examples.foo.ext.bodyParser.Body;
import vertx.handlers.http.examples.foo.ext.parameters.Parameters;

import java.util.Map;

public class FooContext implements Body<FooBar>, Parameters {

    private final Map<String, Object> parameters;
    private FooBar body;

    public FooContext(Map<String, Object> parameters) {

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
    public Map<String, Object> parameters() {
        return parameters;
    }

}
