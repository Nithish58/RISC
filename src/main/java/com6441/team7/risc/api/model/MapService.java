package com6441.team7.risc.api.model;

import org.apache.commons.lang3.StringUtils;
import org.jgrapht.Graph;
import org.jgrapht.alg.connectivity.KosarajuStrongConnectivityInspector;
import org.jgrapht.graph.*;
import java.util.*;
import java.util.stream.Collectors;
import static java.util.Objects.isNull;


public class MapService extends Observable {
    private Set<Country> countries = new HashSet<>();
    private Set<Continent> continents = new HashSet<>();
    private Map<Integer, Set<Integer>> adjacencyCountriesMap = new HashMap<>();
    private Map<Integer, Set<Integer>> continentCountriesMap = new HashMap<>();
    private Graph<Integer, DefaultEdge> directedGraph = new DefaultDirectedGraph<>(DefaultEdge.class);
    
    private GameState gameState = GameState.LOAD_MAP;
    
    static int counter=0;
    
    public int mapId=-1;

    public MapService(){
    	counter++;
    	this.mapId=counter;
    	System.out.println(mapId);
    }

    @Override
    public void addObserver(Observer observer) {
        super.addObserver(observer);
        setState(gameState);
    }

    public GameState getGameState(){
        return gameState;
    }


    public void setState(GameState gameState){
        this.gameState = gameState;
        setChanged();
        notifyObservers(gameState);
    }

    public boolean isMapValid() {
        return isStronglyConnected();
    }



    public void addCountry(Country country) {
        countries.add(country);

        String continentName = convertNameToKeyFormat(country.getContinentName());
        int countryId = country.getId();

        continents.stream()
                .filter(continent -> convertNameToKeyFormat(continent.getName()).equals(continentName))
                .map(Continent::getId)
                .findFirst()
                .ifPresent(continentId -> continentCountriesMap.get(continentId).add(countryId));

     //   view.displayMessage("country " + country.getCountryName() + " is successfully added.");

    }

    public void addCountry(Collection<Country> countriesCollection) {
        countriesCollection.forEach(country -> {
            countries.add(country);
            putCountryIntoContinentCountriesMap(country);
        });
    }

    private void putCountryIntoContinentCountriesMap(Country country) {
        Integer continentId = country.getContinentIdentifier();
        continentCountriesMap.get(continentId).add(country.getId());
    }

    public void addContinent(Continent continent) {
        continents.add(continent);

        if (!continentCountriesMap.containsKey(continent.getId())) {
            continentCountriesMap.put(continent.getId(), new HashSet<>());
        }

       // view.displayMessage("continent " + continent.getName() + " is successfully added.");

    }

    public void addContinent(Collection<Continent> continentsCollection) {
        continents.addAll(continentsCollection);

        Set<Integer> continentId = continentsCollection.stream()
                .map(Continent::getId)
                .collect(Collectors.toSet());

        continentId.forEach(id -> continentCountriesMap.put(id, new HashSet<Integer>()));

    }

    public void addNeighboringCountries(Map<Integer, Set<Integer>> map) {
        map.forEach((key, value) -> adjacencyCountriesMap.put(key, value));
    }

    public void addNeighboringCountries(String country, String neighboringCountry) {

        int countryId = findCorrespondingIdByCountryName(country).get();
        int neghboringCountryId = findCorrespondingIdByCountryName(neighboringCountry).get();

        if (adjacencyCountriesMap.containsKey(countryId)) {
            adjacencyCountriesMap.get(countryId).add(neghboringCountryId);
        } else {
            Set<Integer> neighboringCountrySet = new HashSet<>();
            neighboringCountrySet.add(neghboringCountryId);
            adjacencyCountriesMap.put(countryId, neighboringCountrySet);
        }

      //  view.displayMessage("Neighboring countries " + country + " " + neighboringCountry + " is successfully added.");
    }

    public void removeNeighboringCountriesByName(String country, String neighboringCountry) {

        int countryId = findCorrespondingIdByCountryName(country).get();
        int neghboringCountryId = findCorrespondingIdByCountryName(neighboringCountry).get();

        adjacencyCountriesMap.get(countryId).remove(neghboringCountryId);

        //view.displayMessage("neighboring country " + neighboringCountry + " is sucessfully removed from " + country);

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

    public boolean countryNameNotExist(String countryName) {
        return !countryNameExist(countryName);
    }

    public boolean continentNameExist(String continentName) {
        return Optional.ofNullable(continentName)
                .map(this::convertNameToKeyFormat)
                .filter(name -> getContinentNameSet().contains(name))
                .isPresent();
    }

    public boolean continentNameNotExist(String continentName) {
        return !continentNameExist(continentName);
    }

    public boolean continentIdExist(Integer continentId) {
        return Optional.ofNullable(continentId)
                .filter(id -> getContinentIdSet().contains(id))
                .isPresent();
    }

    public boolean continentIdNotExist(Integer continentId) {
        return !continentIdExist(continentId);
    }

    public boolean countryIdExist(Integer countryId) {
        return Optional.ofNullable(countryId)
                .filter(id -> getCountryIdSet().contains(id))
                .isPresent();
    }

    public boolean countryIdNotExist(Integer countryId) {
        return !countryIdExist(countryId);
    }

    public boolean countryExist(Country country) {
        return Optional.ofNullable(country)
                .filter(c -> countryNameExist(c.getCountryName()) || countryIdExist(c.getId()))
                .isPresent();
    }


    public void removeCountryByName(String countryName) {
        if (isNull(countryName)) {
            return;
        }

        Optional<Country> toBeRemoved = findCountryToBeRemoved(countryName);

        toBeRemoved.ifPresent(country -> {
            countries.remove(country);
            removeCountryFromContinentCountryMap(country);
            removeCountryFromAdjacentCountryMap(country);


        });
    }

    private void removeCountryFromContinentCountryMap(Country country) {

        int countryId = country.getId();
        findCorrespondingIdByContinentName(country.getContinentName())
                .ifPresent(continentId -> continentCountriesMap.get(continentId).remove(countryId));
    }



    private void removeCountryFromAdjacentCountryMap(Country country) {
        int countryId = country.getId();
        adjacencyCountriesMap.remove(countryId);

        for (Map.Entry<Integer, Set<Integer>> entry : adjacencyCountriesMap.entrySet()) {
            entry.getValue().remove(countryId);
        }

    }

    public Optional<Integer> findCorrespondingIdByContinentName(String name) {
        return continents.stream()
                .filter(continent -> convertNameToKeyFormat(continent.getName()).equals(convertNameToKeyFormat(name)))
                .map(Continent::getId)
                .findFirst();
    }

    public Optional<Integer> findCorrespondingIdByCountryName(String name) {
        return countries.stream()
                .filter(country -> convertNameToKeyFormat(country.getCountryName()).equals(convertNameToKeyFormat(name)))
                .map(Country::getId)
                .findFirst();
    }

    public Optional<String> findCorrespondingNameByContidentId(Integer id){
        return continents.stream()
                .filter(continent -> continent.getId() == id)
                .map(Continent::getName)
                .findFirst();
    }


    private Optional<Country> findCountryToBeRemoved(String countryName) {
        String normalizedCountryName = convertNameToKeyFormat(countryName);
        return countries.stream()
                .filter(country -> convertNameToKeyFormat(country.getCountryName()).equals(normalizedCountryName))
                .findFirst();


    }

    public void removeCountryById(int id) {
    }

    public void removeContinentByName(String continentName) {
        if (isNull(continentName)) {
            return;
        }
        String normalizedContinentName = convertNameToKeyFormat(continentName);
        Optional<Continent> toBeRemoved = continents.stream()
                .filter(continent -> convertNameToKeyFormat(continent.getName()).equals(normalizedContinentName))
                .findFirst();
        toBeRemoved.ifPresent(continent -> {
            continents.remove(continent);
            continentCountriesMap.remove(continent.getId());
        });

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


    public void addEdge(int src, Set<Integer> dest) {
        if (!directedGraph.containsVertex(src)) {
            directedGraph.addVertex(src);
        }

        for (Integer countryId : dest) {
            if (!directedGraph.containsVertex(countryId)) {
                directedGraph.addVertex(countryId);
            }

        }

        for (Integer country : dest) {
            directedGraph.addEdge(src, country);
            directedGraph.addEdge(country, src);
        }

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



    public boolean isMapNotValid() {
        return !isMapValid();
    }

    public boolean isStronglyConnected() {

        if(countries.size() != 0 && adjacencyCountriesMap.size() == 0){
            return false;
        }
        for (Map.Entry<Integer, Set<Integer>> entry : adjacencyCountriesMap.entrySet()) {
            Set<Integer> set = new HashSet<>(entry.getValue());
            addEdge(entry.getKey(), set);
        }

        int totalCountry = countries.size();
        return new KosarajuStrongConnectivityInspector<>(directedGraph)
                .getStronglyConnectedComponents()
                .stream()
                .map(Graph::vertexSet)
                .map(Set::size)
                .allMatch(num -> num == totalCountry);
    }

    public void printCountryInfo(){
        System.out.println("[Country]");
        countries.forEach(
                country -> {
                    System.out.print(country.getId() + " ");
                    System.out.print(country.getCountryName() + " ");
                    System.out.print(country.getContinentIdentifier() + " ");
                    System.out.print(country.getContinentName() + " ");
                    System.out.println("\n");
                }
        );

    }

    public void printContinentInfo(){
        System.out.println("[Continent]");
        continents.forEach(continent -> {
            System.out.print(continent.getId() + " ");
            System.out.print(continent.getName() + " ");
            System.out.print(continent.getContinentValue() + " ");
            System.out.println("\n");
        });

    }

    public void printNeighboringCountryInfo(){
        System.out.println("[Border]");
        for(Map.Entry<Integer, Set<Integer>> entry : adjacencyCountriesMap.entrySet()){
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }

    private String convertNameToKeyFormat(String name) {
        return StringUtils.deleteWhitespace(name).toLowerCase(Locale.CANADA);
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
