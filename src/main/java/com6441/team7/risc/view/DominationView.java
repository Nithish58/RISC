package com6441.team7.risc.view;

import com6441.team7.risc.api.model.PlayerService;

import java.util.Observable;

public class DominationView implements GameView {
    @Override
    public void receiveCommand() {

    }

    @Override
    public void displayMessage(String string) {

    }

    @Override
    public void update(Observable o, Object arg) {
        if(o instanceof PlayerService){

            //TODO: o.getTotalArmies();
            //TODO: o.getOccupiedCountriesPercentage();
            //TODO: o.getOccupiedContinents();
            //TODO: call displayMessge to display messages
        }
    }
}
