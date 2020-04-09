package com._98point6.droptoken.model.factories;

import com._98point6.droptoken.exceptions.InvalidBoardSizeException;
import com._98point6.droptoken.model.board.DefaultBoard;
import com._98point6.droptoken.model.interfaces.Board;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BoardFactory {

    static final int MIN_BOARD = 4;
    
    private int maxBoard;

    public BoardFactory(@Value("${game.max-board}") int maxBoard) {
        this.maxBoard = maxBoard;
    }
    
    public Board create(int size) {
        if (size < MIN_BOARD || size > maxBoard) {
            throw new InvalidBoardSizeException();
        }
        
        return new DefaultBoard(size);
    }
    
}
