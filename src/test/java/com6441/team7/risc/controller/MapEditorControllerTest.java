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
	static private MapLoaderController testMapLoader;
	static private GameController testGameController;
	String test_map;
	
	//define test params
	URI uri;
	String mapname, file, inputcommand, editorcommand1, editorcommand2, editorcommand3, editorcommand4, 
		editorcommand5, editorcommand6, editorcommand7, editorcommand8,	editorcommand9, 
		editorcommand10, editorcommand11, editorcommand12, editorcommand13, editorcommand14,
		editorcommand15, editorcommand16, editorcommand17, editorcommand18, editorcommand19, 
		editorcommand20, newcontinentstr1, newcontinentstr2a, newcontinentstr2b, delcontinentstr1, delcontinentstr2a,
		delcontinentstr2b, newcontinentstr3, delcontinentstr3, newcontinentstr4a, newcontinentstr4b, 
		delcontinentstr4a, delcontinentstr4b, newcountrystr1, countrycontinentstr1, newcountrystr2a, countrycontinentstr2a,
		newcountrystr2b, countrycontinentstr2b, delcountrystr1, delcountrystr2a, delcountrystr2b, newcountrystr3, 
		countrycontinentstr3, delcountrystr3, newcountrystr4a, newcountrystr4b, countrycontinentstr4a, countrycontinentstr4b,
		delcountrystr4a, delcountrystr4b;
	String[] editorcommands1, editorcommands2, editorcommands3, editorcommands4, editorcommands5,
		editorcommands6, editorcommands7, editorcommands8, editorcommands9, editorcommands10,
		editorcommands11, editorcommands12, editorcommands13, editorcommands14, editorcommands15,
		editorcommands16, editorcommands17, editorcommands18, editorcommands19, editorcommands20;
	int initcontinentsize, initcountrysize, expectedcontinentsize1, expectedcontinentsize2,
		expectedcontinentsize3, expectedcontinentsize4, expectedcontinentsize5
		, expectedcontinentsize6, expectedcountrysize1, expectedcountrysize2,
		expectedcountrysize3, expectedcountrysize4, expectedcountrysize5
		, expectedcountrysize6;
	Optional<String> inputmap;
	Optional<Integer> country1, neighbor1, country2, neighbor2a, neighbor2b, country3, neighbor3,
		country4, neighbor4a, neighbor4b, country5, neighbor5a, neighbor5b,
		country6, neighbor6a, neighbor6b, neighbor6c, neighbor6d;
	Map<Integer, Set<Integer>> borders1, borders2, borders3, borders4, borders5, borders6;
	Set<Integer> pair1, pair2, pair3, pair4, pair5, pair6;

	String message;
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
	public void beginMethod() throws Exception{
		System.out.printf("==========%nBeginning of method%n==========%n");
		mapname = "ameroki.map";
		System.out.println("Map name is : "+mapname);
		System.out.println(testMapLoader.getMapService().getContinentCountriesMap());
		System.out.println(testMapLoader.getMapService().getCountries());
		testMapLoader.getMapService().printNeighboringCountryInfo();
		System.out.println("Number of continents before test: "+testMapLoader.getMapService().getContinents().size());
		System.out.println("Number of countries before test: "+testMapLoader.getMapService().getCountries().size());
		/**
		 * URI variable uri is assigned URI parameter for reading file and executing editmap command
		 */
		URI uri = getClass().getClassLoader().getResource(mapname).toURI(); 
		/**
		 * file reads the file retrieved from the uri as string.
		 * it uses UTF-8 charsets.
		 */
		file = FileUtils.readFileToString(new File(uri), StandardCharsets.UTF_8);
		/**
		 * The followings are for test case 002 : executing editmap
		 */
		inputcommand = "editmap "+uri;
		inputmap = testMapLoader.editMap(inputcommand);
		//size of continent list before one continent is added
		/**
		 * The followings are for test cases on editing continent
		 * <li>initcontinentsize defines the size of continent before continent edit 
		 * operation is performed.
		 */
		initcontinentsize = testMapLoader.getMapService().getContinents().size();
		/**
		 * The followings are for the test cases of editing continent
		 * expectedcontinentsize is the expected size of continent list
		 * after edit operation
		 * <li>editorcommand contains the editcontinent commands.
		 * <li>editorcommands is an array that contains the split string for parameters
		 */
		newcontinentstr1 = "Nord_Asia"; //one continent to be added
		newcontinentstr2a = "Southeast_Asia"; newcontinentstr2b = "Northeast_Asia"; //two continents to be added
		delcontinentstr1 = "ulstrailia"; //one continent to be removed
		delcontinentstr2a = "ameroki"; delcontinentstr2b = "amerpoll"; //two continents to be deleted
		newcontinentstr3 = "NordWest_Asia"; delcontinentstr3 = "Southeast_Asia"; //continents to be added and deleted one each in a line
		newcontinentstr4a= "NordEast_Europe"; newcontinentstr4b = "SouthWest_Europe"; delcontinentstr4a="utropa"; delcontinentstr4b="afrori"; 
		expectedcontinentsize1 = initcontinentsize + 1; //continent size is expected to increase by one after adding one continent
		editorcommand1 = "editcontinent -add "+newcontinentstr1+" 1";
		editorcommand1 = StringUtils.substringAfter(editorcommand1, "-");
		editorcommands1 = StringUtils.split(editorcommand1, "-");
		expectedcontinentsize2 = initcontinentsize + 2;
		editorcommand2 = "editcontinent -add "+newcontinentstr2a+" 1 -add "+newcontinentstr2b+" 1";
		editorcommand2 = StringUtils.substringAfter(editorcommand2, "-");
		editorcommands2 = StringUtils.split(editorcommand2, "-");
		expectedcontinentsize3 = initcontinentsize - 1;
		editorcommand3 = "editcontinent -remove "+delcontinentstr1;
		editorcommand3 = StringUtils.substringAfter(editorcommand3, "-");
		editorcommands3 = StringUtils.split(editorcommand3, "-");
		expectedcontinentsize4 = initcontinentsize - 2;
		editorcommand4 = "editcontinent -remove "+delcontinentstr2a+" 1 -remove "+delcontinentstr2b+" 1";
		editorcommand4 = StringUtils.substringAfter(editorcommand4, "-");
		editorcommands4 = StringUtils.split(editorcommand4, "-");
		expectedcontinentsize5 = initcontinentsize;
		editorcommand5 = "editcontinent -add "+newcontinentstr3+" 9 -remove "+delcontinentstr3;
		editorcommand5 = StringUtils.substringAfter(editorcommand5, "-");
		editorcommands5 = StringUtils.split(editorcommand5, "-");
		expectedcontinentsize6 = initcontinentsize;
		editorcommand6 = "editcontinent -add  "+newcontinentstr4a+" 4 -add "+newcontinentstr4b+" 3 -remove "+delcontinentstr4a+" -remove "+delcontinentstr4b;
		editorcommand6 = StringUtils.substringAfter(editorcommand6, "-");
		editorcommands6 = StringUtils.split(editorcommand6, "-");
		/**
		 * The followings are for the test cases of editing countries
		 * expectedcountrysize is the expected size of country list
		 * after edit operation
		 * <li>editorcommand contains the editcountry commands.
		 * <li>editorcommands is an array that contains the split string for parameters
		 */
		initcountrysize = testMapLoader.getMapService().getCountries().size();
		newcountrystr1 = "Nordenstan"; countrycontinentstr1 = "Nord_Asia";//one country to be added
		newcountrystr2a = "Nordennavic"; countrycontinentstr2a = "NordEast_Europe"; newcountrystr2b = "Northeast_Asia"; 
		newcountrystr2b = "United_Islands"; countrycontinentstr2b = "Northeast_Asia";//two countrys to be added
		delcountrystr1 = "united_islands"; //one country to be removed
		delcountrystr2a = "yazteck"; delcountrystr2b = "kongrolo"; //two countrys to be deleted
		newcountrystr3 = "Fiji"; countrycontinentstr3="azio"; delcountrystr3 = "Nordenstan"; //countrys to be added and deleted one each in a line
		newcountrystr4a= "Sky_Republic"; countrycontinentstr4a="Nord_Asia"; 
		newcountrystr4b = "Ocean_Republic"; countrycontinentstr4b="Northeast_Asia"; delcountrystr4a="sluci"; delcountrystr4b="kancheria"; 
		expectedcountrysize1 = initcountrysize+1;
		editorcommand7 = "editcountry -add "+newcountrystr1+" "+countrycontinentstr1;
		editorcommand7 = StringUtils.substringAfter(editorcommand7, "-");
		editorcommands7 = StringUtils.split(editorcommand7, "-");
		expectedcountrysize2 = initcountrysize+2;
		editorcommand8 = "editcountry -add "+newcountrystr2a+" "+countrycontinentstr2a+" -add  "+newcountrystr2b+" "+countrycontinentstr2b;
		editorcommand8 = StringUtils.substringAfter(editorcommand8, "-");
		editorcommands8 = StringUtils.split(editorcommand8, "-");
		expectedcountrysize3 = initcountrysize-1;
		editorcommand9 = "editcountry -remove "+delcountrystr1;
		editorcommand9 = StringUtils.substringAfter(editorcommand9, "-");
		editorcommands9 = StringUtils.split(editorcommand9, "-");
		expectedcountrysize4 = initcountrysize-2;
		editorcommand10 = "editcountry -remove  "+delcountrystr2a+" -remove "+delcountrystr2b;
		editorcommand10 = StringUtils.substringAfter(editorcommand10, "-");
		editorcommands10 = StringUtils.split(editorcommand10, "-");
		expectedcountrysize5 = initcountrysize;
		editorcommand11 = "editcountry -add "+newcountrystr3+" "+countrycontinentstr3+" -remove "+delcountrystr3;
		editorcommand11 = StringUtils.substringAfter(editorcommand11, "-");
		editorcommands11 = StringUtils.split(editorcommand11, "-");
		expectedcountrysize6 = initcountrysize;
		editorcommand12 = "editcountry -add "+newcountrystr4a+" "+countrycontinentstr4a
					+" -add "+newcountrystr4b+" "+countrycontinentstr4b+" -remove "+delcountrystr4a+" -remove "+delcountrystr4b;
		editorcommand12 = StringUtils.substringAfter(editorcommand12, "-");
		editorcommands12 = StringUtils.split(editorcommand12, "-");
		/**
		 * The followings are for neighbor adding test:
		 * 
		 */
		editorcommand13 = "editneighbor -add nordennavic siberia";
		editorcommand13 = StringUtils.substringAfter(editorcommand13, "-");
		editorcommands13 = StringUtils.split(editorcommand13, "-");
		country1 = testMapLoader.getMapService().findCorrespondingIdByCountryName("nordennavic");
		neighbor1 = testMapLoader.getMapService().findCorrespondingIdByCountryName("siberia");
		editorcommand14 = "editneighbor -add fiji japan -add fiji india";
		editorcommand14 = StringUtils.substringAfter(editorcommand14, "-");
		editorcommands14 = StringUtils.split(editorcommand14, "-");
		country2 = testMapLoader.getMapService().findCorrespondingIdByCountryName("fiji");
		neighbor2a = testMapLoader.getMapService().findCorrespondingIdByCountryName("japan");
		neighbor2b = testMapLoader.getMapService().findCorrespondingIdByCountryName("india");
		editorcommand15 = "editneighbor -remove siberia worrick";
		editorcommand15 = StringUtils.substringAfter(editorcommand15, "-");
		editorcommands15 = StringUtils.split(editorcommand15, "-");
		country3 = testMapLoader.getMapService().findCorrespondingIdByCountryName("siberia");
		neighbor3 = testMapLoader.getMapService().findCorrespondingIdByCountryName("worrick");
		editorcommand16 = "editneighbor -remove worrick india -remove worrick nordennavic";
		editorcommand16 = StringUtils.substringAfter(editorcommand16, "-");
		editorcommands16 = StringUtils.split(editorcommand16, "-");
		country4 = testMapLoader.getMapService().findCorrespondingIdByCountryName("worrick");
		neighbor4a = testMapLoader.getMapService().findCorrespondingIdByCountryName("india");
		neighbor4b = testMapLoader.getMapService().findCorrespondingIdByCountryName("nordennavic");
		editorcommand17 = "editneighbor -add japan afganistan -remove japan fiji";
		editorcommand17 = StringUtils.substringAfter(editorcommand17, "-");
		editorcommands17 = StringUtils.split(editorcommand17, "-");
		country5 = testMapLoader.getMapService().findCorrespondingIdByCountryName("japan");
		neighbor5a = testMapLoader.getMapService().findCorrespondingIdByCountryName("afganistan");
		neighbor5b = testMapLoader.getMapService().findCorrespondingIdByCountryName("fiji");
		editorcommand18 = "editneighbor -add china sky_republic -add china ocean_republic -remove china middle_east -remove china heal";
		editorcommand18 = StringUtils.substringAfter(editorcommand18, "-");
		editorcommands18 = StringUtils.split(editorcommand18, "-");
		country6 = testMapLoader.getMapService().findCorrespondingIdByCountryName("china");
		neighbor6a = testMapLoader.getMapService().findCorrespondingIdByCountryName("sky_republic");
		neighbor6b = testMapLoader.getMapService().findCorrespondingIdByCountryName("ocean_republic");
		neighbor6c = testMapLoader.getMapService().findCorrespondingIdByCountryName("middle_east");
		neighbor6d = testMapLoader.getMapService().findCorrespondingIdByCountryName("heal");
	}
	
	/**
	 * endMethod() is called after every method is performed.
	 */
	@After
	public void endMethod() {
		System.out.printf("%n%n==========%nEnd of method%n==========%n");
		System.out.println(testMapLoader.getMapService().getContinentCountriesMap());
		System.out.println(testMapLoader.getMapService().getCountries());
		testMapLoader.getMapService().printNeighboringCountryInfo();
		System.out.println("Number of continents after test: "+testMapLoader.getMapService().getContinents().size());
		System.out.println("Number of countries after test: "+testMapLoader.getMapService().getCountries().size());
	}
	
	

	/**
	 * test1_readFile() tests command to load map from file.
	 * @throws Exception upon invalid values
	 */
	@Test
	public void test001_readFile() throws Exception{
		message = "The map is not valid";
		assertTrue(message, testMapLoader.parseFile(file));
	}
	
	/**
	 * test2_editMap() checks if the editmap command is valid.
	 * @throws Exception upon invalid values
	 */
	@Test
	public void test002_editMap() throws Exception{
		System.out.printf("Testing editmap command.%n");
		message = "Invalid command.";
		assertTrue(inputmap.isPresent());		
	}
	
	/**
	 * test3_addOneContinent() tests adding one continent to the continent list.
	 * @throws Exception upon invalid values
	 */
	@Test
	public void test003_addOneContinent() throws Exception{
		System.out.printf("Adding one continent%n------------%n");
		System.out.println(editorcommand1);
		testMapLoader.editContinents(editorcommands1);
		assertSame(expectedcontinentsize1, testMapLoader.getMapService().getContinents().size());
	}
	
	/**
	 * test4_addTwoContinents() tests adding two continents to the continent list in one command.
	 * @throws Exception upon invalid values
	 */
	@Test
	public void test004_addTwoContinents() throws Exception{
		System.out.printf("Adding two continents%n------------%n");
		System.out.println(editorcommand2);
		testMapLoader.editContinents(editorcommands2);
		assertSame(expectedcontinentsize2, testMapLoader.getMapService().getContinents().size());
	}
	
	/**
	 * test5_removeOneContinent() tests deleting one continent.
	 * @throws Exception upon invalid values
	 */
	@Test
	public void test005_removeOneContinent() throws Exception{
		System.out.printf("Removing one continent%n------------%n");
		System.out.println(editorcommand3);
		testMapLoader.editContinents(editorcommands3);
		assertSame(expectedcontinentsize3, testMapLoader.getMapService().getContinents().size());
	}
	
	/**
	 * test6_removeTwoContinents() tests removing continents from the continent list in one command.
	 * @throws Exception upon invalid values
	 */
	@Test
	public void test006_removeTwoContinents() throws Exception{
		System.out.printf("Removing two continents%n------------%n");
		System.out.println(editorcommand4);
		testMapLoader.editContinents(editorcommands4);
		assertSame(expectedcontinentsize4, testMapLoader.getMapService().getContinents().size());
	}
	
	/**
	 * test7_addOneContinentRemoveOneContinent() tests adding and removing one continent from the continent list in one command.
	 * @throws Exception upon invalid values
	 */
	@Test
	public void test007_addOneContinentRemoveOneContinent() throws Exception{
		System.out.printf("Adding and removing one continent%n------------%n");
		System.out.println(editorcommand5);
		testMapLoader.editContinents(editorcommands5);
		assertSame(expectedcontinentsize5, testMapLoader.getMapService().getContinents().size());
	}
	
	/**
	 * test8_addTwoContinentsRemoveTwoContinents() tests adding and removing two continents from the continent list in one command.
	 * @throws Exception upon invalid values
	 */
	@Test
	public void test008_addTwoContinentsRemoveTwosContinents() throws Exception{
		System.out.printf("Adding and removing two continents%n------------%n");
		System.out.println(editorcommand6);
		testMapLoader.editContinents(editorcommands6);
		assertSame(expectedcontinentsize6, testMapLoader.getMapService().getContinents().size());
	}
	
	/**
	 * test9_addOneCountry() tests adding one country to the country list.
	 * @throws Exception upon invalid values
	 */
	@Test
	public void test009_addOneCountry() throws Exception{
		System.out.printf("Adding one country%n------------%n");
		System.out.println(editorcommand7);
		testMapLoader.editCountries(editorcommands7);
		assertSame(expectedcountrysize1, testMapLoader.getMapService().getCountries().size());
	}
	
	/**
	 * test10_addTwoCountries() tests adding one country to the country list.
	 * @throws Exception upon invalid values
	 */
	@Test
	public void test010_addTwoCountries() throws Exception{
		System.out.printf("Adding two countries%n------------%n");
		System.out.println(editorcommand8);
		testMapLoader.editCountries(editorcommands8);
		assertSame(expectedcountrysize2, testMapLoader.getMapService().getCountries().size());
	}
	
	/**
	 * test011_removeOneCountry() tests removing one country from the country list.
	 * @throws Exception upon invalid values
	 */
	@Test
	public void test011_removeOneCountry() throws Exception{
		System.out.printf("Removing one country%n------------%n");
		//size of country list before one country is removed
		System.out.println(editorcommand9);
		testMapLoader.editCountries(editorcommands9);
		assertSame(expectedcountrysize3, testMapLoader.getMapService().getCountries().size());
	}
	
	/**
	 * test012_removeTwoCountries() tests removing two countries from the country list.
	 * @throws Exception upon invalid values
	 */
	@Test
	public void test012_removeTwoCountries() throws Exception{
		System.out.printf("Removing two countries%n------------%n");
		System.out.println(editorcommand10);
		testMapLoader.editCountries(editorcommands10);
		assertSame(expectedcountrysize4, testMapLoader.getMapService().getCountries().size());
	}
	
	/**
	 * test013_addOneCountryRemoveOneCountry() tests adding and removing one country from the country list in one command.
	 * @throws Exception upon invalid values
	 */
	@Test
	public void test013_addOneCountryRemoveOneCountry() throws Exception{
		System.out.printf("Adding and removing one country%n------------%n");
		System.out.println(editorcommand11);
		testMapLoader.editCountries(editorcommands11);
		assertSame(expectedcountrysize5, testMapLoader.getMapService().getCountries().size());
	}
	
	/**
	 * test014_addTwoCountriesRemoveTwoCountries() tests adding and removing two countries from the country list in one command.
	 * @throws Exception upon invalid values
	 */
	@Test
	public void test014_addTwoCountriesRemoveTwoCountries() throws Exception{
		System.out.printf("Adding and removing two countries%n------------%n");
		System.out.println(editorcommand12);
		testMapLoader.editCountries(editorcommands12);
		assertSame(expectedcountrysize6, testMapLoader.getMapService().getCountries().size());
	}
	
	/**
	 * test015_addOneNeighbor() tests adding one neighbor of a country.
	 * @throws Exception upon invalid values
	 */
	@Test
	public void test015_addOneNeighbor() throws Exception{
		System.out.printf("Adding one neighbor to a country%n------------%n");
		System.out.println(editorcommand13);
		borders1 = testMapLoader.getMapService().getAdjacencyCountriesMap();
		testMapLoader.editNeighbor(editorcommands13);
		pair1 = borders1.get(country1.get());
		//Check if map object contains both country ID and neighbor ID
		assertTrue("Country is not found",borders1.containsKey(country1.get()));
		assertTrue("Neighbor country is not found", pair1.contains(neighbor1.get()));
	}
	
	/**
	 * test016_addTwoNeighbors() tests adding two neighbors for one or two countries in one command.
	 * @throws Exception upon invalid values
	 */
	@Test
	public void test016_addTwoNeighbors() throws Exception{
		System.out.printf("Adding two neighbors to one country%n------------%n");
		//Set the command string to add two neighbors
		System.out.println(editorcommand14);
		borders2 = testMapLoader.getMapService().getAdjacencyCountriesMap();
		testMapLoader.editNeighbor(editorcommands14);
		//get pair of country and neighbor
		pair2 = borders2.get(country2.get());
		//Check if map object contains both country ID and neighbor ID
		assertTrue("Country is not found",borders2.containsKey(country2.get()));
		assertTrue("First neighbor country is not found", pair2.contains(neighbor2a.get()));
		assertTrue("Second neighbor country is not found", pair2.contains(neighbor2b.get()));
	}
	
	/**
	 * test017_removeOneNeighbor() tests removing one neighbor from a country.
	 * @throws Exception upon invalid values
	 */
	@Test
	public void test017_removeOneNeighbor() throws Exception{
		System.out.printf("Removing one neighbor from a country%n------------%n");
		System.out.println(editorcommand15);
		borders3 = testMapLoader.getMapService().getAdjacencyCountriesMap();
		testMapLoader.editNeighbor(editorcommands15);
		//get pair of country and neighbor
		pair3 = borders3.get(country3.get());
		//Check if map object contains both country ID and neighbor ID
		assertTrue("Country is not found",borders3.containsKey(country3.get()));
		assertFalse("Neighbor country is found", pair3.contains(neighbor3.get()));
	}
	
	/**
	 * test018_removeTwoNeighbors() tests removing two neighbors from one or two countries in one command.
	 * @throws Exception upon invalid values
	 */
	@Test
	public void test018_removeTwoNeighbors() throws Exception{
		System.out.printf("Removing two neighbors from one country%n------------%n");
		//Set the command string to remove two neighbors
		System.out.println(editorcommand16);
		testMapLoader.editNeighbor(editorcommands16);
		borders4 = testMapLoader.getMapService().getAdjacencyCountriesMap();
		//get pair of country and neighbor
		Set<Integer> pair4 = borders4.get(country4.get());
		//Check if map object contains both country ID and neighbor ID
		assertTrue("Country is not found",borders4.containsKey(country4.get()));
		assertFalse("First neighbor country is found", pair4.contains(neighbor4a.get()));
		assertFalse("Second neighbor country is found", pair4.contains(neighbor4a.get()));
	}
	
	/**
	 * test019_addOneNeighborRemoveOneNeighbor() tests adding and removing one neighbor in one command.
	 * @throws Exception upon invalid values
	 */
	@Test
	public void test019_addOneNeighborRemoveOneNeighbor() throws Exception{
		System.out.printf("Adding and removing one neighbor from one country%n------------%n");
		//Set the command string to remove two neighbors
		System.out.println(editorcommand17);
		testMapLoader.editNeighbor(editorcommands17);
		//create map object from adjacency list
		borders5 = testMapLoader.getMapService().getAdjacencyCountriesMap();
		//get pair of country and neighbor
		pair5 = borders5.get(country5.get());
		//Check if map object contains both country ID and neighbor ID
		assertTrue("Country is not found",borders5.containsKey(country5.get()));
		assertTrue("First neighbor country is not found", pair5.contains(neighbor5a.get()));
		assertFalse("Second neighbor country is found", pair5.contains(neighbor5b.get()));
	}
	
	/**
	 * test020_addTwoNeighborsRemoveTwoNeighbors() tests adding and removing two neighbors in one command.
	 * @throws Exception upon invalid values
	 */
	@Test
	public void test020_addTwoNeighborsRemoveTwoNeighbors() throws Exception{
		System.out.printf("Adding and removing two neighbors from one country%n------------%n");
		//Set the command string to remove two neighbors
		System.out.println(editorcommand18);
		testMapLoader.editNeighbor(editorcommands18);
		//create map object from adjacency list
		borders6 = testMapLoader.getMapService().getAdjacencyCountriesMap();
		//get pair of country and neighbor
		pair6 = borders6.get(country6.get());
		//Check if map object contains both country ID and neighbor ID
		assertTrue("Country is not found",borders6.containsKey(country6.get()));
		assertTrue("First added neighbor country is not found", pair6.contains(neighbor6a.get()));
		assertTrue("Second added neighbor country is not found", pair6.contains(neighbor6b.get()));
		assertFalse("First removed neighbor country is found", pair6.contains(neighbor6c.get()));
		assertFalse("Second removed neighbor country is found", pair6.contains(neighbor6d.get()));
	}
	
	/**
	 * test021_validateMap() tests if map is valid.
	 */
	@Test
	public void test021_validateMap() {
		System.out.printf("%nTesting map validation%n");
		assertTrue("This map is invalid", testMapLoader.getMapService().isMapValid());
	}
	
	/**
	 * test022_checkGraphConnection() tests if map is a connected graph.
	 */
	@Test
	public void test022_checkGraphConnection() {
		System.out.printf("%nTesting if map is a connected graph%n");
		//returns true if map is a connected graph.
		assertTrue("This map is not a connected graph", testMapLoader.getMapService().isStronglyConnected());
	}
	
	/**
	 * test4_saveMap() tests if map can be saved.
	 */
	@Ignore
	@Test
	public void test024_saveMap() {
		System.out.printf("%nTesting map saving%n");
		try {
			testMapLoader.saveMap("savemap");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}