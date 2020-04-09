package com._98point6.droptoken.model.factories;

import com._98point6.droptoken.exceptions.NotEnoughPlayersException;
import com._98point6.droptoken.exceptions.PlayerAlreadyExistsException;
import com._98point6.droptoken.exceptions.TooManyPlayersException;
import com._98point6.droptoken.model.DefaultPlayerSet;
import com._98point6.droptoken.model.interfaces.PlayerSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

@Component
public class PlayerSetFactory {
    
    private int maxPlayers;

    public PlayerSetFactory(@Value("${game.max-players}") int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public PlayerSet create(List<String> players) {
        if (players == null || players.size() < 2) {
            throw new NotEnoughPlayersException();
        }
        
        if (players.size() > maxPlayers) {
            throw new TooManyPlayersException();
        }

        HashSet<String> ids = new LinkedHashSet<>();
        players.forEach(p -> {
            if (ids.contains(p)) {
                throw new PlayerAlreadyExistsException();
            }
            
            ids.add(p);
        });
        
        return new DefaultPlayerSet(ids);
    }
    
}
