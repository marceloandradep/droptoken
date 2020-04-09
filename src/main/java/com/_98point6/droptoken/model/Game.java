package com._98point6.droptoken.model;

import com._98point6.droptoken.exceptions.IllegalMoveException;
import com._98point6.droptoken.exceptions.PlayerNotFoundException;
import com._98point6.droptoken.exceptions.PlayerOutOfTurnException;
import com._98point6.droptoken.model.interfaces.Board;
import com._98point6.droptoken.model.constants.GameState;
import com._98point6.droptoken.model.interfaces.MoveList;
import com._98point6.droptoken.model.interfaces.PlayerSet;

public class Game {
    
    private GameState state;
    private String winner;
    
    private PlayerSet playerSet;
    private MoveList moveList;
    private Board board;
    
    public Game(PlayerSet playerSet, MoveList moveList, Board board) {
        this.playerSet = playerSet;
        this.moveList = moveList;
        this.board = board;
        
        state = GameState.IN_PROGRESS;
    }

    public Move dropToken(String playerId, int column) {
        if (isDone()) {
            throw new IllegalMoveException();
        }
        
        if (!playerSet.contains(playerId)) {
            throw new PlayerNotFoundException();
        }
        
        if (!playerSet.isPlayerTurn(playerId)) {
            throw new PlayerOutOfTurnException();
        }
        
        Player player = playerSet.getCurrentPlayer();
        
        if (board.dropToken(player.getToken(), column)) {
            playerSet.next();
        } else {
            throw new IllegalMoveException();
        }

        if (board.isFull()) {
            finish();
        }
        
        if (board.hasWinner()) {
            winner = playerId;
            finish();
        }
        
        return moveList.addDropToken(playerId, column);
    }
    
    public Move quit(String playerId) {
        if (isDone()) {
            throw new IllegalMoveException();
        }

        if (!playerSet.contains(playerId)) {
            throw new PlayerNotFoundException();
        }
        
        playerSet.remove(playerId);
        
        if (playerSet.activePlayersCount() == 1) {
            Player player = playerSet.getSinglePlayerLeft();
            winner = player.getId();
            finish();
        }
        
        return moveList.addQuit(playerId);
    }
    
    public boolean isDone() {
        return GameState.DONE.equals(state);
    }
    
    public void finish() {
        state = GameState.DONE;
    }

    public String getWinner() {
        return winner;
    }
}
