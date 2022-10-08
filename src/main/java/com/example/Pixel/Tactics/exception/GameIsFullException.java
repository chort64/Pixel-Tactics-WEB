package com.example.Pixel.Tactics.exception;

public class GameIsFullException  extends Exception{
    String message;

    public GameIsFullException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    } 
}
