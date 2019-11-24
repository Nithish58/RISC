package com6441.team7.risc.api.model;

import java.util.Locale;
import java.util.Objects;

import static com6441.team7.risc.api.RiscConstants.EOL;
import static com6441.team7.risc.api.RiscConstants.WHITESPACE;

/**
 * This class stores country information
 */
public class Country{

    /**
     * The id of country in the map
     */
    private Integer id;

    /**
     * The countryName of the country
     */
    private String countryName;

    /**
     * The Id of continent in which this country is located
     */
    private Integer continentIdentifier;

    /**
     * The countryName of continent where this country is located
     */
    private String continentName;

    /**
     * The X Cordinate of country's location in map
     */
    private Integer coordinateX;

    /**
     * The y cordinate of country's location in map
     */
    private Integer coordinateY;

    /**
     * The Player which owns this country
     */
    private Player player;

    /**
     * the initial soldiers of this country is set to 0
     */
    private int soldiers = 0;

    /**
     * The constructor to initialize country with parameter id and countryName
     * @param id The id of country which is being initialized
     * @param countryName The countryName of country which is being initialized
     */
    public Country(Integer id, String countryName) {
        this.id = id;
        this.countryName = countryName.toLowerCase(Locale.CANADA);
    }

    public Country(){}

    /**
     * The constructor to initialize the country with parameter id, countryName and continentName
     * @param id The id of country which is being initialized
     * @param countryName The countryName of country which is being initialized
     * @param continentName The countryName of continent where the country is loacated
     */
    public Country(Integer id, String countryName, String continentName){
        this.id = id;
        this.countryName = countryName.toLowerCase(Locale.CANADA);
        this.continentName = continentName;
    }

    /**
     * The constructor to initialize the country with parameter id, countryName and continentName
     * @param id The id of country which is being initialized
     * @param countryName The countryName of country which is being initialized
     * @param continentId The Id of continent where the country is loacated
     */
    public Country(Integer id, String countryName, Integer continentId){
        this.id = id;
        this.countryName = countryName.toLowerCase(Locale.CANADA);
        this.continentIdentifier = continentId;
    }

    /**
     * Getter method to get Id of country
     * @return Id of country
     */
    public Integer getId() {
        return id;
    }

    /**
     * Getter method to get Name of country
     * @return Name of country.
     */
    public String getCountryName() {
        return countryName;
    }

    /**
     * setter method to set continent Id to country
     * @param continentIdentifier Continent Identifier
     * @return the country
     */
    public Country setContinentIdentifier(Integer continentIdentifier) {
        this.continentIdentifier = continentIdentifier;
        return this;
    }

    /**
     * To get continent Id of country
     * @return continent Id
     */
    public Integer getContinentIdentifier() {
        return continentIdentifier;
    }

    /**
     * To set continent countryName
     * @param continentName continent countryName
     * @return country
     */
    public Country setContinentName(String continentName){
        this.continentName = continentName.toLowerCase(Locale.CANADA);
        return this;
    }

    /**
     *To get continent countryName of country
     * @return continent countryName
     */
    public String getContinentName() {
        return continentName;
    }

    /**
     * To get x cordinate of country's location
     * @return x xordinate of country's location
     */
    public Integer getCoordinateX() {
        return coordinateX;
    }

    /**
     * To set x coordinates of country's location
     * @param coordinateX value of X coordinate
     * @return country
     */
    public Country setCoordinateX(Integer coordinateX) {
        this.coordinateX = coordinateX;
        return this;
    }

    /**
     *To get y cordinate of country's location
     * @return value of Y coordinate
     */
    public Integer getCoordinateY() {
        return coordinateY;
    }

    /**
     * To set y cordinate of country's location
     * @param coordinateY value of y coordinate
     * @return country
     */
    public Country setCoordinateY(Integer coordinateY) {
        this.coordinateY = coordinateY;
        return this;
    }

    /**
     * To get player who occupies the country
     * @return Player whoc occupies the country
     */
    public Player getPlayer() {
        return player;
    }

    /**
     *To set the ownership of the country to player
     * @param player transfering ownership to the player
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     *To get the number of armies associated with this country
     * @return number of soldiers
     */
    public Integer getSoldiers() {
        return soldiers;
    }

    /**
     * To set the number of armies to the the country
     * @param soldiers Soldiers
     */
    public void setSoldiers(Integer soldiers) {
    	
    	//soldiers are reset to 100 when int limit exceeded (especially for cheater)
    	if(this.soldiers > 1000000) {
    		this.soldiers = 100;
    		return;
    	} 
    	
        this.soldiers = soldiers; 
        
        //For showing Observer Pattern during demo, commented code below not removed.
        
        //setChanged();
        //notifyObservers(this);
        
    }

    /**
     * To add additional armies to the country
     * @param number the numbers to add to existing armies
     */
    public void addSoldiers(int number){
        this.soldiers += number;
        
        //setChanged();
        //notifyObservers(this);
    }

    /**
     * The reduce the armies of the country by given number
     * @param number the number to reduce from present armies.
     */
    public void removeSoldiers(int number){
        this.soldiers -= number;
        
        //setChanged();
        //notifyObservers(this);
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
                && Objects.equals(countryName, country.countryName);
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public void setSoldiers(int soldiers) {
        this.soldiers = soldiers;
    }

    /**
     * Returns hashcode of object
     * Used for Setting IDs (Unique Value)
     * @return hashcode of object
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     *Used to print country information
     * @return id countryName continentIdentifier coordinateX CoordinateY.
     */
    @Override
    public String toString() {
        return id + WHITESPACE +
                countryName + WHITESPACE +
                continentIdentifier + WHITESPACE +
                coordinateX + WHITESPACE +
                coordinateY + EOL;
    }
    
	/*
	 * @Override public void addObserver(Observer observer) {
	 * 
	 * super.addObserver(observer); }
	 */
    
}
