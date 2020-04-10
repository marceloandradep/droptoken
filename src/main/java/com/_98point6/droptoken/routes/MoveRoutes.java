package com._98point6.droptoken.routes;

import com._98point6.droptoken.handlers.MoveHandler;
import com._98point6.droptoken.vertx.annotations.Routes;
import com._98point6.droptoken.vertx.http.interfaces.RoutesBuilder;
import com._98point6.droptoken.vertx.http.validation.ValidationHandlerFactory;
import io.vertx.reactivex.ext.web.Router;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Routes
@Component
public class MoveRoutes implements RoutesBuilder {

    @Autowired
    private ValidationHandlerFactory factory;
    
    @Autowired
    private MoveHandler gameHandler;
    
    @Override
    public void build(Router router) {
        router
                .get("/drop_token/:gameId/moves")
                .produces(CONTENT_TYPE_APPLICATION_JSON)
                .handler(gameHandler::getMoves);

        router
                .get("/drop_token/:gameId/moves/:moveNumber")
                .produces(CONTENT_TYPE_APPLICATION_JSON)
                .handler(gameHandler::getMove);
        
    }
}
