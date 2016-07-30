# Vert.x handlers-http-example

## Example website

Static website with simple form processing.

``` java

Router router = router();

ServerRequestHandlers<Context> common = build(
        new ExceptionHandler<>(),
        new TimeoutHandler<>(vertx),
        new LogHandler<>());

String appFolder = String.format("%s%s", System.getProperty("vertx.cwd"), "/app");

BiConsumer<HttpServerRequest, Context> statik = build(common)
        .andThen(new StatikFileHandler<>(appFolder))
        .apply(new ErrorResponse(), new SuccessHandler<>());

router.get("/*filepath", (req, params) -> {
    statik.accept(req, new Context(params));
});

BiConsumer<HttpServerRequest, Context> bodyParser = build(common)
        .andThen(new JsonBodyParser(Form.class), new FormHandler())
        .apply(new ErrorResponse(), new SuccessHandler<>());

router.post("/foobar", (req, params) -> {
    bodyParser.accept(req, new Context(params));
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
