package com6441.team7.risc.controller;

import com6441.team7.risc.api.exception.ContinentParsingException;
import com6441.team7.risc.api.exception.CountryParsingException;
import com6441.team7.risc.api.exception.NeighborParsingException;
import com6441.team7.risc.api.model.Continent;
import com6441.team7.risc.api.model.Country;
import com6441.team7.risc.api.model.MapService;
import com6441.team7.risc.view.CommandPromptView;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class MapLoaderControllerTest {

    private MapLoaderController mapLoaderController;
    private CommandPromptView view;
    private MapService mapService;
    private GameController gameController;

    @Before
    public void setUp() throws Exception {
        view = new CommandPromptView( mapLoaderController, gameController);
        mapService = new MapService();
        mapLoaderController = new MapLoaderController(mapService);
        mapLoaderController.setView(view);
    }


    @Test
    public void readExistingFile() throws Exception{
        URI uri = getClass().getClassLoader().getResource("ameroki.map").toURI();
        String file = FileUtils.readFileToString(new File(uri), StandardCharsets.UTF_8);

        boolean result = mapLoaderController.parseFile(file);
        assertTrue(result);
    }

    @Test
    public void readNewCreatedFile() throws Exception{
        URI uri = getClass().getClassLoader().getResource("test.map").toURI();
        String file = FileUtils.readFileToString(new File(uri), StandardCharsets.UTF_8);

        boolean result = mapLoaderController.parseFile(file);

        assertTrue(result);
    }


    @Test
    public void createContinentFromValidContinentInfo() throws Exception{
        String continentsInfo = validContinentString();
        Set<Continent> continents =  mapLoaderController.parseRawContinents(continentsInfo);
        assertEquals(continents.size(), 6);
    }

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

    @Test
    public void createCountriesFromValidCountryInfo() throws Exception{

        String continentsInfo = validContinentString();
        mapLoaderController.parseRawContinents(continentsInfo);

        String countriesInfo = validCountryString();

        Set<Country> countries =  mapLoaderController.parseRawCountries(countriesInfo);
        assertEquals(countries.size(), 5);

    }

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

    @Test
    public void testValidEditContinentCommand() throws Exception{
        String command = "editcontinent -add Asia 6 -add America 5 -add Africa 4 -remove Africa";

        command = StringUtils.substringAfter(command, "-");
        String[] commands = StringUtils.split(command, "-");

        mapLoaderController.editContinents(commands);
        assertEquals(mapLoaderController.getMapService().getContinents().size(),2);
    }

    @Test
    public void testInvalidAddContinentCommand() throws Exception{
        String command = "editcontinent -add Asia -add America 5 -add Africa 4";
        command = StringUtils.substringAfter(command, "-");
        String[] commands = StringUtils.split(command, "-");

        mapLoaderController.editContinents(commands);
        assertEquals(mapLoaderController.getMapService().getContinents().size(), 2);

    }

    @Test
    public void testInvalidRemoveContinentCommand() throws Exception{
        String command = "editcontinent -add Asia -add America 5 -add Africa 4 -remove";
        command = StringUtils.substringAfter(command, "-");
        String[] commands = StringUtils.split(command, "-");

        mapLoaderController.editContinents(commands);
        assertEquals(mapLoaderController.getMapService().getContinents().size(), 2);

    }

    @Test
    public void testValidAddCountryCommand() throws Exception{
        MapService mapService = addValidContinentInfo();
        String command = "editcountry -add China asia -add India asia -add egypt africa";
        command = StringUtils.substringAfter(command, "-");
        String[] commands = StringUtils.split(command, "-");

        mapLoaderController.editCountries(commands);
        assertEquals(3, mapService.getCountries().size());
    }

    @Test
    public void testInValidAddCountryCommand() throws Exception{
        MapService mapService = addValidContinentInfo();

        String command = "editcountry -add China sia -add India asia -add egypt africa";
        command = StringUtils.substringAfter(command, "-");
        String[] commands = StringUtils.split(command, "-");

        mapLoaderController.editCountries(commands);
        assertEquals(mapService.getCountries().size(), 2);
    }

    @Test
    public void testSaveCommand() throws Exception{

        String continentsInfo = validContinentString();
        mapLoaderController.parseRawContinents(continentsInfo);

        String countriesInfo = validCountryString();
        mapLoaderController.parseRawCountries(countriesInfo);

        String adjacencyInfo = "[borders]\r\n" +
                "1 2\r\n" +
                "2 1 4 5\r\n" +
                "3 1 5\r\n" +
                "4 2 1\r\n" +
                "5 2 3 4\r\n";
        Map<Integer, Set<Integer>> adjacencyMap = mapLoaderController.parseRawNeighboringCountries(adjacencyInfo);

        String command = "savemap /Users/xinjiezeng/eclipse-workspace/RISC/src/test/resources/test.map";
        mapLoaderController.saveMap(command);

//        assertTrue(lines.isPresent());
//        assertEquals(lines.get().size(), 21);

    }

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

    private String validContinentString(){
        return "parts2: continents]\r\n" +
                "azio 5 #9aff80\r\n" +
                "ameroki 10 yellow\r\n" +
                "utropa 10 #a980ff\r\n" +
                "amerpoll 5 red\r\n" +
                "afrori 5 #ffd780\r\n" +
                "ulstrailia 5 magenta";
    }

    private String validCountryString(){
        return "[countries]\r\n" +
                "1 siberia 1 329 152\r\n" +
                "2 worrick 1 308 199\r\n" +
                "3 yazteck 1 284 260\r\n" +
                "4 kongrolo 1 278 295\r\n" +
                "5 china 1 311 350\r\n";
    }



}