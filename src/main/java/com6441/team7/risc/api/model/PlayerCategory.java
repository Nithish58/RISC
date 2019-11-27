package com6441.team7.risc.api.model;

/**
 * This class stores player category
 */
public enum PlayerCategory {
	/**
	 * Aggressive
	 */
    AGGRESSIVE("aggressive"),
    /**
     * Random
     */
    RANDOM("random"),
    /**
     * Cheater
     */
    CHEATER("cheater"),
    /**
     * Benevolent
     */
    BENEVOLENT("benevolent"),
    /**
     * Human
     */
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
