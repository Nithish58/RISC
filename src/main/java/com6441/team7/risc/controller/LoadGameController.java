package com6441.team7.risc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com6441.team7.risc.api.model.*;
import com6441.team7.risc.utils.CommonUtils;
import com6441.team7.risc.utils.MapDisplayUtils;
import com6441.team7.risc.view.GameView;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;

import static com6441.team7.risc.api.RiscConstants.WHITESPACE;

public class LoadGameController implements Controller {

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


    @Override
    public void readCommand(String command) throws Exception {
        RiscCommand commandType = RiscCommand.parse(StringUtils.split(command, WHITESPACE)[0]);
        String fileName = StringUtils.split(command, WHITESPACE)[1];
        File file = new File(fileName);
        switch (commandType) {
            case LOADGAME:
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
        GameStatusEntity entity = objectMapper.readValue(saveGameFile, GameStatusEntity.class);
        MapStatusEntity mapStatusEntity = entity.getMapStatusEntity();
        PlayerStatusEntity playerStatusEntity = entity.getPlayerStatusEntity();

        mapService.setState(mapStatusEntity.getGameState());
        mapService.setContinents(mapStatusEntity.getContinents());
        mapService.setCountries(mapStatusEntity.getCountries());
        mapService.setContinentCountriesMap(mapStatusEntity.getContinentCountriesMap());
        mapService.setAdjacencyCountriesMap(mapStatusEntity.getAdjacencyCountriesMap());
        mapService.notifyObservers(mapStatusEntity);

        playerService.setCurrentPlayer(playerStatusEntity.getCurrentPlayer());
        playerService.setListPlayers(playerStatusEntity.getListPlayers());
        playerService.setMapFiles(playerStatusEntity.getMapFiles());
        playerService.setCurrentGameNumber(playerStatusEntity.getCurrentGameNumber());
        playerService.setMaximumDices(playerStatusEntity.getMaximumDices());
        playerService.setResult(playerStatusEntity.getResults());
        playerService.notifyObservers(playerStatusEntity);

    }

    public void exitLoadGame(){
        phaseView.displayMessage("exit loading the game");
        mapService.setState(GameState.START_UP);
    }
}
