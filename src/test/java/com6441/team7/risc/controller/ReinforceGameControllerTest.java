package com6441.team7.risc.controller;

import com6441.team7.risc.api.exception.ReinforceParsingException;
import com6441.team7.risc.api.model.Country;
import com6441.team7.risc.api.model.MapService;
import com6441.team7.risc.api.model.Player;
import com6441.team7.risc.api.model.PlayerService;
import com6441.team7.risc.view.PhaseView;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

public class ReinforceGameControllerTest {

    private ReinforceGameController reinforceGameController;
    private PlayerService playerService;
    private MapService mapService;

    @Before
    public void setUp() throws Exception {
        mapService = new MapService();
        playerService = new PlayerService(mapService);
        Player player = new Player("jenny");
        playerService.setCurrentPlayer(player);
        PhaseView phaseView = new PhaseView();
        reinforceGameController = new ReinforceGameController(playerService);
        reinforceGameController.setView(phaseView);
    }


    /**
     * get a valid domination map
     * @return
     * @throws Exception
     */
    private String getFile() throws Exception{
        URI uri = getClass().getClassLoader().getResource("jenny.map").toURI();
        return FileUtils.readFileToString(new File(uri), StandardCharsets.UTF_8);
    }

    /**
     * read an invalid command for reinforcement
     * @throws Exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void readInvalidCommand() throws Exception {
        String command = "invalid command";
        reinforceGameController.readCommand(command);
    }


    @Test
    public void readValidCommand() throws Exception {
        mockPlayerCountryInformation();
        String command = "reinforce siberia 1";
        reinforceGameController.readCommand(command);
    }



    /**
     * add a soldier from reinforcement to the country occupied
     * @throws URISyntaxException
     * @throws IOException
     */

    @Test
    public void reinforceArmy() throws URISyntaxException, IOException {
        mockPlayerCountryInformation();
        Player player = playerService.getCurrentPlayer();

        reinforceGameController.reinforceArmy(player, "siberia", 1);

        int armyNum = mapService.getCountries().stream()
                .filter(country -> country.getCountryName().equalsIgnoreCase("siberia"))
                .findFirst()
                .get()
                .getSoldiers();

        assertEquals(11, armyNum);

    }

    private void occupyCountry(Player player, String name, int number){
        mapService.getCountries().stream()
                .filter(country -> country.getCountryName().equalsIgnoreCase(name))
                .findFirst()
                .ifPresent(country -> {
                    country.setPlayer(player);
                    country.setSoldiers(number);
                });
    }

    private void mockPlayerCountryInformation() throws IOException, URISyntaxException {
        MapLoaderController mapLoaderController = new MapLoaderController(mapService);
        URI uri = getClass().getClassLoader().getResource("jenny.map").toURI();
        String file = FileUtils.readFileToString(new File(uri), StandardCharsets.UTF_8);
        mapLoaderController.parseFile(file);

        Player player = playerService.getCurrentPlayer();
        Player player2 = new Player("jake");
        occupyCountry(player, "siberia", 10);
        occupyCountry(player, "yazteck", 20);
        occupyCountry(player2, "worrick", 10);
    }

    /**
     * test when player conquers less than 3 countries and did not conquer a continent and did not trade in cards
     * @throws IOException
     * @throws URISyntaxException
     */
    @Test
    public void calculateReinforcedArmies() throws IOException, URISyntaxException {
        mockPlayerCountryInformation();
        Player player = playerService.getCurrentPlayer();
        reinforceGameController.calculateReinforcedArmies(player);
        assertEquals(3, reinforceGameController.getReinforcedArmies());
    }

    @Test
    public void exchangeCards() {
    }
}