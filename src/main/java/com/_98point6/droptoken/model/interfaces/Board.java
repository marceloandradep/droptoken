package com._98point6.droptoken.model.interfaces;

/**
 * The purpose of this interface is to decouple the
 * board implementation from the game logic
 */
public interface Board {

    /**
     * Drop a token in the specified column. Returns true
     * if it was a valid movement
     * 
     * @param token
     * @param column
     * @return
     */
    boolean dropToken(byte token, int column);

    /**
     * Check if the board is full
     * @return
     */
    boolean isFull();

    /**
     * Return true if for any token there is a full column, row
     * or diagonals filled with the same token
     * @return
     */
    boolean hasWinner();
    
}
