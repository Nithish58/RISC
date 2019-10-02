package com6441.team7.risc.api.model;

import org.apache.commons.lang3.StringUtils;
import org.jgrapht.Graph;
import org.jgrapht.graph.*;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;


public class MapService {
    private Set<Country> countries = new HashSet<>();
    private Set<Continent> continents = new HashSet<>();
    private Map<Integer, Set<Integer>> adjacencyCountriesMap = new HashMap<>();
    private Map<Integer, Set<Integer>> continentCountriesMap = new HashMap<>();
    private Graph<Integer, DefaultEdge> directedGraph = new DefaultDirectedGraph<>(DefaultEdge.class);

    public void addCountry(Country country) {
        countries.add(country);
    }

    public void addCountry(Collection<Country> countriesCollection) {
        countriesCollection.forEach(country -> {
            countries.add(country);
            putCountryIntoContinentCountriesMap(country);
        });
    }

    private void putCountryIntoContinentCountriesMap(Country country) {
        Integer continentId = country.getContinentIdentifier();
        if (continentCountriesMap.containsKey(continentId)) {
            continentCountriesMap.get(continentId).add(country.getId());
            return;
        }
        Set<Integer> countriesIdSet = new HashSet<>();
        countriesIdSet.add(country.getId());
        continentCountriesMap.put(continentId, countriesIdSet);
    }

    public void addContinents(Collection<Continent> continentsCollection) {
        continents.addAll(continentsCollection);
    }

    public void addNeighboringCountries(Map<Integer, Set<Integer>> map) {
        map.forEach((key, value) -> adjacencyCountriesMap.put(key, value));
    }

    public Set<Country> getCountries() {
        return countries;
    }

    public Set<Continent> getContinents() {
        return continents;
    }

    public boolean countryNameExist(String countryName) {
        return Optional.ofNullable(countryName)
                .map(this::convertNameToKeyFormat)
                .filter(name -> getCountryNameSet().contains(name))
                .isPresent();
    }

    public boolean continentNameExist(String continentName) {
        return Optional.ofNullable(continentName)
                .map(this::convertNameToKeyFormat)
                .filter(name -> getContinentNameSet().contains(name))
                .isPresent();
    }

    public boolean continentIdExist(Integer continentId) {
        return Optional.ofNullable(continentId)
                .filter(id -> getContinentIdSet().contains(id))
                .isPresent();
    }

    public boolean countryIdExist(Integer countryId) {
        return Optional.ofNullable(countryId)
                .filter(id -> getCountryIdSet().contains(id))
                .isPresent();
    }

    public boolean countryExist(Country country) {
        return Optional.ofNullable(country)
                .filter(c -> countryNameExist(c.getCountryName()) || countryIdExist(c.getId()))
                .isPresent();
    }

    public void removeCountryByName(String countryName) {
    }

    public void removeCountryById(int id) {
    }

    public void addContinent(Continent continent) {
    }

    public void removeContinentByName(String continentName) {
    }

    public void removeContinentById(int id) {
    }

    public void addNeighboringCountriesByName(List<String> contriesName) {
    }

    public void addNeighboringCountriesById(List<Integer> countriesId) {
    }

    public void removeNeighboringCountriesByName(String name) {
    }

    public void removeNeighboringCountriesById(int id) {
    }

    public Optional<Graph<Integer, DefaultEdge>> showMap() {
        return Optional.empty();
    }


    public void addEdge(int src, List<Integer> dest) {
    }

    public boolean isStronlyConnectec() {
        return false;
    }

    public Optional<Country> getCountryByName(String name) {
        return Optional.empty();
    }

    public Optional<Country> getCountryById(int id) {
        return Optional.empty();
    }

    public Optional<Continent> getContinentByName(String name) {
        return Optional.empty();
    }

    public Optional<Continent> getContinentById(int id) {
        return Optional.empty();
    }

    public Map<Integer, Set<Integer>> getAdjacencyCountriesMap() {
        return adjacencyCountriesMap;
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

    private Set<String> getCountryNameSet() {
        return countries.stream().map(Country::getCountryName)
                .map(this::convertNameToKeyFormat).collect(Collectors.toSet());
    }

    private Set<String> getContinentNameSet() {
        return continents.stream().map(Continent::getName)
                .map(this::convertNameToKeyFormat).collect(Collectors.toSet());
    }

    private Set<Integer> getCountryIdSet() {
        return countries.stream().map(Country::getId).collect(Collectors.toSet());
    }

    private Set<Integer> getContinentIdSet() {
        return continents.stream().map(Continent::getId).collect(Collectors.toSet());
    }
}
