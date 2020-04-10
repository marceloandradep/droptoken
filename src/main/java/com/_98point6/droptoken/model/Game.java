package com._98point6.droptoken.model;

import com._98point6.droptoken.exceptions.GameDoneException;
import com._98point6.droptoken.exceptions.IllegalMoveException;
import com._98point6.droptoken.exceptions.NotFoundException;
import com._98point6.droptoken.exceptions.PlayerOutOfTurnException;
import com._98point6.droptoken.model.interfaces.Board;
import com._98point6.droptoken.model.constants.GameState;
import com._98point6.droptoken.model.interfaces.MoveList;
import com._98point6.droptoken.model.interfaces.PlayerSet;
import com.fasterxml.jackson.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@JsonPropertyOrder({ "players", "state", "winner" })
@JsonFilter("customPropertyFilter")
public class Game {
    
    private String id;
    
    private GameState state;
    private String winner;
    
    private PlayerSet playerSet;
    private MoveList moveList;
    private Board board;
    
    public Game(PlayerSet playerSet, MoveList moveList, Board board) {
        this.id = UUID.randomUUID().toString();
        this.playerSet = playerSet;
        this.moveList = moveList;
        this.board = board;
        
        state = GameState.IN_PROGRESS;
    }

    public int dropToken(String playerId, int column) {
        if (isDone()) {
            throw new IllegalMoveException();
        }
        
        if (!playerSet.contains(playerId)) {
            throw new NotFoundException();
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
    
    public int quit(String playerId) {
        if (isDone()) {
            throw new GameDoneException();
        }

        if (!playerSet.contains(playerId)) {
            throw new NotFoundException();
        }
        
        playerSet.remove(playerId);
        
        if (playerSet.activePlayersCount() == 1) {
            Player player = playerSet.getSinglePlayerLeft();
            winner = player.getId();
            finish();
        }
        
        return moveList.addQuit(playerId);
    }
    
    @JsonIgnore
    public boolean isDone() {
        return GameState.DONE.equals(state);
    }
    
    @JsonIgnore
    public boolean isDraw() {
        return board.isFull() && winner == null;
    }
    
    public void finish() {
        state = GameState.DONE;
    }

    @JsonIgnore
    public String getId() {
        return id;
    }

    @JsonGetter("players")
    public String[] getPlayersIds() {
        return Arrays.stream(playerSet.getPlayers()).map(Player::getId).toArray(String[]::new);
    }
    
    public GameState getState() {
        return state;
    }

    public String getWinner() {
        return winner;
    }
    
    @JsonIgnore
    public List<Move> getMoves(Integer start, Integer until) {
        List<Move> moves = moveList.getMoves();
        
        if (moves.isEmpty()) {
            throw new NotFoundException();
        }
        
        if (start != null && until != null) {
            try {
                moves = moves.subList(start, until);
            } catch (IndexOutOfBoundsException e) {
                throw new NotFoundException();
            }
        }
        
        return moves;
    }

    @JsonIgnore
    public Move getMove(int moveNumber) {
        Move move = null;
        
        try {
            move = moveList.getMoves().get(moveNumber);
        } catch (IndexOutOfBoundsException e) {
            throw new NotFoundException();
        }
        
        return move;
    }
}
