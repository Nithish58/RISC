package com6441.team7.risc.view;

import com6441.team7.risc.controller.Controller;

import java.util.Collection;
import java.util.Observer;

/**
 * an interface of the game view
 */
public interface GameView extends Observer {

    /**
     * receive commands from player
     */
    void receiveCommand();

    /**
     * display messages
     * @param string
     */
    void displayMessage(String string);
}
