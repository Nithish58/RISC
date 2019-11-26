package com6441.team7.risc.utils.builder;

import com6441.team7.risc.api.model.*;

import java.util.Map;
import java.util.Set;

public class ConcreteMapServiceBuilder extends AbstractMapServiceBuilder {


    @Override
    public void buildCountries(Set<Country> countries) {
        mapServiceEntity.setCountries(countries);
    }

    @Override
    public void buildContinents(Set<Continent> continents) {
        mapServiceEntity.setContinents(continents);

    }

    @Override
    public void buildCountryContinentRelations(Map<Integer, Set<Integer>> map) {
        mapServiceEntity.setContinentCountriesMap(map);
    }

    @Override
    public void buildAdjacencyCountries(Map<Integer, Set<Integer>> map) {
        mapServiceEntity.setAdjacencyCountriesMap(map);
    }

    @Override
    public void buildGameState(GameState gameState) {
        mapServiceEntity.setGameState(gameState);
    }
}
