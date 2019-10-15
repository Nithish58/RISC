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
    	
    	/*
    	 * NEED TO CHANGE ORDER:
    	 * NOTES AND JOEY SPECIFICALLY SAID THAT:
    	 * CREATE MODEL FIRST
    	 * THEN CREATE VIEW
    	 * THEN CONTROLLER
    	 * 
    	 * EVEN THOUGH IT DOESN'T MATTER, LET'S ADHERE TO WHAT HE SAYS
    	 * BECAUSE HE LIKES TO REMOVE MARKS FOR UNNECESSARY THINGS...
    	 */
    	
    	
        MapService mapService = new MapService();
        
        MapLoaderController mapLoaderController = new MapLoaderController(mapService);
        GameController gameController = new GameController(mapLoaderController,mapService);
		
        CommandPromptView view = new CommandPromptView(mapLoaderController, gameController);
        
        mapLoaderController.setView(view);
        mapService.addObserver(view);
        view.receiveCommand();

    }
}
