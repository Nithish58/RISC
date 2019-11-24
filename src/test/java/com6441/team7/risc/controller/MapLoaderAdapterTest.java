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
     * a reference of StartupGameController
     */
    private StartupGameController startupController;

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
		System.out.println(mapLoaderController.getMapService().getContinentCountriesMap());
		System.out.println(mapLoaderController.getMapService().getCountries());
		mapLoaderController.getMapService().printNeighboringCountryInfo();
		System.out.println("Number of continents before test: "+mapLoaderController.getMapService().getContinents().size());
		System.out.println("Number of countries before test: "+mapLoaderController.getMapService().getCountries().size());
		//URI variable uri is assigned URI parameter for reading file and executing editmap command
		URI uri = getClass().getClassLoader().getResource(mapname).toURI();
		//file reads the file retrieved from the uri as string.
		//it uses UTF-8 charsets.
		file = FileUtils.readFileToString(new File(uri), StandardCharsets.UTF_8);
		//if the testCounter is not less than 3, the editmap command and map parsing must be skipped.
		//This so that the subsequent tests won't be impacted by it.
		inputcommand = "editmap "+mapname;
		
		editMap(mapname);
		
		//size of continent list before one continent is added or removed
		initcontinentsize = mapLoaderController.getMapService().getContinents().size();
		//size of country list before one continent is added or removed
		initcountrysize = mapLoaderController.getMapService().getCountries().size();


		//This sets the variable for map saving command.
		savename = "edittedmap.map";
       
        
    }


//    /**
//     * read existing map from the directory given by its map name
//     * The test will pass if it ables to read and parses the map file and returns true
//     * @throws Exception exception on invalid
//     */
//    @Test
//    public void readExistingFile() throws Exception{
//        URI uri = getClass().getClassLoader().getResource("ameroki.map").toURI();
//        String file = FileUtils.readFileToString(new File(uri), StandardCharsets.UTF_8);
//
//        boolean result = mapLoaderController.parseFile(file);
//        assertTrue(result);
//    }
//
//    /**
//     * read a map does not exist given by its map name
//     * The test should be able to create a new map file and return true
//     * @throws Exception exception on error
//     */
//    @Test
//    public void readNewCreatedFile() throws Exception{
//        URI uri = getClass().getClassLoader().getResource("test.map").toURI();
//        String file = FileUtils.readFileToString(new File(uri), StandardCharsets.UTF_8);
//
//        boolean result = mapLoaderController.parseFile(file);
//
//        assertTrue(result);
//    }
//
//
//    /**
//     * create continent objects by valid continent strings
//     * The test should be able to read and parse the strings and creates continents
//     * the test will pass if the number of newly created continents is 6
//     * @throws Exception exception on error
//     */
//    @Test
//    public void createContinentFromValidContinentInfo() throws Exception{
//        String continentsInfo = validContinentString();
//        Set<Continent> continents =  mapLoaderController.parseRawContinents(continentsInfo);
//        assertEquals(continents.size(), 6);
//    }
//
//
//    /**
//     * create continent objects if missing continent power
//     * The test should throw an exception when continent power is missing
//     * @throws Exception exception on invalid
//     */
//    @Test(expected=ContinentParsingException.class)
//    public void createContinentMissingContinentPower() throws Exception{
//        String continentsInfo = "parts2: continents]\r\n" +
//                "azio 5 #9aff80\r\n" +
//                "ameroki yellow\r\n" +
//                "utropa 10 #a980ff\r\n" +
//                "amerpoll 5 red\r\n" +
//                "afrori 5 #ffd780\r\n" +
//                "ulstrailia 5 magenta";
//
//      mapLoaderController.parseRawContinents(continentsInfo);
//
//    }
//
//
//    /**
//     * create continents if the continent power is not an integer
//     * The test should throw an exception when continent power is not an integer
//     * @throws Exception exception on invalid
//     */
//    @Test(expected = ContinentParsingException.class)
//    public void createContinentWithContinentPowerNotInteger() throws Exception{
//        String continentsInfo = "parts2: continents]\r\n" +
//                "azio 5 #9aff80\r\n" +
//                "ameroki abc yellow\r\n" +
//                "utropa 10 #a980ff\r\n" +
//                "amerpoll 5 red\r\n" +
//                "afrori 5 #ffd780\r\n" +
//                "ulstrailia 5 magenta";
//
//        mapLoaderController.parseRawContinents(continentsInfo);
//    }
//
//    /**
//     * create countries with valid country information
//     * pass the tests if the number of newly created countries is 5.
//     * @throws Exception exception on invalid
//     */
//    @Test
//    public void createCountriesFromValidCountryInfo() throws Exception{
//
//        String continentsInfo = validContinentString();
//        mapLoaderController.parseRawContinents(continentsInfo);
//
//        String countriesInfo = validCountryString();
//
//        Set<Country> countries =  mapLoaderController.parseRawCountries(countriesInfo);
//        assertEquals(countries.size(), 5);
//
//    }
//
//    /**
//     * create countries with continent id missing
//     * the test should throw an countryParsingException
//     * @throws Exception exception on invalid
//     */
//    @Test(expected = CountryParsingException.class)
//    public void createCountriesMissingContinentInfo() throws Exception{
//
//        String continentsInfo = validContinentString();
//        mapLoaderController.parseRawContinents(continentsInfo);
//
//        String countriesInfo = "[countries]\r\n" +
//                "1 siberia 329 152\r\n" +
//                "2 worrick 1 308 199\r\n" +
//                "3 yazteck 1 284 260\r\n" +
//                "4 kongrolo 1 278 295\r\n" +
//                "5 china 1 311 350\r\n";
//
//        mapLoaderController.parseRawCountries(countriesInfo);
//
//    }
//
//    /**
//     * create countries with continent id not exist
//     * the test should throw CountryParsingException
//     * @throws Exception exception on invalid
//     */
//    @Ignore
//    @Test(expected = CountryParsingException.class)
//    public void createCountriesWithInvalidContinentInfo() throws Exception{
//
//        String continentsInfo = validContinentString();
//        mapLoaderController.parseRawContinents(continentsInfo);
//
//        String countriesInfo = "[countries]\r\n" +
//                "1 siberia 10 329 152\r\n" +
//                "2 worrick 1 308 199\r\n" +
//                "3 yazteck 1 284 260\r\n" +
//                "4 kongrolo 1 278 295\r\n" +
//                "5 china 1 311 350\r\n";
//
//       mapLoaderController.parseRawCountries(countriesInfo);
//    }
//
//
//    /**
//     * create countries with country id missing when reading existing map file
//     * the tests should throw CountryParsingException
//     * @throws Exception exception on invalid
//     */
//    @Test(expected = CountryParsingException.class)
//    public void createCountriesMissingUniqueIdentifier() throws Exception{
//
//        String continentsInfo = validContinentString();
//        mapLoaderController.parseRawContinents(continentsInfo);
//
//        String countriesInfo = "[countries]\r\n" +
//                "siberia 1 329 152\r\n" +
//                "2 worrick 1 308 199\r\n" +
//                "3 yazteck 1 284 260\r\n" +
//                "4 kongrolo 1 278 295\r\n" +
//                "5 china 1 311 350\r\n";
//
//        mapLoaderController.parseRawCountries(countriesInfo);
//
//    }
//
//
//    /**
//     * create countries with continent id not an integer when reading map file
//     * the test should throw a CountryParsingException
//     * @throws Exception exception on invalid
//     */
//    @Test(expected = CountryParsingException.class)
//    public void createCountriesWithContinentIdNotInteger() throws Exception{
//        String continentsInfo = validContinentString();
//        mapLoaderController.parseRawContinents(continentsInfo);
//
//        String countriesInfo = "[countries]\r\n" +
//                "1 siberia one 329 152\r\n" +
//                "2 worrick 1 308 199\r\n" +
//                "3 yazteck 1 284 260\r\n" +
//                "4 kongrolo 1 278 295\r\n" +
//                "5 china 1 311 350\r\n";
//
//        mapLoaderController.parseRawCountries(countriesInfo);
//
//
//    }
//
//
//    /**
//     * create adjacency countries with valid information
//     * expect the number of newly created adjacency countries info is 5
//     * @throws Exception exception on invalid
//     */
//    @Test
//    public void createAdjascencyCountriesWithValidInfo() throws Exception{
//
//        String continentsInfo = validContinentString();
//        mapLoaderController.parseRawContinents(continentsInfo);
//
//        String countriesInfo = validCountryString();
//        mapLoaderController.parseRawCountries(countriesInfo);
//
//        String adjacencyInfo = "[borders]\r\n" +
//                "1 2 3 4\r\n" +
//                "2 1 4 5 \r\n" +
//                "3 1 5\r\n" +
//                "4 2 1\r\n" +
//                "5 2 3 4\r\n";
//        Map<Integer, Set<Integer>> adjacencyMap = mapLoaderController.parseRawNeighboringCountries(adjacencyInfo);
//        assertEquals(adjacencyMap.size(), 5);
//
//    }
//
//    /**
//     * create a neighboring info with no adjacency countries id
//     * the test should throw a neighboringParsingException
//     * @throws Exception exception on invalid
//     */
//    @Test(expected = NeighborParsingException.class)
//    public void createAdjascencyCountriesWithNoAdjacency() throws Exception{
//        String continentsInfo = validContinentString();
//        mapLoaderController.parseRawContinents(continentsInfo);
//
//        String countriesInfo = validCountryString();
//        mapLoaderController.parseRawCountries(countriesInfo);
//
//        String adjacencyInfo = "[borders]\r\n" +
//                "1\r\n" +
//                "2 1 4 5\r\n" +
//                "3 1 5\r\n" +
//                "4 2 1\r\n" +
//                "5 2 3 4\r\n";
//        mapLoaderController.parseRawNeighboringCountries(adjacencyInfo);
//    }
//
//    /**
//     * create adjacency Countries Information with Countries ID not exist
//     * the test should throw a neighboringParsingException
//     * @throws Exception exception on invalid
//     */
//    @Test(expected = NeighborParsingException.class)
//    public void createAdjascencyCountriesWithInvalidCountryIdAdjacency() throws Exception{
//        String continentsInfo = validContinentString();
//        mapLoaderController.parseRawContinents(continentsInfo);
//
//        String countriesInfo = validCountryString();
//        mapLoaderController.parseRawCountries(countriesInfo);
//
//        String adjacencyInfo = "[borders]\r\n" +
//                "1 100\r\n" +
//                "2 1 4 5\r\n" +
//                "3 1 5\r\n" +
//                "4 2 1\r\n" +
//                "5 2 3 4\r\n";
//        mapLoaderController.parseRawNeighboringCountries(adjacencyInfo);
//
//    }
//
//    /**
//     * create adjacency countries with countries id not an integer
//     * expect the test to throw a NeighboringParsingException
//     * @throws Exception exception exception
//     */
//    @Test(expected = NeighborParsingException.class)
//    public void createAdjacencyCountriesWithValueNotInteger() throws Exception{
//
//        String continentsInfo = validContinentString();
//        mapLoaderController.parseRawContinents(continentsInfo);
//
//        String countriesInfo = validCountryString();
//        mapLoaderController.parseRawCountries(countriesInfo);
//
//        String adjacencyInfo = "[borders]\r\n" +
//                "1 two\r\n" +
//                "2 1 4 5\r\n" +
//                "3 1 5\r\n" +
//                "4 2 1\r\n" +
//                "5 2 3 4\r\n";
//        mapLoaderController.parseRawNeighboringCountries(adjacencyInfo);
//
//    }
//
//    /**
//     * parse a valid editcontinent command to add three continents and remove an existing continent
//     * the test will pass if the number of continents in the mapService increases by 2
//     * @throws Exception exception
//     */
//    @Test
//    public void testValidEditContinentCommand() throws Exception{
//        String command = "editcontinent -add Asia 6 -add America 5 -add Africa 4 -remove Africa";
//        command = StringUtils.substringAfter(command, "-"); // This is for getting the command substring after every dash
//        String[] commands = StringUtils.split(command, "-"); //This is for splitting the strings divided by dashes
//        expectedcontinentsize = initcontinentsize+2;
//        mapLoaderController.editContinents(commands);
//        assertEquals(mapLoaderController.getMapService().getContinents().size(),expectedcontinentsize);
//    }
//
//    /**
//     * parse an editcontinent command to add three continents while one continent missing continent power
//     * the test will pass if the number of continents in the mapService increases by 2
//     * this is because one continent does not receive valid param to be added to the list
//     * @throws Exception exception
//     */
//    @Test
//    public void testInvalidAddContinentCommand() throws Exception{
//        String command = "editcontinent -add Asia -add America 5 -add Africa 4";
//        command = StringUtils.substringAfter(command, "-");
//        String[] commands = StringUtils.split(command, "-");
//        expectedcontinentsize = initcontinentsize+2; //This sets the expected size of continent list to be the same as its initial size
//        mapLoaderController.editContinents(commands);
//        assertEquals(mapLoaderController.getMapService().getContinents().size(), expectedcontinentsize);
//
//    }
//
//    /**
//     * parse an editcontinent command to add three continents and to remove a continent
//     * add Asia is not valid, add America and add Africa is valid, remove is not valid
//     * pass the test if the number of continents in the mapService remains the same
//     * @throws Exception exception
//     */
//    @Ignore
//    @Test
//    public void testInvalidRemoveContinentCommand() throws Exception{
//        String command = "editcontinent -add Asia -add America 5 -add Africa 4 -remove";
//        command = StringUtils.substringAfter(command, "-");
//        String[] commands = StringUtils.split(command, "-");
//        expectedcontinentsize = initcontinentsize+2;
//        mapLoaderController.editContinents(commands);
//        assertEquals(mapLoaderController.getMapService().getContinents().size(), expectedcontinentsize);
//
//    }
//
//    /**
//     * test the editcountry command with three addition of countries
//     * the test will pass if the number of newly added country is 3
//     * @throws Exception exception
//     */
//    @Ignore
//    @Test
//    public void testValidAddCountryCommand() throws Exception{
//    	//Add valid continent information
//        MapService mapService = addValidContinentInfo();
//		//Retrieve the continent where the country will be added
//		Optional<Continent> continentFromService = mapLoaderController.getMapService().getContinentById(1);
//		Continent continent = continentFromService.get();
//		String continentName = continent.getName();
//        String command = "editcountry -add Sky_Republic "+continentName+" -add Ocean_Republic "+continentName+" -add Desert_Republic "+continentName;
//        command = StringUtils.substringAfter(command, "-");
//        String[] commands = StringUtils.split(command, "-");
//        expectedcountrysize = initcountrysize+3;
//        mapLoaderController.editCountries(commands);
//        assertEquals(expectedcountrysize, mapService.getCountries().size());
//    }
//
//    /**
//     * test the addCountry command with one invalid addition and 2 valid addition
//     * will pass the test if the number of newly added country is 2
//     * @throws Exception exception
//     */
//    @Ignore
//    @Test
//    public void testInValidAddCountryCommand() throws Exception{
//        MapService mapService = addValidContinentInfo();
//
//        String command = "editcountry -add China sia -add India asia -add egypt africa";
//        command = StringUtils.substringAfter(command, "-");
//        String[] commands = StringUtils.split(command, "-");
//        expectedcountrysize = initcountrysize;
//        mapLoaderController.editCountries(commands);
//        assertEquals(mapService.getCountries().size(), expectedcountrysize);
//    }


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



//	/**
//	 * Tests command to load map from file.
//	 * The method receives the <i>file</i> parameter from the context
//	 * and then it is parsed. The runner passes the test if the result
//	 * returns true.
//	 * @throws Exception exception upon invalid values
//	 */
//	@Ignore
//	@Test
//	public void test001_readFile() throws Exception{
//		message = "The map is not valid";
//		assertTrue(message, mapLoaderController.parseFile(file));
//	}


	/**
	 * Tests adding one continent to the continent list.
	 * The method uses continentcommand1 as the command to be checked.
	 * The test passes if the number of continent increases by 1 after the test.
	 * @throws Exception exception upon invalid values
	 */
	@Test
	public void test002_addContinent() throws Exception{
		System.out.printf("Adding one continent%n------------%n");
		addContinent("Nord_Asia","1");
		expectedcontinentsize = initcontinentsize + 1; //Continent list size is expected to increase by 1
		assertSame(expectedcontinentsize, mapLoaderController.getMapService().getContinents().size());
	}

	/**
	 * Tests deleting one continent.
	 * The method uses continentcommand3 as the command to be checked.
	 * The test passes if the number of continents decreases by 1 after the test.
	 * @throws Exception exception upon invalid values
	 */
	@Test
	public void test004_removeContinent() throws Exception{
		
		System.out.printf("Removing one continent%n------------%n");
		Optional<Continent> continentFromService = mapLoaderController.getMapService().getContinentById(1);
		Continent continent = continentFromService.get();
		String continentName = continent.getName();
		removeContinent(continentName);
		expectedcontinentsize = initcontinentsize - 1; //Continent list size is expected to decrease by 1
		assertSame(expectedcontinentsize, mapLoaderController.getMapService().getContinents().size());
	}

	/**
	 * Tests adding and removing one continent from the continent list in one command.
	 * The method uses continentcommand5.
	 * The test passes if the number of continents stays the same after the test.
	 * @throws Exception exception upon invalid values
	 */
	@Test
	public void test005_addAndRemoveContinent() throws Exception{
		System.out.printf("Adding and removing one continent%n------------%n");
		//Set continent to be removed
		Optional<Continent> continentFromService = mapLoaderController.getMapService().getContinentById(1);
		Continent continent = continentFromService.get();
		String removedContinentName = continent.getName();
		String addedContinent = "Nord_Asia";
		String addedContinentPower = "1";
		System.out.println(addedContinent+" "+addedContinentPower+" "+removedContinentName);
		addAndRemoveContinent(addedContinent,addedContinentPower,removedContinentName);
		expectedcontinentsize = initcontinentsize; //Continent size should remain the same
		assertSame(expectedcontinentsize, mapLoaderController.getMapService().getContinents().size());
	}

	/**
	 * Tests adding one country to the country list.
	 * The method uses countrycommand1 as the command to be checked.
	 * The test passes if the number of countries increases by 1 after the test.
	 * @throws Exception exception upon invalid values
	 */
	@Test
	public void test006_addCountry() throws Exception{
		System.out.printf("Adding one country%n------------%n");
		//Retrieve the continent where the country will be added
		Optional<Continent> continentFromService = mapLoaderController.getMapService().getContinentById(1);
		Continent continent = continentFromService.get();
		String continentName = continent.getName();
		//Name of the country to be added
		String countryName = "Sky_Republic";
		System.out.println(countryName+" "+continentName);
		addCountry(countryName, continentName);
		expectedcountrysize = initcountrysize+1; //Country list size is expected to increase by 1
		assertSame(expectedcountrysize, mapLoaderController.getMapService().getCountries().size());
	}

	/**
	 * Tests removing one country from the country list.
	 * The method uses countrycommand3 as the command to be checked.
	 * The test passes if the number of countries decreases by 1 after the test.
	 * @throws Exception exception upon invalid values
	 */
	@Test
	public void test007_removeCountry() throws Exception{
		System.out.printf("Removing one country%n------------%n");
		//Get set of countries using service
		Set<Country> countriesFromService = mapLoaderController.getMapService().getCountries();
		//Get the first country in the set using iterator
		Iterator<Country> countryIterator = countriesFromService.iterator();
		//Set the country
		Country country = countryIterator.next();
		//Retrieve the country name
		String countryName = country.getCountryName();
		//Call the removeCountry method with countryName as the param
		removeCountry(countryName);
		System.out.println(countryName);
		expectedcountrysize = initcountrysize-1; //Country list size is expected to decrease by 1
		assertSame(expectedcountrysize, mapLoaderController.getMapService().getCountries().size());
	}

	/**
	 * Tests adding and removing one country from the country list in one command.
	 * The method uses countrycommand5 as the command to be checked.
	 * The test passes if the number of countries stays the same after the test.
	 * @throws Exception exception upon invalid values
	 */
	@Test
	public void test008_addAndRemoveCountry() throws Exception{
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
		//Retrieve the country name
		String removedCountryName = country.getCountryName();
		//Call the removeCountry method with countryName as the param
		removeCountry(removedCountryName);
		System.out.println("Remove "+removedCountryName);
		addAndRemoveCountry(countryName, continentName, removedCountryName);
		expectedcountrysize = initcountrysize; //Country list size should remian the same
		assertSame(expectedcountrysize, mapLoaderController.getMapService().getCountries().size());
	}

	/**
	 * Tests adding one neighbor of a country.
	 * The method uses neighborcommand1 as the command to be checked.
	 * The test passes if the origin country exists in the adjacency list of
	 * the entire map and the neighboring country is among the
	 * origin country's adjacency's list after the test.
	 * @throws Exception exception upon invalid values
	 */
	@Test
	public void test009_addNeighbor() throws Exception{
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
		addNeighbor(originCountryName, countryName);
		//Check if map object contains both country ID and neighbor ID
		assertTrue("Country is not found", borders1.containsKey(country1.get()));
		assertTrue("Neighboring is not found", pair1.contains(neighbor1.get()));
	}

	/**
	 * Tests removing one neighbor from a country.
	 * The method uses neighborcommand3 as the command to be checked.
	 * The test passes if the origin country exists in the adjacency list of
	 * the entire map and the neighboring country is not among the
	 * origin country's adjacency's list after the test.
	 * @throws Exception exception upon invalid values
	 */
	@Test
	public void test010_removeNeighbor() throws Exception{
		System.out.printf("Removing one neighbor from a country%n------------%n");
		//get first country from the country list
		//Get set of countries using service
		Set<Country> countriesFromService = mapLoaderController.getMapService().getCountries();
		//Get the first country in the set using iterator
		Iterator<Country> countryIterator = countriesFromService.iterator();
		//Set the country
		Country originCountry = countryIterator.next();
		//Retrieve the country name
		String originCountryName = originCountry.getCountryName();
		//Retrieve the origin country's adjacency list
		Set<Integer> originCountryAdjacencyList = mapService.getAdjacencyCountries(originCountry.getId());
		//get the first country in adjacency list
		//Retrieve an adjacent country
		Country neighborCountry = null;
		for(Integer i: originCountryAdjacencyList) {
			neighborCountry=mapService.getCountryById(i).get();
			break;
		}
		//Set adjacent country's name
		String neighborCountryName = neighborCountry.getCountryName();
		//get pair of country and neighbor
		removeNeighbor(originCountryName, neighborCountryName);
		//Check if map object contains both country ID and neighbor ID
		assertTrue("Country is not found",borders1.containsKey(country1.get()));
		assertFalse("Neighboring country is found", pair1.contains(neighbor1.get()));
	}


	/**
	 * Tests adding and removing one neighbor in one command.
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
		//Add a country first
		//Retrieve the continent where the country will be added
		Optional<Continent> continentFromService = mapLoaderController.getMapService().getContinentById(1);
		Continent continent = continentFromService.get();
		String continentName = continent.getName();
		//Name of the country to be added
		String newCountryName = "Sky_Republic";
		System.out.println(newCountryName+" "+continentName);
		addCountry(newCountryName, continentName);
		//Retrieve the origin country
		Set<Country> countriesFromService = mapLoaderController.getMapService().getCountries();
		//Get the first country in the set using iterator
		Iterator<Country> countryIterator = countriesFromService.iterator();
		//Set the country
		Country originCountry = countryIterator.next();
		//Retrieve the country name
		String originCountryName = originCountry.getCountryName();
		//Get adjacency list of origin country
		Set<Integer> originCountryAdjacencyList = mapService.getAdjacencyCountries(originCountry.getId());
		//get the first country in adjacency list
		//Retrieve an adjacent country
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
	 * The test passes if the getMapService().isMapNotValid() returns false,
	 * which it should if the preceding tests on managing continents, countries,
	 * and neighbors invalidate the map file.
	 */
	@Test
	public void test012_invalidateMap() {
		System.out.printf("%nInvalidating map%n");
		assertFalse(mapLoaderController.getMapService().isMapNotValid());
	}

	/**
	 * Test if map is valid.
	 * The test passes if the getMapService().isMapNotValid() returns true,
	 * which it should if the preceding tests on managing continents, countries,
	 * and neighbors maintain the validity of the map file.
	 */
	@Test
	public void test013_validateMap() {
		System.out.printf("%nValidating map%n");
		assertTrue("This map is invalid", mapLoaderController.getMapService().isMapValid());
	}

	/**
	 * Test if map can be saved.
	 * The test passes if the saved map is found using the parseFile() method of the map controller.
	 */
//	@Test
//	public void test014_saveMap() {
//		System.out.printf("%nTesting map saving%n");
//		message = "Map is invalid";
//		try {
//			mapLoaderController.saveMap("savemap "+savename);
//			file = FileUtils.readFileToString(new File(savename), StandardCharsets.UTF_8);
//			assertTrue(message, mapLoaderController.parseFile(file));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}
	
	/**
	 * Test if map is empty.
	 * @throws Exception  on invalid IO
	 */
	@Test
	public void test015_validateEmptyMap() throws Exception {
		message = "Map is empty";
		editMap("newmap.map");
		assertTrue(message, mapLoaderController.getMapService().isMapNotValid());
	}
	
	/**
	 * Test if duplicate countries exist. 
	 * Expected: Duplicate countries not added and map remains valid.
	 * @throws Exception on invalid IO
	 */
//	@Ignore
	@Test
	public void test016_validateDuplicateCountry() throws Exception {
		addCountry("japan", "azio");
		assertFalse(testMapLoader.getMapService().isMapValid());
	}
	
	/**
	 * Test for unconnected map. 
	 * Add country Mauritius to continent afrori. Do not add any neighbours. 
	 * Expected: Map must be invalid.
	 * @throws Exception on invalid IO
	 */
	@Test public void test017_invalidUnconnectedCountriesMap() throws Exception {
		//Context: add unconnected country without neighbours
		addCountry("Mauritius","afrori");
		
		//Evaluation
		assertFalse(testMapLoader.getMapService().isMapValid());
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
	 * This method is executed by {@link #test004_removeContinent()}
	 * @param name is the name of the continent to be removed
	 * @throws Exception  on invalid values
	 */
	public void removeContinent(String name) throws Exception {
		mapLoaderController.readCommand("editcontinent -remove "+name);
	}
	
	/**
	 * This method is executed by {@link #test005_addAndRemoveContinent()}
	 * @param name1 is the name of the continent to be added
	 * @param power Power
	 * @param name2 is the name of the continent to be removed
	 * @throws Exception 
	 */
	public void addAndRemoveContinent(String name1, String power, String name2) throws Exception {
		testMapLoader.readCommand("editcontinent -add "+name1+" "+power+" -remove "+name2);
	}
	
	/**
	 * This method is executed by {@link #test006_addCountry()}
	 * @param name is the name of the country to be added
	 * @param continentName is the continent of the added country
	 * @throws Exception 
	 */
	public void addCountry(String name, String continentName) throws Exception {
		mapLoaderController.readCommand("editcountry -add "+name+" "+continentName);
	}
	
	/**
	 * This method is executed by {@link #test007_removeCountry()}
	 * @param name is the name of the country to be removed
	 * @throws Exception 
	 */
	public void removeCountry(String name) throws Exception {
		mapLoaderController.readCommand("editcountry -remove "+name);
	}
	
	/**
	 * This method is executed by {@link #test008_addAndRemoveCountry()}
	 * @param name1 is the country to be added
	 * @param continentName1 is the continent of the added country
	 * @param name2 is the country to be removed
	 * @throws Exception 
	 */
	public void addAndRemoveCountry(String name1, String continentName1, String name2) throws Exception {
		mapLoaderController.readCommand("editcountry -add "+name1+" "+continentName1+" "+" -remove "+name2);
	}
	
	/**
	 * This method is executed by {@link #test009_addNeighbor()}.
	 * country1 is the origin country retrieved by the testMapLoader.
	 * neighbor1 is the neighboring country retrieved by the testMapLoader.
	 * borders1 is the map that stores countries and their adjacent neighbors.
	 * part1 is the adjacency list for country1.
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
	 * This method is executed by {@link #test010_removeNeighbor()}.
	 * <i>country1</i> is the origin country retrieved by the testMapLoader.
	 * <i>neighbor1</i> is the neighboring country retrieved by the testMapLoader.
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
	 * This method is executed by {@link #test011_addAndRemoveNeighbor()}. 
	 * <i>neighbor1</i> is the to-be-added neighboring country retrieved by the testMapLoader.
	 * <i>borders1</i> is the map that stores countries and their adjacent neighbors.
	 * <i>part1</i> is the adjacency list of country1.
	 * <i>country2</i> is the second origin country retrieved by the testMapLoader.
	 * <i>neighbor2</i> is the to-be-removed neighboring country retrieved by the testMapLoader.
	 * <i>pair2</i> is the adjacency list of country2.
	 * @param origin1 receives the country whose neighbor will be added
	 * @param neighborCountry1 receives the neighbor country to be added
	 * @param origin2 receives the country whose neighbor will be removed
	 * @param neighborCountry2 receives the neighbor country to be removed
	 * @throws Exception on invalid IO
	 * country1 is the first origin country retrieved by the testMapLoader
	 * 
	 */
	public void addAndRemoveNeighbor(String origin1, String neighborCountry1, String origin2, String neighborCountry2) throws Exception {
		country1 = mapLoaderController.getMapService().findCorrespondingIdByCountryName(origin1);
		neighbor1 = mapLoaderController.getMapService().findCorrespondingIdByCountryName(neighborCountry1);
		country2 = mapLoaderController.getMapService().findCorrespondingIdByCountryName(origin2);
		neighbor2 = mapLoaderController.getMapService().findCorrespondingIdByCountryName(neighborCountry2);
		
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