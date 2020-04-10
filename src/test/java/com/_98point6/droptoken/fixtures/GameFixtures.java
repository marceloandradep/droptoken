package com._98point6.droptoken.fixtures;

import com._98point6.droptoken.model.Game;
import com._98point6.droptoken.model.constants.GameState;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class GameFixtures {
    
    public static List<Game> listOfGames() {
        return Arrays.asList(
                new Game(UUID.randomUUID().toString(), GameState.IN_PROGRESS), 
                new Game(UUID.randomUUID().toString(), GameState.IN_PROGRESS), 
                new Game(UUID.randomUUID().toString(), GameState.DONE));
    }
    
}
