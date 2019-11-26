package com6441.team7.risc.api.exception;

/**
 * create an self-defined exception if errors happen in reinforcement phase
 */
public class ReinforceParsingException extends RiscGameException {

    /**
     * constructor of ReinforceParsingException
     * @param message to be passed/displayed in string format 
     * @param e ReinforceParsingException happens when reinforcement command cannot be parsed correctly
     */
    public ReinforceParsingException(String message, Exception e) {
        super(message, e);
    }

    /**
     * constructor of ReinforceParsingException
     * @param message to be passed/displayed in string format 
     */
    public ReinforceParsingException(String message) {
        super(message);
    }

    /**
     * constructor of ReinforceParsingException
     * @param e ReinforceParsingException happens when reinforcement command cannot be parsed correctly
     */
    public ReinforceParsingException(Exception e) {
        super(e);
    }
}
