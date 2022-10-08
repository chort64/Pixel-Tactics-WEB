package com.example.Pixel.Tactics.exception;

public class MaxCardsInHandException extends Exception {
    String message;

    public MaxCardsInHandException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    } 
}
