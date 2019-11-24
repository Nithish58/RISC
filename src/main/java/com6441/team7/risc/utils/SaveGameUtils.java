package com6441.team7.risc.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com6441.team7.risc.api.model.*;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class SaveGameUtils {
    public static void saveGame(Map<String, Object> entities){
        try {

            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(new File("game.json"), entities);
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
