package com._98point6.droptoken.handlers;

import com._98point6.droptoken.exceptions.IllegalMoveException;
import com._98point6.droptoken.handlers.dto.DropTokenDTO;
import com._98point6.droptoken.model.Move;
import com._98point6.droptoken.services.MoveService;
import com._98point6.droptoken.vertx.http.utils.HttpUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.vertx.reactivex.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MoveHandler {

    private Logger logger = LoggerFactory.getLogger(MoveHandler.class);
    
    @Autowired
    private MoveService moveService;

    public void getMoves(RoutingContext context) {
        logger.info("Handling GET " + context.request().path());

        String gameId = HttpUtils.getParam(context, "gameId", String.class);
        Integer start = HttpUtils.getParam(context, "start", Integer.class);
        Integer until = HttpUtils.getParam(context, "until", Integer.class);

        moveService
                .getMoves(gameId, start, until)
                .map(this::wrapMoveList)
                .subscribe(d -> HttpUtils.ok(context, d), e -> HttpUtils.handleFailure(context, e));
    }
    
    public void getMove(RoutingContext context) {
        logger.info("Handling GET " + context.request().path());

        String gameId = HttpUtils.getParam(context, "gameId", String.class);
        
        Integer moveNumber = null;
        try {
            moveNumber = HttpUtils.getParam(context, "moveNumber", Integer.class);
        } catch (NumberFormatException e) {
            logger.warn("Invalid move number", e);
        }
        
        if (moveNumber == null) {
            HttpUtils.handleFailure(context, new IllegalMoveException());
            return;
        }

        moveService
                .getMove(gameId, moveNumber)
                .subscribe(d -> HttpUtils.ok(context, d), e -> HttpUtils.handleFailure(context, e));
    }
    
    public void dropToken(RoutingContext context) {
        logger.info("Handling POST " + context.request().path());

        String gameId = HttpUtils.getParam(context, "gameId", String.class);
        String playerId = HttpUtils.getParam(context, "playerId", String.class);

        DropTokenDTO dropTokenRequest = null;
        try {
            dropTokenRequest = HttpUtils.parseBody(context, DropTokenDTO.class);
        } catch (JsonProcessingException e) {
            HttpUtils.handleFailure(context, e);
            return;
        }
        
        moveService
                .dropToken(gameId, playerId, dropTokenRequest.getColumn())
                .map(moveNumber -> wrapMove(gameId, moveNumber))
                .subscribe(d -> HttpUtils.ok(context, d), e -> HttpUtils.handleFailure(context, e));
    }
    
    public void quit(RoutingContext context) {
        
    }
    
    private Map<String, List<Move>> wrapMoveList(List<Move> moves) {
        HashMap<String, List<Move>> response = new HashMap<>();
        response.put("moves", moves);
        return response;
    }
    
    private Map<String, String> wrapMove(String gameId, Integer moveNumber) {
        HashMap<String, String> response = new HashMap<>();
        response.put("move", gameId + "/moves/" + moveNumber);
        return response;
    }
    
}
