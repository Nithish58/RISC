package com6441.team7.risc.api.wrapperview;

import com6441.team7.risc.api.model.Card;
import com6441.team7.risc.api.model.Player;

import java.util.List;

/**
 * a self-defined wrapper to show reinforce information after trading in
 */
public class ReinforcedArmyAfterTradingCard {


    private Player player;
    private int soldier;

    public ReinforcedArmyAfterTradingCard(Player player, int soldier) {
        this.player = player;
        this.soldier = soldier;

    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getSoldier() {
        return soldier;
    }

    public void setSoldier(int soldier) {
        this.soldier = soldier;
    }

}
