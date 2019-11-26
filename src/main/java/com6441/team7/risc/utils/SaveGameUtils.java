package com6441.team7.risc.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com6441.team7.risc.api.model.*;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * this class is used to store the game status in the json file
 */
public class SaveGameUtils {

    /**
     * save the game to a json file.
     * @param entities the objects that to be stored
     */
    public static void saveGame(Map<String, Object> entities){
        try {

            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(new File("game.json"), entities);
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * store different game status in the entities
     * @param entities Map entity where details are put in.
     * @param clazz 
     * @param entity 
     */
    public static void putIntoMap(Map<String, Object> entities, Class clazz, Object entity) {
        entities.put(clazz.getSimpleName(), entity);
    }
}
