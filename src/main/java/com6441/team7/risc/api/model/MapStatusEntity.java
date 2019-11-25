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

    /**
     * the inner class to build the MapStatusEntity
     */
    public static class MapStatusEntityBuilder {

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
         * create new instance of MapStatusEntityBuilder
         * @return an object of MapStatusEntityBuilder
         */
        public static MapStatusEntityBuilder newInstance() {
            return new MapStatusEntityBuilder();
        }

        /**
         * build country set
         * @param countries the set of countries
         * @return MapStatusEntityBuilder
         */
        public MapStatusEntityBuilder countries(Set<Country> countries) {
            this.countries = countries;
            return this;
        }

        /**
         * build continents set
         * @param continents the set of continents
         * @return MapStatusEntityBuilder
         */
        public MapStatusEntityBuilder continents(Set<Continent> continents) {
            this.continents = continents;
            return this;
        }

        /**
         * build adjacencyCountriesMap
         * @param adjacencyCountriesMap the map contains neighboring countries information
         * @return MapStatusEntityBuilder
         */
        public MapStatusEntityBuilder adjacencyCountriesMap(Map<Integer, Set<Integer>> adjacencyCountriesMap) {
            this.adjacencyCountriesMap = adjacencyCountriesMap;
            return this;
        }

        /**
         * build continentCountriesMap
         * @param continentCountriesMap the map contains continent Id and corresponding countries
         * @return MapStatusEntityBuilder
         */
        public MapStatusEntityBuilder continentCountriesMap(Map<Integer, Set<Integer>> continentCountriesMap) {
            this.continentCountriesMap = continentCountriesMap;
            return this;
        }

        /**
         * build gameState
         * @param gameState the game state
         * @return MapStatusEntityBuilder
         */
        public MapStatusEntityBuilder gameState(GameState gameState) {
            this.gameState = gameState;
            return this;
        }

        /**
         * create the MapStatusEntity object
         * @return new object of MapStatusEntity
         */
        public MapStatusEntity build() {
            return new MapStatusEntity(countries, continents, adjacencyCountriesMap,
                    continentCountriesMap, gameState);
        }
    }

}
