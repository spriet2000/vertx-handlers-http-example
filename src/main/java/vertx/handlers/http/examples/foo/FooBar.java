package vertx.handlers.http.examples.foo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FooBar {
    public final String foo;

    public FooBar(@JsonProperty("foo") String foo) {
        this.foo = foo;
    }
}
