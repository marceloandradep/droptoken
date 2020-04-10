package com._98point6.droptoken.services;

import com._98point6.droptoken.model.Game;
import io.reactivex.Single;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GameService {
    
    public Single<List<Game>> getGamesInProgress() {
        return null;
    }
    
    public Single<Game> getGameState(String gameId) {
        return null;
    }
    
    public Single<String> createGame(List<String> players, int size) {
        return null;
    }
    
}
