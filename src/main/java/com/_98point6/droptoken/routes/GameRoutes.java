package com._98point6.droptoken.routes;

import com._98point6.droptoken.handlers.GameHandler;
import com._98point6.droptoken.vertx.annotations.Routes;
import com._98point6.droptoken.vertx.http.interfaces.RoutesBuilder;
import com._98point6.droptoken.vertx.http.validation.ValidationHandlerFactory;
import io.vertx.reactivex.ext.web.Router;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Routes
@Component
public class GameRoutes implements RoutesBuilder {

    @Autowired
    private ValidationHandlerFactory factory;
    
    @Autowired
    private GameHandler gameHandler;
    
    @Override
    public void build(Router router) {
        router
                .get("/drop_token")
                .produces(CONTENT_TYPE_APPLICATION_JSON)
                .handler(gameHandler::getGamesInProgress);

        router
                .get("/drop_token/:gameId")
                .produces(CONTENT_TYPE_APPLICATION_JSON)
                .handler(gameHandler::getGameState);

        router
                .post("/drop_token")
                .consumes(CONTENT_TYPE_APPLICATION_JSON)
                .produces(CONTENT_TYPE_APPLICATION_JSON)
                .handler(factory.getSchemaValidationHandler("game-post"))
                .handler(gameHandler::createGame);
    }
}
