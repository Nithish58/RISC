package com6441.team7.risc.api.wrapperview;

import com6441.team7.risc.api.model.Card;
import com6441.team7.risc.api.model.Player;

import java.util.List;

/**
 * a self-defined wrapper for reinforcement stage
 */
public class ReinforcedCardWrapper {

    /**
     * a list of cards for the player
     */
    private List<Card> cards;

    /**
     * the current player
     */
    private Player player;


    /**
     * constructor
     * @param player reference player
     * @param cards reference card list
     */
    public ReinforcedCardWrapper(Player player, List<Card> cards) {
        this.player = player;
        this.cards = cards;
    }

    /**
     * constructor
     * @param player reference player
     * @param number reference number of card
     */
    public ReinforcedCardWrapper(Player player, int number){
        this.player = player;
    }

    /**
     * get the list of cards
     * @return cards
     */
    public List<Card> getCards() {
        return cards;
    }

    /**
     * set the list of cards
     * @param cards reference card list
     */
    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    /**
     * get current player
     * @return player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * set current player
     * @param player reference player
     */
    public void setPlayer(Player player) {
        this.player = player;
    }


}
