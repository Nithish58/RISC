package com6441.team7.risc.api.model;

/**
 * This class stores card category
 */
public enum Card {
    INFANTRY("infantry"),
    CAVALRY("cavalry"),
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
     * get name of the card
     * @return name of card type
     */
    public String getName() {
        return name;
    }
}
