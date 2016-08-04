package vertx.handlers.http.examples.foo.impl;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FormData {
    public final String foo;

    public FormData(@JsonProperty("foo") String foo) {
        this.foo = foo;
    }
}
