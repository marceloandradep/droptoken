package com._98point6.droptoken.model.interfaces;

import com._98point6.droptoken.model.Player;

/**
 * The purpose of this interface is to decouple
 * the player management and turns logic
 */
public interface PlayerSet {

    /**
     * Check if a player is in the game
     * @param playerId
     * @return
     */
    boolean contains(String playerId);

    /**
     * Called when a player quits the game
     * @param playerId
     */
    void remove(String playerId);

    /**
     * Check if it is the player's turn
     * @param playerId
     * @return
     */
    boolean isPlayerTurn(String playerId);

    /**
     * Finishes the current turn and pick the next player to play
     */
    void next();
    
    /**
     * Returns the number of active players in the game
     * @return
     */
    int activePlayersCount();

    /**
     * Returns the player that should play the current turn
     * @return
     */
    Player getCurrentPlayer();

    /**
     * If all players but one have quit this method return the single player left.
     * Return null if there is more than one player.
     * @return
     */
    Player getSinglePlayerLeft();
    
}
