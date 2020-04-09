package com._98point6.droptoken.model;

import com._98point6.droptoken.model.interfaces.MoveList;

import java.util.ArrayList;
import java.util.List;

public class DefaultMoveList implements MoveList {
    
    private ArrayList<Move> moves;

    public DefaultMoveList() {
        this.moves = new ArrayList<>();
    }
    
    @Override
    public Move addDropToken(String player, int column) {
        Move move = Move.dropTokenAt(player, column);
        moves.add(move);
        return move;
    }

    @Override
    public Move addQuit(String player) {
        Move move = Move.quit(player);
        moves.add(move);
        return move;
    }

    @Override
    public List<Move> getMoves() {
        return moves;
    }
}
