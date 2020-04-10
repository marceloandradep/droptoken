package com._98point6.droptoken.services;

import com._98point6.droptoken.model.Move;
import io.reactivex.Completable;
import io.reactivex.Single;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MoveService {
    
    public Single<List<Move>> getMoves(String gameId, Integer start, Integer until) {
        return null;
    }
    
    public Single<Move> getMove(String gameId, int moveNumber) {
        return null;
    }
    
    public Single<Integer> dropToken(String gameId, String playerId, int column) {
        return null;
    }
    
    public Completable quit(String gameId, String playerId) {
        return null;
    }
    
}
