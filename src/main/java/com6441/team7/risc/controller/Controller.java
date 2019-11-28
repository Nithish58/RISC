package com6441.team7.risc.controller;

/**
 * an interface for the controller
 * All controllers must implement the readCommand() method.
 * This readcommand() is where phaseView routes user commands and where they must be validated.
 */
public interface Controller {

    /**
     * read command from the controller
     * @param command Command
     * @throws Exception on invalid value
     */
    void readCommand(String command) throws Exception;

}
