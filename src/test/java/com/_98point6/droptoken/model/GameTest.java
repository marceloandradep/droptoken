package com._98point6.droptoken.model;

import com._98point6.droptoken.exceptions.GameDoneException;
import com._98point6.droptoken.exceptions.IllegalMoveException;
import com._98point6.droptoken.exceptions.NotFoundException;
import com._98point6.droptoken.exceptions.PlayerOutOfTurnException;
import com._98point6.droptoken.model.interfaces.Board;
import com._98point6.droptoken.model.interfaces.MoveList;
import com._98point6.droptoken.model.interfaces.PlayerSet;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
    
    @Test(expected = NotFoundException.class)
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
        byte token = 1;
        int column = 0;
        
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
        byte token = 1;
        int column = 0;

        Player player = new Player(playerId, token, true);
        
        when(playerSet.contains(playerId)).thenReturn(true);
        when(playerSet.isPlayerTurn(playerId)).thenReturn(true);
        when(playerSet.getCurrentPlayer()).thenReturn(player);

        when(board.dropToken(token, column)).thenReturn(true);
        when(board.isFull()).thenReturn(false);
        when(board.hasWinner()).thenReturn(false);
        
        when(moveList.addDropToken(playerId, column)).thenReturn(0);

        int index = game.dropToken(playerId, column);

        verify(playerSet).next();
        assertFalse(game.isDone());
        assertNull(game.getWinner());
        assertEquals(0, index);
    }

    @Test
    public void when_a_player_makes_a_move_and_the_game_draws() {
        String playerId = "valid";
        byte token = 1;
        int column = 0;

        Player player = new Player(playerId, token, true);
        
        when(playerSet.contains(playerId)).thenReturn(true);
        when(playerSet.isPlayerTurn(playerId)).thenReturn(true);
        when(playerSet.getCurrentPlayer()).thenReturn(player);

        when(board.dropToken(token, column)).thenReturn(true);
        when(board.isFull()).thenReturn(true);
        when(board.hasWinner()).thenReturn(false);

        when(moveList.addDropToken(playerId, column)).thenReturn(0);

        int index = game.dropToken(playerId, column);

        verify(playerSet).next();
        assertTrue(game.isDone());
        assertNull(game.getWinner());
        assertEquals(0, index);
    }

    @Test
    public void when_a_player_makes_a_move_and_wins_the_game() {
        String playerId = "valid";
        byte token = 1;
        int column = 0;

        Player player = new Player(playerId, token, true);
        
        when(playerSet.contains(playerId)).thenReturn(true);
        when(playerSet.isPlayerTurn(playerId)).thenReturn(true);
        when(playerSet.getCurrentPlayer()).thenReturn(player);

        when(board.dropToken(token, column)).thenReturn(true);
        when(board.isFull()).thenReturn(false);
        when(board.hasWinner()).thenReturn(true);

        when(moveList.addDropToken(playerId, column)).thenReturn(0);

        int index = game.dropToken(playerId, column);

        verify(playerSet).next();
        assertTrue(game.isDone());
        assertEquals(playerId, game.getWinner());
        assertEquals(0, index);
    }
    
    @Test(expected = GameDoneException.class)
    public void when_a_player_tries_to_quit_a_finished_game_should_throw_error() {
        String playerId = "valid";
        
        game.finish();
        game.quit(playerId);
        
        fail();
    }
    
    @Test(expected = NotFoundException.class)
    public void when_a_player_tries_to_quit_a_game_which_it_does_not_belong_should_throw_error() {
        String playerId = "valid";
        
        when(playerSet.contains(playerId)).thenReturn(false);
        
        game.quit(playerId);
        
        fail();
    }
    
    @Test
    public void when_one_player_quits_and_there_is_many_players_left_the_game_should_continue() {
        String playerId = "valid";
        
        when(playerSet.contains(playerId)).thenReturn(true);
        when(playerSet.activePlayersCount()).thenReturn(2);
        when(moveList.addQuit(playerId)).thenReturn(0);

        int index = game.quit(playerId);
        
        verify(playerSet).remove(playerId);
        
        assertEquals(0, index);
        assertFalse(game.isDone());
        assertNull(game.getWinner());
    }
    
    @Test
    public void when_one_player_quits_and_one_player_remains_the_game_should_finish() {
        String playerId = "valid";

        Player player = new Player("winner", (byte)1, true);
        
        when(playerSet.contains(playerId)).thenReturn(true);
        when(playerSet.activePlayersCount()).thenReturn(1);
        when(playerSet.getSinglePlayerLeft()).thenReturn(player);
        when(moveList.addQuit(playerId)).thenReturn(0);

        int index = game.quit(playerId);

        verify(playerSet).remove(playerId);

        assertEquals(0, index);
        assertTrue(game.isDone());
        assertEquals(player.getId(), game.getWinner());
    }

    @Test(expected = NotFoundException.class)
    public void when_getting_moves_and_there_is_any_should_throw_error() {
        when(moveList.getMoves()).thenReturn(Collections.emptyList());

        game.getMoves(0, 0);

        fail();
    }

    @Test(expected = NotFoundException.class)
    public void when_move_filter_out_of_bounds_should_throw_error() {
        List<Move> moves = Arrays.asList(Move.dropTokenAt("player1", 1));
        
        when(moveList.getMoves()).thenReturn(moves);

        game.getMoves(0, 2);

        fail();
    }

    @Test
    public void when_getting_moves_with_null_filters_should_return_all_moves() {
        List<Move> moves = 
                Arrays.asList(Move.dropTokenAt("player1", 1), Move.dropTokenAt("player1", 1));

        when(moveList.getMoves()).thenReturn(moves);

        assertEquals(moves, game.getMoves(null, null));
    }

    @Test
    public void when_getting_moves_with_filters_should_return_sublist_of_moves() {
        List<Move> moves =
                Arrays.asList(Move.dropTokenAt("player1", 1), Move.dropTokenAt("player1", 1));

        when(moveList.getMoves()).thenReturn(moves);

        assertEquals(1, game.getMoves(0, 1).size());
    }
}
