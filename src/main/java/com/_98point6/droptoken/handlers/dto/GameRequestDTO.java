package com._98point6.droptoken.handlers.dto;

import java.util.List;

public class GameRequestDTO {
    
    private List<String> players;
    private int columns;
    private int rows;

    public GameRequestDTO() {
        this(null, 0, 0);
    }

    public GameRequestDTO(List<String> players, int columns, int rows) {
        this.players = players;
        this.columns = columns;
        this.rows = rows;
    }

    public List<String> getPlayers() {
        return players;
    }

    public int getColumns() {
        return columns;
    }

    public int getRows() {
        return rows;
    }
}
