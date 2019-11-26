package com6441.team7.risc.utils.builder;

import com6441.team7.risc.api.model.*;

import java.util.Map;
import java.util.Set;

public abstract class AbstractMapServiceBuilder {
    protected MapStatusEntity mapServiceEntity;

    public MapStatusEntity getMapServiceEntity(){
        return mapServiceEntity;
    }

    public void createNewMapStatusEntity(){
        mapServiceEntity = new MapStatusEntity();
    }


    public abstract void buildCountries(Set<Country> countries);
    public abstract void buildContinents(Set<Continent> continents);
    public abstract void buildCountryContinentRelations(Map<Integer, Set<Integer>> map);
    public abstract void buildAdjacencyCountries(Map<Integer, Set<Integer>> map);
    public abstract void buildGameState(GameState gameState);
}
