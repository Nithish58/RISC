package com6441.team7.risc;

import com6441.team7.risc.api.model.MapLoader;
import com6441.team7.risc.api.model.MapService;
import com6441.team7.risc.controller.GameController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;
import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class App {
    static Logger logger = LogManager.getLogger(App.class);

    public static void main( String[] args ) {
        try(Scanner scanner = new Scanner(System.in)) {
            MapLoader mapLoader = new MapLoader();
            Optional<MapService> mapService = mapLoader.loadMap(scanner);

            if (mapService.isPresent()) {
                GameController gameController = new GameController(mapService.get());
                gameController.play(scanner);
            }


        }


    }
}
