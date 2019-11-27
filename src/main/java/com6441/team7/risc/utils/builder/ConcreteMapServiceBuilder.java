package com6441.team7.risc.utils.builder;

import com6441.team7.risc.api.model.*;

import java.util.Map;
import java.util.Set;

/**
 * this class is used to build data in MapService
 */
public class ConcreteMapServiceBuilder extends AbstractMapServiceBuilder {


    /**
     * call mapServiceEntity methods to set countries
     * @param countries countries in the mapService
     */
    @Override
    public void buildCountries(Set<Country> countries) {
        mapServiceEntity.setCountries(countries);
    }

    /**
     * call mapServiceEntity methods to set continents
     * @param continents continents in the mapService
     */
    @Override
    public void buildContinents(Set<Continent> continents) {
        mapServiceEntity.setContinents(continents);

    }

    /**
     * call mapServiceEntity methods to set ContinentCountriesMap
     * @param map continentCountryMap in mapService
     */
    @Override
    public void buildCountryContinentRelations(Map<Integer, Set<Integer>> map) {
        mapServiceEntity.setContinentCountriesMap(map);
    }

    /**
     * call mapServiceEntity methods to set adjacencyCountries
     * @param map adjacencyCountries
     */
    @Override
    public void buildAdjacencyCountries(Map<Integer, Set<Integer>> map) {
        mapServiceEntity.setAdjacencyCountriesMap(map);
    }

    /**
     * call mapServiceEntity methods to set the game state
     * @param gameState game state
     */
    @Override
    public void buildGameState(GameState gameState) {
        mapServiceEntity.setGameState(gameState);
    }
}
