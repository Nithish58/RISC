package com6441.team7.risc.view;

import com6441.team7.risc.api.model.CardWrapper;
import com6441.team7.risc.api.model.Player;
import com6441.team7.risc.api.model.PlayerService;
import com6441.team7.risc.controller.Controller;

import java.util.Collection;
import java.util.List;
import java.util.Observable;
import static com6441.team7.risc.api.RiscConstants.CARD_EXCHANGE_VIEW_STRING;

public class CardExchangeView implements GameView{

    public CardExchangeView(){
        System.out.println("card exchange view has been created");
    }

    @Override
    public void receiveCommand() {

    }

    @Override
    public void displayMessage(String string) {
    	System.out.println(CARD_EXCHANGE_VIEW_STRING + string);
    }


    //I put an example here to show how to get the object from playerService and display it
    //if you have multiple attributes to send, use a self-defined wrapper like CardWrapper I use
    @Override
    public void update(Observable o, Object arg) {
        if(arg instanceof CardWrapper){

            System.out.println("card exchange view: current player: " + ((CardWrapper)arg).getPlayer().getName());

            System.out.println("card exchange view: reinforced country: " + ((CardWrapper) arg).getCountryName());

            System.out.println("card exchange view: reinforced country: " + ((CardWrapper) arg).getNum());


            //jenny: the card exchange view will only display message when users trade in cards.

//            List<String> cardList = ((Player)arg).getCardList();
//            System.out.println("card exchange view: after trading in, the cards left are: \n");
//            cardList.forEach(card -> System.out.print(card + ","));

        }
    }
}
