package com._98point6.droptoken.services;

import com._98point6.droptoken.model.Move;
import com._98point6.droptoken.repositories.GameRepository;
import io.reactivex.Completable;
import io.reactivex.Single;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MoveService {
    
    @Autowired
    private GameRepository gameRepository;
    
    public Single<List<Move>> getMoves(String gameId, Integer start, Integer until) {
        return gameRepository
                .getGame(gameId)
                .map(game -> game.getMoves(start, until));
    }
    
    public Single<Move> getMove(String gameId, int moveNumber) {
        return gameRepository
                .getGame(gameId)
                .map(game -> game.getMove(moveNumber));
    }
    
    public Single<Integer> dropToken(String gameId, String playerId, int column) {
        return gameRepository
                .getGame(gameId)
                .flatMap(game -> {
                    int move = game.dropToken(playerId, column);
                    return gameRepository
                            .saveMove(game, game.getMove(move))
                            .toSingle(() -> move);
                });
    }
    
    public Completable quit(String gameId, String playerId) {
        return gameRepository
                .getGame(gameId)
                .flatMap(game -> {
                    int move = game.quit(playerId);
                    return gameRepository
                            .saveMove(game, game.getMove(move))
                            .toSingle(() -> move);
                })
                .ignoreElement();
    }
    
}
