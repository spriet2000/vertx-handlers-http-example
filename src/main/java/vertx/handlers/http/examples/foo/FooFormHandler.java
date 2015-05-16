package vertx.handlers.http.examples.foo;


import com.github.spriet2000.vertx.handlers.http.server.ServerController;
import com.github.spriet2000.vertx.handlers.http.server.ServerHandler;
import io.vertx.core.Handler;

public class FooFormHandler implements ServerController {

    @Override
    public ServerHandler<FooContext> handle(Handler fail, Handler next) {
        return (req, res, ctx) -> {
            res.end(String.format("Result from server. \nParsed body to type %s.\nfoo: %s",
                    ctx.getClass().getSimpleName(),
                    ctx.body().foo));
            next.handle(ctx);
        };
    }
}
