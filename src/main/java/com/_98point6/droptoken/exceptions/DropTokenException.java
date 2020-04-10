package com._98point6.droptoken.exceptions;

public class DropTokenException extends RuntimeException {
    
    private int statusCode;

    public DropTokenException() {
        this.statusCode = 400;
    }
    
    public DropTokenException(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
