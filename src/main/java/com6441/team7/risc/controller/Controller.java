package com6441.team7.risc.controller;

/**
 * an interface for the controller
 */
public interface Controller {

    /**
     * read command from the controller
     * @param command Command
     * @throws Exception on invalid value
     */
    void readCommand(String command) throws Exception;
}
