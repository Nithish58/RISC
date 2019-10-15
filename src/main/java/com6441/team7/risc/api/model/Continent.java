package com6441.team7.risc.api.model;
/**
 *
 */

import java.util.Locale;
import java.util.Objects;

/**
 * This class stores continent information
 */
public class Continent {
    private final int id;
    private final String name;
    private int continentValue;
    private String color;

    /**
     *
     * @param id
     * @param name
     */
    public Continent(int id, String name) {
        this.id = id;
        this.name = name.toLowerCase(Locale.CANADA);
    }

    /**
     *
     * @param id
     * @param name
     * @param continentValue
     */
    public Continent(int id, String name, int continentValue){
        this.id = id;
        this.name = name.toLowerCase(Locale.CANADA);
        this.continentValue = continentValue;
    }


    /**
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param continentValue
     * @return
     */
    public Continent setContinentValue(int continentValue) {
        this.continentValue = continentValue;
        return this;
    }

    /**
     * This method is to get continent value.
     * @return
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
     *
     * @return hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
