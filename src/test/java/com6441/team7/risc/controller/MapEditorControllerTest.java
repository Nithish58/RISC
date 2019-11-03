package com6441.team7.risc.controller;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.temporal.TemporalAmount;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.*;

import com6441.team7.risc.view.PhaseView;
import com6441.team7.risc.view.PhaseViewTest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com6441.team7.risc.view.PhaseView;
import com6441.team7.risc.controller.MapLoaderController;
import com6441.team7.risc.api.model.MapService;

/**
 *
 * MapEditorTest class tests cases relevant with the controller components of the map editor.
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MapEditorControllerTest {

	PhaseViewTest phaseViewTest;
	static private PhaseView testPhaseView;
	static private MapService testMapService;
	static private MapLoaderController testMapLoader;
	String test_map;

	//define test variables
	URI uri;
	String mapname, file, savename, inputcommand;

	int initcontinentsize, initcountrysize, expectedcontinentsize1, expectedcontinentsize2,
		expectedcontinentsize3, expectedcontinentsize4, expectedcontinentsize5
		, expectedcontinentsize6, expectedcountrysize1, expectedcountrysize2,
		expectedcountrysize3, expectedcountrysize4, expectedcountrysize5;
	static int  testCounter;
	boolean mapIsRead;
	Optional<String> inputmap;
	Optional<Integer> country1, neighbor1, country2, neighbor2;
	Map<Integer, Set<Integer>> borders1, borders2, borders3, borders4, borders5, borders6;
	Set<Integer> pair1, pair2;

	String message;
	@BeforeClass
	public static void beginClass() {
		testCounter = 0;
		testMapService = new MapService();
		testMapLoader = new MapLoaderController(testMapService);
		testPhaseView = new PhaseView();
		testMapLoader.setView(testPhaseView);

	}

	/**
	 * 
	 * @throws Exception on invalid value
	 */
	@Before
	public void beginMethod() throws Exception{
		System.out.printf("==========%nBeginning of method%n==========%n");
		mapname = "ameroki.map";
		System.out.println("Map name is : "+mapname);
		System.out.println(testMapLoader.getMapService().getContinentCountriesMap());
		System.out.println(testMapLoader.getMapService().getCountries());
		testMapLoader.getMapService().printNeighboringCountryInfo();
		System.out.println("Number of continents before test: "+testMapLoader.getMapService().getContinents().size());
		System.out.println("Number of countries before test: "+testMapLoader.getMapService().getCountries().size());
		//URI variable uri is assigned URI parameter for reading file and executing editmap command
		URI uri = getClass().getClassLoader().getResource(mapname).toURI();
		//file reads the file retrieved from the uri as string.
		//it uses UTF-8 charsets.
		file = FileUtils.readFileToString(new File(uri), StandardCharsets.UTF_8);
		//if the testCounter is not less than 3, the editmap command and map parsing must be skipped.
		//This so that the subsequent tests won't be impacted by it.
		if (testCounter < 3) {
		inputcommand = "editmap "+mapname;
		testMapLoader.parseFile(file);
		//if the testCounter is not less than 2, the editMap() method in the controller
		//must be skipped. This so that the subsequent tests won't be impacted by it.
		if(testCounter < 2) {
			inputmap = testMapLoader.editMap(inputcommand);
		}
		}
		//size of continent list before one continent is added
		initcontinentsize = testMapLoader.getMapService().getContinents().size();

		initcountrysize = testMapLoader.getMapService().getCountries().size();


		//This sets the variable for map saving command.
		savename = "edittedmap.map";
	}

	/**
	 * endMethod() is called after every method is performed. It prints out
	 * the continents list, countries list, neighbor info list, and numbers of continents
	 * and countries after each test.
	 * <i>testCounter</i> is incremented here.
	 *
	 */
	@After
	public void endMethod() {
		System.out.printf("%n%n==========%nEnd of method%n==========%n");
		System.out.println(testMapLoader.getMapService().getContinentCountriesMap());
		System.out.println(testMapLoader.getMapService().getCountries());
		testMapLoader.getMapService().printNeighboringCountryInfo();
		System.out.println("Number of continents after test: "+testMapLoader.getMapService().getContinents().size());
		System.out.println("Number of countries after test: "+testMapLoader.getMapService().getCountries().size());
		testCounter++;
	}



	/**
	 * test001_readFile() tests command to load map from file.
	 * <p>The method receives the <i>file</i>param from the context
	 * and then it is parsed. The runner passes the test if the result
	 * returns true.
	 * @throws Exception upon invalid values
	 */
	@Test
	public void test001_readFile() throws Exception{
		message = "The map is not valid";
		assertTrue(message, testMapLoader.parseFile(file));
	}

	/**
	 * test002_editMap() checks if the editmap command is valid.
	 * If the method isPresent() from the controller returns true,
	 * the test passes.
	 * @throws Exception upon invalid values
	 */
	@Test
	public void test002_editMap() throws Exception{
		System.out.printf("Testing editmap command.%n");
		message = "Invalid command.";
		assertTrue(inputmap.isPresent());
	}

	/**
	 * test003_addOneContinent() tests adding one continent to the continent list.
	 * The method uses continentcommand1 as the command to be checked.
	 * The test passes if the number of continent increases by 1 after the test.
	 * @throws Exception upon invalid values
	 */
	@Test
	public void test003_addContinent() throws Exception{
		System.out.printf("Adding one continent%n------------%n");
		addContinent("Nord_Asia","1");
		expectedcontinentsize1 = initcontinentsize + 1; //Continent list size is expected to increase by 1
		assertSame(expectedcontinentsize1, testMapLoader.getMapService().getContinents().size());
	}

	/**
	 * test005_removeOneContinent() tests deleting one continent.
	 * The method uses continentcommand3 as the command to be checked.
	 * The test passes if the number of continents decreases by 1 after the test.
	 * @throws Exception upon invalid values
	 */
	@Test
	public void test004_removeContinent() throws Exception{
		
		System.out.printf("Removing one continent%n------------%n");
		removeContinent("ulstrailia");
		expectedcontinentsize3 = initcontinentsize - 1; //Continent list size is expected to decrease by 1
		assertSame(expectedcontinentsize3, testMapLoader.getMapService().getContinents().size());
	}

	/**
	 * test007_addOneContinentRemoveOneContinent() tests adding and removing one continent from the continent list in one command.
	 * The method uses continentcommand5.
	 * The test passes if the number of continents stays the same after the test.
	 * @throws Exception upon invalid values
	 */
	@Test
	public void test005_addAndRemoveContinent() throws Exception{
		System.out.printf("Adding and removing one continent%n------------%n");
		System.out.println("East_Asia, 1 , Nord_Asia");
		addAndRemoveContinent("East_Asia","1", "Nord_Asia");
		expectedcontinentsize5 = initcontinentsize; //Continent size should remain the same
		assertSame(expectedcontinentsize5, testMapLoader.getMapService().getContinents().size());
	}

	/**
	 * test009_addOneCountry() tests adding one country to the country list.
	 * The method uses countrycommand1 as the command to be checked.
	 * The test passes if the number of countries increases by 1 after the test.
	 * @throws Exception upon invalid values
	 */
	@Test
	public void test006_addCountry() throws Exception{
		System.out.printf("Adding one country%n------------%n");
		System.out.println("Sky_Republic, East_Asia");
		addCountry("Ocean_Republic", "East_Asia");
		expectedcountrysize1 = initcountrysize+1; //Country list size is expected to increase by 1
		assertSame(expectedcountrysize1, testMapLoader.getMapService().getCountries().size());
	}

	/**
	 * test011_removeOneCountry() tests removing one country from the country list.
	 * The method uses countrycommand3 as the command to be checked.
	 * The test passes if the number of countries decreases by 1 after the test.
	 * @throws Exception upon invalid values
	 */
	@Test
	public void test007_removeCountry() throws Exception{
		System.out.printf("Removing one country%n------------%n");
		//size of country list before one country is removed
		System.out.println("heal");
		expectedcountrysize3 = initcountrysize-1; //Country list size is expected to decrease by 1
		removeCountry("heal");
		assertSame(expectedcountrysize3, testMapLoader.getMapService().getCountries().size());
	}

	/**
	 * test013_addOneCountryRemoveOneCountry() tests adding and removing one country from the country list in one command.
	 * The method uses countrycommand5 as the command to be checked.
	 * The test passes if the number of countries stays the same after the test.
	 * @throws Exception upon invalid values
	 */
	@Test
	public void test009_addAndRemoveCountry() throws Exception{
		System.out.printf("Adding and removing one country%n------------%n");
		addAndRemoveCountry("Sky_Republic", "East_Asia", "Ocean_Republic");
		expectedcountrysize5 = initcountrysize; //Country list size should remian the same
		assertSame(expectedcountrysize5, testMapLoader.getMapService().getCountries().size());
	}

	/**
	 * test015_addOneNeighbor() tests adding one neighbor of a country.
	 * The method uses neighborcommand1 as the command to be checked.
	 * The test passes if the origin country exists in the adjacency list of
	 * the entire map and the neighboring country is among the
	 * origin country's adjacency's list after the test.
	 * @throws Exception upon invalid values
	 */
	@Test
	public void test010_addNeighbor() throws Exception{
		System.out.printf("Adding one neighbor to a country%n------------%n");
		System.out.println("south afrori, india");
		addNeighbor("Sky_Republic", "india");
		//Check if map object contains both country ID and neighbor ID
		assertTrue("Country is not found", borders1.containsKey(country1.get()));
		assertTrue("Neighboring is not found", pair1.contains(neighbor1.get()));
	}

	/**
	 * test017_removeOneNeighbor() tests removing one neighbor from a country.
	 * The method uses neighborcommand3 as the command to be checked.
	 * The test passes if the origin country exists in the adjacency list of
	 * the entire map and the neighboring country is not among the
	 * origin country's adjacency's list after the test.
	 * @throws Exception upon invalid values
	 */
	@Test
	public void test011_removeNeighbor() throws Exception{
		System.out.printf("Removing one neighbor from a country%n------------%n");
		//get pair of country and neighbor
		removeNeighbor("siberia", "worrick");
		//Check if map object contains both country ID and neighbor ID
		assertTrue("Country is not found",borders1.containsKey(country1.get()));
		assertFalse("Neighboring country is found", pair1.contains(neighbor1.get()));
	}


	/**
	 * test019_addOneNeighborRemoveOneNeighbor() tests adding and removing one neighbor in one command.
	 * The method uses neighborcommand5 as the command to be checked.
	 * The test passes if the origin country exists in the adjacency list of
	 * the entire map, the added neighboring country is among the
	 * origin country's adjacency's list, and the removed neighboring country
	 * is not among the origin country's adjacency's list after the test.
	 * @throws Exception upon invalid values
	 */
	@Test
	public void test012_addAndRemoveNeighbor() throws Exception{
		System.out.printf("Adding and removing one neighbor from one country%n------------%n");
		//Set the command string to remove two neighbors
		addAndRemoveNeighbor("Sky_Republic", "siberia", "worrick", "yazteck");
		//Check if map object contains both country ID and neighbor ID
		assertTrue("Country is not found",borders1.containsKey(country1.get()));
		assertTrue("Country is not found",borders1.containsKey(country2.get()));
		assertTrue("First neighbor country is not found", pair1.contains(neighbor1.get()));
		assertFalse("Second neighbor country is found", pair2.contains(neighbor2.get()));
	}

	/**
	 * test021_invalidateMap() tests if map is invalid.
	 * The test passes if the getMapService().isMapNotValid() returns false,
	 * which it should if the preceding tests on managing continents, countries,
	 * and neighbors invalidate the map file.
	 */
	@Ignore
	@Test
	public void test013_invalidateMap() {
		System.out.printf("%nInvalidating map%n");
		assertTrue("This map is valid", testMapLoader.getMapService().isMapNotValid());
	}

	/**
	 * test022_validateMap() tests if map is valid.
	 * The test passes if the getMapService().isMapNotValid() returns true,
	 * which it should if the preceding tests on managing continents, countries,
	 * and neighbors maintain the validity of the map file.
	 */
	@Test
	public void test014_validateMap() {
		System.out.printf("%nValidating map%n");
		assertTrue("This map is invalid", testMapLoader.getMapService().isMapValid());
	}

	/**
	 * test23_saveMap() tests if map can be saved.
	 * The test passes if the saved map is found using the parseFile() method of
	 * the map controller.
	 */
	@Ignore
	@Test
	public void test015_saveMap() {
		System.out.printf("%nTesting map saving%n");
		message = "Map is invalid";
		try {
			testMapLoader.saveMap("savemap "+savename);
			file = FileUtils.readFileToString(new File(savename), StandardCharsets.UTF_8);
			assertTrue(message, testMapLoader.parseFile(file));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * This method is executed by {@link #test003_addContinent()} command for testing continent adding.
	 * @param name
	 * @param power
	 * @throws IOException
	 */
	public void addContinent(String name, String power) throws IOException {
		testMapLoader.readCommand("editcontinent -add "+name+" "+power);
	}
	
	public void removeContinent(String name) throws IOException {
		testMapLoader.readCommand("editcontinent -remove "+name);
	}
	
	public void addAndRemoveContinent(String name1, String power, String name2) throws IOException {
		testMapLoader.readCommand("editcontinent -add "+name1+" "+power+" -remove "+name2);
	}
	
	public void addCountry(String name, String continentName) throws IOException {
		testMapLoader.readCommand("editcountry -add "+name+" "+continentName);
	}
	
	public void removeCountry(String name) throws IOException {
		testMapLoader.readCommand("editcountry -remove "+name);
	}
	
	public void addAndRemoveCountry(String name1, String continentName1, String name2) throws IOException {
		testMapLoader.readCommand("editcountry -add "+name1+" "+continentName1+" "+" -remove "+name2);
	}
	
	public void addNeighbor(String origin, String neighbor) throws IOException {
		country1 = testMapLoader.getMapService().findCorrespondingIdByCountryName(origin);
		neighbor1 = testMapLoader.getMapService().findCorrespondingIdByCountryName(neighbor);
		testMapLoader.readCommand("editneighbor -add "+origin+" "+neighbor);
		borders1 = testMapLoader.getMapService().getAdjacencyCountriesMap();
		pair1 = borders1.get(country1.get());
	}
	
	public void removeNeighbor(String origin, String neighborCountry) throws IOException {
		country1 = testMapLoader.getMapService().findCorrespondingIdByCountryName(origin);
		neighbor1 = testMapLoader.getMapService().findCorrespondingIdByCountryName(neighborCountry);
		testMapLoader.readCommand("editneighbor -remove "+origin+" "+neighborCountry);
		borders1 = testMapLoader.getMapService().getAdjacencyCountriesMap();
		pair1 = borders1.get(country1.get());
	}
	
	public void addAndRemoveNeighbor(String origin1, String neighborCountry1, String origin2, String neighborCountry2) throws IOException {
		country1 = testMapLoader.getMapService().findCorrespondingIdByCountryName(origin1);
		neighbor1 = testMapLoader.getMapService().findCorrespondingIdByCountryName(neighborCountry1);
		country2 = testMapLoader.getMapService().findCorrespondingIdByCountryName(origin2);
		neighbor2 = testMapLoader.getMapService().findCorrespondingIdByCountryName(neighborCountry2);
		
		testMapLoader.readCommand("editneighbor -add "+origin1+" "+neighborCountry1+" -remove "+origin2+" "+neighborCountry2);
		//create map object from adjacency list
		borders1 = testMapLoader.getMapService().getAdjacencyCountriesMap();
		//get pair of country and neighbor
		pair1 = borders1.get(country1.get());
		pair2 = borders1.get(country2.get());
	}
	
	

}