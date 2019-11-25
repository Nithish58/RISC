package com6441.team7.risc.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com6441.team7.risc.api.model.*;
import com6441.team7.risc.api.model.StartupStateEntity;
import com6441.team7.risc.view.GameView;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com6441.team7.risc.api.RiscConstants.WHITESPACE;

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
    private StartupGameController startupGameController;
    private ReinforceGameController reinforceGameController;
    private FortifyGameController fortifyGameController;


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

    public void loadGame(File saveGameFile) throws IOException {
        ObjectMapper objectMapper =new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JsonNode entity = objectMapper.readValue(saveGameFile, JsonNode.class);

        loadMapStatusEntity(entity);
        loadPlayerStatusEntity(entity);
        loadStartUpState(entity);
        loadReinforceState(entity);
       //loadFortifyState(entity);

        displayLoadMessage();


    }



    private void displayLoadMessage() {
        phaseView.displayMessage("game has been successfully loaded");
        phaseView.displayMessage("players' data is successfully loaded.");
        phaseView.displayMessage("next command is: " + playerService.getCommand());
    }

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

    private void loadPlayerStatusEntity(JsonNode entity) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        PlayerStatusEntity playerStatusEntity = objectMapper.treeToValue(entity.get(PlayerStatusEntity.class.getSimpleName()), PlayerStatusEntity.class);
        playerService.setCurrentPlayer(playerStatusEntity.getCurrentPlayer());
        playerService.setListPlayers(playerStatusEntity.getListPlayers());
        playerService.setCurrentPlayerIndex(playerStatusEntity.getCurrentPlayerIndex());
        
        // playerService.getPlayerList().forEach(player -> player.updateCountryPlayerList(mapService));
        

        //Added By Keshav
        
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

    private void loadStartUpState(JsonNode entity) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        StartupStateEntity startupStateEntity = objectMapper.treeToValue(entity.get(StartupStateEntity.class.getSimpleName()), StartupStateEntity.class);
        Optional.ofNullable(startupStateEntity).ifPresent(status -> startupGameController.setStatus(status));
    }


    private void loadReinforceState(JsonNode entity) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ReinforceStateEntity reinforceStateEntity = objectMapper.treeToValue(entity.get(ReinforceStateEntity.class.getSimpleName()), ReinforceStateEntity.class);
        Optional.ofNullable(reinforceStateEntity).ifPresent(reinforceGameController::setStatus);
    }


    public void exitLoadGame(){
        phaseView.displayMessage("exit loading the game");
        mapService.setState(GameState.START_UP);
    }
}
