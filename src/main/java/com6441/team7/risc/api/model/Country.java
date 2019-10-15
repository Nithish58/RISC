package com6441.team7.risc.api.model;

import java.util.Locale;
import java.util.Objects;

import static com6441.team7.risc.api.RiscConstants.EOL;
import static com6441.team7.risc.api.RiscConstants.WHITESPACE;

/**
 * This class stores country information
 */
public class Country {
    private final Integer id;
    private final String name;
    private Integer continentIdentifier;
    private String continentName;
    private Integer coordinateX;
    private Integer coordinateY;
    private Player player;
    private int soldiers = 0;

    public Country(Integer id, String countryName) {
        this.id = id;
        this.name = countryName.toLowerCase(Locale.CANADA);
    }

    public Country(Integer id, String countryName, String continentName){
        this.id = id;
        this.name  = countryName.toLowerCase(Locale.CANADA);
        this.continentName = continentName;
    }

    public Country(Integer id, String countryName, Integer continentId){
        this.id = id;
        this.name = countryName.toLowerCase(Locale.CANADA);
        this.continentIdentifier = continentId;
    }


    public Integer getId() {
        return id;
    }

    public String getCountryName() {
        return name;
    }


    public Country setContinentIdentifier(Integer continentIdentifier) {
        this.continentIdentifier = continentIdentifier;
        return this;
    }

    public Integer getContinentIdentifier() {
        return continentIdentifier;
    }


    public Country setContinentName(String continentName){
        this.continentName = continentName.toLowerCase(Locale.CANADA);
        return this;
    }

    public String getContinentName() {
        return continentName;
    }

    public Integer getCoordinateX() {
        return coordinateX;
    }

    public Country setCoordinateX(Integer coordinateX) {
        this.coordinateX = coordinateX;
        return this;
    }

    public Integer getCoordinateY() {
        return coordinateY;
    }

    public Country setCoordinateY(Integer coordinateY) {
        this.coordinateY = coordinateY;
        return this;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Integer getSoldiers() {
        return soldiers;
    }

    public void setSoldiers(Integer soldiers) {
        this.soldiers = soldiers;
    }

    public void addSoldiers(int number){
        this.soldiers += number;
    }

    public void removeSoldiers(int number){
        this.soldiers -= number;
    }

    /**
     * It will compare this country with another country.
     * @param o object with which it will be compared.
     * @return true or false depending upon comparision.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Country country = (Country) o;
        return Objects.equals(id, country.id)
                && Objects.equals(name, country.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return id + WHITESPACE +
                name + WHITESPACE +
                continentIdentifier + WHITESPACE +
                coordinateX + WHITESPACE +
                coordinateY + EOL;
    }
}
