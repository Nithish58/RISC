package com6441.team7.risc.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com6441.team7.risc.api.model.*;
import com6441.team7.risc.view.GameView;

import java.io.File;
import java.io.IOException;

public class SaveGameUtils {
    public static void saveGame(MapService mapService, PlayerService playerService, GameView view){
        try {
            GameStatusEntity entity = new GameStatusEntity();
            MapStatusEntity mapStatusEntity = mapService.getMapStatusEntity();
            entity.setMapStatusEntity(mapStatusEntity);

            PlayerStatusEntity playerStatusEntity = playerService.getPlayerStatusEntitiy();
            entity.setPlayerStatusEntity(playerStatusEntity);

            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(new File("saveGame.json"), entity);
            view.displayMessage("successfully saves the game");
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
