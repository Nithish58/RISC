package com6441.team7.risc.controller;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import static org.junit.Assert.*;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com6441.team7.risc.view.CommandPromptView;
import com6441.team7.risc.controller.MapLoaderController;
import com6441.team7.risc.api.model.MapService;
//import com6441.team7.risc.controller.StateContext;

/**
 * 
 * MapEditorTest class tests cases relevant with the controller components of the map editor.
 * @author Binsar Hutapea
 * 
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MapEditorControllerTest {

	static private CommandPromptView testCmdView;
	static private MapService testMapService;
//	static private StateContext testState;
	static private MapLoaderController testMapLoader;
	static private GameController testGameController;
	String testMap;

	@BeforeClass
	public static void beginClass() {
		testCmdView = new CommandPromptView(testMapLoader, testGameController);
//		testState = new StateContext();
		testMapService = new MapService();
		testMapLoader = new MapLoaderController(testMapService);
		testMapLoader.setView(testCmdView);
		
	}
	
	/**
	 * beginMethod() is called before every method is performed.
	 */
	@Before
	public void beginMethod() {
		System.out.printf("==========%nBeginning of method%n==========%n");
		System.out.println(testMapLoader.getMapService().getContinentCountriesMap());
		System.out.println(testMapLoader.getMapService().getCountries());
		System.out.println(testMapLoader.getMapService().getDirectedGraph());
		System.out.println("Number of continents before test: "+testMapLoader.getMapService().getContinents().size());
		System.out.println("Number of countries before test: "+testMapLoader.getMapService().getCountries().size());
	}
	
	/**
	 * endMethod() is called after every method is performed.
	 */
	@After
	public void endMethod() {
		System.out.printf("%n%n==========%nEnd of method%n==========%n");
		System.out.println(testMapLoader.getMapService().getContinentCountriesMap());
		System.out.println(testMapLoader.getMapService().getCountries());
		System.out.println(testMapLoader.getMapService().getDirectedGraph());
		System.out.println("Number of continents after test: "+testMapLoader.getMapService().getContinents().size());
		System.out.println("Number of countries after test: "+testMapLoader.getMapService().getCountries().size());
	}
	
	

	/**
	 * test1_readFile() tests command to load map from file.
	 * @throws Exception upon invalid values
	 */
	@Test
	public void test001_readFile() throws Exception{
		System.out.printf("%nTesting readFile method.%n");
		URI uri = getClass().getClassLoader().getResource("ameroki.map").toURI(); 
		//readFile contains the file content and will check if the file exists.
		String file = FileUtils.readFileToString(new File(uri), StandardCharsets.UTF_8);
		boolean readFile = testMapLoader.parseFile(file);
		System.out.println(testMapLoader.getMapService().getContinentCountriesMap());
		assertTrue(readFile);
		assertFalse(!readFile);
	}
	
	/**
	 * test2_editMap() checks if the editmap command is valid.
	 * @throws Exception upon invalid values
	 */
	@Test
	public void test002_editMap() throws Exception{
		System.out.printf("Testing editmap command.%n");
		URI uri = getClass().getClassLoader().getResource("ameroki.map").toURI(); 
		String inputCommand = "editmap "+uri;
		System.out.println(inputCommand);
		//Execute editmap command.
		Optional<String> inputMap = testMapLoader.editMap(inputCommand);
		assertTrue(inputMap.isPresent());		
	}
	
	/**
	 * test3_addOneContinent() tests adding one continent to the continent list.
	 * @throws Exception upon invalid values
	 */
	@Test
	public void test003_addOneContinent() throws Exception{
		System.out.printf("Adding one continent%n------------%n");
		//size of continent list before one continent is added
		int initContinentSize = testMapLoader.getMapService().getContinents().size();
		//Expected size of continent list after one continent is added
		int expectedContinentSize = initContinentSize+1;
		//Set the command string to add one continent
		String editorCommand = "editcontinent -add Nord_Asia 1";
		System.out.println(editorCommand);
		//Retrieve substring(s) after every dash(es)
		editorCommand = StringUtils.substringAfter(editorCommand, "-");
		//Create an array of substrings for param.
		String[] editorCommands = StringUtils.split(editorCommand, "-");
		testMapLoader.editContinents(editorCommands);
		assertSame(expectedContinentSize, testMapLoader.getMapService().getContinents().size());
		//testMapLoader.editCountries(editorCommands);
		//assertSame(expectedContinentSize, testMapLoader.getMapService().getCountries().size());
	}
	
	/**
	 * test4_addTwoContinents() tests adding two continents to the continent list in one command.
	 * @throws Exception upon invalid values
	 */
	@Test
	public void test004_addTwoContinents() throws Exception{
		System.out.printf("Adding two continents%n------------%n");
		//size of continent list before one continent is added
		int initContinentSize = testMapLoader.getMapService().getContinents().size();
		//expected size of continent list after two continent is added
		int expectedContinentSize = initContinentSize+2;
		String editorCommand = "editcontinent -add Southeast_Asia 1 -add Northeast_Asia 1";
		System.out.println(editorCommand);
		//Retrieve substring(s) after every dash(es)
		editorCommand = StringUtils.substringAfter(editorCommand, "-");
		//Create an array of substrings for param.
		String[] editorCommands = StringUtils.split(editorCommand, "-");
		testMapLoader.editContinents(editorCommands);
		assertSame(expectedContinentSize, testMapLoader.getMapService().getContinents().size());
	}
	
	/**
	 * test5_removeOneContinent() tests deleting one continent.
	 * @throws Exception upon invalid values
	 */
	@Test
	public void test005_removeOneContinent() throws Exception{
		System.out.printf("Removing one continent%n------------%n");
		//size of continent list before one continent is removed
		int initContinentSize = testMapLoader.getMapService().getContinents().size();
		//expected size of continent list after one continent is removed
		int expectedContinentSize = initContinentSize-1;
		String editorCommand = "editcontinent -remove ulstrailia";
		System.out.println(editorCommand);
		//Retrieve substring(s) after every dash(es)
		editorCommand = StringUtils.substringAfter(editorCommand, "-");
		//Create an array of substrings for param.
		String[] editorCommands = StringUtils.split(editorCommand, "-");
		testMapLoader.editContinents(editorCommands);
		assertSame(expectedContinentSize, testMapLoader.getMapService().getContinents().size());
	}
	
	/**
	 * test6_removeTwoContinents() tests removing continents from the continent list in one command.
	 * @throws Exception upon invalid values
	 */
	@Test
	public void test006_removeTwoContinents() throws Exception{
		System.out.printf("Removing two continents%n------------%n");
		//size of continent list before two continents are removed
		int initContinentSize = testMapLoader.getMapService().getContinents().size();
		//expected size of continent list after two continents are removed
		int expectedContinentSize = initContinentSize-2;
		String editorCommand = "editcontinent -remove ameroki 1 -remove amerpoll 1";
		System.out.println(editorCommand);
		//Retrieve substring(s) after every dash(es)
		editorCommand = StringUtils.substringAfter(editorCommand, "-");
		//Create an array of substrings for param.
		String[] editorCommands = StringUtils.split(editorCommand, "-");
		testMapLoader.editContinents(editorCommands);
		assertSame(expectedContinentSize, testMapLoader.getMapService().getContinents().size());
	}
	
	/**
	 * test7_addOneContinentRemoveOneContinent() tests adding and removing one continent from the continent list in one command.
	 * @throws Exception upon invalid values
	 */
	@Test
	public void test007_addOneContinentRemoveOneContinent() throws Exception{
		System.out.printf("Adding and removing one continent%n------------%n");
		//size of continent list before after one continent is added and one continent is removed
		int initContinentSize = testMapLoader.getMapService().getContinents().size();
		//expected size of continent list after one continent is added and one countinent is removed
		int expectedContinentSize = initContinentSize;
		String editorCommand = "editcontinent -add NordWest_Asia 9 -remove Southeast_Asia";
		System.out.println(editorCommand);
		//Retrieve substring(s) after every dash(es)
		editorCommand = StringUtils.substringAfter(editorCommand, "-");
		//Create an array of substrings for param.
		String[] editorCommands = StringUtils.split(editorCommand, "-");
		testMapLoader.editContinents(editorCommands);
		assertSame(expectedContinentSize, testMapLoader.getMapService().getContinents().size());
	}
	
	/**
	 * test8_addTwoContinentsRemoveTwoContinents() tests adding and removing two continents from the continent list in one command.
	 * @throws Exception upon invalid values
	 */
	@Test
	public void test008_addTwoContinentsRemoveTwosContinents() throws Exception{
		System.out.printf("Adding and removing two continents%n------------%n");
		//size of continent list before after one country is added and one country is removed
		int initContinentSize = testMapLoader.getMapService().getContinents().size();
		//expected size of continent list after one country is added and one country is removed
		int expectedContinentSize = initContinentSize;
		String editorCommand = "editcontinent -add NordEast_Europe 4 -add SouthWest_Europe 3 -remove utropa -remove afrori";
		System.out.println(editorCommand);
		//Retrieve substring(s) after every dash(es)
		editorCommand = StringUtils.substringAfter(editorCommand, "-");
		//Create an array of substrings for param.
		String[] editorCommands = StringUtils.split(editorCommand, "-");
		testMapLoader.editContinents(editorCommands);
		assertSame(expectedContinentSize, testMapLoader.getMapService().getContinents().size());
	}
	
	/**
	 * test9_addOneCountry() tests adding one country to the country list.
	 * @throws Exception upon invalid values
	 */
	@Test
	public void test009_addOneCountry() throws Exception{
		System.out.printf("Adding one country%n------------%n");
		//size of country list before one country is added
		int initCountrySize = testMapLoader.getMapService().getCountries().size();
		//Expected size of country list after one country is added
		int expectedCountrySize = initCountrySize+1;
		//Set the command string to add one country
		String editorCommand = "editcountry -add Nordenstan Nord_Asia";
		System.out.println(editorCommand);
		//Retrieve substring(s) after every dash(es)
		editorCommand = StringUtils.substringAfter(editorCommand, "-");
		//Create an array of substrings for param.
		String[] editorCommands = StringUtils.split(editorCommand, "-");
		testMapLoader.editCountries(editorCommands);
		assertSame(expectedCountrySize, testMapLoader.getMapService().getCountries().size());
	}
	
	/**
	 * test10_addTwoCountries() tests adding one country to the country list.
	 * @throws Exception upon invalid values
	 */
	@Test
	public void test010_addTwoCountries() throws Exception{
		System.out.printf("Adding two countries%n------------%n");
		//size of country list before one country is added
		int initCountrySize = testMapLoader.getMapService().getCountries().size();
		//Expected size of country list after one country is added
		int expectedCountrySize = initCountrySize+2;
		//Set the command string to add two countries
		String editorCommand = "editcountry -add Nordennavic NordEast_Europe -add United_Islands Northeast_Asia";
		System.out.println(editorCommand);
		//Retrieve substring(s) after every dash(es)
		editorCommand = StringUtils.substringAfter(editorCommand, "-");
		//Create an array of substrings for param.
		String[] editorCommands = StringUtils.split(editorCommand, "-");
		testMapLoader.editCountries(editorCommands);
		assertSame(expectedCountrySize, testMapLoader.getMapService().getCountries().size());
	}
	
	/**
	 * test011_removeOneCountry() tests removing one country from the country list.
	 * @throws Exception upon invalid values
	 */
	@Test
	public void test011_removeOneCountry() throws Exception{
		System.out.printf("Removing one country%n------------%n");
		//size of country list before one country is removed
		int initCountrySize = testMapLoader.getMapService().getCountries().size();
		//Expected size of country list after one country is removed
		int expectedCountrySize = initCountrySize-1;
		//Set the command string to remove one country
		String editorCommand = "editcountry -remove united_islands";
		System.out.println(editorCommand);
		//Retrieve substring(s) after every dash(es)
		editorCommand = StringUtils.substringAfter(editorCommand, "-");
		//Create an array of substrings for param.
		String[] editorCommands = StringUtils.split(editorCommand, "-");
		testMapLoader.editCountries(editorCommands);
		assertSame(expectedCountrySize, testMapLoader.getMapService().getCountries().size());
	}
	
	/**
	 * test012_removeTwoCountries() tests removing two countries from the country list.
	 * @throws Exception upon invalid values
	 */
	@Test
	public void test011_removeTwoCountries() throws Exception{
		System.out.printf("Removing two countries%n------------%n");
		//size of country list before two countries are removed
		int initCountrySize = testMapLoader.getMapService().getCountries().size();
		//Expected size of country list after two countries are removed
		int expectedCountrySize = initCountrySize-2;
		//Set the command string to remove two countries
		String editorCommand = "editcountry -remove maganar -remove pero";
		System.out.println(editorCommand);
		//Retrieve substring(s) after every dash(es)
		editorCommand = StringUtils.substringAfter(editorCommand, "-");
		//Create an array of substrings for param.
		String[] editorCommands = StringUtils.split(editorCommand, "-");
		testMapLoader.editCountries(editorCommands);
		assertSame(expectedCountrySize, testMapLoader.getMapService().getCountries().size());
	}
	
	/**
	 * test013_addOneCountryRemoveOneCountry() tests adding and removing one country from the country list in one command.
	 * @throws Exception upon invalid values
	 */
	@Test
	public void test013_addOneCountryRemoveOneCountry() throws Exception{
		System.out.printf("Adding and removing one country%n------------%n");
		//size of country list before after one country is added and one country is removed
		int initCountrySize = testMapLoader.getMapService().getCountries().size();
		//expected size of country list after one country is added and one country is removed
		int expectedCountrySize = initCountrySize;
		String editorCommand = "editcountry -add Fiji azio -remove Nordenstan";
		System.out.println(editorCommand);
		//Retrieve substring(s) after every dash(es)
		editorCommand = StringUtils.substringAfter(editorCommand, "-");
		//Create an array of substrings for param.
		String[] editorCommands = StringUtils.split(editorCommand, "-");
		testMapLoader.editCountries(editorCommands);
		assertSame(expectedCountrySize, testMapLoader.getMapService().getCountries().size());
	}
	
	/**
	 * test014_addTwoCountriesRemoveTwoCountries() tests adding and removing two countries from the country list in one command.
	 * @throws Exception upon invalid values
	 */
	@Test
	public void test014_addTwoCountriesRemoveTwoCountries() throws Exception{
		System.out.printf("Adding and removing two countries%n------------%n");
		//size of country list before after two countries are added and two countries are removed
		int initCountrySize = testMapLoader.getMapService().getCountries().size();
		//expected size of country list after two countries are added and two countries are removed
		int expectedCountrySize = initCountrySize;
		String editorCommand = "editcountry -add Sky_Republic Nord Asia -add Edmonton Alberta -remove vinenlant -remove heal";
		System.out.println(editorCommand);
		//Retrieve substring(s) after every dash(es)
		editorCommand = StringUtils.substringAfter(editorCommand, "-");
		//Create an array of substrings for param.
		String[] editorCommands = StringUtils.split(editorCommand, "-");
		testMapLoader.editCountries(editorCommands);
		assertSame(expectedCountrySize, testMapLoader.getMapService().getCountries().size());
	}
	
	/**
	 * test015_addOneNeighbor() tests adding one neighbor of a country.
	 * @throws Exception upon invalid values
	 */
	@Ignore
	@Test
	public void test015_addOneNeighbor() throws Exception{
		System.out.printf("Adding one neighbor to a country%n------------%n");
		//size of country list before one country is added
		//int initCountrySize = testMapLoader.getMapService().getCountries().size();
		//Expected size of country list after one country is added
		//int expectedCountrySize = initCountrySize+1;
		//Set the command string to add one country
		String editorCommand = "editneighbor -add nordennavic northern_utropa";
		System.out.println(editorCommand);
		//Retrieve substring(s) after every dash(es)
		editorCommand = StringUtils.substringAfter(editorCommand, "-");
		//Create an array of substrings for param.
		String[] editorCommands = StringUtils.split(editorCommand, "-");
		testMapLoader.editCountries(editorCommands);
		//assertSame(expectedCountrySize, testMapLoader.getMapService().getCountries().size());
	}
	
	/**
	 * test19_validateMap() tests if map is valid.
	 */
	@Ignore
	@Test
	public void test019_validateMap() {
		System.out.printf("%nTesting map validation%n");
	}
	
	/**
	 * test4_saveMap() tests if map can be saved.
	 */
	@Ignore
	@Test
	public void test020_saveMap() {
		System.out.printf("%nTesting map saving%n");
		try {
			testMapLoader.saveMap("savemap");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
