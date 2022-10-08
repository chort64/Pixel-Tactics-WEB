package com.example.Pixel.Tactics.exception;

public class InvalidMove extends Exception{
    String message;

    public InvalidMove(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    } 
}
