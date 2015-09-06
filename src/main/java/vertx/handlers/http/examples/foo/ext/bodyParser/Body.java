package vertx.handlers.http.examples.foo.ext.bodyParser;


public interface Body<T> {
    void body(T body);

    T body();
}
