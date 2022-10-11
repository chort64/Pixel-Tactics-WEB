package com.example.Pixel.Tactics.exception;

public class LoginIsBusy extends Exception{
    String message;

    public LoginIsBusy(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    } 
}
