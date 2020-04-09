package com._98point6.droptoken.model;

import com._98point6.droptoken.model.constants.MoveType;

public class Move {
    
    private Integer number;
    private MoveType type;
    
    private String player;
    private Integer column;
    
    public static Move dropTokenAt(Integer number, String player, Integer column) {
        return new Move(number, MoveType.MOVE, player, column);
    }
    
    public static Move quit(Integer number, String player) {
        return new Move(number, MoveType.QUIT, player, null);
    }

    private Move(Integer number, MoveType type, String player, Integer column) {
        this.number = number;
        this.type = type;
        this.player = player;
        this.column = column;
    }

    public Integer getNumber() {
        return number;
    }

    public MoveType getType() {
        return type;
    }

    public String getPlayer() {
        return player;
    }

    public Integer getColumn() {
        return column;
    }
}
