package com._98point6.droptoken.model;

import com._98point6.droptoken.model.constants.MoveType;
import com.fasterxml.jackson.annotation.JsonInclude;

public class Move {
    
    private MoveType type;
    
    private String player;
    private Integer column;
    
    public static Move dropTokenAt(String player, Integer column) {
        return new Move(MoveType.MOVE, player, column);
    }
    
    public static Move quit(String player) {
        return new Move(MoveType.QUIT, player, null);
    }

    private Move(MoveType type, String player, Integer column) {
        this.type = type;
        this.player = player;
        this.column = column;
    }

    public MoveType getType() {
        return type;
    }

    public String getPlayer() {
        return player;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Integer getColumn() {
        return column;
    }
}
