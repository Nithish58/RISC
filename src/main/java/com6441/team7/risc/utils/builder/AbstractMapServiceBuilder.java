package com6441.team7.risc.utils.builder;

import com6441.team7.risc.api.model.*;

import java.util.Map;
import java.util.Set;

/**
 * the class is used to build mapServiceEntity
 */
public abstract class AbstractMapServiceBuilder {

    /**
     * a reference of MapStatusEntity to store data in map
     */
    protected MapStatusEntity mapServiceEntity;

    /**
     * get the reference of mapStatusEntity
     * @return mapServiceEntity
     */
    public MapStatusEntity getMapServiceEntity(){
        return mapServiceEntity;
    }

    /**
     * create a new mapStatusEntity
     */
    public void createNewMapStatusEntity(){
        mapServiceEntity = new MapStatusEntity();
    }


    /**
     * build the countries
     * @param countries countries in the mapService
     */
    public abstract void buildCountries(Set<Country> countries);

    /**
     * build the continents
     * @param continents continents in the mapService
     */
    public abstract void buildContinents(Set<Continent> continents);

    /**
     * build the CountryContinentMap
     * @param map continentCountryMap in mapService
     */
    public abstract void buildCountryContinentRelations(Map<Integer, Set<Integer>> map);

    /**
     * build adjacencyCountries
     * @param map adjacencyCountries
     */
    public abstract void buildAdjacencyCountries(Map<Integer, Set<Integer>> map);

    /**
     * build game state
     * @param gameState game state
     */
    public abstract void buildGameState(GameState gameState);
}
