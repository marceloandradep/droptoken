package com._98point6.droptoken.services;

import com._98point6.droptoken.model.Game;
import com._98point6.droptoken.model.factories.GameFactory;
import com._98point6.droptoken.repositories.GameRepository;
import io.reactivex.Single;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GameService {

    @Autowired
    private GameFactory gameFactory;
    
    @Autowired
    private GameRepository gameRepository;
    
    public Single<List<Game>> getGamesInProgress() {
        return gameRepository.getGamesInProgress();
    }
    
    public Single<Game> getGameState(String gameId) {
        return gameRepository.getGame(gameId);
    }
    
    public Single<String> createGame(List<String> players, int size) {
        Game game = gameFactory.createGame(players, size);
        
        return gameRepository
                .save(game)
                .map(Game::getId);
    }
    
}
