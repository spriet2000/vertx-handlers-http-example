package vertx.handlers.http.examples.foo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Form {
    public final String foo;

    public Form(@JsonProperty("foo") String foo) {
        this.foo = foo;
    }
}
