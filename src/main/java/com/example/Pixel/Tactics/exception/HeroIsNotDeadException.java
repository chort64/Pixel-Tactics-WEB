package com.example.Pixel.Tactics.exception;

public class HeroIsNotDeadException extends Exception{
    String message;

    public HeroIsNotDeadException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    } 
}
