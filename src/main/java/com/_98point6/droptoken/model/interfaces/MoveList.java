package com._98point6.droptoken.model.interfaces;

import com._98point6.droptoken.model.Move;

import java.util.List;

/**
 * This interface specifies a component that
 * is responsible for move creation
 */
public interface MoveList {

    /**
     * Add a drop token move to the list
     * @param player
     * @param column
     * @return
     */
    Move addDropToken(String player, int column);

    /**
     * Add a quit move to the list
     * @param player
     * @return
     */
    Move addQuit(String player);
    
    List<Move> getMoves();
    
}
