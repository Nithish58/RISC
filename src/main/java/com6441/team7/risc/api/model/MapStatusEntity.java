package com6441.team7.risc.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import java.util.Map;
import java.util.Set;

public class MapStatusEntity {
    public MapStatusEntity(){}
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

    public static class MapStatusEntityBuilder {

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

        public static MapStatusEntityBuilder newInstance() {
            return new MapStatusEntityBuilder();
        }

        public MapStatusEntityBuilder countries(Set<Country> countries) {
            this.countries = countries;
            return this;
        }

        public MapStatusEntityBuilder continents(Set<Continent> continents) {
            this.continents = continents;
            return this;
        }

        public MapStatusEntityBuilder adjacencyCountriesMap(Map<Integer, Set<Integer>> adjacencyCountriesMap) {
            this.adjacencyCountriesMap = adjacencyCountriesMap;
            return this;
        }

        public MapStatusEntityBuilder continentCountriesMap(Map<Integer, Set<Integer>> continentCountriesMap) {
            this.continentCountriesMap = continentCountriesMap;
            return this;
        }

        public MapStatusEntityBuilder gameState(GameState gameState) {
            this.gameState = gameState;
            return this;
        }

        public MapStatusEntity build() {
            return new MapStatusEntity(countries, continents, adjacencyCountriesMap,
                    continentCountriesMap, gameState);
        }


    }
    public Set<Country> getCountries() {
        return countries;
    }

    public Set<Continent> getContinents() {
        return continents;
    }

    public Map<Integer, Set<Integer>> getAdjacencyCountriesMap() {
        return adjacencyCountriesMap;
    }

    public Map<Integer, Set<Integer>> getContinentCountriesMap() {
        return continentCountriesMap;
    }

    public GameState getGameState() {
        return gameState;
    }
}
