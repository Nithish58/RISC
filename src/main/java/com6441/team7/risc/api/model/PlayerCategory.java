package com6441.team7.risc.api.model;

/**
 * This class stores player category
 */
public enum PlayerCategory {
    AGGRESSIVE("aggressive"),
    RANDOM("random"),
    CHEATER("cheater"),
    BENEVOLENT("benevolent"),
    HUMAN("human");

    /**
     * the name of the player category
     */
    private String name;

    /**
     * constructor
     * @param name player category name
     */
    PlayerCategory(String name){
        this.name = name;
    }

    /**
     * get category name
     * @return name category name
     */
    public String getName(){
        return name;
    }



}
