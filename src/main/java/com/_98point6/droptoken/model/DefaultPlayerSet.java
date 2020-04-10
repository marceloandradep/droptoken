package com._98point6.droptoken.model;

import com._98point6.droptoken.exceptions.NotEnoughPlayersException;
import com._98point6.droptoken.exceptions.TooManyPlayersException;
import com._98point6.droptoken.model.interfaces.PlayerSet;

import java.util.Set;

public class DefaultPlayerSet implements PlayerSet {

    Player[] players;
    private int currentPlayer;
    private int activePlayersCount;
    
    public DefaultPlayerSet(Set<String> ids) {
        assert !ids.isEmpty();
        
        players = new Player[ids.size()];
        currentPlayer = 0;
        activePlayersCount = players.length;
        
        byte token = 1;
        int index = 0;
        for (String id : ids) {
            players[index++] = new Player(id, token++, true);
        }
    }
    
    private Player findPlayer(String playerId) {
        for (Player player : players) {
            if (player.getId().equals(playerId)) {
                return player;
            }
        }
        return null;
    }

    @Override
    public boolean contains(String playerId) {
        Player player = findPlayer(playerId);
        return player != null && player.isPlaying();
    }

    @Override
    public void remove(String playerId) {
        if (activePlayersCount == 1) {
            throw new NotEnoughPlayersException();
        }
        
        Player player = findPlayer(playerId);
        
        if (player != null) {
            player.quit();
            activePlayersCount--;

            if (isPlayerTurn(playerId)) {
                next();
            }
        }
    }

    @Override
    public boolean isPlayerTurn(String playerId) {
        return players[currentPlayer].getId().equals(playerId);
    }

    @Override
    public void next() {
        if (activePlayersCount == 1) {
            return;
        }
        
        do {
            currentPlayer = (currentPlayer + 1) % players.length;
        } while (!players[currentPlayer].isPlaying());
    }

    @Override
    public int activePlayersCount() {
        return activePlayersCount;
    }

    @Override
    public Player getCurrentPlayer() {
        return players[currentPlayer];
    }

    @Override
    public Player getSinglePlayerLeft() {
        if (activePlayersCount == 1) {
            return players[currentPlayer];
        }
        
        throw new TooManyPlayersException();
    }

    @Override
    public Player[] getPlayers() {
        return players;
    }
}
