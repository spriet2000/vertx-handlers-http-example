# Vert.x handlers-http-example

## Example website

Static website with simple form processing.

```
    Router router = router();
    
    ServerHandlers common = handlers(new TimeOutHandler(vertx),
            new ResponseTimeHandler(), new LogHandler())
            .exceptionHandler(new ErrorHandler());
    
    ServerHandlers directory = handlers(common)
            .then(new DirectoryHandler(vertx, "/app/dir"));
    
    router.get("/dir/*filepath", (request, params) -> {
        directory.handle(request, new DirectoryContext());
    });
    
    ServerHandlers statik = handlers(common)
            .then(new Statik("/app"));
    
    router.get("/*filepath", statik);
    
    ServerHandlers foo = handlers(common)
            .then(new JsonBodyParser(FooBar.class), new FooFormHandler());
    
    router.post("/foobar", (request, params) -> {
        foo.handle(request, new FooContext(params));
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
