package com6441.team7.risc.controller;


import com6441.team7.risc.api.model.Continent;
import com6441.team7.risc.api.model.Country;
import com6441.team7.risc.api.model.MapService;
import com6441.team7.risc.view.GameView;
import com6441.team7.risc.view.PhaseView;


import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * tests for mapLoader Controller
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MapLoaderAdapterTest {

    /**
     * a reference of mapLoaderController
     */
    private MapLoaderController mapLoaderController;
    /**
     * a reference of GameView
     */
    private GameView view;

    /**
     * a reference of mapService
     */
    private MapService mapService;


    /**
     * setup method to set up the attributes
     * @throws Exception exception on Invalid
     */
    @Before
    public void setUp() throws Exception {
    	
        mapService = new MapService();
        
        mapLoaderController = new MapLoaderController(mapService);
        
        view = new PhaseView();
        
        mapLoaderController.setView(view);
        
		System.out.printf("==========%nBeginning of method%n==========%n");
		
		mapname = "Aden.map";
		
		System.out.println("Map name is : "+mapname);
		
		System.out.println(mapLoaderController.getMapService().getContinentCountriesMap());
		
		System.out.println(mapLoaderController.getMapService().getCountries());
		
		mapLoaderController.getMapService().printNeighboringCountryInfo();
		
		System.out.println("Number of continents before test: "+mapLoaderController.getMapService().getContinents().size());
		
		System.out.println("Number of countries before test: "+mapLoaderController.getMapService().getCountries().size());

		inputcommand = "editmap "+mapname;
		
		editMap(mapname);
		
		//size of continent list before one continent is added or removed
		initcontinentsize = mapLoaderController.getMapService().getContinents().size();
		//size of country list before one continent is added or removed
		initcountrysize = mapLoaderController.getMapService().getCountries().size();


		//This sets the variable for map saving command.
		savename = "edittedmap.map";
       
        
    }

	/**
	 * mapname is for setting the name of the map file to be loaded.
	 * file is for handling file parsing.
	 * savename is the name of the edited map file to be saved by the test runner.
	 * inputcommand is for setting every map command.
	 */
	String mapname, file, savename, inputcommand;

	/**
	 * initcontinentsize is the size of the continent list map before every test is performed.
	 * initcountrysize is the size of the country list map before every test is performed.
	 * expectedcontinentsize is the size of the continent list map after every test is performed.
	 * expectedcountrysize is the size of the country list map after every test is performed.
	 */
	int initcontinentsize, initcountrysize, expectedcontinentsize, expectedcountrysize;
	
	/**
	 * testCounter is for counting the number of tests that is performed
	 */
	static int  testCounter;
	
	/**
	 * country1, neighbor1, country2, neighbor2, 
	 * are used for testing adding and deleting neighboring countries
	 */
	Optional<Integer> country1, neighbor1, country2, neighbor2;
	/**
	 * borders1, borders2 are used for testing adding and deleting neighboring countries
	 */
	Map<Integer, Set<Integer>> borders1, borders2;
	/**
	 * pair1, and pair2 
	 * are used for testing adding and deleting neighboring countries
	 */
	Set<Integer> pair1, pair2;

	/**
	 * message is for setting the message upon test failure
	 */
	String message;

	/**
	 * endMethod() is called after every method is performed. It prints out
	 * the continents list, countries list, neighbor info list, and numbers of continents
	 * and countries after each test.
	 *
	 */
	@After
	public void endMethod() {
		
		System.out.printf("%n%n==========%nEnd of method%n==========%n");
		
		System.out.println(mapLoaderController.getMapService().getContinentCountriesMap());
		
		System.out.println(mapLoaderController.getMapService().getCountries());
		
		mapLoaderController.getMapService().printNeighboringCountryInfo();
		
		System.out.println("Number of continents after test: "+mapLoaderController.getMapService().getContinents().size());
		System.out.println("Number of countries after test: "+mapLoaderController.getMapService().getCountries().size());
	}



	/**
	 * Tests command to load map from file.
	 * The map is loaded in the setup. 
	 * The runner passes the test if the result returns true.
	 * @throws Exception exception upon invalid values
	 */
	@Test
	public void test001_readFile() throws Exception{
		
		assertTrue(message, mapLoaderController.validateMap());
	}


	/**
	 * Tests adding one continent to the continent list.
	 * The test passes if the number of continent increases by 1 after the test.
	 * @throws Exception exception upon invalid values
	 */
	@Test
	public void test002_addContinent() throws Exception{
		
		System.out.printf("Adding one continent%n------------%n");
		
		addContinent("Nord_Asia","1");
		
		//Continent list size is expected to increase by 1
		expectedcontinentsize = initcontinentsize + 1; 
		
		assertSame(expectedcontinentsize, mapLoaderController.getMapService().getContinents().size());
	}

	/**
	 * Tests deleting one continent.
	 * The test passes if the number of continents decreases by 1 after the test.
	 * @throws Exception exception upon invalid values
	 */
	@Test
	public void test003_removeContinent() throws Exception{
		
		System.out.printf("Removing one continent%n------------%n");
		
		//Get the first continent
		Optional<Continent> continentFromService = mapLoaderController.getMapService().getContinentById(1);
		Continent continent = continentFromService.get();
		String continentName = continent.getName();
		
		//Remove the continent
		removeContinent(continentName);
		
		//Continent list size is expected to decrease by 1
		expectedcontinentsize = initcontinentsize - 1;
		
		assertSame(expectedcontinentsize, mapLoaderController.getMapService().getContinents().size());
	}

	/**
	 * Tests adding and removing one continent from the continent list in one command.
	 * The test passes if the number of continents stays the same after the test.
	 * @throws Exception exception upon invalid values
	 */
	@Test
	public void test004_addAndRemoveContinent() throws Exception{
		
		System.out.printf("Adding and removing one continent%n------------%n");
		
		//Set continent to be removed
		Optional<Continent> continentFromService = mapLoaderController.getMapService().getContinentById(1);
		Continent continent = continentFromService.get();
		String removedContinentName = continent.getName();
		
		//Set continent to be added
		String addedContinent = "Nord_Asia";
		String addedContinentPower = "1";
		System.out.println(addedContinent+" "+addedContinentPower+" "+removedContinentName);
		
		//Add and remove continents
		addAndRemoveContinent(addedContinent,addedContinentPower,removedContinentName);
		
		//Continent size should remain the same
		expectedcontinentsize = initcontinentsize; 
		
		assertSame(expectedcontinentsize, mapLoaderController.getMapService().getContinents().size());
	}

	/**
	 * Tests adding one country to the country list.
	 * The test passes if the number of countries increases by 1 after the test.
	 * @throws Exception exception upon invalid values
	 */
	@Test
	public void test005_addCountry() throws Exception{
		
		System.out.printf("Adding one country%n------------%n");
		
		//Retrieve the continent where the country will be added
		Optional<Continent> continentFromService = mapLoaderController.getMapService().getContinentById(1);
		Continent continent = continentFromService.get();
		String continentName = continent.getName();
		
		//Name of the country to be added
		String countryName = "Sky_Republic";
		System.out.println(countryName+" "+continentName);
		
		//Add country
		addCountry(countryName, continentName);
		
		//Country list size is expected to increase by 1
		expectedcountrysize = initcountrysize+1; 
		
		assertSame(expectedcountrysize, mapLoaderController.getMapService().getCountries().size());
	}

	/**
	 * Tests removing one country from the country list.
	 * The test passes if the number of countries decreases by 1 after the test.
	 * @throws Exception exception upon invalid values
	 */
	@Test
	public void test006_removeCountry() throws Exception{
		
		System.out.printf("Removing one country%n------------%n");
		
		//Get the first country from the set and set the first one to be deleted
		Set<Country> countriesFromService = mapLoaderController.getMapService().getCountries();
		Iterator<Country> countryIterator = countriesFromService.iterator();
		Country country = countryIterator.next();
		String countryName = country.getCountryName();
		
		//Call the removeCountry method with countryName as the param
		removeCountry(countryName);
		System.out.println(countryName);
		
		expectedcountrysize = initcountrysize-1; //Country list size is expected to decrease by 1
		
		assertSame(expectedcountrysize, mapLoaderController.getMapService().getCountries().size());
	}

	/**
	 * Tests adding and removing one country from the country list in one command.
	 * The test passes if the number of countries stays the same after the test.
	 * @throws Exception exception upon invalid values
	 */
	@Test
	public void test007_addAndRemoveCountry() throws Exception{
		
		System.out.printf("Adding and removing one country%n------------%n");
		
		//Retrieve the continent where the country will be added
		Optional<Continent> continentFromService = mapLoaderController.getMapService().getContinentById(1);
		Continent continent = continentFromService.get();
		String continentName = continent.getName();
		
		//Name of the country to be added
		String countryName = "Sky_Republic";
		System.out.println(countryName+" "+continentName);
		
		//Get set of countries using service
		Set<Country> countriesFromService = mapLoaderController.getMapService().getCountries();
		
		//Get the first country in the set using iterator
		Iterator<Country> countryIterator = countriesFromService.iterator();
		
		//Set the country to be removed
		Country country = countryIterator.next();
		
		//Retrieve the name of the country that is to be removed
		String removedCountryName = country.getCountryName();
		
		//Call the command to add and remove countries
		addAndRemoveCountry(countryName, continentName, removedCountryName);
		
		//Expected country size is set to the same as before this test is performed
		expectedcountrysize = initcountrysize; 
		
		assertSame(expectedcountrysize, mapLoaderController.getMapService().getCountries().size());
	}

	/**
	 * Tests adding one neighbor of a country.
	 * The test passes if the origin country exists in the adjacency list of
	 * the entire map and the neighboring country is among the
	 * origin country's adjacency's list after the test.
	 * @throws Exception exception upon invalid values
	 */
	@Test
	public void test008_addNeighbor() throws Exception{
		
		System.out.printf("Adding one neighbor to a country%n------------%n");
		
		//Add a country first
		//Retrieve the continent where the country will be added
		Optional<Continent> continentFromService = mapLoaderController.getMapService().getContinentById(1);
		Continent continent = continentFromService.get();
		String continentName = continent.getName();
		
		//Name of the country to be added
		String countryName = "Sky_Republic";
		System.out.println(countryName+" "+continentName);
		addCountry(countryName, continentName);
		
		//Retrieve the origin country
		Set<Country> countriesFromService = mapLoaderController.getMapService().getCountries();
		
		//Get the first country in the set using iterator
		Iterator<Country> countryIterator = countriesFromService.iterator();
		
		//Set the country
		Country country = countryIterator.next();
		
		//Retrieve the country name
		String originCountryName = country.getCountryName();		
		System.out.println(originCountryName+" "+countryName);
		
		//Add the neighbor
		addNeighbor(originCountryName, countryName);
		
		//Check if map object contains both country ID and neighbor ID
		assertTrue("Country is not found", borders1.containsKey(country1.get()));
		assertTrue("Neighboring is not found", pair1.contains(neighbor1.get()));
	}

	/**
	 * Tests removing one neighbor from a country.
	 * The test passes if the origin country exists in the adjacency list of
	 * the entire map and the neighboring country is not among the
	 * origin country's adjacency's list after the test.
	 * @throws Exception exception upon invalid values
	 */
	@Test
	public void test009_removeNeighbor() throws Exception{
		
		System.out.printf("Removing one neighbor from a country%n------------%n");
		
		//get first country from the country list and set it as the origin country
		Set<Country> countriesFromService = mapLoaderController.getMapService().getCountries();
		Iterator<Country> countryIterator = countriesFromService.iterator();
		Country originCountry = countryIterator.next();
		String originCountryName = originCountry.getCountryName();
		
		//Retrieve the origin country's adjacency list and set the first adjacent country as the neighbor
		Set<Integer> originCountryAdjacencyList = mapService.getAdjacencyCountries(originCountry.getId());
		Country neighborCountry = null;
		for(Integer i: originCountryAdjacencyList) {
			neighborCountry=mapService.getCountryById(i).get();
			break;
		}
		
		//Set adjacent country's name
		String neighborCountryName = neighborCountry.getCountryName();
		
		//Remove the neighbor
		removeNeighbor(originCountryName, neighborCountryName);
		
		//Check if map object contains both country ID and neighbor ID
		assertTrue("Country is not found",borders1.containsKey(country1.get()));
		assertFalse("Neighboring country is found", pair1.contains(neighbor1.get()));
	}


	/**
	 * Tests adding and removing one neighbor in one command.
	 * The test passes if the origin country exists in the adjacency list of
	 * the entire map, the added neighboring country is among the
	 * origin country's adjacency's list, and the removed neighboring country
	 * is not among the origin country's adjacency's list after the test.
	 * @throws Exception exception upon invalid values
	 */
	@Test
	public void test010_addAndRemoveNeighbor() throws Exception{
		
		System.out.printf("Adding and removing one neighbor from one country%n------------%n");
		
		//Add a country first after retrieving the continent where the country will be added
		Optional<Continent> continentFromService = mapLoaderController.getMapService().getContinentById(1);
		Continent continent = continentFromService.get();
		String continentName = continent.getName();
		String newCountryName = "Sky_Republic";
		System.out.println(newCountryName+" "+continentName);
		addCountry(newCountryName, continentName);
		
		//Get first country from the country list and set it as the origin country
		Set<Country> countriesFromService = mapLoaderController.getMapService().getCountries();
		Iterator<Country> countryIterator = countriesFromService.iterator();
		Country originCountry = countryIterator.next();
		String originCountryName = originCountry.getCountryName();
		
		//Get adjacency list of origin country and retrieve the first adjacent country
		Set<Integer> originCountryAdjacencyList = mapService.getAdjacencyCountries(originCountry.getId());
		Country neighborCountry = null;
		for(Integer i: originCountryAdjacencyList) {
			neighborCountry=mapService.getCountryById(i).get();
			break;
		}
		
		//Set name of an adjacent country to be removed
		String neighborCountryName = neighborCountry.getCountryName();
		
		//Call method to add and remove neighbors
		addAndRemoveNeighbor(originCountryName, newCountryName, originCountryName, neighborCountryName);
		
		//Check if map object contains both country ID and neighbor ID
		assertTrue("Country is not found",borders1.containsKey(country1.get()));
		assertTrue("Country is not found",borders1.containsKey(country2.get()));
		assertTrue("First neighbor country is not found", pair1.contains(neighbor1.get()));
		assertFalse("Second neighbor country is found", pair2.contains(neighbor2.get()));
	}

	/**
	 * Test if map is invalid.
	 * This test uses an invalid map.
	 * The test passes if the getMapService().isMapNotValid() returns true.
	 * @throws Exception on invalid values
	 */
	@Test
	public void test011_invalidateMap() throws Exception {
		
		System.out.printf("%nInvalidating map%n");
		
		//load an invalid map first
		editMap("invalid_ameroki.map");
		
		assertTrue(mapLoaderController.getMapService().isMapNotValid());
	}

	/**
	 * Test if map is valid.
	 * The test passes if the getMapService().isMapValid() returns true.
	 */
	@Test
	public void test012_validateMap() {
		
		System.out.printf("%nValidating map%n");
		
		assertTrue(mapLoaderController.getMapService().isMapValid());
	}
	
	/**
	 * Test if map is empty.
	 * The test passes if the isMapNotVallid method call returns true
	 * @throws Exception  on invalid IO
	 */
	@Test
	public void test013_validateEmptyMap() throws Exception {
		
		//Read an empty map
		editMap("newemptymap.map");
		
		assertTrue(mapLoaderController.getMapService().isMapNotValid());
	}
	
	/**
	 * Test if duplicate countries exist. 
	 * Expected: Duplicate countries not added and map remains valid.
	 * @throws Exception on invalid IO
	 */
	@Test
	public void test014_validateDuplicateCountry() throws Exception {
		
		//Retrieve the continent where the country will be added
		Optional<Continent> continentFromService = mapLoaderController.getMapService().getContinentById(1);
		Continent continent = continentFromService.get();
		String continentName = continent.getName();
		
		//Get the first country from the set and set the first one to be deleted
		Set<Country> countriesFromService = mapLoaderController.getMapService().getCountries();
		Iterator<Country> countryIterator = countriesFromService.iterator();
		Country country = countryIterator.next();
		String countryName = country.getCountryName();
		
		addCountry(countryName, continentName);
		
		assertTrue(mapLoaderController.getMapService().isMapValid());
	}
	
	/**
	 * Test for unconnected map. 
	 * Add country Mauritius to the first continent. Do not add any neighbours. 
	 * Expected: Map must be invalid.
	 * @throws Exception on invalid IO
	 */
	@Test public void test015_invalidUnconnectedCountriesMap() throws Exception {
		
		//Retrieve the continent where the country will be added
		Optional<Continent> continentFromService = mapLoaderController.getMapService().getContinentById(1);
		Continent continent = continentFromService.get();
		String continentName = continent.getName();
		
		//Context: add unconnected country without neighbours
		addCountry("Mauritius",continentName);
		
		//Evaluation
		assertFalse(mapLoaderController.getMapService().isMapValid());
	}
	
	
	/**
	 * This method is executed by {@link #test002_addContinent()}
	 * @param name Name
	 * @param power Power
	 * @throws Exception  on invalid IO
	 */
	public void addContinent(String name, String power) throws Exception {
		mapLoaderController.readCommand("editcontinent -add "+name+" "+power);
	}
	
	/**
	 * This method is executed by {@link #test003_removeContinent()}
	 * @param name is the name of the continent to be removed
	 * @throws Exception  on invalid values
	 */
	public void removeContinent(String name) throws Exception {
		mapLoaderController.readCommand("editcontinent -remove "+name);
	}
	
	/**
	 * This method is executed by {@link #test004_addAndRemoveContinent()}
	 * @param name1 is the name of the continent to be added
	 * @param power Power
	 * @param name2 is the name of the continent to be removed
	 * @throws Exception when failed to parse read command
	 */
	public void addAndRemoveContinent(String name1, String power, String name2) throws Exception {
		mapLoaderController.readCommand("editcontinent -add "+name1+" "+power+" -remove "+name2);
	}
	
	/**
	 * This method is executed by {@link #test005_addCountry()}
	 * @param name is the name of the country to be added
	 * @param continentName is the continent of the added country
	 * @throws Exception when unable to read command
	 */
	public void addCountry(String name, String continentName) throws Exception {
		mapLoaderController.readCommand("editcountry -add "+name+" "+continentName);
	}
	
	/**
	 * This method is executed by {@link #test006_removeCountry()}
	 * @param name is the name of the country to be removed
	 * @throws Exception when unable to read command
	 */
	public void removeCountry(String name) throws Exception {
		mapLoaderController.readCommand("editcountry -remove "+name);
	}
	
	/**
	 * This method is executed by {@link #test007_addAndRemoveCountry()}
	 * @param name1 is the country to be added
	 * @param continentName1 is the continent of the added country
	 * @param name2 is the country to be removed
	 * @throws Exception when unable to read command
	 */
	public void addAndRemoveCountry(String name1, String continentName1, String name2) throws Exception {
		mapLoaderController.readCommand("editcountry -add "+name1+" "+continentName1+" "+" -remove "+name2);
	}
	
	/**
	 * This method is executed by {@link #test008_addNeighbor()}.
	 * country1 is the origin country.
	 * neighbor1 is the neighboring country.
	 * borders1 is the map that stores countries and their adjacent neighbors.
	 * pair1 is the adjacency list for country1.
	 * @param origin receives the country whose neighbor will be added
	 * @param neighborCountry receives the neighbor country to be added
	 * @throws Exception  on invalid IO
	 * 
	 */
	public void addNeighbor(String origin, String neighborCountry) throws Exception {
		
		country1 = mapLoaderController.getMapService().findCorrespondingIdByCountryName(origin);
		
		neighbor1 = mapLoaderController.getMapService().findCorrespondingIdByCountryName(neighborCountry);
		
		mapLoaderController.readCommand("editneighbor -add "+origin+" "+neighborCountry);
		
		borders1 = mapLoaderController.getMapService().getAdjacencyCountriesMap();
		
		pair1 = borders1.get(country1.get());
	}
	
	/**
	 * This method is executed by {@link #test009_removeNeighbor()}.
	 * <i>country1</i> is the origin country.
	 * <i>neighbor1</i> is the neighboring country.
	 * <i>borders1</i> is the map that stores countries and their adjacent neighbors.
	 * <i>part1</i> is the adjacency list of country1.
	 * @param origin receives the country whose neighbor will be removed
	 * @param neighborCountry receives the neighbor country to be removed
	 * @throws Exception  on invalid IO
	 * 
	 */
	public void removeNeighbor(String origin, String neighborCountry) throws Exception {
		country1 = mapLoaderController.getMapService().findCorrespondingIdByCountryName(origin);
		
		neighbor1 = mapLoaderController.getMapService().findCorrespondingIdByCountryName(neighborCountry);
		
		mapLoaderController.readCommand("editneighbor -remove "+origin+" "+neighborCountry);
		
		borders1 = mapLoaderController.getMapService().getAdjacencyCountriesMap();
		
		pair1 = borders1.get(country1.get());
	}
	
	/**
	 * This method is executed by {@link #test010_addAndRemoveNeighbor()}. 
	 * <i>country1</i> is the first origin country.
	 * <i>neighbor1</i> is the to-be-added neighboring country.
	 * <i>borders1</i> is the map that stores countries and their adjacent neighbors.
	 * <i>part1</i> is the adjacency list of country1.
	 * <i>country2</i> is the second origin country.
	 * <i>neighbor2</i> is the to-be-removed neighboring country.
	 * <i>pair2</i> is the adjacency list of country2.
	 * @param origin1 receives the country whose neighbor will be added
	 * @param neighborCountry1 receives the neighbor country to be added
	 * @param origin2 receives the country whose neighbor will be removed
	 * @param neighborCountry2 receives the neighbor country to be removed
	 * @throws Exception on invalid IO
	 * 
	 */
	public void addAndRemoveNeighbor(String origin1, String neighborCountry1, String origin2, String neighborCountry2) throws Exception {
		
		country1 = mapLoaderController.getMapService().findCorrespondingIdByCountryName(origin1);
		
		neighbor1 = mapLoaderController.getMapService().findCorrespondingIdByCountryName(neighborCountry1);
		
		country2 = mapLoaderController.getMapService().findCorrespondingIdByCountryName(origin2);
		
		neighbor2 = mapLoaderController.getMapService().findCorrespondingIdByCountryName(neighborCountry2);
		
		//call the readCommand
		mapLoaderController.readCommand("editneighbor -add "+origin1+" "+neighborCountry1+" -remove "+origin2+" "+neighborCountry2);
		
		//create map object from adjacency list
		borders1 = mapLoaderController.getMapService().getAdjacencyCountriesMap();
		
		//get pair of country and neighbor
		pair1 = borders1.get(country1.get());
		pair2 = borders1.get(country2.get());
	}
	
	
	/**
	 * This method is executed by {@link #test002_addContinent()}
	 * @param name Name of continent to be added
	 * @throws Exception  on invalid IO
	 */
	public void editMap(String name) throws Exception {
		mapLoaderController.readCommand("editmap "+name);
	}


}