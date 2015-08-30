# Vert.x handlers-http-example

## Example website

Static website with simple form processing.

``` java

// handling
BiConsumer<Object, Throwable> exception = (e, a) -> logger.error(a);
BiConsumer<Object, Object> success = (e, a) -> logger.info(a);

// common
RequestHandlers<HttpServerRequest, Object> common =
        new RequestHandlers<>(exception, success);
common.andThen(new TimeOutHandler(vertx), new ResponseTimeHandler());

// directory todo
RequestHandlers<HttpServerRequest, Directory> directory =
        new RequestHandlers<>(exception, success);
directory.andThen(new DirectoryHandler(vertx, "/app/dir"));

// configure router
Router router = router();

// directory listing
router.get("/dir/*filepath", (req, params) -> {
    common.handle(req, null);
    directory.handle(req, new DirectoryContext());
});

// statik serving
RequestHandlers<HttpServerRequest, Object> statik =
        new RequestHandlers<>(exception, success);
statik.andThen(new Statik("/app"));

router.get("/*filepath", (req, params) -> {
    common.handle(req, null);
    statik.handle(req, null);
});

// body parser
RequestHandlers<HttpServerRequest, Body<FooBar>> bodyParser =
        new RequestHandlers<>(exception, success);
bodyParser.andThen(new JsonBodyParser(FooBar.class), new FooFormHandler());

router.post("/foobar", (req, params) -> {
    common.handle(req, null);
    bodyParser.handle(req, new FooContext(params));
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
