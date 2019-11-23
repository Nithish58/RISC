package com6441.team7.risc.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com6441.team7.risc.api.model.GameStatusEntity;
import com6441.team7.risc.api.model.MapStatusEntity;
import com6441.team7.risc.api.model.PlayerStatusEntity;

import java.io.File;
import java.io.IOException;

public class SaveGameUtils {
    public static void saveGame(GameStatusEntity entity){
        try {

            MapStatusEntity mapStatusEntity = entity.getMapStatusEntity();
            entity.setMapStatusEntity(mapStatusEntity);

            PlayerStatusEntity playerStatusEntity = entity.getPlayerStatusEntity();
            entity.setPlayerStatusEntity(playerStatusEntity);

            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(new File("game.json"), entity);
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
