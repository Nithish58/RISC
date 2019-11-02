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

/**
 *
 * MapEditorTest class tests cases relevant with the controller components of the map editor.
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MapEditorControllerTest {

	static private PhaseView testCmdView;
	static private MapService testMapService;
	static private MapLoaderController testMapLoader;
	static private GameController testGameController;
	String test_map;

	//define test variables
	URI uri;
	String mapname, file, savename, inputcommand, continentcommand1, continentcommand2, continentcommand3, continentcommand4,
		continentcommand5, continentcommand6, countrycommand1, countrycommand2,	countrycommand3,
		countrycommand4, countrycommand5, countrycommand6, neighborcommand1, neighborcommand2,
		neighborcommand3, neighborcommand4, neighborcommand5, neighborcommand6, newcontinentstr1, newcontinentstr2a, newcontinentstr2b,
		delcontinentstr1, delcontinentstr2a,delcontinentstr2b, newcontinentstr3, delcontinentstr3, newcontinentstr4a, newcontinentstr4b,
		delcontinentstr4a, delcontinentstr4b, newcountrystr1, countrycontinentstr1, newcountrystr2a, countrycontinentstr2a,
		newcountrystr2b, countrycontinentstr2b, delcountrystr1, delcountrystr2a, delcountrystr2b, newcountrystr3,
		countrycontinentstr3, delcountrystr3, newcountrystr4a, newcountrystr4b, countrycontinentstr4a, countrycontinentstr4b,
		delcountrystr4a, delcountrystr4b, countrystr1, neighborstr1, countrystr2a, neighborstr2a, countrystr2b, neighborstr2b,
		countrystr3, neighborstr3, countrystr4a, neighborstr4a, countrystr4b, neighborstr4b,
		countrystr5a, neighborstr5a, countrystr5b, neighborstr5b, countrystr6a, neighborstr6a, countrystr6b, neighborstr6b,
		countrystr6c, neighborstr6c, countrystr6d, neighborstr6d;
	String[] continentcommands1, continentcommands2, continentcommands3, continentcommands4, continentcommands5,
		continentcommands6, countrycommands1, countrycommands2, countrycommands3, countrycommands4,
		countrycommands5, countrycommands6, neighborcommands1, neighborcommands2, neighborcommands3,
		neighborcommands4, neighborcommands5, neighborcommands6;
	int initcontinentsize, initcountrysize, expectedcontinentsize1, expectedcontinentsize2,
		expectedcontinentsize3, expectedcontinentsize4, expectedcontinentsize5
		, expectedcontinentsize6, expectedcountrysize1, expectedcountrysize2,
		expectedcountrysize3, expectedcountrysize4, expectedcountrysize5
		, expectedcountrysize6;
	static int  testCounter;
	boolean mapIsRead;
	Optional<String> inputmap;
	Optional<Integer> country1, neighbor1, country2a, neighbor2a, country2b, neighbor2b, country3, neighbor3,
		country4a, neighbor4a, country4b, neighbor4b, country5a, neighbor5a, country5b, neighbor5b,
		country6a, country6b, country6c, country6d, neighbor6a, neighbor6b, neighbor6c, neighbor6d;
	Map<Integer, Set<Integer>> borders1, borders2, borders3, borders4, borders5, borders6;
	Set<Integer> pair1, pair2a, pair2b, pair3, pair4a, pair4b, pair5a, pair5b, pair6a, pair6b, pair6c, pair6d;

	String message;
	@BeforeClass
	public static void beginClass() {
		testCounter = 0;
		testMapService = new MapService();
		testMapLoader = new MapLoaderController(testMapService);
		testCmdView = new CommandPromptView(testMapLoader, testGameController);
		testMapLoader.setView(testCmdView);

	}

	/**
	 * <p>
	 * beginMethod() is called before every method is performed. It prints list of countinents, countries, and borders.
	 * Test variables are set here.
	 * <ul>
	 * <li><b>mapname</b> contains the map file to be loaded for testing.
	 * <li><b>URI uri</b> is set as the URI of the filename.
	 * <li><b>file</b> reads the file as string using UTF-8 character sets.
	 * </ul>
	 * </p>
	 * <p>
	 * The followings are for editmap execution test case.
	 * <ul>
	 * <li><b>inputcommand</b> defines the editmap command in form of <i>editmap filename</i>.
	 * <li><b>inputmap</b> returns the <i>editmap</i> execution to be passed on to <i>test002_editMap()</i>
	 * </ul>
	 * </p>
	 * <p>
	 * The followings are for continent editing command tests
	 * <ul>
	 * <li><b>initcontinentsize</b> retrieves the recent size of the continent list.
	 * It is used alongside with <i>expectedcontinentsize</i> to calculate the expected result for assertion.
	 * <li><b>newcontinentstr<i>n</i></b> set continent name to be added for test <i>n</i> on continent adding tests.
	 * <li><b>delcontinentstr<i>n</i></b> set continent name to be removed for test <i>n</i> on continent removal tests.
	 * </ul>
	 * </p>
	 * <p>
	 * The followings are for country editing command tests
	 * <ul>
	 * <li><b>initcountrysize</b> retrieves the recent size of the country list.
	 * It is used alongside with <i>expectedcountrysize</i> to calculate the expected result for assertion.
	 * <li><b>newcountrystr<i>n</i></b> set country name to be added for test <i>n</i> on country adding tests.
	 * <li><b>delcountrystr<i>n</i></b> set country name to be removed for test <i>n</i> on country removal tests.
	 * </ul>
	 * </p>
	 * <p>
	 * The followings are for neighbor editing command tests
	 * <ul>
	 * <li><b>countrystr<i>n</i></b> set the countries involved in neighbor edit testing.
	 * <li><b>neighborcommand<i>n</i></b> executes neighbor edit command for test <i>n</i> on neighbor edit tests.
	 * </ul>
	 * </p>
	 * @throws Exception on encountering invalid values
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
		inputcommand = "editmap "+uri;
		testMapLoader.parseFile(file);
		//if the testCounter is not less than 2, the editMap() method in the controller
		//must be skipped. This so that the subsequent tests won't be impacted by it.
		if(testCounter < 2) {
			inputmap = testMapLoader.editMap(inputcommand);
		}
		}
		//size of continent list before one continent is added
		initcontinentsize = testMapLoader.getMapService().getContinents().size();
		// The followings are for the test cases of editing continent
		//expectedcontinentsize is the expected size of continent list
		//after each edit operation
		//continentcommand contains the editcontinent commands.
		//continentcommands is an array that contains the split string for parameters
		newcontinentstr1 = "Nord_Asia"; //one continent to be added
		newcontinentstr2a = "Southeast_Asia"; newcontinentstr2b = "Northeast_Asia"; //two continents to be added

		delcontinentstr1 = "ulstrailia"; //one continent to be removed
		delcontinentstr2a = "ameroki"; delcontinentstr2b = "amerpoll"; //two continents to be deleted

		newcontinentstr3 = "NordWest_Asia"; delcontinentstr3 = "Southeast_Asia"; //continents to be added and deleted one each in a line
		newcontinentstr4a= "NordEast_Europe"; newcontinentstr4b = "SouthWest_Europe"; delcontinentstr4a="utropa"; delcontinentstr4b="afrori";
		//continentcommand1 and continentcommands1 are for adding one continent
		continentcommand1 = "editcontinent -add "+newcontinentstr1+" 1";
		continentcommand1 = StringUtils.substringAfter(continentcommand1, "-");
		continentcommands1 = StringUtils.split(continentcommand1, "-");
		//continentcommand2 and continentcommands2 are for adding two continents
		continentcommand2 = "editcontinent -add "+newcontinentstr2a+" 1 -add "+newcontinentstr2b+" 1";
		continentcommand2 = StringUtils.substringAfter(continentcommand2, "-");
		continentcommands2 = StringUtils.split(continentcommand2, "-");
		//continentcommand3 and continentcommands3 are for removing one continent
		continentcommand3 = "editcontinent -remove "+delcontinentstr1;
		continentcommand3 = StringUtils.substringAfter(continentcommand3, "-");
		continentcommands3 = StringUtils.split(continentcommand3, "-");
		//continentcommand4 and continentcommands4 are for removing two continents
		continentcommand4 = "editcontinent -remove "+delcontinentstr2a+" 1 -remove "+delcontinentstr2b+" 1";
		continentcommand4 = StringUtils.substringAfter(continentcommand4, "-");
		continentcommands4 = StringUtils.split(continentcommand4, "-");
		//continentcommand5 and continentcommands5 are for adding and removing one continent
		continentcommand5 = "editcontinent -add "+newcontinentstr3+" 9 -remove "+delcontinentstr3;
		continentcommand5 = StringUtils.substringAfter(continentcommand5, "-");
		continentcommands5 = StringUtils.split(continentcommand5, "-");
		//continentcommand6 and continentcommands6 are for adding and removing two continents
		continentcommand6 = "editcontinent -add  "+newcontinentstr4a+" 4 -add "+newcontinentstr4b+" 3 -remove "+delcontinentstr4a+" -remove "+delcontinentstr4b;
		continentcommand6 = StringUtils.substringAfter(continentcommand6, "-");
		continentcommands6 = StringUtils.split(continentcommand6, "-");

		// The followings are for the test cases of editing continent
		//expectedcountrysize is the expected size of continent list
		//after edit operation
		//countrycommand contains the editcountry commands.
		//countrycommands is an array that contains the split string for parameters
		initcountrysize = testMapLoader.getMapService().getCountries().size();

		newcountrystr1 = "Nordenstan"; countrycontinentstr1 = "Nord_Asia";//one country to be added
		newcountrystr2a = "Nordennavic"; countrycontinentstr2a = "NordEast_Europe"; newcountrystr2b = "Northeast_Asia";
		newcountrystr2b = "United_Islands"; countrycontinentstr2b = "Northeast_Asia";//two countrys to be added

		delcountrystr1 = "united_islands"; //one country to be removed
		delcountrystr2a = "yazteck"; delcountrystr2b = "kongrolo"; //two countrys to be deleted

		newcountrystr3 = "Fiji"; countrycontinentstr3="azio"; delcountrystr3 = "Nordenstan"; //countrys to be added and deleted one each in a line

		newcountrystr4a= "Sky_Republic"; countrycontinentstr4a="Nord_Asia";
		newcountrystr4b = "Ocean_Republic"; countrycontinentstr4b="Northeast_Asia"; delcountrystr4a="sluci"; delcountrystr4b="kancheria";
		//countrycommand1 and countrycommands1 are for adding one country
		countrycommand1 = "editcountry -add "+newcountrystr1+" "+countrycontinentstr1;
		countrycommand1 = StringUtils.substringAfter(countrycommand1, "-");
		countrycommands1 = StringUtils.split(countrycommand1, "-");
		//countrycommand2 and countrycommands2 are for adding two countries
		countrycommand2 = "editcountry -add "+newcountrystr2a+" "+countrycontinentstr2a+" -add  "+newcountrystr2b+" "+countrycontinentstr2b;
		countrycommand2 = StringUtils.substringAfter(countrycommand2, "-");
		countrycommands2 = StringUtils.split(countrycommand2, "-");
		//countrycommand3 and countrycommands3 are for removing one country
		countrycommand3 = "editcountry -remove "+delcountrystr1;
		countrycommand3 = StringUtils.substringAfter(countrycommand3, "-");
		countrycommands3 = StringUtils.split(countrycommand3, "-");
		//countrycommand4 and countrycommands4 are for removing two countries
		countrycommand4 = "editcountry -remove  "+delcountrystr2a+" -remove "+delcountrystr2b;
		countrycommand4 = StringUtils.substringAfter(countrycommand4, "-");
		countrycommands4 = StringUtils.split(countrycommand4, "-");
		//countrycommand5 and countrycommands5 are for adding and removing one country
		countrycommand5 = "editcountry -add "+newcountrystr3+" "+countrycontinentstr3+" -remove "+delcountrystr3;
		countrycommand5 = StringUtils.substringAfter(countrycommand5, "-");
		countrycommands5 = StringUtils.split(countrycommand5, "-");
		//countrycommand6 and countrycommands6 are for removing two countries
		countrycommand6 = "editcountry -add "+newcountrystr4a+" "+countrycontinentstr4a
					+" -add "+newcountrystr4b+" "+countrycontinentstr4b+" -remove "+delcountrystr4a+" -remove "+delcountrystr4b;
		countrycommand6 = StringUtils.substringAfter(countrycommand6, "-");
		countrycommands6 = StringUtils.split(countrycommand6, "-");
		//The followings are for neighbor adding test:
		countrystr1 = "nordennavic"; neighborstr1 = "siberia";
		countrystr2a = "fiji"; neighborstr2a = "japan"; countrystr2b = "fiji"; neighborstr2b = "india";
		countrystr3 = "siberia"; neighborstr3 = "worrick";
		countrystr4a = "worrick"; neighborstr4a = "india"; countrystr4b = "worrick"; neighborstr4b = "nordennavic";
		countrystr5a = "japan"; neighborstr5a = "afganistan"; countrystr5b = "japan"; neighborstr5b = "fiji";
		countrystr6a = "china"; neighborstr6a = "sky_republic"; countrystr6b = "china"; neighborstr6b = "ocean_republic";
		countrystr6c = "china"; neighborstr6c = "middle_east"; countrystr6d = "china"; neighborstr6d = "heal";
		//neighborcommand1,neighborcommands1, country1, and neighbor1 are for adding one neighbor
		neighborcommand1 = "editneighbor -add "+countrystr1+" "+ neighborstr1;
		neighborcommand1 = StringUtils.substringAfter(neighborcommand1, "-");
		neighborcommands1 = StringUtils.split(neighborcommand1, "-");
		country1 = testMapLoader.getMapService().findCorrespondingIdByCountryName(countrystr1);
		neighbor1 = testMapLoader.getMapService().findCorrespondingIdByCountryName(neighborstr1);
		//neighborcommand2,neighborcommands2, country2a, country2db, neighbor 2a, and neighbor2b are for adding two neighbor
		neighborcommand2 = "editneighbor -add "+countrystr2a+" "+neighborstr2a+" -add "+countrystr2b+" "+neighborstr2b;
		neighborcommand2 = StringUtils.substringAfter(neighborcommand2, "-");
		neighborcommands2 = StringUtils.split(neighborcommand2, "-");
		country2a = testMapLoader.getMapService().findCorrespondingIdByCountryName(countrystr2a);
		country2b = testMapLoader.getMapService().findCorrespondingIdByCountryName(countrystr2b);
		neighbor2a = testMapLoader.getMapService().findCorrespondingIdByCountryName(neighborstr2a);
		neighbor2b = testMapLoader.getMapService().findCorrespondingIdByCountryName(neighborstr2b);
		//neighborcommand3,neighborcommands3, country3, and neighbor3 are for removing one neighbor
		neighborcommand3 = "editneighbor -remove "+countrystr3+" "+neighborstr3;
		neighborcommand3 = StringUtils.substringAfter(neighborcommand3, "-");
		neighborcommands3 = StringUtils.split(neighborcommand3, "-");
		country3 = testMapLoader.getMapService().findCorrespondingIdByCountryName(countrystr3);
		neighbor3 = testMapLoader.getMapService().findCorrespondingIdByCountryName(neighborstr3);
		//neighborcommand4,neighborcommands4, country4a, country4b, neighbor4a, and neighbor4b are for removing two neighbors
		neighborcommand4 = "editneighbor -remove "+countrystr4a+" "+neighborstr4a+" -remove "+countrystr4b+" "+neighborstr4b;
		neighborcommand4 = StringUtils.substringAfter(neighborcommand4, "-");
		neighborcommands4 = StringUtils.split(neighborcommand4, "-");
		country4a = testMapLoader.getMapService().findCorrespondingIdByCountryName(countrystr4a);
		country4b = testMapLoader.getMapService().findCorrespondingIdByCountryName(countrystr4b);
		neighbor4a = testMapLoader.getMapService().findCorrespondingIdByCountryName(neighborstr4a);
		neighbor4b = testMapLoader.getMapService().findCorrespondingIdByCountryName(neighborstr4b);
		//neighborcommand5,neighborcommands5, country5a, country5b, neighbor5a, and neighbor5b are for adding and removing one neighbor
		neighborcommand5 = "editneighbor -add "+countrystr5a+" "+neighborstr5a+"-remove "+countrystr5b+" "+neighborstr5b;
		neighborcommand5 = StringUtils.substringAfter(neighborcommand5, "-");
		neighborcommands5 = StringUtils.split(neighborcommand5, "-");
		country5a = testMapLoader.getMapService().findCorrespondingIdByCountryName(countrystr5a);
		country5b = testMapLoader.getMapService().findCorrespondingIdByCountryName(countrystr5b);
		neighbor5a = testMapLoader.getMapService().findCorrespondingIdByCountryName(neighborstr5a);
		neighbor5b = testMapLoader.getMapService().findCorrespondingIdByCountryName(neighborstr5b);
		//neighborcommand6,neighborcommands6, country6a, country6b, country6c, country6d, neighbor6a, neighbor6b, neighbor6c, and neighbor6d are for adding and removing two neighbors
		neighborcommand6 = "editneighbor -add china sky_republic -add china ocean_republic -remove china middle_east -remove china heal";
		neighborcommand6 = StringUtils.substringAfter(neighborcommand6, "-");
		neighborcommands6 = StringUtils.split(neighborcommand6, "-");
		country6a = testMapLoader.getMapService().findCorrespondingIdByCountryName(countrystr6a);
		country6b = testMapLoader.getMapService().findCorrespondingIdByCountryName(countrystr6b);
		country6c = testMapLoader.getMapService().findCorrespondingIdByCountryName(countrystr6c);
		country6d = testMapLoader.getMapService().findCorrespondingIdByCountryName(countrystr6d);
		neighbor6a = testMapLoader.getMapService().findCorrespondingIdByCountryName(neighborstr6a);
		neighbor6b = testMapLoader.getMapService().findCorrespondingIdByCountryName(neighborstr6b);
		neighbor6c = testMapLoader.getMapService().findCorrespondingIdByCountryName(neighborstr6c);
		neighbor6d = testMapLoader.getMapService().findCorrespondingIdByCountryName(neighborstr6d);
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
	public void test003_addOneContinent() throws Exception{
		System.out.printf("Adding one continent%n------------%n");
		System.out.println(continentcommand1);
		testMapLoader.editContinents(continentcommands1);
		expectedcontinentsize1 = initcontinentsize + 1; //Continent list size is expected to increase by 1
		assertSame(expectedcontinentsize1, testMapLoader.getMapService().getContinents().size());
	}

	/**
	 * test004_addTwoContinents() tests adding two continents to the continent list in one command.
	 * The method uses continentcommand2 as the command to be checked.
	 * The test passes if the number of continent increases by 2 after the test.
	 * @throws Exception upon invalid values
	 */
	@Test
	public void test004_addTwoContinents() throws Exception{
		System.out.printf("Adding two continents%n------------%n");
		System.out.println(continentcommand2);
		testMapLoader.editContinents(continentcommands2);
		expectedcontinentsize2 = initcontinentsize + 2; //Continent list size is expected to increase by 2
		assertSame(expectedcontinentsize2, testMapLoader.getMapService().getContinents().size());
	}

	/**
	 * test005_removeOneContinent() tests deleting one continent.
	 * The method uses continentcommand3 as the command to be checked.
	 * The test passes if the number of continents decreases by 1 after the test.
	 * @throws Exception upon invalid values
	 */
	@Test
	public void test005_removeOneContinent() throws Exception{
		System.out.printf("Removing one continent%n------------%n");
		System.out.println(continentcommand3);
		testMapLoader.editContinents(continentcommands3);
		expectedcontinentsize3 = initcontinentsize - 1; //Continent list size is expected to decrease by 1
		assertSame(expectedcontinentsize3, testMapLoader.getMapService().getContinents().size());
	}

	/**
	 * test006_removeTwoContinents() tests removing continents from the continent list in one command.
	 * The method uses continentcommand4 as the command to be checked.
	 * The test passes if the number of continents decreases by 2 after the test.
	 * @throws Exception upon invalid values
	 */
	@Test
	public void test006_removeTwoContinents() throws Exception{
		System.out.printf("Removing two continents%n------------%n");
		System.out.println(continentcommand4);
		testMapLoader.editContinents(continentcommands4);
		expectedcontinentsize4 = initcontinentsize - 2; //Continent list size is expected to decrease by 2
		assertSame(expectedcontinentsize4, testMapLoader.getMapService().getContinents().size());
	}

	/**
	 * test007_addOneContinentRemoveOneContinent() tests adding and removing one continent from the continent list in one command.
	 * The method uses continentcommand5.
	 * The test passes if the number of continents stays the same after the test.
	 * @throws Exception upon invalid values
	 */
	@Test
	public void test007_addOneContinentRemoveOneContinent() throws Exception{
		System.out.printf("Adding and removing one continent%n------------%n");
		System.out.println(continentcommand5);
		testMapLoader.editContinents(continentcommands5);
		expectedcontinentsize5 = initcontinentsize; //Continent size should remain the same
		assertSame(expectedcontinentsize5, testMapLoader.getMapService().getContinents().size());
	}

	/**
	 * test008_addTwoContinentsRemoveTwoContinents() tests adding and removing two continents from the continent list in one command.
	 * The method uses continentcommand6.
	 * The test passes if the number of continents stays the same after the test.
	 * @throws Exception upon invalid values
	 */
	@Test
	public void test008_addTwoContinentsRemoveTwosContinents() throws Exception{
		System.out.printf("Adding and removing two continents%n------------%n");
		System.out.println(continentcommand6);
		testMapLoader.editContinents(continentcommands6);
		expectedcontinentsize6 = initcontinentsize; //Continent size should remain the same
		assertSame(expectedcontinentsize6, testMapLoader.getMapService().getContinents().size());
	}

	/**
	 * test009_addOneCountry() tests adding one country to the country list.
	 * The method uses countrycommand1 as the command to be checked.
	 * The test passes if the number of countries increases by 1 after the test.
	 * @throws Exception upon invalid values
	 */
	@Test
	public void test009_addOneCountry() throws Exception{
		System.out.printf("Adding one country%n------------%n");
		System.out.println(countrycommand1);
		testMapLoader.editCountries(countrycommands1);
		expectedcountrysize1 = initcountrysize+1; //Country list size is expected to increase by 1
		assertSame(expectedcountrysize1, testMapLoader.getMapService().getCountries().size());
	}

	/**
	 * test010_addTwoCountries() tests adding two country to the country list.
	 * The method uses countrycommand2 as the command to be checked.
	 * The test passes if the number of countries increases by 2 after the test.
	 * @throws Exception upon invalid values
	 */
	@Test
	public void test010_addTwoCountries() throws Exception{
		System.out.printf("Adding two countries%n------------%n");
		System.out.println(countrycommand2);
		testMapLoader.editCountries(countrycommands2);
		expectedcountrysize2 = initcountrysize+2; //Country list size is expected to increase by 2
		assertSame(expectedcountrysize2, testMapLoader.getMapService().getCountries().size());
	}

	/**
	 * test011_removeOneCountry() tests removing one country from the country list.
	 * The method uses countrycommand3 as the command to be checked.
	 * The test passes if the number of countries decreases by 1 after the test.
	 * @throws Exception upon invalid values
	 */
	@Test
	public void test011_removeOneCountry() throws Exception{
		System.out.printf("Removing one country%n------------%n");
		//size of country list before one country is removed
		System.out.println(countrycommand3);
		expectedcountrysize3 = initcountrysize-1; //Country list size is expected to decrease by 1
		testMapLoader.editCountries(countrycommands3);
		assertSame(expectedcountrysize3, testMapLoader.getMapService().getCountries().size());
	}

	/**
	 * test012_removeTwoCountries() tests removing two countries from the country list.
	 * The method uses countrycommand4 as the command to be checked.
	 * The test passes if the number of countries decreases by 2 after the test.
	 * @throws Exception upon invalid values
	 */
	@Test
	public void test012_removeTwoCountries() throws Exception{
		System.out.printf("Removing two countries%n------------%n");
		System.out.println(countrycommand4);
		testMapLoader.editCountries(countrycommands4);
		expectedcountrysize4 = initcountrysize-2; //Country list size is expected to decrease by 2
		assertSame(expectedcountrysize4, testMapLoader.getMapService().getCountries().size());
	}

	/**
	 * test013_addOneCountryRemoveOneCountry() tests adding and removing one country from the country list in one command.
	 * The method uses countrycommand5 as the command to be checked.
	 * The test passes if the number of countries stays the same after the test.
	 * @throws Exception upon invalid values
	 */
	@Test
	public void test013_addOneCountryRemoveOneCountry() throws Exception{
		System.out.printf("Adding and removing one country%n------------%n");
		System.out.println(countrycommand5);
		testMapLoader.editCountries(countrycommands5);
		expectedcountrysize5 = initcountrysize; //Country list size should remian the same
		assertSame(expectedcountrysize5, testMapLoader.getMapService().getCountries().size());
	}

	/**
	 * test014_addTwoCountriesRemoveTwoCountries() tests adding and removing two countries from the country list in one command.
	 * The method uses countrycommand6 as the command to be checked.
	 * The test passes if the number of countries stays the same after the test.
	 * @throws Exception upon invalid values
	 */
	@Test
	public void test014_addTwoCountriesRemoveTwoCountries() throws Exception{
		System.out.printf("Adding and removing two countries%n------------%n");
		System.out.println(countrycommand6);
		testMapLoader.editCountries(countrycommands6);
		expectedcountrysize6 = initcountrysize; //Country list size should remain the same
		assertSame(expectedcountrysize6, testMapLoader.getMapService().getCountries().size());
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
	public void test015_addOneNeighbor() throws Exception{
		System.out.printf("Adding one neighbor to a country%n------------%n");
		System.out.println(neighborcommand1);
		borders1 = testMapLoader.getMapService().getAdjacencyCountriesMap();
		testMapLoader.editNeighbor(neighborcommands1);
		pair1 = borders1.get(country1.get());
		//Check if map object contains both country ID and neighbor ID
		assertTrue("Country is not found",borders1.containsKey(country1.get()));
		assertTrue("Neighbor country is not found", pair1.contains(neighbor1.get()));
	}

	/**
	 * test016_addTwoNeighbors() tests adding two neighbors for one or two countries in one command.
	 * The method uses neighborcommand2 as the command to be checked.
	 * The test passes if the origin country exists in the adjacency list of
	 * the entire map and the neighboring countries are among the
	 * origin country's adjacency's list after the test.
	 * @throws Exception upon invalid values
	 */
	@Test
	public void test016_addTwoNeighbors() throws Exception{
		System.out.printf("Adding two neighbors to one country%n------------%n");
		//Set the command string to add two neighbors
		System.out.println(neighborcommand2);
		borders2 = testMapLoader.getMapService().getAdjacencyCountriesMap();
		testMapLoader.editNeighbor(neighborcommands2);
		//Get pair of country and neighbor
		pair2a = borders2.get(country2a.get());
		pair2b = borders2.get(country2b.get());
		//Check if map object contains both country ID and neighbor ID
		assertTrue("Country is not found",borders2.containsKey(country2a.get()));
		assertTrue("First neighbor country is not found", pair2a.contains(neighbor2a.get()));
		assertTrue("Second neighbor country is not found", pair2b.contains(neighbor2b.get()));
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
	public void test017_removeOneNeighbor() throws Exception{
		System.out.printf("Removing one neighbor from a country%n------------%n");
		System.out.println(neighborcommand3);
		borders3 = testMapLoader.getMapService().getAdjacencyCountriesMap();
		testMapLoader.editNeighbor(neighborcommands3);
		//get pair of country and neighbor
		pair3 = borders3.get(country3.get());
		//Check if map object contains both country ID and neighbor ID
		assertTrue("Country is not found",borders3.containsKey(country3.get()));
		assertFalse("Neighbor country is found", pair3.contains(neighbor3.get()));
	}

	/**
	 * test018_removeTwoNeighbors() tests removing two neighbors from one or two countries in one command.
	 * The method uses neighborcommand4 as the command to be checked.
	 * The test passes if the origin country exists in the adjacency list of
	 * the entire map and the neighboring countries are not among the
	 * origin country's adjacency's list after the test.
	 * @throws Exception upon invalid values
	 */
	@Test
	public void test018_removeTwoNeighbors() throws Exception{
		System.out.printf("Removing two neighbors from one country%n------------%n");
		//Set the command string to remove two neighbors
		System.out.println(neighborcommand4);
		testMapLoader.editNeighbor(neighborcommands4);
		borders4 = testMapLoader.getMapService().getAdjacencyCountriesMap();
		//get pair of country and neighbor
		pair4a = borders4.get(country4a.get());
		pair4b = borders4.get(country4b.get());
		//Check if map object contains both country ID and neighbor ID
		assertTrue("Country is not found",borders4.containsKey(country4a.get()));
		assertTrue("Country is not found",borders4.containsKey(country4b.get()));
		assertFalse("First neighbor country is found", pair4a.contains(neighbor4a.get()));
		assertFalse("Second neighbor country is found", pair4b.contains(neighbor4b.get()));
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
	public void test019_addOneNeighborRemoveOneNeighbor() throws Exception{
		System.out.printf("Adding and removing one neighbor from one country%n------------%n");
		//Set the command string to remove two neighbors
		System.out.println(neighborcommand5);
		testMapLoader.editNeighbor(neighborcommands5);
		//create map object from adjacency list
		borders5 = testMapLoader.getMapService().getAdjacencyCountriesMap();
		//get pair of country and neighbor
		pair5a = borders5.get(country5a.get());
		pair5b = borders5.get(country5b.get());
		//Check if map object contains both country ID and neighbor ID
		assertTrue("Country is not found",borders5.containsKey(country5a.get()));
		assertTrue("Country is not found",borders5.containsKey(country5b.get()));
		assertTrue("First neighbor country is not found", pair5a.contains(neighbor5a.get()));
		assertFalse("Second neighbor country is found", pair5b.contains(neighbor5b.get()));
	}

	/**
	 * test020_addTwoNeighborsRemoveTwoNeighbors() tests adding and removing two neighbors in one command.
	 * The method uses neighborcommand6 as the command to be checked.
	 * The test passes if the origin country exists in the adjacency list of
	 * the entire map, the added neighboring countries are among the
	 * origin country's adjacency's list, and the removed neighboring countries
	 * are not among the origin country's adjacency's list after the test.
	 * @throws Exception upon invalid values
	 */
	@Test
	public void test020_addTwoNeighborsRemoveTwoNeighbors() throws Exception{
		System.out.printf("Adding and removing two neighbors from one country%n------------%n");
		//Set the command string to remove two neighbors
		System.out.println(neighborcommand6);
		testMapLoader.editNeighbor(neighborcommands6);
		//create map object from adjacency list
		borders6 = testMapLoader.getMapService().getAdjacencyCountriesMap();
		//get pair of country and neighbor
		pair6a = borders6.get(country6a.get());
		pair6b = borders6.get(country6b.get());
		pair6c = borders6.get(country6c.get());
		pair6d = borders6.get(country6d.get());
		//Check if map object contains both country ID and neighbor ID
		assertTrue("Country is not found",borders6.containsKey(country6a.get()));
		assertTrue("Country is not found",borders6.containsKey(country6b.get()));
		assertTrue("Country is not found",borders6.containsKey(country6c.get()));
		assertTrue("Country is not found",borders6.containsKey(country6d.get()));
		assertTrue("First added neighbor country is not found", pair6a.contains(neighbor6a.get()));
		assertTrue("Second added neighbor country is not found", pair6b.contains(neighbor6b.get()));
		assertFalse("First removed neighbor country is found", pair6c.contains(neighbor6c.get()));
		assertFalse("Second removed neighbor country is found", pair6d.contains(neighbor6d.get()));
	}

	/**
	 * test021_invalidateMap() tests if map is invalid.
	 * The test passes if the getMapService().isMapNotValid() returns false,
	 * which it should if the preceding tests on managing continents, countries,
	 * and neighbors invalidate the map file.
	 */
	@Test
	public void test021_invalidateMap() {
		System.out.printf("%nInvalidating map%n");
		assertTrue("This map is valid", testMapLoader.getMapService().isMapNotValid());
	}

	/**
	 * test022_validateMap() tests if map is valid.
	 * The test passes if the getMapService().isMapNotValid() returns true,
	 * which it should if the preceding tests on managing continents, countries,
	 * and neighbors maintain the validity of the map file.
	 */
	@Ignore
	@Test
	public void test022_validateMap() {
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
	public void test023_saveMap() {
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

}