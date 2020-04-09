package com._98point6.droptoken.model.interfaces;

import com._98point6.droptoken.model.Move;

/**
 * This interface specifies a component that
 * is responsible for move creation
 */
public interface MoveList {

    /**
     * Add a drop token move to the list
     * @param playerId
     * @param column
     * @return
     */
    Move addDropToken(String playerId, int column);

    /**
     * Add a quit move to the list
     * @param playerId
     * @return
     */
    Move addQuit(String playerId);
    
}
