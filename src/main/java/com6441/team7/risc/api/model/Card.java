package com6441.team7.risc.api.model;

/**
 * This class stores card category
 */
public enum Card {
    INFANTRY("infantry"),
    CAVALRY("cavalry"),
    ARTILLERY("artillery");

    private String name;

    Card(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
