package com.example.Pixel.Tactics.exception;

public class CardNotFoundException extends Exception{
    String message;

    public CardNotFoundException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }  
}
