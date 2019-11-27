package com6441.team7.risc.api.model;

/**
 * This class stores map category
 */
public enum  MapCategory {
	/**
	 * Conquest
	 */
    CONQUEST("conquest"),
    /**
     * Domination
     */
    DOMINATION("domination"),
    /**
     * Unknown
     */
    UNKNOWN("unknown");

    /**
     * the name of the map category
     */
    private String name;

    /**
     * constructor
     * @param name of map category
     */
    MapCategory(String name){
        this.name = name;
    }

    /**
     * get name of the map category
     * @return name of map category
     */
    public String getName(){
        return name;
    }
}
