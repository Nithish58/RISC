package com6441.team7.risc.view;

import java.util.Observable;
import static com6441.team7.risc.api.RiscConstants.CARD_EXCHANGE_VIEW_STRING;

/**
 * This view is created in reinforcement phase when player exchange cards
 */
public class CardExchangeView implements GameView{

    /**
     * constructor
     */
    public CardExchangeView(){
        System.out.println("card exchange view has been created");
    }

    /**
     * extends method from GameView to receiveCommand
     */
    @Override
    public void receiveCommand() {

    }

    /**
     * extends method from GameView to displayMessage
     * @param string
     */
    @Override
    public void displayMessage(String string) {
    	System.out.println(CARD_EXCHANGE_VIEW_STRING + string);
    }


    /**
     * when the cards has been added or removed from player, it will update method to show updates.
     * @param o
     * @param arg
     */
    @Override
    public void update(Observable o, Object arg) {



    }
}
