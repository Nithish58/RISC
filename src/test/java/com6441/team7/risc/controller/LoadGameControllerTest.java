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
import java.util.*;

import static org.junit.Assert.*;

/**
 * tests for load and save game
 */
public class LoadGameControllerTest {

    /**
     * a reference of mapService to store map information
     */
    private MapService mapService;

    /**
     * a reference of playerService to store player information
     */
    private PlayerService playerService;

    /**
     * a reference of mapLoaderController to load the game
     */
    private MapLoaderController mapLoaderController;

    /**
     * a reference of startupGameController to handle commands in the startup phase
     */
    private StartupGameController startupGameController;

    /**
     * a reference of startupStateEntity to store state in the startUpController
     */
    private StartupStateEntity startupStateEntity;

    /**
     * a reference of mapStatusEntity to store map data
     */
    private MapStatusEntity mapStatusEntity;

    /**
     * a reference of playerStatusEntity to store player information
     */
    private PlayerStatusEntity playerStatusEntity;

    /**
     * a reference of loadGameController to load the game
     */
    private LoadGameController loadGameController;

    /**
     * a reference of reinforceController to manage the state in reinforce phase
     */
    private ReinforceGameController reinforceGameController;

    /**
     * a reference of phase view to display information
     */
    private GameView phaseView;


    /**
     * setup method to set up the attributes
     * @throws Exception exception on Invalid
     */
    @Before
    public void setUp() throws Exception {
        mapService = new MapService();
        mapLoaderController = new MapLoaderController(mapService);
        playerService = new PlayerService(mapService);
        startupGameController = new StartupGameController(mapLoaderController, playerService);
        loadGameController = new LoadGameController(mapService, playerService);
        ReinforceGameController reinforceGameController = new ReinforceGameController(playerService);
        loadGameController.setControllers(Arrays.asList(startupGameController, reinforceGameController));
        phaseView = new PhaseView();
        startupGameController.setView(phaseView);
        loadGameController.setView(phaseView);
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
     * @throws IOException
     */
    @Test
    public void loadGame() throws Exception {
        File file = new File("test1.json");
        loadGameController.readCommand("loadgame test1.json");

        assertEquals(GameState.START_UP, mapService.getMapStatusEntity().getGameState());
        assertEquals(RiscCommand.PLACE_ALL.getName() + RiscCommand.PLACE_ARMY.getName(), playerService.getCommand());
        assertEquals(4, mapService.getCountries().size());
        assertEquals(2, mapService.getContinents().size());
        assertEquals(2, playerService.getPlayerList().size());
    }

    /**
     * save the game status in the test2.json
     * @throws Exception if throws Exception in saving file in test2.json
     * when loading the test2.json, the number of countries in the mapService expects to be 4
     * the number of continents expects to be 2
     * the number of players in the playerService expects to be 2
     * the game state expects to be StartUp
     * the command for the game expects to be placeAllplaceArmy
     *
     */
    @Test
    public void saveGame() throws Exception {
        mockStateInStartUpPhase();
        loadGameController.readCommand("loadgame test2.json");
        assertEquals(GameState.START_UP, mapService.getMapStatusEntity().getGameState());
        assertEquals(RiscCommand.PLACE_ALL.getName() + RiscCommand.PLACE_ARMY.getName(), playerService.getCommand());
        assertEquals(4, mapService.getCountries().size());
        assertEquals(2, mapService.getContinents().size());
        assertEquals(2, playerService.getPlayerList().size());

    }


    /**
     * mock the data in mapService, PlayerService and state in StartUpController
     * and save the data in the test2.json
     * @throws IOException if there is failure in saving as a json file
     */
    private void mockStateInStartUpPhase() throws IOException {
        mapLoaderController = new MapLoaderController(mapService);
        mapLoaderController.readFile("conquest_test.map");


        Player player = new Player("jenny");
        Player player2 = new Player("jake");

        ArrayList<Player> playerList = new ArrayList<>();
        playerList.add(player);
        playerList.add(player2);


        occupyCountry(player, "a1", 10);
        occupyCountry(player, "b1", 20);
        occupyCountry(player2, "b2", 10);
        occupyCountry(player2, "b3", 10);

        mapService.setState(GameState.START_UP);

        playerService.setCommand(RiscCommand.PLACE_ALL.getName() + RiscCommand.PLACE_ARMY.getName());
        playerService.setListPlayers(playerList);
        playerService.setCurrentPlayer(player);
        player.setPlayerCategory("HUMAN");
        player2.setPlayerCategory("HUMAN");
        playerService.setCurrentPlayerIndex(0);



        mapStatusEntity = mapService.getMapStatusEntity();
        playerStatusEntity = playerService.getPlayerStatusEntity();
        boolean[] boolArrayCountriesPlaced = {false, false, false, false};

        StartupStateEntity startupStateEntity = new StartupStateEntity();
        startupStateEntity.setBoolMapLoaded(true);
        startupStateEntity.setBoolGamePlayerAdded(true);
        startupStateEntity.setBoolAllGamePlayersAdded(true);
        startupStateEntity.setBoolCountriesPopulated(true);
        startupStateEntity.setBoolArrayCountriesPlaced(boolArrayCountriesPlaced);
        startupStateEntity.setBoolAllCountriesPlaced(false);


        startupGameController.setStatus(startupStateEntity);
        save();
    }

    /**
     * save the game state to the test2.json
     */
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
     * @param player current player
     * @param name the name of the country
     * @param number the number of soldiers
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