package com._98point6.droptoken.model;

import com._98point6.droptoken.exceptions.IllegalMoveException;
import com._98point6.droptoken.exceptions.PlayerNotFoundException;
import com._98point6.droptoken.exceptions.PlayerOutOfTurnException;
import com._98point6.droptoken.model.interfaces.Board;
import com._98point6.droptoken.model.interfaces.MoveList;
import com._98point6.droptoken.model.interfaces.PlayerSet;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class GameTest {
    
    private Game game;
    private PlayerSet playerSet;
    private MoveList moveList;
    private Board board;
    
    @Before
    public void setup() {
        playerSet = mock(PlayerSet.class);
        moveList = mock(MoveList.class);
        board = mock(Board.class);
        
        game = new Game(playerSet, moveList, board);
    }
    
    @Test(expected = IllegalMoveException.class)
    public void when_a_player_tries_to_play_a_finished_game_should_throw_error() {
        String player = "valid";
        
        game.finish();
        game.dropToken(player, 0);
        
        fail();
    }
    
    @Test(expected = PlayerNotFoundException.class)
    public void when_a_player_that_is_not_in_the_game_tries_to_play_should_throw_error() {
        String player = "invalid";
        
        when(playerSet.contains(player)).thenReturn(false);
        
        game.dropToken(player, 0);
        
        fail();
    }

    @Test(expected = PlayerOutOfTurnException.class)
    public void when_a_player_tries_to_play_out_of_his_turn_should_throw_error() {
        String player = "valid";

        when(playerSet.contains(player)).thenReturn(true);
        when(playerSet.isPlayerTurn(player)).thenReturn(false);

        game.dropToken(player, 0);
        
        fail();
    }

    @Test(expected = IllegalMoveException.class)
    public void when_a_player_tries_to_drop_token_in_a_invalid_column_should_throw_error() {
        String playerId = "valid";
        int token = 1, column = 0;
        
        Player player = new Player(playerId, token, true);
        
        when(playerSet.contains(playerId)).thenReturn(true);
        when(playerSet.isPlayerTurn(playerId)).thenReturn(true);
        when(playerSet.getCurrentPlayer()).thenReturn(player);
        
        when(board.dropToken(token, column)).thenReturn(false);

        game.dropToken(playerId, column);
        
        fail();
    }
    
    @Test
    public void when_a_player_makes_a_move_and_the_game_continues() {
        String playerId = "valid";
        int token = 1, column = 0;

        Player player = new Player(playerId, token, true);
        Move move = Move.dropTokenAt(0, playerId, column);

        when(playerSet.contains(playerId)).thenReturn(true);
        when(playerSet.isPlayerTurn(playerId)).thenReturn(true);
        when(playerSet.getCurrentPlayer()).thenReturn(player);

        when(board.dropToken(token, column)).thenReturn(true);
        when(board.isFull()).thenReturn(false);
        when(board.hasWinner()).thenReturn(false);
        
        when(moveList.addDropToken(playerId, column)).thenReturn(move);

        Move actualMove = game.dropToken(playerId, column);

        verify(playerSet).next();
        assertFalse(game.isDone());
        assertNull(game.getWinner());
        assertEquals(move, actualMove);
    }

    @Test
    public void when_a_player_makes_a_move_and_the_game_draws() {
        String playerId = "valid";
        int token = 1, column = 0;

        Player player = new Player(playerId, token, true);
        Move move = Move.dropTokenAt(0, playerId, column);

        when(playerSet.contains(playerId)).thenReturn(true);
        when(playerSet.isPlayerTurn(playerId)).thenReturn(true);
        when(playerSet.getCurrentPlayer()).thenReturn(player);

        when(board.dropToken(token, column)).thenReturn(true);
        when(board.isFull()).thenReturn(true);
        when(board.hasWinner()).thenReturn(false);

        when(moveList.addDropToken(playerId, column)).thenReturn(move);

        Move actualMove = game.dropToken(playerId, column);

        verify(playerSet).next();
        assertTrue(game.isDone());
        assertNull(game.getWinner());
        assertEquals(move, actualMove);
    }

    @Test
    public void when_a_player_makes_a_move_and_wins_the_game() {
        String playerId = "valid";
        int token = 1, column = 0;

        Player player = new Player(playerId, token, true);
        Move move = Move.dropTokenAt(0, playerId, column);

        when(playerSet.contains(playerId)).thenReturn(true);
        when(playerSet.isPlayerTurn(playerId)).thenReturn(true);
        when(playerSet.getCurrentPlayer()).thenReturn(player);

        when(board.dropToken(token, column)).thenReturn(true);
        when(board.isFull()).thenReturn(false);
        when(board.hasWinner()).thenReturn(true);

        when(moveList.addDropToken(playerId, column)).thenReturn(move);

        Move actualMove = game.dropToken(playerId, column);

        verify(playerSet).next();
        assertTrue(game.isDone());
        assertEquals(playerId, game.getWinner());
        assertEquals(move, actualMove);
    }
    
    @Test(expected = IllegalMoveException.class)
    public void when_a_player_tries_to_quit_a_finished_game_should_throw_error() {
        String playerId = "valid";
        
        game.finish();
        game.quit(playerId);
        
        fail();
    }
    
    @Test(expected = PlayerNotFoundException.class)
    public void when_a_player_tries_to_quit_a_game_which_it_does_not_belong_should_throw_error() {
        String playerId = "valid";
        
        when(playerSet.contains(playerId)).thenReturn(false);
        
        game.quit(playerId);
        
        fail();
    }
    
    @Test
    public void when_one_player_quits_and_there_is_many_players_left_the_game_should_continue() {
        String playerId = "valid";
        
        Move move = Move.quit(0, playerId);
        
        when(playerSet.contains(playerId)).thenReturn(true);
        when(playerSet.activePlayersCount()).thenReturn(2);
        when(moveList.addQuit(playerId)).thenReturn(move);

        Move actualMove = game.quit(playerId);
        
        verify(playerSet).remove(playerId);
        
        assertEquals(move, actualMove);
        assertFalse(game.isDone());
        assertNull(game.getWinner());
    }
    
    @Test
    public void when_one_player_quits_and_one_player_remains_the_game_should_finish() {
        String playerId = "valid";

        Player player = new Player("winner", 1, true);
        Move move = Move.quit(0, playerId);

        when(playerSet.contains(playerId)).thenReturn(true);
        when(playerSet.activePlayersCount()).thenReturn(1);
        when(playerSet.getSinglePlayerLeft()).thenReturn(player);
        when(moveList.addQuit(playerId)).thenReturn(move);

        Move actualMove = game.quit(playerId);

        verify(playerSet).remove(playerId);

        assertEquals(move, actualMove);
        assertTrue(game.isDone());
        assertEquals(player.getId(), game.getWinner());
    }
}
