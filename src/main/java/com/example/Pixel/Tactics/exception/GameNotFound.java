package com.example.Pixel.Tactics.exception;

public class GameNotFound extends Exception {
    String message;

    public GameNotFound(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
