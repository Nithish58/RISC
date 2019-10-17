package com6441.team7.risc;

import com6441.team7.risc.api.model.MapService;
import com6441.team7.risc.controller.GameController;
import com6441.team7.risc.controller.MapLoaderController;
import com6441.team7.risc.view.CommandPromptView;

/**
 * This is the main class.
 *
 */
public class App {
    public static void main( String[] args ) {
    		
        MapService mapService = new MapService();
        
        MapLoaderController mapLoaderController = new MapLoaderController(mapService);
        GameController gameController = new GameController(mapLoaderController,mapService);
		
        CommandPromptView view = new CommandPromptView(mapLoaderController, gameController);
        
        mapLoaderController.setView(view);
        gameController.setView(view);
        mapService.addObserver(view);
        view.receiveCommand();

    }
}
