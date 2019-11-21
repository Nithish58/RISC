package com6441.team7.risc.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    private MapStatusEntity.MapStatusEntityBuilder builder = MapStatusEntity.MapStatusEntityBuilder.newInstance();
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

    /**
     * Default Constructor
     */
    public MapService() {
    }

    /**
     * Overrides super class Observable method
     *
     * @param observer class
     */
    @Override
    public void addObserver(Observer observer) {
        super.addObserver(observer);
        setState(gameState);
    }

    /**
     * Getter method for gamestate
     *
     * @return gamestate
     */
    public GameState getGameState() {
        return gameState;
    }


    /**
     * set game state
     *
     * @param gameState the state of game
     */
    public void setState(GameState gameState) {
        this.gameState = gameState;
        setChanged();
        notifyObservers(gameState);
    }

    /**
     * check if map is valid
     *
     * @return true if map is valid, false if map is invalid
     */
    public boolean isMapValid() {
        return isStronglyConnected();
    }


    /**
     * add one country
     *
     * @param country reference country
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
     *
     * @param countriesCollection reference collection of countries
     */
    public void addCountry(Collection<Country> countriesCollection) {
        countriesCollection.forEach(country -> {
            countries.add(country);
            putCountryIntoContinentCountriesMap(country);
        });
    }

    /**
     * add countryId to corresponding countinentId Set
     *
     * @param country
     */
    private void putCountryIntoContinentCountriesMap(Country country) {
        Integer continentId = country.getContinentIdentifier();
        continentCountriesMap.get(continentId).add(country.getId());
    }

    /**
     * add a continent
     *
     * @param continent reference continent
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
     *
     * @param continentsCollection Collections of Continents
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
     *
     * @param map the map which is used to play
     */
    public void addNeighboringCountries(Map<Integer, Set<Integer>> map) {
        map.forEach((key, value) -> adjacencyCountriesMap.put(key, value));
    }

    /**
     * add a neighborhood with country name and its neighboring country name
     *
     * @param country            Name of country
     * @param neighboringCountry Name of neighbouring country
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
     *
     * @param countryId           Id of Country
     * @param neghboringCountryId Id of neighbouring country.
     */
    private void addNeighboringCountry(int countryId, int neghboringCountryId) {
        if (adjacencyCountriesMap.containsKey(countryId)) {
            adjacencyCountriesMap.get(countryId).add(neghboringCountryId);
        } else {
            Set<Integer> neighboringCountrySet = new HashSet<>();
            neighboringCountrySet.add(neghboringCountryId);
            adjacencyCountriesMap.put(countryId, neighboringCountrySet);
        }


        //***IN YOUR IF STATEMENT...shouldnt you add this as well:
        //adjacencyCountriesMap.get(neghboringCountryId).add(countryId);

    }

    /**
     * remove a neighborhood with countryName and neighboringCountryName
     *
     * @param country            name of country
     * @param neighboringCountry name of neighbouring country
     */
    public void removeNeighboringCountriesByName(String country, String neighboringCountry) {

        int countryId = findCorrespondingIdByCountryName(country).get();
        int neghboringCountryId = findCorrespondingIdByCountryName(neighboringCountry).get();

        adjacencyCountriesMap.get(countryId).remove(neghboringCountryId);

        adjacencyCountriesMap.get(neghboringCountryId).remove(countryId);

        directedGraph.removeEdge(countryId, neghboringCountryId);

        //view.displayMessage("neighboring country " + neighboringCountry + " is sucessfully removed from " + country);

    }

    /**
     * To clear the map
     */
    public void emptyMap() {

        if (directedGraph.vertexSet().size() != 0) {

            Set<Integer> set = adjacencyCountriesMap.keySet();
            directedGraph.removeAllVertices(set);

        }

        countries.clear();
        continents.clear();
        continentCountriesMap.keySet().clear();
        adjacencyCountriesMap.keySet().clear();


    }

    /**
     * To get set of countries
     *
     * @return countries set
     */
    public Set<Country> getCountries() {
        return countries;
    }

    /**
     * To get set of Continents
     *
     * @return set of continents
     */
    public Set<Continent> getContinents() {
        return continents;
    }

    /**
     * check if a country name exists
     *
     * @param countryName country's name
     * @return true if country name exist in map
     */
    public boolean countryNameExist(String countryName) {
        return Optional.ofNullable(countryName)
                .map(this::convertNameToKeyFormat)
                .filter(name -> getCountryNameSet().contains(name))
                .isPresent();
    }

    /**
     * check if a country name does not exist
     *
     * @param countryName country's name
     * @return true if country name doesn't exist in map
     */
    public boolean countryNameNotExist(String countryName) {
        return !countryNameExist(countryName);
    }

    /**
     * check if a continent name exist
     *
     * @param continentName continent's name
     * @return true if continents's name exist in map
     */
    public boolean continentNameExist(String continentName) {
        return Optional.ofNullable(continentName)
                .map(this::convertNameToKeyFormat)
                .filter(name -> getContinentNameSet().contains(name))
                .isPresent();
    }

    /**
     * check if a continent name not exist
     *
     * @param continentName continent's name
     * @return true if continents's name doesn't exist in map
     */
    public boolean continentNameNotExist(String continentName) {
        return !continentNameExist(continentName);
    }

    /**
     * check if a continent ID exist
     *
     * @param continentId continent's Id
     * @return true if continents's Id exist in map
     */
    public boolean continentIdExist(Integer continentId) {
        return Optional.ofNullable(continentId)
                .filter(id -> getContinentIdSet().contains(id))
                .isPresent();
    }

    /**
     * check if a continent id not exist
     *
     * @param continentId continent's Id
     * @return true if continents's Id doesn't exist in map
     */
    public boolean continentIdNotExist(Integer continentId) {
        return !continentIdExist(continentId);
    }

    /**
     * check if a country id exist
     *
     * @param countryId country id
     * @return true if country's Id exist in map
     */
    public boolean countryIdExist(Integer countryId) {
        return Optional.ofNullable(countryId)
                .filter(id -> getCountryIdSet().contains(id))
                .isPresent();
    }

    /**
     * check if a country id not exist
     *
     * @param countryId country's Id
     * @return true if country's Id doesn't exist in map
     */
    public boolean countryIdNotExist(Integer countryId) {
        return !countryIdExist(countryId);
    }

    /**
     * check if a country exist
     *
     * @param country country's Name
     * @return true if country doesn't exist in map
     */
    public boolean countryExist(Country country) {
        return Optional.ofNullable(country)
                .filter(c -> countryNameExist(c.getCountryName()) || countryIdExist(c.getId()))
                .isPresent();
    }

    /**
     * remove a country by country name
     *
     * @param countryName Country Name
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
            directedGraph.removeVertex(country.getId());
        });
    }

    /**
     * remove a country id from continentCountryMap
     *
     * @param country Country objects
     */
    private void removeCountryFromContinentCountryMap(Country country) {

        int countryId = country.getId();
        findCorrespondingIdByContinentName(country.getContinentName())
                .ifPresent(continentId -> continentCountriesMap.get(continentId).remove(countryId));
    }


    /**
     * remove country id from neighboring countries
     *
     * @param country Country Objects
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
     *
     * @param name continent's name
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
     *
     * @param name country's name
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
     *
     * @param id continent's id
     * @return if continent exist, will return its name, if not, return empty
     */
    public Optional<String> findCorrespondingNameByContidentId(Integer id) {
        return continents.stream()
                .filter(continent -> continent.getId() == id)
                .map(Continent::getName)
                .findFirst();
    }

    /**
     * find country to be removed by its name
     *
     * @param countryName country Name
     * @return if country exist, will return the country, if not, return empty
     */
    private Optional<Country> findCountryToBeRemoved(String countryName) {
        String normalizedCountryName = convertNameToKeyFormat(countryName);
        return countries.stream()
                .filter(country -> convertNameToKeyFormat(country.getCountryName()).equals(normalizedCountryName))
                .findFirst();


    }

    /**
     * remove country
     *
     * @param id id of country
     */
    public void removeCountryById(int id) {
    }

    /**
     * remove a continent by a given name, if continent exist, remove from continents, contries, neighboring map
     *
     * @param continentName reference name of continent
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
            Set<Integer> countryId = continentCountriesMap.get(continent.getId());
            directedGraph.removeAllVertices(countryId);
            continents.remove(continent);
            continentCountriesMap.remove(continent.getId());
            removeNeighboringCountryByContinentId(continent.getId());
            removeCountryByContinentName(continent.getName());


        });

    }


    /**
     * remove countries that belong to a specific continent
     *
     * @param name continent name
     */
    private void removeCountryByContinentName(String name) {
        List<Country> toBeRemoved = findCountryByContinentName(name);
        countries.removeAll(toBeRemoved);
    }


    /**
     * find countries that belong to a specific continent
     *
     * @param name continent name
     * @return list of Country
     */
    public List<Country> findCountryByContinentName(String name) {
        return countries.stream()
                .filter(country -> convertNameToKeyFormat(country.getContinentName()).equals(convertNameToKeyFormat(name)))
                .collect(Collectors.toList());
    }

    /**
     * find countries id that belong to a specific continent
     *
     * @param id continent id
     * @return list of countries id
     */
    private List<Integer> findCountryIdByContinentId(int id) {

        List<Integer> countryId = countries.stream()
                .filter(country -> country.getContinentIdentifier() == id)
                .map(Country::getId)
                .collect(Collectors.toList());

        return countryId;
    }

    /**
     * remove countries in the neighboring list given a continent id
     *
     * @param id continent id
     * @return
     */
    private void removeNeighboringCountryByContinentId(int id) {
        findCountryIdByContinentId(id)
                .forEach(countryId -> {
                    adjacencyCountriesMap.remove(countryId);
                    adjacencyCountriesMap.entrySet().stream().map(Map.Entry::getValue)
                            .forEach(countrySet -> countrySet.remove(countryId));
                });
    }

    /**
     * To remove continent by id
     *
     * @param id id of continent
     */
    public void removeContinentById(int id) {
    }

    /**
     * To add neighboring countries by name
     *
     * @param contriesName list of countries name which is to be added.
     */
    public void addNeighboringCountriesByName(List<String> contriesName) {
    }

    /**
     * To add neighboring countries by name
     *
     * @param countriesId list of countries id which is to be added.
     */
    public void addNeighboringCountriesById(List<Integer> countriesId) {
    }

    /**
     * To remove neighboring countries by name
     *
     * @param name name of country
     */
    public void removeNeighboringCountriesByName(String name) {
    }

    /**
     * Remove Neighbour Country By Id.
     *
     * @param id of country
     */
    public void removeNeighboringCountriesById(int id) {
    }

    /**
     * To show the map
     *
     * @return Optional.empty() if empty
     */
    public Optional<Graph<Integer, DefaultEdge>> showMap() {
        return Optional.empty();
    }


    /**
     * build the graph based on the list of countries id
     *
     * @param src  source value
     * @param dest set of destinatiton value
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

    /**
     * To remove Vertex from map
     *
     * @param id index of vertex which is to be removed
     */
    private void removeVertex(int id) {
        directedGraph.removeVertex(id);
    }

    /**
     * To remove edge from map
     *
     * @param src  source node
     * @param dest destination node
     */
    private void removeEdge(int src, int dest) {
        directedGraph.removeEdge(src, dest);

    }

    /**
     * To get country by the its name
     *
     * @param name name of country
     * @return Optional.of(country) if id exist else Optional.empty()
     */
    public Optional<Country> getCountryByName(String name) {

        for (Country c : countries) {
            if (c.getCountryName().equalsIgnoreCase(name)) return Optional.of(c);
        }

        return Optional.empty();
    }

    /**
     * To get country by the its id
     *
     * @param id Id of country
     * @return Optional.of(country) if id exist else Optional.empty()
     */
    public Optional<Country> getCountryById(int id) {

        //Modified By Keshav
        for (Country c : countries) {
            if (c.getId() == id) return Optional.of(c);
        }

        return Optional.empty();
    }

    /**
     * To get continent by the its name
     *
     * @param name name of continent
     * @return Optional.of(continent) if id exist else Optional.empty()
     */
    public Optional<Continent> getContinentByName(String name) {

        for (Continent c : continents) {
            if (c.getName().equalsIgnoreCase(name)) return Optional.of(c);
        }

        return Optional.empty();
    }

    /**
     * To get continent by the its Id
     *
     * @param id id of continent
     * @return Optional.of(continent) if id exist else Optional.empty()
     */
    public Optional<Continent> getContinentById(int id) {

        for (Continent c : continents) {
            if (c.getId() == id) return Optional.of(c);
        }

        return Optional.empty();
    }

    /**
     * TO get map of adjacent countries map
     *
     * @return map of adjacent countries map
     */
    public Map<Integer, Set<Integer>> getAdjacencyCountriesMap() {
        return adjacencyCountriesMap;
    }

    public Set<Integer> getAdjacencyCountries(int countryId) {
        return adjacencyCountriesMap.get(countryId);
    }

    /**
     * To get map of continent's countries
     *
     * @return map of continent's countries
     */
    public Map<Integer, Set<Integer>> getContinentCountriesMap() {
        return continentCountriesMap;
    }

    /**
     * To get directed graph
     *
     * @return directed graph
     */
    public Graph<Integer, DefaultEdge> getDirectedGraph() {

        return directedGraph;
    }


    /**
     * check if the map is not valid
     *
     * @return true if map is not valid
     */
    public boolean isMapNotValid() {
        return !isMapValid();
    }

    /**
     * check if the map is strongly connected
     *
     * @return true if strongly connected
     */
    public boolean isStronglyConnected() {    	
    	
    	if(countries.isEmpty()) {
    		return false;
    	}

        
    	if(adjacencyCountriesMap.isEmpty()) {
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

    /**
     * To print the country information
     */
    public void printCountryInfo() {
        System.out.println("[Country]");
        countries.forEach(
                country -> {
                    System.out.print(country.getId() + " ");
                    System.out.print(country.getCountryName() + " ");
                    System.out.print(country.getContinentIdentifier() + " ");
                    System.out.print(country.getCoordinateX() + " ");
                    System.out.println(country.getCoordinateY());
                    System.out.println("\n");
                }
        );

    }

    /**
     * To print the continent information
     */
    public void printContinentInfo() {
        System.out.println("[Continent]");
        continents.forEach(continent -> {
            System.out.print(continent.getId() + " ");
            System.out.print(continent.getName() + " ");
            System.out.print(continent.getContinentValue() + " ");
            System.out.println("\n");
        });

    }

    /**
     * To print the neighboring country information
     */
    public void printNeighboringCountryInfo() {
        System.out.println("[Border]");
        for (Map.Entry<Integer, Set<Integer>> entry : adjacencyCountriesMap.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }

    /**
     * delete white spaces and make received name to lower case
     *
     * @param name name whose white is removed and further changed to lowercase
     * @return Edited name
     */
    private String convertNameToKeyFormat(String name) {
        return StringUtils.deleteWhitespace(name).toLowerCase(Locale.CANADA);
    }

    /**
     * To get sets of country Name
     *
     * @return sets of country Name
     */
    private Set<String> getCountryNameSet() {
        return countries.stream().map(Country::getCountryName)
                .map(this::convertNameToKeyFormat).collect(Collectors.toSet());
    }

    /**
     * To get sets of continent Name
     *
     * @return sets of continent Name
     */
    private Set<String> getContinentNameSet() {
        return continents.stream().map(Continent::getName)
                .map(this::convertNameToKeyFormat).collect(Collectors.toSet());
    }

    /**
     * To get sets of country Id
     *
     * @return sets of country Id
     */
    private Set<Integer> getCountryIdSet() {
        return countries.stream().map(Country::getId).collect(Collectors.toSet());
    }

    /**
     * To get sets of continent Id
     *
     * @return sets of continent Id
     */
    private Set<Integer> getContinentIdSet() {
        return continents.stream().map(Continent::getId).collect(Collectors.toSet());
    }



    /**
     * count number of countries occupied by the player
     * @param player reference player
     * @return number of countries
     */
    public long getConqueredCountriesNumber(Player player){
        return countries.stream()
                .filter(country -> convertNameToKeyFormat(country.getPlayer().getName()).equals(convertNameToKeyFormat(player.getName())))
                .count();
    }


    //-------------------------------reinforcement functions-------------------------------ewikxz


    /**
     * count number of reinforced armies get if occupying whole continents by the player
     * @param player reference player
     * @return num of reinforcements
     */
    public long getReinforceArmyByConqueredContinents(Player player){
        List<Integer> countriesId = getCountryIdOccupiedByPlayer(player);

        long num = 0;
        for (Map.Entry<Integer, Set<Integer>> entry : continentCountriesMap.entrySet()) {

            if(countriesId.containsAll(entry.getValue())) {
                int continentId = entry.getKey();
                int continentValue = findCorrespoindingContinentValueByContinentId(continentId).get();
                num += continentValue;
            }
        }
        return num;

    }


    /**
     * find corresponding continent power by continent id
     * @param id reference continent id
     * @return continent power
     */
    public Optional<Integer> findCorrespoindingContinentValueByContinentId(int id) {
        return continents.stream()
                .filter(continent -> continent.getId() == id)
                .map(Continent::getContinentValue)
                .findFirst();
    }



    /**
     * get conquered countries by the player
     * @param player refrence player
     * @return list of country names occupied the player
     */
    public List<String> getConqueredCountriesNameByPlayer (Player player){
        return countries.stream()
                .filter(country -> convertNameToKeyFormat(country.getPlayer().getName()).equals(convertNameToKeyFormat(player.getName())))
                .map(Country::getCountryName)
                .collect(Collectors.toList());
    }



    /**
     * get countryId occupied by the player
     * @param player
     * @return list of country id
     */
    private List<Integer> getCountryIdOccupiedByPlayer(Player player){
        return countries.stream()
                .filter(country -> convertNameToKeyFormat(country.getPlayer().getName()).equals(convertNameToKeyFormat(player.getName())))
                .map(Country::getId)
                .collect(Collectors.toList());
    }


    /**
     * reinforce the number of soldiers to the country
     * @param name reference name
     * @param armyNum reference num of armies
     */
    public void reinforceArmyToCountry(String name, int armyNum){
        int id = findCorrespondingIdByCountryName(name).get();

        countries.stream()
                .filter(country -> country.getId() == id)
                .findFirst()
                .ifPresent(country -> country.addSoldiers(armyNum));
    }

    public Optional<String> findCorrespondingNameByCountryId(Integer id){
        return countries.stream()
                .filter(country -> country.getId() == id)
                .map(Country::getCountryName)
                .findFirst();
    }

    public MapStatusEntity getMapStatusEntity() {
        return builder
                .countries(countries)
                .continents(continents)
                .adjacencyCountriesMap(adjacencyCountriesMap)
                .continentCountriesMap(continentCountriesMap)
                .directedGraph(directedGraph)
                .gameState(gameState)
                .build();
    }


    public void setCountries(Set<Country> countries) {
        this.countries = countries;
    }

    public void setContinents(Set<Continent> continents) {
        this.continents = continents;
    }

    public void setAdjacencyCountriesMap(Map<Integer, Set<Integer>> adjacencyCountriesMap) {
        this.adjacencyCountriesMap = adjacencyCountriesMap;
    }

    public void setContinentCountriesMap(Map<Integer, Set<Integer>> continentCountriesMap) {
        this.continentCountriesMap = continentCountriesMap;
    }

    public void setDirectedGraph(Graph<Integer, DefaultEdge> directedGraph) {
        this.directedGraph = directedGraph;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }
}
