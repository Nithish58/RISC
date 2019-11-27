package com6441.team7.risc.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com6441.team7.risc.api.model.*;
import com6441.team7.risc.api.model.StartupStateEntity;
import com6441.team7.risc.view.GameView;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com6441.team7.risc.api.RiscConstants.WHITESPACE;

/**
 * This class handles functionality of loading the game and exiting loading game
 */
public class LoadGameController implements Controller{
    /**
     * a reference of mapService
     */
    private MapService mapService;

    /**
     * a reference of plaerService
     */
    private PlayerService playerService;

    /**
     * a reference of phaseView
     */
    private GameView phaseView;

    /**
     * a reference of StartupGameController
     */
    private StartupGameController startupGameController;

    /**
     * a reference of ReinforceGameController
     */
    private ReinforceGameController reinforceGameController;

    /**
     * a reference of FortifyGameController
     */
    private FortifyGameController fortifyGameController;


    /**
     * the constructor
     * @param mapService store all information of map
     * @param playerService store all inforamtion of players
     */
    public LoadGameController(MapService mapService, PlayerService playerService){
        this.mapService = mapService;
        this.playerService = playerService;

       
    }

    /**
     * set the view
     *
     * @param view GameView
     */
    public void setView(GameView view) {
        this.phaseView = view;
    }

    /**
     * set the controllers
     * @param list a list of controllers
     */
    public void setControllers(List<Controller> list){
        list.forEach(controller -> {

            if(controller instanceof StartupGameController){
                this.startupGameController = (StartupGameController) controller;
            }
            else if(controller instanceof ReinforceGameController){

                this.reinforceGameController = (ReinforceGameController) controller;
            }
            else if(controller instanceof FortifyGameController){
                this.fortifyGameController = (FortifyGameController) controller;
            }
        });
    }


    /**
     * receive commands from phase view
     * check the command type,
     * if it is loadgame, call loadgame()
     * if it is exit, call exitloadgame()
     * else the command is not valid, will throw an exception
     * @param command reference command
     * @throws Exception on invalid value
     */
    @Override
    public void readCommand(String command) throws Exception {
        RiscCommand commandType = RiscCommand.parse(StringUtils.split(command, WHITESPACE)[0]);

        switch (commandType) {
            case LOADGAME:
                String fileName = StringUtils.split(command, WHITESPACE)[1];
                File file = new File(fileName);
                loadGame(file);
                break;
            case EXITLOADGAME:
                exitLoadGame();
                break;
            default:
                throw new IllegalArgumentException("cannot recognize this command");
        }
    }

    /**
     * load the game state
     * @param saveGameFile file that saves games state
     * @throws IOException if there is JSON parsing exception
     */
    public void loadGame(File saveGameFile) throws IOException {
        ObjectMapper objectMapper =new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JsonNode entity = objectMapper.readValue(saveGameFile, JsonNode.class);

        loadMapStatusEntity(entity);
        loadPlayerStatusEntity(entity);
        loadStartUpState(entity);
        loadReinforceState(entity);
        displayLoadMessage();


    }


    /**
     * display the successfully loading inforamtion
     */
    private void displayLoadMessage() {
        phaseView.displayMessage("game has been successfully loaded");
        phaseView.displayMessage("players' data is successfully loaded.");
        phaseView.displayMessage("next command is: " + playerService.getCommand());
    }

    /**
     * load the mapStatusEntity and store in the mapService
     * @param entity json node to store game data
     * @throws JsonProcessingException if there is JSON parsing exception
     */
    private void loadMapStatusEntity(JsonNode entity) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        MapStatusEntity mapStatusEntity = objectMapper.treeToValue(entity.get(MapStatusEntity.class.getSimpleName()), MapStatusEntity.class);
        mapService.setState(mapStatusEntity.getGameState());
        mapService.setContinents(mapStatusEntity.getContinents());
        mapService.setCountries(mapStatusEntity.getCountries());
        mapService.setContinentCountriesMap(mapStatusEntity.getContinentCountriesMap());
        mapService.setAdjacencyCountriesMap(mapStatusEntity.getAdjacencyCountriesMap());
        mapService.notifyMapServiceObservers(mapStatusEntity);
        
    }

    /**
     * load the playerStatusEntity and store in playerService
     * @param entity json node to store game data
     * @throws JsonProcessingException if there is JSON parsing exception
     */
    private void loadPlayerStatusEntity(JsonNode entity) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        PlayerStatusEntity playerStatusEntity = objectMapper.treeToValue(entity.get(PlayerStatusEntity.class.getSimpleName()), PlayerStatusEntity.class);
        playerService.setCurrentPlayer(playerStatusEntity.getCurrentPlayer());
        playerService.setListPlayers(playerStatusEntity.getListPlayers());
        playerService.setCurrentPlayerIndex(playerStatusEntity.getCurrentPlayerIndex());

        
           for(Player p:playerService.getPlayerList()) {
        	   
        	   
        	   for(Country c:mapService.getCountries()) {
        		   if(p.getName().equalsIgnoreCase(c.getPlayer().getName())) {
        			   
        			   p.addCountryToPlayerList(c);
        			   
        			   c.setPlayer(p);
        			   
        		   }
        	   }
        	   
           }        	
        
        playerService.setCommand(playerStatusEntity.getCommand());
        playerService.notifyPlayerServiceObservers(playerStatusEntity);
        
        
        
    }

    /**
     * store startUpSate in startUpGameController
     * @param entity json node to store game data
     * @throws JsonProcessingException if there is JSON parsing exception
     */
    private void loadStartUpState(JsonNode entity) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        StartupStateEntity startupStateEntity = objectMapper.treeToValue(entity.get(StartupStateEntity.class.getSimpleName()), StartupStateEntity.class);
        Optional.ofNullable(startupStateEntity).ifPresent(status -> startupGameController.setStatus(status));
   

		boolean[] boolArrayCountriesPlaced=new boolean[playerService.getPlayerList().size()];
		
		for(int i=0;i<playerService.getPlayerList().size();i++) {
			if(playerService.getPlayerList().get(i).getArmies()<=0) {
				boolArrayCountriesPlaced[i]=true;
			}
			else boolArrayCountriesPlaced[i]=false;
		}
		
		startupGameController.setBoolArrayCountriesPlaced(boolArrayCountriesPlaced);
    
    }


    /**
     * load the reinforceState to reinforceController
     * @param entity json node to store game data
     * @throws JsonProcessingException if there is JSON parsing exception
     */
    private void loadReinforceState(JsonNode entity) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ReinforceStateEntity reinforceStateEntity = objectMapper.treeToValue(entity.get(ReinforceStateEntity.class.getSimpleName()), ReinforceStateEntity.class);
        Optional.ofNullable(reinforceStateEntity).ifPresent(reinforceGameController::setStatus);
    }


    /**
     * exit game load phase and goes to the startup phase
     */
    public void exitLoadGame(){
        phaseView.displayMessage("exit loading the game");
        mapService.setState(GameState.START_UP);
    }
}
