package com6441.team7.risc;

import com6441.team7.risc.api.model.MapService;
import com6441.team7.risc.api.model.PlayerService;
import com6441.team7.risc.controller.*;
import com6441.team7.risc.view.DominationView;
import com6441.team7.risc.view.GameView;
import com6441.team7.risc.view.PhaseView;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the main class.
 *
 */
public class App {
    public static void main( String[] args ) {
    	
        MapService mapService = new MapService();
        PlayerService playerService = new PlayerService(mapService);

        PhaseView phaseView = new PhaseView();
        GameView dominationView = new DominationView();

        List<Controller> controllerList = new ArrayList<>();

        MapLoaderController mapLoaderController = new MapLoaderController(mapService);
        StartupGameController startupGameController = new StartupGameController(mapLoaderController, playerService);
        ReinforceGameController reinforceGameController = new ReinforceGameController(playerService);
        FortifyGameController fortifyGameController = new FortifyGameController(playerService);
        AttackGameController attackController = new AttackGameController(playerService);

        controllerList.add(mapLoaderController);
        controllerList.add(startupGameController);
        controllerList.add(reinforceGameController);
        controllerList.add(fortifyGameController);
        controllerList.add(attackController);

        phaseView.addController(controllerList);

        mapLoaderController.setView(phaseView);
        startupGameController.setView(phaseView);
        reinforceGameController.setView(phaseView);
        fortifyGameController.setView(phaseView);
        attackController.setView(phaseView);

        mapService.addObserver(phaseView);
        mapService.addObserver(dominationView);
        playerService.addObserver(phaseView);
        playerService.addObserver(dominationView);

        //Added by Keshav
        startupGameController.setDominationView(dominationView);
        
        phaseView.receiveCommand();

      
        
    }
}
