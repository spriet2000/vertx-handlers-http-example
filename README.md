# Vert.x handlers-http-example

## Example website

Static website with simple form processing.

``` java

Router router = router();

BiHandlers<HttpServerRequest, Context> common = compose(
        new ExceptionHandler<>(),
        new TimeoutHandler<>(vertx),
        new LogHandler<>(),
        new ResponseTimeHandler<>());

String appFolder =  String.format("%s%s", System.getProperty("vertx.cwd"), "/app");

BiConsumer<HttpServerRequest, Context> statik = new BiHandlers<>(common)
        .andThen(new StatikFileHandler<>(appFolder))
        .apply(new Error(), new Success<>());

router.get("/*filepath", (req, params) -> {
    statik.accept(req, null);
});

BiConsumer<HttpServerRequest, Context> bodyParser = compose(common)
        .andThen(new JsonBodyParser(Form.class), new FormHandler())
        .apply(new Error(), new Success<>());

router.post("/foobar", (req, params) -> {
    bodyParser.accept(req, new Context(params));
});

vertx.createHttpServer(new HttpServerOptions().setPort(8080))
        .requestHandler(router).listen();
                
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
