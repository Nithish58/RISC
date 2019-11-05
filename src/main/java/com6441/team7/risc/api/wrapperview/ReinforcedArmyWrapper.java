package com6441.team7.risc.api.wrapperview;

import com6441.team7.risc.api.model.Player;

/**
 * a self-defined wrapper for reinforcement stage
 */
public class ReinforcedArmyWrapper {

    /**
     * a reference of player
     */
    private Player player;

    /**
     * a string of country name
     */
    private String countryName;

    /**
     * the soldier numbers
     */
    private Integer num;

    /**
     * constructor of ReinforcedArmyWrapper
     * @param player
     * @param countryName
     * @param num
     */
    public ReinforcedArmyWrapper(Player player, String countryName, Integer num) {
        this.player = player;
        this.countryName = countryName;
        this.num = num;
    }

    /**
     * get the player
     * @return player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * set the player
     * @param player
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * get the country name
     * @return country name
     */
    public String getCountryName() {
        return countryName;
    }

    /**
     * set country name
     * @param countryName
     */
    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    /**
     * return the soldier number
     * @return
     */
    public Integer getNum() {
        return num;
    }

    /**
     * set the soldier number
     * @param num
     */
    public void setNum(Integer num) {
        this.num = num;
    }
}
