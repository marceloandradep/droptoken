package com._98point6.droptoken.fixtures;

import com._98point6.droptoken.model.Player;

import java.util.Arrays;
import java.util.List;

public class PlayerFixtures {
    
    public static List<Player> listOfPlayers() {
        return Arrays.asList(
                new Player("player1", (byte)0, false),
                new Player("player2", (byte)0, false),
                new Player("player3", (byte)0, false)
        );
    }
    
}
