package com6441.team7.risc;

import com6441.team7.risc.api.model.MapService;
import com6441.team7.risc.controller.GameController;
import com6441.team7.risc.controller.MapLoaderController;
import com6441.team7.risc.view.CommandPromptView;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ) {
        MapService mapService = new MapService();
        GameController gameController = new GameController();
        MapLoaderController mapLoaderController = new MapLoaderController(mapService);
        CommandPromptView view = new CommandPromptView(mapLoaderController, gameController);
        mapLoaderController.setView(view);
        mapService.addObserver(view);
        view.receiveCommand();

    }
}
