package com6441.team7.risc.api.model;

import java.util.Locale;
import java.util.Objects;

/**
 * This class stores continent information
 */
public class Continent {

    /**
     * It reflects the ID of continents in a given map
     */
    private int id;

    /**
     * The name of Continent
     */
    private String name;

    /**
     * The continent value in the map
     */
    private int continentValue;
    
    /**
     * Continent color value
     */
    private String color;

    /**
     *Constructor to initialize continent with id and name as parameter.
     * @param id The id given to new continent
     * @param name The name given to new continent
     */
    public Continent(int id, String name) {
        this.id = id;
        this.name = name.toLowerCase(Locale.CANADA);
    }

    public Continent(){}

    /**
     * Constructor to initialize continent with id, name and continentValue as paramater
     * @param id The id given to new continent
     * @param name The name given to new continent
     * @param continentValue the power of continent
     */
    public Continent(int id, String name, int continentValue){
        this.id = id;
        this.name = name.toLowerCase(Locale.CANADA);
        this.continentValue = continentValue;
    }


    /**
     *Getter method to get continent name
     * @return continent name
     */
    public String getName() {
        return name;
    }

    /**
     *setter method to set continent value
     * @param continentValue the power of continent
     * @return continent
     */
    public Continent setContinentValue(int continentValue) {
        this.continentValue = continentValue;
        return this;
    }

    /**
     * Getter method to get continent value.
     * @return continent value
     */
    public int getContinentValue() {
        return continentValue;
    }

    /**
     * This helps in setting color to continent.
     * @param color The name of color.
     * @return The continent whose color is set.
     */
    public Continent setColor(String color) {
        this.color = color;
        return this;
    }

    /**
     * This is used get color of any continents
     * @return return color of continents
     */
    public String getColor() {
        return color;
    }

    /**
     * This is used get Id of any continents
     * @return return id of continents
     */
    public int getId() {
        return id;
    }


    /**
     * It will compare the continent with another continent
     * @param o object with which it will be compared.
     * @return true or false depending upon comparision.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Continent continent = (Continent) o;
        return id == continent.id &&
                Objects.equals(name, continent.name);
    }

    /**
     *Get hashcode of object
     *Used for Setting IDs (Unique Value)
     * @return hashcode of object
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
