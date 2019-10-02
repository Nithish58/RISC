package com6441.team7.risc.controller;

import com6441.team7.risc.api.model.Continent;
import com6441.team7.risc.api.model.Country;
import com6441.team7.risc.view.CommandPromptView;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.*;

public class MapLoaderControllerTest {

    private MapLoaderController controller;
    private StateContext context;
    private CommandPromptView view;

    @Before
    public void setUp() throws Exception {
        context = new StateContext();
        view = new CommandPromptView();
        controller = new MapLoaderController(context, view);
    }

    @After
    public void tearDown() {

    }


    @Test
    public void readExistingFile() throws Exception{

        Optional<String> content = controller.readFile("/Users/xinjiezeng/eclipse-workspace/RISC/src/test/resources/ameroki.map");
        assertTrue(content.isPresent());
        assertFalse(content.get().isEmpty());
    }


    @Test
    public void receiveValidEditMapCommand() throws Exception{
        String command = "editmap " +"/Users/xinjiezeng/eclipse-workspace/RISC/src/test/resources/ameroki.map";;
        Optional<String> result = controller.validateEditMapCommands(command);
        assertTrue(result.isPresent());
    }


    @Test
    public void createContinentFromValidContinentInfo() throws Exception{
        String continentsInfo = validContinentString();
        Set<Continent> continents =  controller.readContinentsFromFile(continentsInfo);
        assertEquals(continents.size(), 6);
    }

    @Test
    public void createContinentMissingContinentPower() throws Exception{
        String continentsInfo = "parts2: continents]\n" +
                "azio 5 #9aff80\n" +
                "ameroki yellow\n" +
                "utropa 10 #a980ff\n" +
                "amerpoll 5 red\n" +
                "afrori 5 #ffd780\n" +
                "ulstrailia 5 magenta";

        Set<Continent> continents =  controller.readContinentsFromFile(continentsInfo);
        assertEquals(continents.size(), 5);

    }


    @Test
    public void createContinentWithContinentPowerNotInteger() throws Exception{
        String continentsInfo = "parts2: continents]\n" +
                "azio 5 #9aff80\n" +
                "ameroki abc yellow\n" +
                "utropa 10 #a980ff\n" +
                "amerpoll 5 red\n" +
                "afrori 5 #ffd780\n" +
                "ulstrailia 5 magenta";

        Set<Continent> continents =  controller.readContinentsFromFile(continentsInfo);
        assertEquals(continents.size(), 5);

    }

    @Test
    public void createCountriesFromValidCountryInfo() throws Exception{

        String continentsInfo = validContinentString();
        controller.readContinentsFromFile(continentsInfo);

        String countriesInfo = validCountryString();

        Set<Country> countries =  controller.readCountriesFromFile(countriesInfo);
        assertEquals(countries.size(), 5);

    }

    @Test
    public void createCountriesMissingContinentInfo() throws Exception{

        String continentsInfo = validContinentString();
        controller.readContinentsFromFile(continentsInfo);

        String countriesInfo = "[countries]\n" +
                "1 siberia 329 152\n" +
                "2 worrick 1 308 199\n" +
                "3 yazteck 1 284 260\n" +
                "4 kongrolo 1 278 295\n" +
                "5 china 1 311 350\n";

        Set<Country> countries =  controller.readCountriesFromFile(countriesInfo);
        assertEquals(countries.size(), 4);

    }

    @Test
    public void createCountriesWithInvalidContinentInfo() throws Exception{

        String continentsInfo = validContinentString();
        controller.readContinentsFromFile(continentsInfo);

        String countriesInfo = "[countries]\n" +
                "1 siberia 10 329 152\n" +
                "2 worrick 1 308 199\n" +
                "3 yazteck 1 284 260\n" +
                "4 kongrolo 1 278 295\n" +
                "5 china 1 311 350\n";


        Set<Country> countries = controller.readCountriesFromFile(countriesInfo);
        assertEquals(countries.size(), 4);

    }


    @Test
    public void createCountriesMissingUniqueIdentifier() throws Exception{

        String continentsInfo = validContinentString();
        controller.readContinentsFromFile(continentsInfo);

        String countriesInfo = "[countries]\n" +
                "siberia 1 329 152\n" +
                "2 worrick 1 308 199\n" +
                "3 yazteck 1 284 260\n" +
                "4 kongrolo 1 278 295\n" +
                "5 china 1 311 350\n";

        Set<Country> countries =  controller.readCountriesFromFile(countriesInfo);
        assertEquals(countries.size(), 4);

    }


    @Test
    public void createCountriesWithContinentIdNotInteger() throws Exception{
        String continentsInfo = validContinentString();
        controller.readContinentsFromFile(continentsInfo);

        String countriesInfo = "[countries]\n" +
                "1 siberia one 329 152\n" +
                "2 worrick 1 308 199\n" +
                "3 yazteck 1 284 260\n" +
                "4 kongrolo 1 278 295\n" +
                "5 china 1 311 350\n";

        Set<Country> countries =  controller.readCountriesFromFile(countriesInfo);
        assertEquals(countries.size(), 4);

    }


    @Test
    public void createAdjascencyCountriesWithValidInfo() throws Exception{

        String continentsInfo = validContinentString();
        controller.readContinentsFromFile(continentsInfo);

        String countriesInfo = validCountryString();
        controller.readCountriesFromFile(countriesInfo);

        String adjacencyInfo = "[borders]\n" +
                "1 2 3 4\n" +
                "2 1 4 5 \n" +
                "3 1 5\n" +
                "4 2 1\n" +
                "5 2 3 4\n";
        Map<Integer, Set<Integer>> adjacencyMap = controller.readAdjascentCountriesFromFile(adjacencyInfo);
        assertEquals(adjacencyMap.size(), 5);

    }

    @Test
    public void createAdjascencyCountriesWithNoAdjacency() throws Exception{
        String continentsInfo = validContinentString();
        controller.readContinentsFromFile(continentsInfo);

        String countriesInfo = validCountryString();
        controller.readCountriesFromFile(countriesInfo);

        String adjacencyInfo = "[borders]\n" +
                "1\n" +
                "2 1 4 5\n" +
                "3 1 5\n" +
                "4 2 1\n" +
                "5 2 3 4\n";
        Map<Integer, Set<Integer>> adjacencyMap = controller.readAdjascentCountriesFromFile(adjacencyInfo);
        assertEquals(adjacencyMap.size(), 4);

    }

    @Test
    public void createAdjascencyCountriesWithInvalidCountryIdAdjacency() throws Exception{
        String continentsInfo = validContinentString();
        controller.readContinentsFromFile(continentsInfo);

        String countriesInfo = validCountryString();
        controller.readCountriesFromFile(countriesInfo);

        String adjacencyInfo = "[borders]\n" +
                "1 100\n" +
                "2 1 4 5\n" +
                "3 1 5\n" +
                "4 2 1\n" +
                "5 2 3 4\n";
        Map<Integer, Set<Integer>> adjacencyMap = controller.readAdjascentCountriesFromFile(adjacencyInfo);
        assertEquals(adjacencyMap.size(), 4);

    }


    @Test
    public void createAdjascencyCountriesWithValueNotInteger() throws Exception{

        String continentsInfo = validContinentString();
        controller.readContinentsFromFile(continentsInfo);

        String countriesInfo = validCountryString();
        controller.readCountriesFromFile(countriesInfo);

        String adjacencyInfo = "[borders]\n" +
                "1 two\n" +
                "2 1 4 5\n" +
                "3 1 5\n" +
                "4 2 1\n" +
                "5 2 3 4\n";
        Map<Integer, Set<Integer>> adjacencyMap = controller.readAdjascentCountriesFromFile(adjacencyInfo);
        assertEquals(adjacencyMap.size(), 4);

    }



    private String validContinentString(){
        return "parts2: continents]\n" +
                "azio 5 #9aff80\n" +
                "ameroki 10 yellow\n" +
                "utropa 10 #a980ff\n" +
                "amerpoll 5 red\n" +
                "afrori 5 #ffd780\n" +
                "ulstrailia 5 magenta";
    }

    private String validCountryString(){
        return "[countries]\n" +
                "1 siberia 1 329 152\n" +
                "2 worrick 1 308 199\n" +
                "3 yazteck 1 284 260\n" +
                "4 kongrolo 1 278 295\n" +
                "5 china 1 311 350\n";
    }


}