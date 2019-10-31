package com6441.team7.risc.view;

import com6441.team7.risc.controller.Controller;

import java.util.Collection;
import java.util.Observable;
import static com6441.team7.risc.api.RiscConstants.CARD_EXCHANGE_VIEW_STRING;

public class CardExchangeView implements GameView {
    @Override
    public void receiveCommand() {

    }

    @Override
    public void displayMessage(String string) {
    	System.out.println(CARD_EXCHANGE_VIEW_STRING + string);
    }


    @Override
    public void update(Observable o, Object arg) {

    }
}
