package com._98point6.droptoken.handlers.dto;

public class DropTokenDTO {
    
    private int column;

    public DropTokenDTO() {
        this(0);
    }

    public DropTokenDTO(int column) {
        this.column = column;
    }

    public int getColumn() {
        return column;
    }
}
