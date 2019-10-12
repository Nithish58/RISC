package com6441.team7.risc.api.model;

import org.apache.commons.lang3.StringUtils;
import org.jgrapht.Graph;
import org.jgrapht.alg.connectivity.KosarajuStrongConnectivityInspector;
import org.jgrapht.alg.interfaces.StrongConnectivityAlgorithm;
import org.jgrapht.graph.*;
import java.util.*;
import java.util.stream.Collectors;
import static java.util.Objects.isNull;

/**
 * this class is used to store game map of countries, continents, neighboring countries
 *
 */
public class MapService extends Observable {

    /**
     * a set of countries
     */
	
    private Set<Country> countries = new HashSet<>();

    /**
     * a set of continents
     */
    private Set<Continent> continents = new HashSet<>();

    /**
     * store each country ID, and its neighbor countries ID
     */
    private Map<Integer, Set<Integer>> adjacencyCountriesMap = new HashMap<>();

    /**
     * store each continent ID, and its belonging countries ID
     */
    private Map<Integer, Set<Integer>> continentCountriesMap = new HashMap<>();

    /**
     * store countries as a connected graph
     */
    private Graph<Integer, DefaultEdge> directedGraph = new DefaultDirectedGraph<>(DefaultEdge.class);

    /**
     * the state of the game
     */
    private GameState gameState = GameState.LOAD_MAP;


    public MapService(){}

    @Override
    public void addObserver(Observer observer) {
        super.addObserver(observer);
        setState(gameState);
    }

    public GameState getGameState(){
        return gameState;
    }


    /**
     * set game state
     * @param gameState
     */
    public void setState(GameState gameState){
        this.gameState = gameState;
        setChanged();
        notifyObservers(gameState);
    }

    /**
     * check if map is valid
     * @return true if map is valid, false if map is invalid
     */
    public boolean isMapValid() {
        return isStronglyConnected();
    }


    /**
     * add one country
     * @param country
     */
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

    /**
     * add a collection of countries
     * @param countriesCollection
     */
    public void addCountry(Collection<Country> countriesCollection) {
        countriesCollection.forEach(country -> {
            countries.add(country);
            putCountryIntoContinentCountriesMap(country);
        });
    }

    /**
     * add countryId to corresponding countinentId Set
     * @param country
     */
    private void putCountryIntoContinentCountriesMap(Country country) {
        Integer continentId = country.getContinentIdentifier();
        continentCountriesMap.get(continentId).add(country.getId());
    }

    /**
     * add a continent
     * @param continent
     */
    public void addContinent(Continent continent) {
        continents.add(continent);

        if (!continentCountriesMap.containsKey(continent.getId())) {
            continentCountriesMap.put(continent.getId(), new HashSet<>());
        }

       // view.displayMessage("continent " + continent.getName() + " is successfully added.");

    }

    /**
     * add a collection of continents
     * @param continentsCollection
     */
    public void addContinent(Collection<Continent> continentsCollection) {
        continents.addAll(continentsCollection);

        Set<Integer> continentId = continentsCollection.stream()
                .map(Continent::getId)
                .collect(Collectors.toSet());

        continentId.forEach(id -> continentCountriesMap.put(id, new HashSet<Integer>()));

    }

    /**
     * add collection of countries id and its neighboring countries id
     * @param map
     */
    public void addNeighboringCountries(Map<Integer, Set<Integer>> map) {
        map.forEach((key, value) -> adjacencyCountriesMap.put(key, value));
    }

    /**
     * add a neighborhood with country name and its neighboring country name
     * @param country
     * @param neighboringCountry
     */
    public void addNeighboringCountries(String country, String neighboringCountry) {

        int countryId = findCorrespondingIdByCountryName(country).get();
        int neghboringCountryId = findCorrespondingIdByCountryName(neighboringCountry).get();

        addNeighboringCountry(countryId, neghboringCountryId);
        addNeighboringCountry(neghboringCountryId, countryId);

        //  view.displayMessage("Neighboring countries " + country + " " + neighboringCountry + " is successfully added.");
    }

    /**
     * add a neighborhood with countryId and its neighboring country id
     * @param countryId
     * @param neghboringCountryId
     */
    private void addNeighboringCountry(int countryId, int neghboringCountryId) {
        if (adjacencyCountriesMap.containsKey(countryId)) {
            adjacencyCountriesMap.get(countryId).add(neghboringCountryId);
        } else {
            Set<Integer> neighboringCountrySet = new HashSet<>();
            neighboringCountrySet.add(neghboringCountryId);
            adjacencyCountriesMap.put(countryId, neighboringCountrySet);
        }
    }

    /**
     * remove a neighborhood with countryName and neighboringCountryName
     * @param country
     * @param neighboringCountry
     */
    public void removeNeighboringCountriesByName(String country, String neighboringCountry) {

        int countryId = findCorrespondingIdByCountryName(country).get();
        int neghboringCountryId = findCorrespondingIdByCountryName(neighboringCountry).get();

        adjacencyCountriesMap.get(countryId).remove(neghboringCountryId);

        adjacencyCountriesMap.get(neghboringCountryId).remove(countryId);

        //view.displayMessage("neighboring country " + neighboringCountry + " is sucessfully removed from " + country);

    }

    public void emptyMap(){

        if(directedGraph.vertexSet().size() != 0){

            Set<Integer> set = adjacencyCountriesMap.keySet();
            directedGraph.removeAllVertices(set);

        }

        countries.clear();
        continents.clear();
        continentCountriesMap.keySet().clear();
        adjacencyCountriesMap.keySet().clear();


    }

    public Set<Country> getCountries() {
        return countries;
    }

    public Set<Continent> getContinents() {
        return continents;
    }

    /**
     * check if a country name exists
     * @param countryName
     * @return
     */
    public boolean countryNameExist(String countryName) {
        return Optional.ofNullable(countryName)
                .map(this::convertNameToKeyFormat)
                .filter(name -> getCountryNameSet().contains(name))
                .isPresent();
    }

    /**
     * check if a country name does not exist
     * @param countryName
     * @return
     */
    public boolean countryNameNotExist(String countryName) {
        return !countryNameExist(countryName);
    }

    /**
     * check if a continent name exist
     * @param continentName
     * @return
     */
    public boolean continentNameExist(String continentName) {
        return Optional.ofNullable(continentName)
                .map(this::convertNameToKeyFormat)
                .filter(name -> getContinentNameSet().contains(name))
                .isPresent();
    }

    /**
     * check if a continent name not exist
     * @param continentName
     * @return
     */
    public boolean continentNameNotExist(String continentName) {
        return !continentNameExist(continentName);
    }

    /**
     * check if a continent ID exist
     * @param continentId
     * @return
     */
    public boolean continentIdExist(Integer continentId) {
        return Optional.ofNullable(continentId)
                .filter(id -> getContinentIdSet().contains(id))
                .isPresent();
    }

    /**
     * check if a continent id not exist
     * @param continentId
     * @return
     */
    public boolean continentIdNotExist(Integer continentId) {
        return !continentIdExist(continentId);
    }

    /**
     * check if a country id exist
     * @param countryId
     * @return
     */
    public boolean countryIdExist(Integer countryId) {
        return Optional.ofNullable(countryId)
                .filter(id -> getCountryIdSet().contains(id))
                .isPresent();
    }

    /**
     * check if a country id not exist
     * @param countryId
     * @return
     */
    public boolean countryIdNotExist(Integer countryId) {
        return !countryIdExist(countryId);
    }

    /**
     * check if a country exist
     * @param country
     * @return
     */
    public boolean countryExist(Country country) {
        return Optional.ofNullable(country)
                .filter(c -> countryNameExist(c.getCountryName()) || countryIdExist(c.getId()))
                .isPresent();
    }

    /**
     * remove a country by country name
     * @param countryName
     */
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

    /**
     * remove a country id from continentCountryMap
     * @param country
     */
    private void removeCountryFromContinentCountryMap(Country country) {

        int countryId = country.getId();
        findCorrespondingIdByContinentName(country.getContinentName())
                .ifPresent(continentId -> continentCountriesMap.get(continentId).remove(countryId));
    }


    /**
     * remove country id from neighboring countries
     * @param country
     */
    private void removeCountryFromAdjacentCountryMap(Country country) {
        int countryId = country.getId();
        adjacencyCountriesMap.remove(countryId);

        for (Map.Entry<Integer, Set<Integer>> entry : adjacencyCountriesMap.entrySet()) {
            entry.getValue().remove(countryId);
        }

    }

    /**
     * find continent id by given continent name
     * @param name
     * @return if continent exist, will return its ID, if not, return empty
     */
    public Optional<Integer> findCorrespondingIdByContinentName(String name) {
        return continents.stream()
                .filter(continent -> convertNameToKeyFormat(continent.getName()).equals(convertNameToKeyFormat(name)))
                .map(Continent::getId)
                .findFirst();
    }

    /**
     * find country id by given country name
     * @param name
     * @return if country exist, will return its ID, if not, return empty
     */
    public Optional<Integer> findCorrespondingIdByCountryName(String name) {
        return countries.stream()
                .filter(country -> convertNameToKeyFormat(country.getCountryName()).equals(convertNameToKeyFormat(name)))
                .map(Country::getId)
                .findFirst();
    }

    /**
     * find continent name by given continent id
     * @param id
     * @return if continent exist, will return its name, if not, return empty
     */
    public Optional<String> findCorrespondingNameByContidentId(Integer id){
        return continents.stream()
                .filter(continent -> continent.getId() == id)
                .map(Continent::getName)
                .findFirst();
    }

    /**
     * find country to be removed by its name
     * @param countryName
     * @return if country exist, will return the country, if not, return empty
     */
    private Optional<Country> findCountryToBeRemoved(String countryName) {
        String normalizedCountryName = convertNameToKeyFormat(countryName);
        return countries.stream()
                .filter(country -> convertNameToKeyFormat(country.getCountryName()).equals(normalizedCountryName))
                .findFirst();


    }

    public void removeCountryById(int id) {
    }

    /**
     * remove a continent by a given name, if continent exist, remove from continents, contries, neighboring map
     * @param continentName
     * @return
     */
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
            removeNeighboringCountryByContinentId(continent.getId());
            removeCountryByContinentName(continent.getName());


        });

    }


    /**
     * remove countries that belong to a specific continent
     * @param name continent name
     * @return
     */
    private void removeCountryByContinentName(String name){
        List<Country> toBeRemoved = findCountryByContinentName(name);
        countries.removeAll(toBeRemoved);
    }


    /**
     * find countries that belong to a specific continent
     * @param name continent name
     * @return
     */
    private List<Country> findCountryByContinentName(String name){
        return countries.stream()
                .filter(country -> convertNameToKeyFormat(country.getContinentName()).equals(convertNameToKeyFormat(name)))
                .collect(Collectors.toList());
    }

    /**
     * find countries id that belong to a specific continent
     * @param id continent id
     * @return list of countries id
     */
    private List<Integer> findCountryIdByContinentId(int id){

        List<Integer> countryId =  countries.stream()
                .filter(country -> country.getContinentIdentifier() == id)
                .map(Country::getId)
                .collect(Collectors.toList());

        return countryId;
    }

    /**
     * remove countries in the neighboring list given a continent id
     * @param id continent id
     * @return
     */
    private void removeNeighboringCountryByContinentId(int id){
        findCountryIdByContinentId(id)
                .forEach(countryId -> {
                    adjacencyCountriesMap.remove(countryId);
                    adjacencyCountriesMap.entrySet().stream().map(Map.Entry::getValue)
                            .forEach(countrySet -> countrySet.remove(countryId));
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


    /**
     * build the graph based on the list of countries id
     * @param src
     * @param dest
     */
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


    /**
     * check if the map is strongly connected
     * @return
     */
    public boolean isMapNotValid() {
        return !isMapValid();
    }

    /**
     * check if the map is strongly connected
     * @return
     */
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

    /**
     * delete white spaces and make received name to lower case
     * @param name
     * @return
     */
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
    
    public void clearMapService() {
    	
    	countries.removeAll(countries);
    	continents.removeAll(continents);
    	adjacencyCountriesMap.clear();
    	continentCountriesMap.clear();
    	
    	countries = new HashSet<>();
        continents = new HashSet<>();
        adjacencyCountriesMap = new HashMap<>();
        continentCountriesMap = new HashMap<>();
       directedGraph = new DefaultDirectedGraph<>(DefaultEdge.class);
        
    }
    
}
