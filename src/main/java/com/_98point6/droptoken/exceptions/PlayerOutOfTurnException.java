package com._98point6.droptoken.exceptions;

public class PlayerOutOfTurnException extends DropTokenException {
    public PlayerOutOfTurnException() {
        super(409);
    }
}
