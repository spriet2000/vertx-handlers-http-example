# Vert.x handlers-http-example

## Example website

Static website with simple form processing.

``` java

// configure router
Router router = router();

// error handling
BiConsumer<Object, Throwable> exception = (e, a) -> {
    ((HttpServerRequest) e).response().setStatusCode(
            HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
    ((HttpServerRequest) e).response().end(a.toString());
    logger.error(a);
};

// success handling
BiConsumer<HttpServerRequest, Object> success = (e, a) -> logger.info(a);

// common handlers
Handlers<HttpServerRequest> common = new Handlers<>(
         new TimeOutHandler(vertx),
         new ResponseTimeHandler()
);

// statik serving
Handlers<HttpServerRequest> statik =  new Handlers<>(
        new Statik("/app"));

// body parser
Handlers<HttpServerRequest> bodyParser = new Handlers<>(
        new JsonBodyParser(FooBar.class),
        new FooFormHandler());

router.get("/*filepath", (req, params) -> {
    common.accept(req, null, exception, (event, arg) -> {
        statik.accept(event, arg, exception, success);
    });
});

router.post("/foobar", (req, params) -> {
    common.accept(req, null, exception, (event, arg) -> {
        bodyParser.accept(req, new FooContext(params), exception, success);
    });
});

vertx.createHttpServer(new HttpServerOptions().setPort(8080))
        .requestHandler(router)
        .listen();

```

### Running the example

* Install [vert.x](http://vert-x3.github.io)
* Install [maven](http://maven.apache.org)

```

git clone https://github.com/spriet2000/vertx-handlers-http-example.git
cd vertx-handlers-http-example
mvn package
java -jar target/vertx-handlers-http-example-0.0.1-SNAPSHOT-fat.jar

```
