package com6441.team7.risc.api.model;

import java.util.Map;
import java.util.Set;

/**
 * This class is used to used to store the data in mapService
 */
public class MapStatusEntity {

    /**
     * default constructor
     */
    public MapStatusEntity(){}

    /**
     * constructor
     * @param countries a set of countries
     * @param continents a set of continents
     * @param adjacencyCountriesMap the map to store adjacency countries id
     * @param continentCountriesMap the map to store continent id and corresponding country id
     * @param gameState the game state
     */
    private MapStatusEntity(Set<Country> countries, Set<Continent> continents,
                            Map<Integer, Set<Integer>> adjacencyCountriesMap,
                            Map<Integer, Set<Integer>> continentCountriesMap,
                            GameState gameState) {
        this.countries = countries;
        this.continents = continents;
        this.adjacencyCountriesMap = adjacencyCountriesMap;
        this.continentCountriesMap = continentCountriesMap;
        this.gameState = gameState;
    }

    /**
     * a set of countries
     */
    private Set<Country> countries;

    /**
     * a set of continents
     */
    private Set<Continent> continents;

    /**
     * store each country ID, and its neighbor countries ID
     */
    private Map<Integer, Set<Integer>> adjacencyCountriesMap;

    /**
     * store each continent ID, and its belonging countries ID
     */
    private Map<Integer, Set<Integer>> continentCountriesMap;

    /**
     * the state of the game
     */
    private GameState gameState;


    /**
     * get the set of countries
     * @return the set of countries
     */
    public Set<Country> getCountries() {
        return countries;
    }

    /**
     * get the set of continents
     * @return the set of continents
     */
    public Set<Continent> getContinents() {
        return continents;
    }

    /**
     * get the adjacencyCountriesMap
     * @return adjacencyCountriesMap
     */
    public Map<Integer, Set<Integer>> getAdjacencyCountriesMap() {
        return adjacencyCountriesMap;
    }

    /**
     * get the continentCountriesMap
     * @return continentCountriesMap
     */
    public Map<Integer, Set<Integer>> getContinentCountriesMap() {
        return continentCountriesMap;
    }

    /**
     * get the game state
     * @return gameState
     */
    public GameState getGameState() {
        return gameState;
    }


    public void setCountries(Set<Country> countries){
        this.countries = countries;
    }

    public void setContinents(Set<Continent> continents) {
        this.continents = continents;
    }

    public void setAdjacencyCountriesMap(Map<Integer, Set<Integer>> adjacencyCountriesMap) {
        this.adjacencyCountriesMap = adjacencyCountriesMap;
    }

    public void setContinentCountriesMap(Map<Integer, Set<Integer>> continentCountriesMap) {
        this.continentCountriesMap = continentCountriesMap;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

}
