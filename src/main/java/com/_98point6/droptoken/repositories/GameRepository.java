package com._98point6.droptoken.repositories;

import com._98point6.droptoken.exceptions.NotFoundException;
import com._98point6.droptoken.model.Game;
import com._98point6.droptoken.model.constants.GameState;
import io.reactivex.Observable;
import io.reactivex.Single;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public class GameRepository {
    
    private HashMap<String, Game> games = new HashMap<>();
    
    public Single<Game> save(Game game) {
        games.put(game.getId(), game);
        return Single.just(game);
    }
    
    public Single<List<Game>> getGamesInProgress() {
        return Observable
                .fromIterable(games.values())
                .filter(game -> GameState.IN_PROGRESS.equals(game.getState()))
                .toList();
    }
    
    public Single<Game> getGame(String gameId) {
        Game game = games.get(gameId);
        
        if (game == null) {
            return Single.error(NotFoundException::new);
        }
        
        return Single.just(game);
    }
    
}
