package com.example.Pixel.Tactics.exception;

public class OccupiedPlaceException extends Exception {
    String message;

    public OccupiedPlaceException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }   
}
