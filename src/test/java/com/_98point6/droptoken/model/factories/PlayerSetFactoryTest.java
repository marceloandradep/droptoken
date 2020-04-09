package com._98point6.droptoken.model.factories;

import com._98point6.droptoken.exceptions.NotEnoughPlayersException;
import com._98point6.droptoken.exceptions.PlayerAlreadyExistsException;
import com._98point6.droptoken.exceptions.TooManyPlayersException;
import com._98point6.droptoken.model.interfaces.PlayerSet;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class PlayerSetFactoryTest {

    @Test(expected = NotEnoughPlayersException.class)
    public void when_creating_with_no_players_should_throw_error() {
        PlayerSetFactory factory = new PlayerSetFactory(2);
        factory.create(null);
        fail();
    }

    @Test(expected = NotEnoughPlayersException.class)
    public void when_creating_with_not_enough_players_should_throw_error() {
        PlayerSetFactory factory = new PlayerSetFactory(2);
        factory.create(Collections.singletonList("player1"));
        fail();
    }

    @Test(expected = PlayerAlreadyExistsException.class)
    public void when_creating_where_players_have_same_id_should_throw_error() {
        PlayerSetFactory factory = new PlayerSetFactory(2);
        factory.create(Arrays.asList("player1", "player1"));
        fail();
    }
    
    @Test(expected = TooManyPlayersException.class)
    public void when_creating_with_too_many_players_should_throw_error() {
        PlayerSetFactory factory = new PlayerSetFactory(2);
        factory.create(Arrays.asList("player1", "player2", "player3"));
        fail();
    }
    
    @Test
    public void when_creating_valid_should_return_not_null() {
        PlayerSetFactory factory = new PlayerSetFactory(2);
        PlayerSet set = factory.create(Arrays.asList("player1", "player2"));
        
        assertNotNull(set);
    }
}
