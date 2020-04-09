package com._98point6.droptoken.model.board;

import org.junit.Test;

import static org.junit.Assert.*;

public class DefaultBoardTest {
    
    @Test(expected = IllegalArgumentException.class)
    public void when_adding_an_invalid_token_should_throw_error() {
        DefaultBoard board = new DefaultBoard(4);
        board.dropToken((byte)-1, 0);
        
        fail();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void when_adding_a_token_to_an_invalid_column_should_throw_error() {
        DefaultBoard board = new DefaultBoard(4);
        board.dropToken((byte)1, 5);

        fail();
    }

    @Test
    public void when_adding_a_token_to_a_full_column_should_return_false() {
        DefaultBoard board = new DefaultBoard(4);
        
        assertTrue(board.dropToken((byte)1, 0));
        assertTrue(board.dropToken((byte)1, 0));
        assertTrue(board.dropToken((byte)1, 0));
        assertTrue(board.dropToken((byte)1, 0));
        assertFalse(board.dropToken((byte)1, 0));
    }
    
    @Test
    public void when_adding_some_tokens_should_return_not_full_if_there_is_space_available() {
        DefaultBoard board = new DefaultBoard(4);
        
        board.dropToken((byte)1, 0);
        board.dropToken((byte)1, 2);
        board.dropToken((byte)1, 3);

        assertFalse(board.isFull());
    }
    
    @Test
    public void when_player_makes_a_row_it_win_the_game() {
        DefaultBoard board = new DefaultBoard(4);

        board.dropToken((byte)1, 0);
        board.dropToken((byte)1, 1);
        board.dropToken((byte)1, 2);
        board.dropToken((byte)1, 3);

        assertFalse(board.isFull());
        assertTrue(board.hasWinner());
    }

    @Test
    public void when_board_is_filled_with_different_tokens_then_board_should_return_full_and_have_no_winner() {
        DefaultBoard board = new DefaultBoard(4);

        board.dropToken((byte)1, 0);
        board.dropToken((byte)2, 0);
        board.dropToken((byte)3, 0);
        board.dropToken((byte)4, 0);

        board.dropToken((byte)5, 1);
        board.dropToken((byte)6, 1);
        board.dropToken((byte)7, 1);
        board.dropToken((byte)8, 1);

        board.dropToken((byte)9, 2);
        board.dropToken((byte)10, 2);
        board.dropToken((byte)11, 2);
        board.dropToken((byte)12, 2);

        board.dropToken((byte)13, 3);
        board.dropToken((byte)14, 3);
        board.dropToken((byte)15, 3);
        board.dropToken((byte)16, 3);

        assertTrue(board.isFull());
        assertFalse(board.hasWinner());
    }
}
