package com6441.team7.risc.api.model;

/**
 * This class stores card category
 */
public enum Card {
	/**
	 * Infantry 
	 */
    INFANTRY("infantry"),
    /**
     * Cavalry
     */
    CAVALRY("cavalry"),
    /**
     * Artillery
     */
    ARTILLERY("artillery");

    /**
     * the name of the card
     */
    private String name;

    /**
     * constructor
     * @param name of card type
     */
    Card(String name) {
        this.name = name;
    }

    /**
     * default constructor
     */
    Card(){}

    /**
     * get name of the card
     * @return name of card type
     */
    public String getName() {
        return name;
    }
}
