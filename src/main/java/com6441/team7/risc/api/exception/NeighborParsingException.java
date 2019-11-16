package com6441.team7.risc.api.exception;

/**
 * create an self-defined exception if errors happen in parsing neighboring information
 */
public class NeighborParsingException extends RiscGameException {

    /**
     * constructor of NeighborParsingException
     * @param rootCause Exception
     */
    public NeighborParsingException(Exception rootCause){
        super(rootCause);
    }

    /**
     * constructor of NeighborParsingException
     * @param message String
     * @param rootCause Exception
     */
    public NeighborParsingException(String message, Exception rootCause){
        super(message, rootCause);
    }

    /**
     * constructor of NeighborParsingException
     * @param message String
     */
    public NeighborParsingException(String message){
        super(message);
    }
}
