# Vert.x handlers-http-example

## Example website

Static website with simple form processing.

``` java

Router router = router();

Handlers<HttpServerRequest, FooContext> common = compose(
        new ExceptionHandler(),
        new TimeOutHandler(vertx),
        new ResponseTimeHandler());

Handlers.Composition statik = compose(common)
        .andThen(new Statik("/app"))
        .exceptionHandler(new ErrorHandler())
        .successHandler((e, a) -> logger.info(a));

router.get("/*filepath", (req, params) -> {
    statik.accept(req, null);
});

Handlers.Composition<HttpServerRequest, FooContext> bodyParser = compose(common)
        .andThen(new JsonBodyParser(FooBar.class), new FooFormHandler())
        .exceptionHandler(new ErrorHandler())
        .successHandler((e, a) -> logger.info(a));

router.post("/foobar", (req, params) -> {
    bodyParser.accept(req, new FooContext(params));
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
