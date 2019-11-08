package com6441.team7.risc.controller;

import com6441.team7.risc.api.exception.ContinentParsingException;
import com6441.team7.risc.api.exception.CountryParsingException;
import com6441.team7.risc.api.exception.NeighborParsingException;
import com6441.team7.risc.api.model.Continent;
import com6441.team7.risc.api.model.Country;
import com6441.team7.risc.api.model.MapService;
import com6441.team7.risc.view.GameView;
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

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
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
public class MapLoaderControllerTest {

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
        
        //Binsar variables
        
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
		if (testCounter < 2) {
		inputcommand = "editmap "+mapname;
		testMapLoader.parseFile(file);
		}
		//size of continent list before one continent is added
		initcontinentsize = testMapLoader.getMapService().getContinents().size();

		initcountrysize = testMapLoader.getMapService().getCountries().size();


		//This sets the variable for map saving command.
		savename = "edittedmap.map";
       
        
    }


    /**
     * read existing map from the directory given by its map name
     * The test will pass if it ables to read and parses the map file and returns true
     * @throws Exception exception on invalid
     */
    @Test
    public void readExistingFile() throws Exception{
        URI uri = getClass().getClassLoader().getResource("ameroki.map").toURI();
        String file = FileUtils.readFileToString(new File(uri), StandardCharsets.UTF_8);

        boolean result = mapLoaderController.parseFile(file);
        assertTrue(result);
    }

    /**
     * read a map does not exist given by its map name
     * The test should be able to create a new map file and return true
     * @throws Exception exception on error
     */
    @Test
    public void readNewCreatedFile() throws Exception{
        URI uri = getClass().getClassLoader().getResource("test.map").toURI();
        String file = FileUtils.readFileToString(new File(uri), StandardCharsets.UTF_8);

        boolean result = mapLoaderController.parseFile(file);

        assertTrue(result);
    }


    /**
     * create continent objects by valid continent strings
     * The test should be able to read and parse the strings and creates continents
     * the test will pass if the number of newly created continents is 6
     * @throws Exception exception on error
     */
    @Test
    public void createContinentFromValidContinentInfo() throws Exception{
        String continentsInfo = validContinentString();
        Set<Continent> continents =  mapLoaderController.parseRawContinents(continentsInfo);
        assertEquals(continents.size(), 6);
    }


    /**
     * create continent objects if missing continent power
     * The test should throw an exception when continent power is missing
     * @throws Exception exception on invalid
     */
    @Test(expected=ContinentParsingException.class)
    public void createContinentMissingContinentPower() throws Exception{
        String continentsInfo = "parts2: continents]\r\n" +
                "azio 5 #9aff80\r\n" +
                "ameroki yellow\r\n" +
                "utropa 10 #a980ff\r\n" +
                "amerpoll 5 red\r\n" +
                "afrori 5 #ffd780\r\n" +
                "ulstrailia 5 magenta";

      mapLoaderController.parseRawContinents(continentsInfo);

    }


    /**
     * create continents if the continent power is not an integer
     * The test should throw an exception when continent power is not an integer
     * @throws Exception exception on invalid
     */
    @Test(expected = ContinentParsingException.class)
    public void createContinentWithContinentPowerNotInteger() throws Exception{
        String continentsInfo = "parts2: continents]\r\n" +
                "azio 5 #9aff80\r\n" +
                "ameroki abc yellow\r\n" +
                "utropa 10 #a980ff\r\n" +
                "amerpoll 5 red\r\n" +
                "afrori 5 #ffd780\r\n" +
                "ulstrailia 5 magenta";

        mapLoaderController.parseRawContinents(continentsInfo);
    }

    /**
     * create countries with valid country information
     * pass the tests if the number of newly created countries is 5.
     * @throws Exception exception on invalid
     */
    @Test
    public void createCountriesFromValidCountryInfo() throws Exception{

        String continentsInfo = validContinentString();
        mapLoaderController.parseRawContinents(continentsInfo);

        String countriesInfo = validCountryString();

        Set<Country> countries =  mapLoaderController.parseRawCountries(countriesInfo);
        assertEquals(countries.size(), 5);

    }

    /**
     * create countries with continent id missing
     * the test should throw an countryParsingException
     * @throws Exception exception on invalid
     */
    @Test(expected = CountryParsingException.class)
    public void createCountriesMissingContinentInfo() throws Exception{

        String continentsInfo = validContinentString();
        mapLoaderController.parseRawContinents(continentsInfo);

        String countriesInfo = "[countries]\r\n" +
                "1 siberia 329 152\r\n" +
                "2 worrick 1 308 199\r\n" +
                "3 yazteck 1 284 260\r\n" +
                "4 kongrolo 1 278 295\r\n" +
                "5 china 1 311 350\r\n";

        mapLoaderController.parseRawCountries(countriesInfo);

    }

    /**
     * create countries with continent id not exist
     * the test should throw CountryParsingException
     * @throws Exception exception on invalid
     */
    @Test(expected = CountryParsingException.class)
    public void createCountriesWithInvalidContinentInfo() throws Exception{

        String continentsInfo = validContinentString();
        mapLoaderController.parseRawContinents(continentsInfo);

        String countriesInfo = "[countries]\r\n" +
                "1 siberia 10 329 152\r\n" +
                "2 worrick 1 308 199\r\n" +
                "3 yazteck 1 284 260\r\n" +
                "4 kongrolo 1 278 295\r\n" +
                "5 china 1 311 350\r\n";

       mapLoaderController.parseRawCountries(countriesInfo);
    }


    /**
     * create countries with country id missing when reading existing map file
     * the tests should throw CountryParsingException
     * @throws Exception exception on invalid
     */
    @Test(expected = CountryParsingException.class)
    public void createCountriesMissingUniqueIdentifier() throws Exception{

        String continentsInfo = validContinentString();
        mapLoaderController.parseRawContinents(continentsInfo);

        String countriesInfo = "[countries]\r\n" +
                "siberia 1 329 152\r\n" +
                "2 worrick 1 308 199\r\n" +
                "3 yazteck 1 284 260\r\n" +
                "4 kongrolo 1 278 295\r\n" +
                "5 china 1 311 350\r\n";

        mapLoaderController.parseRawCountries(countriesInfo);

    }


    /**
     * create countries with continent id not an integer when reading map file
     * the test should throw a CountryParsingException
     * @throws Exception exception on invalid
     */
    @Test(expected = CountryParsingException.class)
    public void createCountriesWithContinentIdNotInteger() throws Exception{
        String continentsInfo = validContinentString();
        mapLoaderController.parseRawContinents(continentsInfo);

        String countriesInfo = "[countries]\r\n" +
                "1 siberia one 329 152\r\n" +
                "2 worrick 1 308 199\r\n" +
                "3 yazteck 1 284 260\r\n" +
                "4 kongrolo 1 278 295\r\n" +
                "5 china 1 311 350\r\n";

        mapLoaderController.parseRawCountries(countriesInfo);


    }


    /**
     * create adjacency countries with valid information
     * expect the number of newly created adjacency countries info is 5
     * @throws Exception exception on invalid
     */
    @Test
    public void createAdjascencyCountriesWithValidInfo() throws Exception{

        String continentsInfo = validContinentString();
        mapLoaderController.parseRawContinents(continentsInfo);

        String countriesInfo = validCountryString();
        mapLoaderController.parseRawCountries(countriesInfo);

        String adjacencyInfo = "[borders]\r\n" +
                "1 2 3 4\r\n" +
                "2 1 4 5 \r\n" +
                "3 1 5\r\n" +
                "4 2 1\r\n" +
                "5 2 3 4\r\n";
        Map<Integer, Set<Integer>> adjacencyMap = mapLoaderController.parseRawNeighboringCountries(adjacencyInfo);
        assertEquals(adjacencyMap.size(), 5);

    }

    /**
     * create a neighboring info with no adjacency countries id
     * the test should throw a neighboringParsingException
     * @throws Exception exception on invalid
     */
    @Test(expected = NeighborParsingException.class)
    public void createAdjascencyCountriesWithNoAdjacency() throws Exception{
        String continentsInfo = validContinentString();
        mapLoaderController.parseRawContinents(continentsInfo);

        String countriesInfo = validCountryString();
        mapLoaderController.parseRawCountries(countriesInfo);

        String adjacencyInfo = "[borders]\r\n" +
                "1\r\n" +
                "2 1 4 5\r\n" +
                "3 1 5\r\n" +
                "4 2 1\r\n" +
                "5 2 3 4\r\n";
        mapLoaderController.parseRawNeighboringCountries(adjacencyInfo);
    }

    /**
     * create adjacency Countries Information with Countries ID not exist
     * the test should throw a neighboringParsingException
     * @throws Exception exception on invalid
     */
    @Test(expected = NeighborParsingException.class)
    public void createAdjascencyCountriesWithInvalidCountryIdAdjacency() throws Exception{
        String continentsInfo = validContinentString();
        mapLoaderController.parseRawContinents(continentsInfo);

        String countriesInfo = validCountryString();
        mapLoaderController.parseRawCountries(countriesInfo);

        String adjacencyInfo = "[borders]\r\n" +
                "1 100\r\n" +
                "2 1 4 5\r\n" +
                "3 1 5\r\n" +
                "4 2 1\r\n" +
                "5 2 3 4\r\n";
        mapLoaderController.parseRawNeighboringCountries(adjacencyInfo);

    }

    /**
     * create adjacency countries with countries id not an integer
     * expect the test to throw a NeighboringParsingException
     * @throws Exception exception exception
     */
    @Test(expected = NeighborParsingException.class)
    public void createAdjacencyCountriesWithValueNotInteger() throws Exception{

        String continentsInfo = validContinentString();
        mapLoaderController.parseRawContinents(continentsInfo);

        String countriesInfo = validCountryString();
        mapLoaderController.parseRawCountries(countriesInfo);

        String adjacencyInfo = "[borders]\r\n" +
                "1 two\r\n" +
                "2 1 4 5\r\n" +
                "3 1 5\r\n" +
                "4 2 1\r\n" +
                "5 2 3 4\r\n";
        mapLoaderController.parseRawNeighboringCountries(adjacencyInfo);

    }

    /**
     * parse a valid editcontinent command to add three continents and remove an existing continent
     * the test will pass if the number of continents in the mapService is 2
     * @throws Exception exception
     */
    @Test
    public void testValidEditContinentCommand() throws Exception{
        String command = "editcontinent -add Asia 6 -add America 5 -add Africa 4 -remove Africa";

        command = StringUtils.substringAfter(command, "-");
        String[] commands = StringUtils.split(command, "-");

        mapLoaderController.editContinents(commands);
        assertEquals(mapLoaderController.getMapService().getContinents().size(),2);
    }

    /**
     * parse an editcontinent command to add three continents while one continent missing continent power
     * the test will pass if the number of continents in the mapService is 2
     * @throws Exception exception
     */
    @Test
    public void testInvalidAddContinentCommand() throws Exception{
        String command = "editcontinent -add Asia -add America 5 -add Africa 4";
        command = StringUtils.substringAfter(command, "-");
        String[] commands = StringUtils.split(command, "-");

        mapLoaderController.editContinents(commands);
        assertEquals(mapLoaderController.getMapService().getContinents().size(), 2);

    }

    /**
     * parse an editcontinent command to add three continents and to remove a continent
     * add Asia is not valid, add America and add Africa is valid, remove is not valid
     * pass the test if the number of continents in the mapService is 2
     * @throws Exception exception
     */
    @Test
    public void testInvalidRemoveContinentCommand() throws Exception{
        String command = "editcontinent -add Asia -add America 5 -add Africa 4 -remove";
        command = StringUtils.substringAfter(command, "-");
        String[] commands = StringUtils.split(command, "-");

        mapLoaderController.editContinents(commands);
        assertEquals(mapLoaderController.getMapService().getContinents().size(), 2);

    }

    /**
     * test the editcountry command with three addition of countries
     * the test will pass if the number of newly added country is 3
     * @throws Exception exception
     */
    @Test
    public void testValidAddCountryCommand() throws Exception{
        MapService mapService = addValidContinentInfo();
        String command = "editcountry -add China asia -add India asia -add egypt africa";
        command = StringUtils.substringAfter(command, "-");
        String[] commands = StringUtils.split(command, "-");

        mapLoaderController.editCountries(commands);
        assertEquals(3, mapService.getCountries().size());
    }

    /**
     * test the addCountry command with one invalid addition and 2 valid addition
     * will pass the test if the number of newly added country is 2
     * @throws Exception exception
     */
    @Test
    public void testInValidAddCountryCommand() throws Exception{
        MapService mapService = addValidContinentInfo();

        String command = "editcountry -add China sia -add India asia -add egypt africa";
        command = StringUtils.substringAfter(command, "-");
        String[] commands = StringUtils.split(command, "-");

        mapLoaderController.editCountries(commands);
        assertEquals(mapService.getCountries().size(), 2);
    }


    /**
     * create valid continents info and add to the mapService
     * @return mapService
     */
    private MapService addValidContinentInfo(){
        MapService mapService = mapLoaderController.getMapService();
        Continent asia = new Continent(1, "asia", 5);
        Continent america = new Continent(2, "america", 6);
        Continent africa = new Continent(3, "africa", 4);

        mapService.addContinent(africa);
        mapService.addContinent(asia);
        mapService.addContinent(america);

        return mapService;
    }

    /**
     * the string to parse valid continents
     * @return String
     */
    private String validContinentString(){
        return "parts2: continents]\r\n" +
                "azio 5 #9aff80\r\n" +
                "ameroki 10 yellow\r\n" +
                "utropa 10 #a980ff\r\n" +
                "amerpoll 5 red\r\n" +
                "afrori 5 #ffd780\r\n" +
                "ulstrailia 5 magenta";
    }

    /**
     * the string to parse for valid countries
     * @return String
     */
    private String validCountryString(){
        return "[countries]\r\n" +
                "1 siberia 1 329 152\r\n" +
                "2 worrick 1 308 199\r\n" +
                "3 yazteck 1 284 260\r\n" +
                "4 kongrolo 1 278 295\r\n" +
                "5 china 1 311 350\r\n";
    }

    
    //BINSAR'S TESTS

	/**
	 * This is for viewing output strings
	 */
    static private PhaseView testPhaseView;
    /**
     * testMapService is a MapService object that stores the map information 
     */
	static private MapService testMapService;
	/**
	 * testMapLoader controller is a MapLoaderController object to read map commands
	 */
	static private MapLoaderController testMapLoader;

	/**
	 * uri is a URI variable that handles reading from a file
	 */
	URI uri;
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
	 * country1, neighbor1, country2, neighbor2, borders1, borders2, pair1, and pair2 are used for testing adding and deleting neighboring countries
	 */
	Optional<Integer> country1, neighbor1, country2, neighbor2;
	Map<Integer, Set<Integer>> borders1, borders2;
	Set<Integer> pair1, pair2;

	/**
	 * message is for setting the message upon test failure
	 */
	String message;
	
	/**
	 * This method is called at the very beginning of the test.
	 * testCounter value is initialized as 0.
	 * testMapService, testMapLoader, and testPhaseView are instantiated from MapService, MapLoaderController, and PhaseView.
	 * testMapLoader sets testPhaseView as the view.
	 */
	@BeforeClass
	public static void beginClass() {
		testCounter = 0;
		testMapService = new MapService();
		testMapLoader = new MapLoaderController(testMapService);
		testPhaseView = new PhaseView();
		testMapLoader.setView(testPhaseView);

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
	 * @throws Exception exception upon invalid values
	 */
	@Test
	public void test001_readFile() throws Exception{
		message = "The map is not valid";
		assertTrue(message, testMapLoader.parseFile(file));
	}


	/**
	 * test002_addOneContinent() tests adding one continent to the continent list.
	 * The method uses continentcommand1 as the command to be checked.
	 * The test passes if the number of continent increases by 1 after the test.
	 * @throws Exception exception upon invalid values
	 */
	@Test
	public void test002_addContinent() throws Exception{
		System.out.printf("Adding one continent%n------------%n");
		addContinent("Nord_Asia","1");
		expectedcontinentsize = initcontinentsize + 1; //Continent list size is expected to increase by 1
		assertSame(expectedcontinentsize, testMapLoader.getMapService().getContinents().size());
	}

	/**
	 * test004_removeOneContinent() tests deleting one continent.
	 * The method uses continentcommand3 as the command to be checked.
	 * The test passes if the number of continents decreases by 1 after the test.
	 * @throws Exception exception upon invalid values
	 */
	@Test
	public void test004_removeContinent() throws Exception{
		
		System.out.printf("Removing one continent%n------------%n");
		removeContinent("ulstrailia");
		expectedcontinentsize = initcontinentsize - 1; //Continent list size is expected to decrease by 1
		assertSame(expectedcontinentsize, testMapLoader.getMapService().getContinents().size());
	}

	/**
	 * test005_addAndRemoveContinent() tests adding and removing one continent from the continent list in one command.
	 * The method uses continentcommand5.
	 * The test passes if the number of continents stays the same after the test.
	 * @throws Exception exception upon invalid values
	 */
	@Test
	public void test005_addAndRemoveContinent() throws Exception{
		System.out.printf("Adding and removing one continent%n------------%n");
		System.out.println("East_Asia, 1 , Nord_Asia");
		addAndRemoveContinent("East_Asia","1", "Nord_Asia");
		expectedcontinentsize = initcontinentsize; //Continent size should remain the same
		assertSame(expectedcontinentsize, testMapLoader.getMapService().getContinents().size());
	}

	/**
	 * test006_addCountry() tests adding one country to the country list.
	 * The method uses countrycommand1 as the command to be checked.
	 * The test passes if the number of countries increases by 1 after the test.
	 * @throws Exception exception upon invalid values
	 */
	@Test
	public void test006_addCountry() throws Exception{
		System.out.printf("Adding one country%n------------%n");
		System.out.println("Sky_Republic, East_Asia");
		addCountry("Ocean_Republic", "East_Asia");
		expectedcountrysize = initcountrysize+1; //Country list size is expected to increase by 1
		assertSame(expectedcountrysize, testMapLoader.getMapService().getCountries().size());
	}

	/**
	 * test007_removeCountry() tests removing one country from the country list.
	 * The method uses countrycommand3 as the command to be checked.
	 * The test passes if the number of countries decreases by 1 after the test.
	 * @throws Exception exception upon invalid values
	 */
	@Test
	public void test007_removeCountry() throws Exception{
		System.out.printf("Removing one country%n------------%n");
		//size of country list before one country is removed
		System.out.println("heal");
		expectedcountrysize = initcountrysize-1; //Country list size is expected to decrease by 1
		removeCountry("heal");
		assertSame(expectedcountrysize, testMapLoader.getMapService().getCountries().size());
	}

	/**
	 * test008_addAndRemoveCountry() tests adding and removing one country from the country list in one command.
	 * The method uses countrycommand5 as the command to be checked.
	 * The test passes if the number of countries stays the same after the test.
	 * @throws Exception exception upon invalid values
	 */
	@Test
	public void test008_addAndRemoveCountry() throws Exception{
		System.out.printf("Adding and removing one country%n------------%n");
		addAndRemoveCountry("Sky_Republic", "East_Asia", "Ocean_Republic");
		expectedcountrysize = initcountrysize; //Country list size should remian the same
		assertSame(expectedcountrysize, testMapLoader.getMapService().getCountries().size());
	}

	/**
	 * test009_addNeighbor() tests adding one neighbor of a country.
	 * The method uses neighborcommand1 as the command to be checked.
	 * The test passes if the origin country exists in the adjacency list of
	 * the entire map and the neighboring country is among the
	 * origin country's adjacency's list after the test.
	 * @throws Exception exception upon invalid values
	 */
	@Test
	public void test009_addNeighbor() throws Exception{
		System.out.printf("Adding one neighbor to a country%n------------%n");
		System.out.println("south afrori, india");
		addNeighbor("Sky_Republic", "india");
		//Check if map object contains both country ID and neighbor ID
		assertTrue("Country is not found", borders1.containsKey(country1.get()));
		assertTrue("Neighboring is not found", pair1.contains(neighbor1.get()));
	}

	/**
	 * test010_removeNeighbor() tests removing one neighbor from a country.
	 * The method uses neighborcommand3 as the command to be checked.
	 * The test passes if the origin country exists in the adjacency list of
	 * the entire map and the neighboring country is not among the
	 * origin country's adjacency's list after the test.
	 * @throws Exception exception upon invalid values
	 */
	@Test
	public void test010_removeNeighbor() throws Exception{
		System.out.printf("Removing one neighbor from a country%n------------%n");
		//get pair of country and neighbor
		removeNeighbor("siberia", "worrick");
		//Check if map object contains both country ID and neighbor ID
		assertTrue("Country is not found",borders1.containsKey(country1.get()));
		assertFalse("Neighboring country is found", pair1.contains(neighbor1.get()));
	}


	/**
	 * test011_addAndRemoveNeighbor() tests adding and removing one neighbor in one command.
	 * The method uses neighborcommand5 as the command to be checked.
	 * The test passes if the origin country exists in the adjacency list of
	 * the entire map, the added neighboring country is among the
	 * origin country's adjacency's list, and the removed neighboring country
	 * is not among the origin country's adjacency's list after the test.
	 * @throws Exception exception upon invalid values
	 */
	@Test
	public void test011_addAndRemoveNeighbor() throws Exception{
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
	 * test012_invalidateMap() tests if map is invalid.
	 * The test passes if the getMapService().isMapNotValid() returns false,
	 * which it should if the preceding tests on managing continents, countries,
	 * and neighbors invalidate the map file.
	 */
	@Test
	public void test012_invalidateMap() {
		System.out.printf("%nInvalidating map%n");
		assertFalse(testMapLoader.getMapService().isMapNotValid());
	}

	/**
	 * test013_validateMap() tests if map is valid.
	 * The test passes if the getMapService().isMapNotValid() returns true,
	 * which it should if the preceding tests on managing continents, countries,
	 * and neighbors maintain the validity of the map file.
	 */
	@Test
	public void test013_validateMap() {
		System.out.printf("%nValidating map%n");
		assertTrue("This map is invalid", testMapLoader.getMapService().isMapValid());
	}

	/**
	 * test014_saveMap() tests if map can be saved.
	 * The test passes if the saved map is found using the parseFile() method of
	 * the map controller.
	 */
	@Test
	public void test014_saveMap() {
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
	 * test015_validateEmptyMap() tests if map if empty
	 * @throws IOException  on invalid IO
	 */
	@Test
	public void test015_validateEmptyMap() throws IOException {
		message = "Map is empty";
		editMap("newmap.map");
		assertTrue(message, testMapLoader.getMapService().isMapNotValid());
	}
	
	/**
	 * test016_validateDuplicateCountry() tests if duplicate countries exist
	 * @throws IOException on invalid IO
	 */
	@Ignore
	@Test
	public void test016_validateDuplicateCountry() throws IOException {
		message = "Duplicate contries exist";
		addCountry("nippon", "south_afrori");
		addCountry("nippon", "south_afrori");
		assertTrue(message, testMapLoader.getMapService().isMapNotValid());
	}
	
	/**
	 * Test for unconnected map
	 * Add country Mauritius to continent afrori. Do not add any neighbours
	 * Expected: Map must be invalid
	 * @throws IOException on invalid IO
	 */
	@Test public void test017_invalidUnconnectedCountriesMap() throws IOException {
		//Context: add unconnected country without neighbours
		addCountry("Mauritius","afrori");
		
		//Evaluation
		assertFalse(testMapLoader.getMapService().isMapValid());
	}
	
	
	/**
	 * This method is executed by {@link #test002_addContinent()}
	 * @param name Name
	 * @param power Power
	 * @throws IOException  on invalid IO
	 */
	public void addContinent(String name, String power) throws IOException {
		testMapLoader.readCommand("editcontinent -add "+name+" "+power);
	}
	
	/**
	 * This method is executed by {@link #test004_removeContinent()}
	 * @param name is the name of the continent to be removed
	 * @throws IOException  on invalid IOon invalid values
	 */
	public void removeContinent(String name) throws IOException {
		testMapLoader.readCommand("editcontinent -remove "+name);
	}
	
	/**
	 * This method is executed by {@link #test005_addAndRemoveContinent()}
	 * @param name1 is the name of the continent to be added
	 * @param power Power
	 * @param name2 is the name of the continent to be removed
	 * @throws IOException  on invalid IOon invalid values
	 */
	public void addAndRemoveContinent(String name1, String power, String name2) throws IOException {
		testMapLoader.readCommand("editcontinent -add "+name1+" "+power+" -remove "+name2);
	}
	
	/**
	 * This method is executed by {@link #test006_addCountry()}
	 * @param name is the name of the country to be added
	 * @param continentName is the continent of the added country
	 * @throws IOException  on invalid IOon invalid values
	 */
	public void addCountry(String name, String continentName) throws IOException {
		testMapLoader.readCommand("editcountry -add "+name+" "+continentName);
	}
	
	/**
	 * This method is executed by {@link #test007_removeCountry()}
	 * @param name is the name of the country to be removed
	 * @throws IOException  on invalid IOon invalid values
	 */
	public void removeCountry(String name) throws IOException {
		testMapLoader.readCommand("editcountry -remove "+name);
	}
	
	/**
	 * This method is executed by {@link #test008_addAndRemoveCountry()}
	 * @param name1 is the country to be added
	 * @param continentName1 is the continent of the added country
	 * @param name2 is the country to be removed
	 * @throws IOException  on invalid IOon invalid values
	 */
	public void addAndRemoveCountry(String name1, String continentName1, String name2) throws IOException {
		testMapLoader.readCommand("editcountry -add "+name1+" "+continentName1+" "+" -remove "+name2);
	}
	
	/**
	 * This method is executed by {@link #test009_addNeighbor()}
	 * @param origin receives the country whose neighbor will be added
	 * @param neighborCountry receives the neighbor country to be added
	 * @throws IOException  on invalid IO
	 * country1 is the origin country retrieved by the testMapLoader
	 * neighbor1 is the neighboring country retrieved by the testMapLoader
	 * borders1 is the map that stores countries and their adjacent neighbors
	 * part1 is the adjacency list for country1
	 */
	public void addNeighbor(String origin, String neighborCountry) throws IOException {
		country1 = testMapLoader.getMapService().findCorrespondingIdByCountryName(origin);
		neighbor1 = testMapLoader.getMapService().findCorrespondingIdByCountryName(neighborCountry);
		testMapLoader.readCommand("editneighbor -add "+origin+" "+neighborCountry);
		borders1 = testMapLoader.getMapService().getAdjacencyCountriesMap();
		pair1 = borders1.get(country1.get());
	}
	
	/**
	 * This method is executed by {@link #test010_removeNeighbor()}
	 * @param origin receives the country whose neighbor will be removed
	 * @param neighborCountry receives the neighbor country to be removed
	 * @throws IOException  on invalid IO
	 * country1 is the origin country retrieved by the testMapLoader
	 * neighbor1 is the neighboring country retrieved by the testMapLoader
	 * borders1 is the map that stores countries and their adjacent neighbors
	 * part1 is the adjacency list of country1
	 */
	public void removeNeighbor(String origin, String neighborCountry) throws IOException {
		country1 = testMapLoader.getMapService().findCorrespondingIdByCountryName(origin);
		neighbor1 = testMapLoader.getMapService().findCorrespondingIdByCountryName(neighborCountry);
		testMapLoader.readCommand("editneighbor -remove "+origin+" "+neighborCountry);
		borders1 = testMapLoader.getMapService().getAdjacencyCountriesMap();
		pair1 = borders1.get(country1.get());
	}
	
	/**
	 * This method is executed by {@link #test011_addAndRemoveNeighbor()}
	 * @param origin1 receives the country whose neighbor will be added
	 * @param neighborCountry1 receives the neighbor country to be added
	 * @param origin2 receives the country whose neighbor will be removed
	 * @param neighborCountry2 receives the neighbor country to be removed
	 * @throws IOException on invalid IO
	 * country1 is the first origin country retrieved by the testMapLoader
	 * neighbor1 is the to-be-added neighboring country retrieved by the testMapLoader
	 * borders1 is the map that stores countries and their adjacent neighbors
	 * part1 is the adjacency list of country1
	 * country2 is the second origin country retrieved by the testMapLoader
	 * neighbor2 is the to-be-removed neighboring country retrieved by the testMapLoader
	 * pair2 is the adjacency list of country2
	 */
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
	
	
	/**
	 * This method is executed by {@link #test002_addContinent()}
	 * @param name Name
	 * @throws IOException  on invalid IO
	 */
	public void editMap(String name) throws IOException {
		testMapLoader.readCommand("editmap "+name);
	}


}