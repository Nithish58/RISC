package com6441.team7.risc.view;

import com6441.team7.risc.controller.Controller;

import java.util.Collection;
import java.util.Observer;

public interface GameView extends Observer {
    void receiveCommand();
    void displayMessage(String string);
}
