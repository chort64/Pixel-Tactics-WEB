package com.example.Pixel.Tactics.exception;

public class NotYourMove extends Exception {
    String message;

    public NotYourMove(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }  
}
