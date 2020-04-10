package com._98point6.droptoken.fixtures;

import com._98point6.droptoken.model.Move;
import com._98point6.droptoken.model.Player;

import java.util.ArrayList;
import java.util.List;

public class MoveFixtures {
    
    public static List<Move> winningSequence(List<Player> players, int size) {
        List<Move> moves = new ArrayList<>(); 
        
        for (int i = 0; i < size - 1; i++) {
            for (int j = 0; j < players.size(); j++) {
                moves.add(Move.dropTokenAt(players.get(j).getId(), j));
            }
        }
        
        moves.add(Move.dropTokenAt(players.get(0).getId(), 0));
        return moves;
    }
    
}
