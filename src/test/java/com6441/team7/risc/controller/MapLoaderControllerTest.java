package com6441.team7.risc.controller;

import com6441.team7.risc.api.model.Continent;
import com6441.team7.risc.api.model.Country;
import com6441.team7.risc.view.CommandPromptView;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.*;

public class MapLoaderControllerTest {

    private MapLoaderController controller;
    private CommandPromptView view;
    private StateContext context;

    @Before
    public void setUp() throws Exception {
        context = new StateContext();
        view = new CommandPromptView();
        controller = new MapLoaderController(context, view);
    }


    @Test
    public void readExistingFile() throws Exception{

        Optional<String> content = controller.readFile(validFilePath());
        assertTrue(content.isPresent());
        assertFalse(content.get().isEmpty());
    }


    @Test
    public void receiveValidEditMapCommand() throws Exception{
        String command = "editmap " + validFilePath();
        Optional<String> result = controller.validateEditMapCommands(command);
        assertTrue(result.isPresent());
    }


    @Test
    public void createContinentFromValidContinentInfo() throws Exception{
        String continentsInfo = "parts2: continents]\n" +
                "azio 5 #9aff80\n" +
                "ameroki 10 yellow\n" +
                "utropa 10 #a980ff\n" +
                "amerpoll 5 red\n" +
                "afrori 5 #ffd780\n" +
                "ulstrailia 5 magenta";

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
        String countriesInfo = "[countries]\n" +
                "1 siberia 1 329 152\n" +
                "2 worrick 1 308 199\n" +
                "3 yazteck 1 284 260\n" +
                "4 kongrolo 1 278 295\n" +
                "5 china 1 311 350\n";

        Set<Country> countries =  controller.readCountriesFromFile(countriesInfo);
        assertEquals(countries.size(), 5);

    }

    @Test
    public void createCountriesMissingContinentInfo() throws Exception{
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
    public void createCountriesMissingUniqueIdentifier() throws Exception{
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
    public void createCountriesWithContinentPowerNotInteger() throws Exception{
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
        String adjacencyInfo = "[borders]\n" +
                "1 20 32 2\n" +
                "2 1 10 35 3\n" +
                "3 2 4 6 35\n" +
                "4 3 35 5 6\n" +
                "5 4 6 42 7 9\n" +
                "6 3 5 9 4\n" +
                "7 5 9 8\n" +
                "8 9 7\n" +
                "9 5 8 7 6 10";
        Map<Integer, Set<Integer>> adjacencyMap = controller.readAdjascentCountriesFromFile(adjacencyInfo);
        assertEquals(adjacencyMap.size(), 9);

    }

    @Test
    public void createAdjascencyCountriesWithNoAdjacency() throws Exception{
        String adjacencyInfo = "[borders]\n" +
                "1\n" +
                "2 1 10 35 3\n" +
                "3 2 4 6 35\n" +
                "4 3 35 5 6\n" +
                "5 4 6 42 7 9\n" +
                "6 3 5 9 4\n" +
                "7 5 9 8\n" +
                "8 9 7\n" +
                "9 5 8 7 6 10";
        Map<Integer, Set<Integer>> adjacencyMap = controller.readAdjascentCountriesFromFile(adjacencyInfo);
        assertEquals(adjacencyMap.size(), 8);

    }

    @Test
    public void createAdjascencyCountriesWithValueNotInteger() throws Exception{
        String adjacencyInfo = "[borders]\n" +
                "1 two three four\n" +
                "2 1 10 35 3\n" +
                "3 2 4 6 35\n" +
                "4 3 35 5 6\n" +
                "5 4 6 42 7 9\n" +
                "6 3 5 9 4\n" +
                "7 5 9 8\n" +
                "8 9 7\n" +
                "9 5 8 7 6 10";
        Map<Integer, Set<Integer>> adjacencyMap = controller.readAdjascentCountriesFromFile(adjacencyInfo);
        assertEquals(adjacencyMap.size(), 8);

    }


    private String validFilePath(){
        return "/Users/xinjiezeng/eclipse-workspace/RISC/src/test/resources/ameroki.map";
    }
}