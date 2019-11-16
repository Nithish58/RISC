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
     * @param name
     */
    Card(String name) {
        this.name = name;
    }

    /**
     * get name of the card
     * @return name
     */
    public String getName() {
        return name;
    }
}
