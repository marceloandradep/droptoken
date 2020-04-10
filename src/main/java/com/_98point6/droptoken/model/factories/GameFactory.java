package com._98point6.droptoken.model.factories;

import com._98point6.droptoken.model.Game;
import com._98point6.droptoken.model.interfaces.Board;
import com._98point6.droptoken.model.interfaces.MoveList;
import com._98point6.droptoken.model.interfaces.PlayerSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GameFactory {
    
    private PlayerSetFactory playerSetFactory;
    private MoveListFactory moveListFactory;
    private BoardFactory boardFactory;

    @Autowired
    public GameFactory(PlayerSetFactory playerSetFactory, MoveListFactory moveListFactory, BoardFactory boardFactory) {
        this.playerSetFactory = playerSetFactory;
        this.moveListFactory = moveListFactory;
        this.boardFactory = boardFactory;
    }

    public Game createGame(List<String> players, int size) {
        PlayerSet playerSet = playerSetFactory.create(players);
        Board board = boardFactory.create(size);
        MoveList moveList = moveListFactory.create();
        
        return new Game(playerSet, moveList, board);
    }

    public Game createGame(String id, List<String> players, int size) {
        PlayerSet playerSet = playerSetFactory.create(players);
        Board board = boardFactory.create(size);
        MoveList moveList = moveListFactory.create();

        return new Game(id, playerSet, moveList, board);
    }
    
}
