package com6441.team7.risc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com6441.team7.risc.api.model.*;
import com6441.team7.risc.utils.SaveGameUtils;
import com6441.team7.risc.view.GameView;
import com6441.team7.risc.view.PhaseView;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class LoadGameControllerTest {

    private MapService mapService;
    private PlayerService playerService;
    private MapLoaderController mapLoaderController;
    private StartupGameController startupGameController;
    private StartupStateEntity startupStateEntity;
    private MapStatusEntity mapStatusEntity;
    private PlayerStatusEntity playerStatusEntity;
    private LoadGameController loadGameController;
    private GameView phaseView;


    @Before
    public void setUp() throws Exception {
        mapService = new MapService();
        mapLoaderController = new MapLoaderController(mapService);
        playerService = new PlayerService(mapService);
        startupGameController = new StartupGameController(mapLoaderController, playerService);
        loadGameController = new LoadGameController(mapService, playerService);
        phaseView = new PhaseView();
        startupGameController.setView(phaseView);
    }


    /**
     * load the json of game file test.json from the local computer.
     * This json file stores the game status including map information,player information and startUpController status
     * when the player save files just before populating countries
     * The map is conquest_test.map
     * expect country number equal to 4 and continent number equal to 2
     * expect the gameState equal to startup
     * expect the player number is 2
     * expect the command stored is placeall or placearmy
     * expect the boolAllCountriesPlaced equal to false
     * @throws IOException
     */
    @Test
    public void loadGame() throws Exception {
//        File file = new File("test1.json");
//        loadGameController.readCommand("loadgame test1.json");
//
//        assertEquals(GameState.START_UP, mapService.getMapStatusEntity().getGameState());
//        assertEquals(RiscCommand.PLACE_ALL.getName() + RiscCommand.PLACE_ARMY.getName(), playerService.getCommand());
//        assertEquals(4, mapService.getCountries().size());
//        assertEquals(2, mapService.getContinents().size());
//        assertFalse(startupStateEntity.isBoolAllCountriesPlaced());
    }

    @Test
    public void saveGame() throws IOException {
        //mockStateInStartUpPhase();


    }




    private void mockStateInStartUpPhase() throws IOException {
        mapLoaderController = new MapLoaderController(mapService);
        mapLoaderController.readFile("conquest_test.map");


        Player player = new Player("jenny");
        Player player2 = new Player("jake");

        occupyCountry(player, "a1", 10);
        occupyCountry(player, "b1", 20);
        occupyCountry(player2, "b2", 10);
        occupyCountry(player2, "b3", 10);

        mapService.setState(GameState.START_UP);

        mapStatusEntity = mapService.getMapStatusEntity();
        playerStatusEntity = playerService.getPlayerStatusEntity();


        boolean[] boolArrayCountriesPlaced = {false, false, false, false};
        startupStateEntity = StartupStateEntity.StartupStateEntityBuilder.newInstance()
                .boolMapLoaded(true)
                .boolGamePlayerAdded(true)
                .boolAllGamePlayersAdded(true)
                .boolAllCountriesPlaced(true)
                .boolArrayCountriesPlaced(boolArrayCountriesPlaced)
                .boolAllCountriesPlaced(false)
                .boolCountriesPopulated(true)
                .build();

        startupGameController.setStatus(startupStateEntity);
        save();
    }

    private void save() {
        Map<String, Object> entities = new HashMap<>();
        entities.put(MapStatusEntity.class.getSimpleName(), mapStatusEntity);
        entities.put(PlayerStatusEntity.class.getSimpleName(), playerStatusEntity);
        entities.put(StartupStateEntity.class.getSimpleName(), startupStateEntity);

        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(new File("test2.json"), entities);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * occupy a country with certain number of soldiers number by the player
     *
     * @param player
     * @param name
     * @param number
     */
    private void occupyCountry(Player player, String name, int number) {
        mapService.getCountries().stream()
                .filter(country -> country.getCountryName().equalsIgnoreCase(name))
                .findFirst()
                .ifPresent(country -> {
                    country.setPlayer(player);
                    country.setSoldiers(number);
                });
    }


}