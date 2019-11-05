package com6441.team7.risc.api.exception;

/**
 * create an self-defined exception if errors happen in reinforcement phase
 */
public class ReinforceParsingException extends RiscGameException {

    /**
     * constructor of ReinforceParsingException
     * @param message String
     * @param e Exception
     */
    public ReinforceParsingException(String message, Exception e) {
        super(message, e);
    }

    /**
     * constructor of ReinforceParsingException
     * @param message String
     */
    public ReinforceParsingException(String message) {
        super(message);
    }

    /**
     * constructor of ReinforceParsingException
     * @param e Exception
     */
    public ReinforceParsingException(Exception e) {
        super(e);
    }
}
