package com6441.team7.risc.view;

import com6441.team7.risc.api.model.Player;
import com6441.team7.risc.api.wrapperview.ReinforcedArmyAfterTradingCardWrapper;

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
     * @param string String
     */
    @Override
    public void displayMessage(String string) {
    	System.out.println(CARD_EXCHANGE_VIEW_STRING + string);
    }


    /**
     * when the cards has been added or removed from player, it will update method to show updates.
     * @param o observable o
     * @param arg object arg
     */
    @Override
    public void update(Observable o, Object arg) {
        if(arg instanceof ReinforcedArmyAfterTradingCardWrapper){
            displayReinforcedCard(arg);
        }
    }

    /**
     * Method to display which player got how many soldiers for exchanging cards
     * @param arg to be cast as ReinforcedArmyAfterTradingCardWrapper
     */
    private void displayReinforcedCard(Object arg){

        int number = ((ReinforcedArmyAfterTradingCardWrapper)arg).getSoldier();
        Player player = ((ReinforcedArmyAfterTradingCardWrapper)arg).getPlayer();

        displayMessage("player " + player.getName() + ": receives " + number + " soldiers after exchanging cards");
    }

}