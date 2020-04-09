package com._98point6.droptoken.model;

import com._98point6.droptoken.exceptions.NotEnoughPlayersException;
import com._98point6.droptoken.exceptions.TooManyPlayersException;
import org.junit.Test;
import org.mockito.internal.util.collections.Sets;

import static org.junit.Assert.*;

public class DefaultPlayerSetTest {
    
    @Test(expected = AssertionError.class)
    public void when_creating_set_with_null_players_should_throw_error() {
        DefaultPlayerSet set = new DefaultPlayerSet(Sets.newSet());
        fail();
    }

    @Test
    public void should_create_valid_set() {
        DefaultPlayerSet set = new DefaultPlayerSet(Sets.newSet("player1", "player2"));
        
        assertEquals(2, set.players.length);
        assertEquals("player1", set.getCurrentPlayer().getId());
        assertEquals(2, set.activePlayersCount());
    }
    
    @Test
    public void when_player_is_in_the_set_and_is_playing_contains_should_return_true() {
        DefaultPlayerSet set = new DefaultPlayerSet(Sets.newSet("player1", "player2"));
        assertTrue(set.contains("player1"));
    }
    
    @Test
    public void when_player_is_in_the_set_but_is_not_playing_contains_should_return_false() {
        DefaultPlayerSet set = new DefaultPlayerSet(Sets.newSet("player1", "player2"));
        set.players[0].quit();
        
        assertFalse(set.contains("player1"));
    }
    
    @Test
    public void when_player_is_not_in_the_set_contains_should_return_false() {
        DefaultPlayerSet set = new DefaultPlayerSet(Sets.newSet("player1", "player2"));
        assertFalse(set.contains("player3"));
    }

    @Test
    public void when_only_one_player_is_playing_then_that_player_should_play_next() {
        DefaultPlayerSet set = new DefaultPlayerSet(Sets.newSet("player1", "player2"));
        
        set.players[1].quit();
        set.next();
        
        assertEquals("player1", set.getCurrentPlayer().getId());
    }

    @Test
    public void when_more_than_one_player_is_playing_then_the_player_next_to_current_should_play_next() {
        DefaultPlayerSet set = new DefaultPlayerSet(Sets.newSet("player1", "player2"));

        set.next();

        assertEquals("player2", set.getCurrentPlayer().getId());
    }

    @Test
    public void when_it_is_first_player_turn_and_second_player_is_inactive_then_third_player_should_play_next() {
        DefaultPlayerSet set = new DefaultPlayerSet(Sets.newSet("player1", "player2", "player3"));

        set.players[1].quit();
        set.next();

        assertEquals("player3", set.getCurrentPlayer().getId());
    }
    
    @Test
    public void when_the_current_player_is_the_last_in_the_round_then_the_first_player_should_play_next() {
        DefaultPlayerSet set = new DefaultPlayerSet(Sets.newSet("player1", "player2", "player3"));

        set.next();
        set.next();
        set.next();

        assertEquals("player1", set.getCurrentPlayer().getId());
    }

    @Test
    public void checking_if_it_is_player_turn() {
        DefaultPlayerSet set = new DefaultPlayerSet(Sets.newSet("player1", "player2", "player3"));
        assertTrue(set.isPlayerTurn("player1"));
    }

    @Test
    public void checking_if_it_is_not_player_turn() {
        DefaultPlayerSet set = new DefaultPlayerSet(Sets.newSet("player1", "player2", "player3"));
        assertFalse(set.isPlayerTurn("player2"));
    }

    @Test
    public void when_the_current_player_quits_then_should_decrement_active_players_count_and_should_pick_another_current_player() {
        DefaultPlayerSet set = new DefaultPlayerSet(Sets.newSet("player1", "player2", "player3"));
        
        set.remove("player1");
        
        assertEquals(2, set.activePlayersCount());
        assertEquals("player2", set.getCurrentPlayer().getId());
    }

    @Test
    public void when_a_player_other_than_current_quits_then_should_decrement_active_players_count() {
        DefaultPlayerSet set = new DefaultPlayerSet(Sets.newSet("player1", "player2", "player3"));

        set.remove("player2");

        assertEquals(2, set.activePlayersCount());
        assertEquals("player1", set.getCurrentPlayer().getId());
    }
    
    @Test(expected = NotEnoughPlayersException.class)
    public void when_the_only_player_left_quits_should_throw_error() {
        DefaultPlayerSet set = new DefaultPlayerSet(Sets.newSet("player1"));

        set.remove("player1");

        fail();
    }
    
    @Test
    public void when_there_is_only_one_player_left_then_should_get_single_player() {
        DefaultPlayerSet set = new DefaultPlayerSet(Sets.newSet("player1", "player2"));
        
        set.remove("player2");
        Player player = set.getSinglePlayerLeft();

        assertNotNull(player);
    }
    
    @Test(expected = TooManyPlayersException.class)
    public void when_there_is_more_than_one_player_left_then_get_single_player_should_throw_error() {
        DefaultPlayerSet set = new DefaultPlayerSet(Sets.newSet("player1", "player2"));

        Player player = set.getSinglePlayerLeft();

        fail();
    }
    
}
