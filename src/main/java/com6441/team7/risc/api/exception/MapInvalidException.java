package com6441.team7.risc.api.exception;

public class MapInvalidException extends RiscGameException {
    public MapInvalidException(String message, Exception e) {
        super(message, e);
    }

    public MapInvalidException(String message) {
        super(message);
    }

    public MapInvalidException(Exception e) {
        super(e);
    }
}
