package com6441.team7.risc.api.model;
import org.apache.commons.lang3.StringUtils;
import org.jgrapht.Graph;
import org.jgrapht.graph.*;

import java.util.*;


public class MapService {

     private Map<Integer, Country> countries = new HashMap<>();
     private Map<Integer, Continent> continents = new HashMap<>();
     private Map<String, Integer> countryUniqueIdentifierAndNameTable = new HashMap<>();
     private Map<String, Integer> continentUniqueIdentifierAndNameTable = new HashMap<>();
     private Map<Integer, Set<Integer>> adjascentCountriesMap =  new HashMap<>();
     private Map<Integer, Set<Integer>> continentCountriesMap = new HashMap<>();
     private Graph<Integer, DefaultEdge> directedGraph = new DefaultDirectedGraph<>(DefaultEdge.class);

    public void addCountry(Country country) {
        countries.put(country.getId(), country);
    }

    public void addCountry(Collection<Country> countriesCollection){}

    public void removeCountryByName(String countryName) { }

    public void removeCountryById(int id) { }

    public void addContinent(Continent continent){}

    public void addContinents(Collection<Continent> continentsCollection){}

    public void removeContinentByName(String continentName){}

    public void removeContinentById(int id){}

    public void addNeighboringCountriesByName(List<String> contriesName){}

    public void addNeighboringCountriesById(List<Integer> countriesId){}

    public void addNeighboringCountries(Map<Integer, Set<Integer>> map){}

    public void removeNeighboringCountriesByName(String name){}

    public void removeNeighboringCountriesById(int id){}

    public Optional<Graph<Integer, DefaultEdge>> showMap(){return Optional.empty();}


    public void addEdge(int src, List<Integer> dest){}

    public boolean isStronlyConnectec(){return false;}

    public Map<Integer, Country> getCountries() {
        return countries;
    }

    public Map<Integer, Continent> getContinents() {
        return continents;
    }

    public Optional<Country> getCountryByName(String name){ return Optional.empty(); }

    public Optional<Country> getCountryById(int id){return Optional.empty();}

    public Optional<Continent> getContinentByName(String name){return Optional.empty();}

    public Optional<Continent> getContinentById(int id){return Optional.empty();}

    public Map<String, Integer> getCountryUniqueIdentifierAndNameTable() {
        return countryUniqueIdentifierAndNameTable;
    }

    public Map<String, Integer> getContinentUniqueIdentifierAndNameTable() {
        return continentUniqueIdentifierAndNameTable;
    }

    public Map<Integer, Set<Integer>> getAdjascentCountriesMap() {
        return adjascentCountriesMap;
    }

    public Map<Integer, Set<Integer>> getContinentCountriesMap() {
        return continentCountriesMap;
    }

    public Graph<Integer, DefaultEdge> getDirectedGraph() {
        return directedGraph;
    }

    private String convertNameToKeyFormat(String name) {
        return StringUtils.deleteWhitespace(name).toLowerCase();
    }

}
