package com._98point6.droptoken.handlers;

import com._98point6.droptoken.model.Game;
import com._98point6.droptoken.services.GameService;
import com._98point6.droptoken.vertx.http.utils.HttpUtils;
import io.reactivex.Observable;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class GameHandler {

    private Logger logger = LoggerFactory.getLogger(GameHandler.class);
    
    @Autowired
    private GameService gameService;

    public void getGamesInProgress(RoutingContext context) {
        logger.info("Handling GET " + context.request().path());

        gameService
                .getGamesInProgress()
                .flatMapObservable(Observable::fromIterable)
                .map(Game::getId)
                .toList()
                .map(this::gameIdsResponse)
                .subscribe(d -> HttpUtils.ok(context, d), e -> HttpUtils.handleFailure(context, e));
    }
    
    public void getGameState(RoutingContext context) {
        logger.info("Handling GET " + context.request().path());

        String gameId = HttpUtils.getParam(context, "gameId", String.class);
        
        gameService
                .getGameState(gameId)
                .subscribe(d -> HttpUtils.ok(context, d), e -> HttpUtils.handleFailure(context, e));
    }
    
    private Map<String, List<String>> gameIdsResponse(List<String> ids) {
        HashMap<String, List<String>> response = new HashMap<>();
        response.put("games", ids);
        return response;
    }
}
