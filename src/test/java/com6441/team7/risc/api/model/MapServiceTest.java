package com6441.team7.risc.api.model;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * the tests for MapService class
 */
public class MapServiceTest {
	
	/**
	 * Mapservice object reference
	 */
    private MapService mapService;


    /**
     * Method called before each test method.
     * Instantiates a mapservice object
     * @throws Exception on invalid
     */
    @Before
    public void setUp() throws Exception {
        mapService = new MapService();
    }

    /**
     * call createValidContinents method to add 5 continents to the continentSet in mapService
     * pass the test if the size of continents in mapService is 5.
     * pass the test if the continentCountriesMap has 5 keys for the continents.
     * @throws Exception on invalid
     */
    @Test
    public void testAddContinentSet() throws Exception{
        Set<Continent> continentSet = createValidContinents();
        mapService.addContinent(continentSet);
        assertEquals(mapService.getContinents().size(), continentSet.size());
        assertEquals(mapService.getContinentCountriesMap().size(), continentSet.size());
    }

    /**
     * add two continents to the mapService
     * pass the test if continentSet size is 2
     * pass the test if continentCountriesMap has 2 keys for 2 continents.
     * @throws Exception on invalid
     */
    @Test
    public void testAddSingleContinent() throws Exception{
        Continent asia = new Continent(1, "asia", 5);
        Continent america = new Continent(2, "america", 6);

        mapService.addContinent(asia);
        mapService.addContinent(america);

        assertEquals(mapService.getContinents().size(), 2);
        assertEquals(mapService.getContinentCountriesMap().size(), 2);

    }

    /**
     * add three continents and four countries to the mapService
     * pass the test if mapService has three continents and four countries.
     * @throws Exception on invalid
     */
    @Test
    public void testAddCountrySet() throws Exception{
        Set<Continent> continentSet = createValidContinents();
        mapService.addContinent(continentSet);

        Set<Country> countrySet = createValidCountries();
        mapService.addCountry(countrySet);

        assertEquals(mapService.getCountries().size(), 4);
        assertEquals(mapService.getContinentCountriesMap().keySet().size(), 3);

    }

    /**
     * add three continents and two countries
     * pass the tests if the number of continents is 3 and number of countries is 2.
     * @throws Exception on invalid
     */
    @Test
    public void testAddSingleCountry() throws Exception{
        Set<Continent> continentSet = createValidContinents();
        mapService.addContinent(continentSet);

        Country china = new Country(1, "china", "azio");
        Country india = new Country(2, "india", "ameroki");

        mapService.addCountry(china);
        mapService.addCountry(india);

        assertEquals(mapService.getCountries().size(), 2);
        assertEquals(mapService.getContinentCountriesMap().size(),3);
    }

    /**
     * add three continents, four countries and add four neighboring info in adjacencyCountriesMap
     * pass the tests if the number of adjacencyCountriesMap is 4
     * @throws Exception on invalid
     */
    @Test
    public void testAddNeighboringCountriesSet() throws Exception{
        Set<Continent> continentSet = createValidContinents();
        mapService.addContinent(continentSet);

        Set<Country> countrySet = createValidCountries();
        mapService.addCountry(countrySet);

        Map<Integer, Set<Integer>> map = createValidNeighboringCountries();
        mapService.addNeighboringCountries(map);

        assertEquals(mapService.getAdjacencyCountriesMap().size(), 4);
    }

    /**
     * add 3 continents, 4 countries and 4 neighboring countries information
     * remove a country china from mapService
     * pass the test if the number of countries is 3
     * pass the test if the continentMap does not contain id of china
     * pass the test if the adjacencyCountriesMap does not contain id of china
     * @throws Exception on invalid
     */
    @Test
    public void testRemoveCountryByName() throws Exception{

        mapService = createValidContinentCountryNeighbor();
        mapService.removeCountryByName("china");

        assertEquals(mapService.getCountries().size(), 3);
        assertFalse(mapService.getContinentCountriesMap().get(1).contains(1));
        assertFalse(mapService.getAdjacencyCountriesMap().containsKey(1));
        assertFalse(mapService.getAdjacencyCountriesMap().get(3).contains(1));
    }


    /**
     * add a neighboring country china with us
     * pass the test if china neighboring countries is 2
     * @throws Exception on invalid
     */
    @Test
    public void testAddNeighboringCountry() throws Exception{
        mapService = createValidContinentCountryNeighbor();
        mapService.addNeighboringCountries("china", "us");
        assertEquals(mapService.getAdjacencyCountriesMap().get(1).size(), 2);

        for(Map.Entry<Integer, Set<Integer>> entry : mapService.getAdjacencyCountriesMap().entrySet()){
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }
    }

    /**
     * remove neighboring country from siberia with india
     * pass the tests if the number of adjacency for siberia is 1.
     * @throws Exception on invalid
     */
    @Test
    public void testRemoveNeighboringCountry() throws Exception{
        mapService = createValidContinentCountryNeighbor();
        mapService.removeNeighboringCountriesByName("siberia", "india");
        assertEquals(mapService.getAdjacencyCountriesMap().get(4).size(), 1);

    }

    /**
     * create valid continent, countries and neighboring countries and add it to the mapService
     * @return returns mapservice where details are added in.
     */
    private MapService createValidContinentCountryNeighbor() {
        Continent continent1 = new Continent(1, "asia" , 5);
        Continent continent2 = new Continent(2, "america", 6);

        Country country1 = new Country(1, "china", "asia");
        Country country2 = new Country(2, "US", "america");
        Country country3 = new Country(3, "india", "asia");
        Country country4 = new Country(4, "siberia", "america");

        mapService.addContinent(continent1);
        mapService.addContinent(continent2);
        mapService.addCountry(country1);
        mapService.addCountry(country2);
        mapService.addCountry(country3);
        mapService.addCountry(country4);

        Map<Integer, Set<Integer>> neighboringCountryMap = new HashMap<>();
        Set<Integer> set1 = new HashSet<>(Collections.singletonList(3));
        Set<Integer> set2 = new HashSet<>(Collections.singletonList(4));
        Set<Integer> set3 = new HashSet<>(Collections.singletonList(1));
        Set<Integer> set4 = new HashSet<>(Arrays.asList(2,3));
        neighboringCountryMap.put(1, set1);
        neighboringCountryMap.put(2, set2);
        neighboringCountryMap.put(3, set3);
        neighboringCountryMap.put(4, set4);
        mapService.addNeighboringCountries(neighboringCountryMap);

        return mapService;
    }

    /**
     * create valid neighboring information
     * @throws Exception when creating or adding them to existing countries is invalid map
     * @return returns neighboring country map in key value pairs - 
     * where keys are country id, values are set of neighboring country ids
     */
    private Map<Integer, Set<Integer>> createValidNeighboringCountries() throws Exception{
        Map<Integer, Set<Integer>> neighboringCountryMap = new HashMap<>();
        Set<Integer> set1 = new HashSet<>(Arrays.asList(2,3));
        Set<Integer> set2 = new HashSet<>(Arrays.asList(3,4));
        Set<Integer> set3 = new HashSet<>(Arrays.asList(1,2));
        Set<Integer> set4 = new HashSet<>(Arrays.asList(1,3));


        neighboringCountryMap.put(1, set1);
        neighboringCountryMap.put(2, set2);
        neighboringCountryMap.put(3, set3);
        neighboringCountryMap.put(4, set4);

        return neighboringCountryMap;

    }

    /**
     * add three continents to continentSet in mapService
     * @return a set of continents
     * @throws Exception when creating or adding them to existing continent set is invalid
     */
    private Set<Continent> createValidContinents() throws Exception{
        Set<Continent> continentSet = new HashSet<>();
        Continent continent1 = new Continent(1, "azio", 5);
        Continent continent2 = new Continent(2, "ameroki", 6);
        Continent continent3 = new Continent(3, "utropa", 7);

        continentSet.add(continent1);
        continentSet.add(continent2);
        continentSet.add(continent3);

        return continentSet;
    }

    /**
     * add four countries to countrySet in mapService
     * @return a set of countries
     * @throws Exception when creating or adding them to existing country set is invalid 
     */
    private Set<Country> createValidCountries() throws Exception{
        Set<Country> countrySet = new HashSet<>();
        Country country1 = new Country(1, "siberia", 1);
        Country country2 = new Country(2, "worrick", 2);
        Country country3 = new Country(3, "yazteck", 3);
        Country country4 = new Country(4, "kongrolo", 2);

        countrySet.add(country1);
        countrySet.add(country2);
        countrySet.add(country3);
        countrySet.add(country4);

        return countrySet;
    }




}