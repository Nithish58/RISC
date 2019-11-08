package com6441.team7.risc.api.wrapperview;

import com6441.team7.risc.api.model.Card;
import com6441.team7.risc.api.model.Player;

import java.util.List;

/**
 * a self-defined wrapper to show reinforce information after trading in
 */
public class ReinforcedArmyAfterTradingCardWrapper {


    /**
     * the current player
     */
    private Player player;

    /**
     * the number of reinforced soldiers
     */
    private int soldier;

    /**
     * constructor
     * @param player
     * @param soldier
     */
    public ReinforcedArmyAfterTradingCardWrapper(Player player, int soldier) {
        this.player = player;
        this.soldier = soldier;

    }

    /**
     * get the current player
     * @return
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * set the current player
     * @param player
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * get the number of soldiers
     * @return
     */
    public int getSoldier() {
        return soldier;
    }

    /**
     * set the number of soldiers
     * @param soldier
     */
    public void setSoldier(int soldier) {
        this.soldier = soldier;
    }

}
