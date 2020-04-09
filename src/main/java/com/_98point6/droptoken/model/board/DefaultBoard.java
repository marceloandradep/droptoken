package com._98point6.droptoken.model.board;

import com._98point6.droptoken.model.interfaces.Board;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public class DefaultBoard implements Board {

    private Column[] columns;
    private Row[] rows;
    private Row diagonal1;
    private Row diagonal2;
    
    private int spacesLeft;
    private boolean hasWinner = false;
    
    public DefaultBoard(int size) {
        this.spacesLeft = size * size;
        
        rows = Stream
                .generate(() -> new Row(size))
                .limit(size)
                .toArray(Row[]::new);
        
        diagonal1 = new Row(size);
        diagonal2 = new Row(size);
        
        columns = IntStream
                .range(0, size)
                .mapToObj(i -> new Column(i, rows, diagonal1, diagonal2))
                .toArray(Column[]::new);
    }
    
    @Override
    public boolean dropToken(byte token, int column) {
        if (token < 0) {
            throw new IllegalArgumentException("Token should be positive");
        }
        
        if (column < 0 || column >= columns.length) {
            throw new IllegalArgumentException("Column index out of bounds");
        }

        if (columns[column].isFull()) {
            return false;
        }

        spacesLeft--;
        if (columns[column].drop(token)) {
            hasWinner = true;
        }
        
        return true;
    }

    @Override
    public boolean isFull() {
        return spacesLeft == 0;
    }

    @Override
    public boolean hasWinner() {
        return hasWinner;
    }
}
