# Vert.x handlers-http-example

## Example website

Static website with simple form processing.

``` java

// handling
BiConsumer<Object, Throwable> exception = (e, a) -> logger.error(a);
BiConsumer<HttpServerRequest, Object> success = (e, a) -> logger.info(a);

// common
Handlers<HttpServerRequest> common = new Handlers<>(
        new TimeOutHandler(vertx), new ResponseTimeHandler());

// configure router
Router router = router();

// statik serving
Handlers<HttpServerRequest> statik =  new Handlers<>();
statik.andThen(new Statik("/app"));

router.get("/*filepath", (req, params) -> {
    common.accept(req, null, exception, success);
    statik.accept(req, null, exception, success);
});

// body parser
Handlers<HttpServerRequest> bodyParser = new Handlers<HttpServerRequest>(
        new JsonBodyParser(FooBar.class), new FooFormHandler());

router.post("/foobar", (req, params) -> {
    common.accept(req, null, exception, success);
    bodyParser.accept(req, new FooContext(params), exception, success);
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
