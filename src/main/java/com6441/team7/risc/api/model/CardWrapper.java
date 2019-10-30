package com6441.team7.risc.api.model;

/**
 * if you want to return many attributes than one to view, use the self-defined wrapper
 */
public class CardWrapper {
    private Player player;
    private String countryName;
    private Integer num;

    public CardWrapper(Player player, String countryName, Integer num) {
        this.player = player;
        this.countryName = countryName;
        this.num = num;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }
}
