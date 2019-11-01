package com6441.team7.risc.view;

import static com6441.team7.risc.api.RiscConstants.PHASE_VIEW_STRING;

import java.util.List;
import java.util.Observable;

import com6441.team7.risc.api.model.Country;
import com6441.team7.risc.api.model.GameState;
import com6441.team7.risc.api.model.Player;
import com6441.team7.risc.api.model.PlayerService;
import com6441.team7.risc.api.wrapperview.PlayerChangeWrapper;
import com6441.team7.risc.api.wrapperview.PlayerEditWrapper;
import com6441.team7.risc.api.wrapperview.PlayerFortificationWrapper;
import com6441.team7.risc.api.wrapperview.PlayerInitialArmyWrapper;
import com6441.team7.risc.api.wrapperview.PlayerInitialCountryAssignmentWrapper;
import com6441.team7.risc.api.wrapperview.PlayerPlaceArmyWrapper;
import com6441.team7.risc.controller.AttackGameController;
import com6441.team7.risc.controller.Controller;
import com6441.team7.risc.controller.FortifyGameController;
import com6441.team7.risc.controller.MapLoaderController;
import com6441.team7.risc.controller.ReinforceGameController;
import com6441.team7.risc.controller.StartupGameController;

public class PhaseViewTest implements GameView{
	
	
    private Controller mapLoaderController;
    private Controller startUpGameController;
    private Controller reinforceGameController;
    private Controller fortifyGameController;
    private Controller attackController;
    
    private GameState gameState;
    private Player currentPlayer=null;
    
    private Object obj;
    
    private String strDisplayMessage="";
    
    PlayerEditWrapper playerEditWrapper;

    public void displayMessage(String string) {
    	this.strDisplayMessage=string;
        System.out.println(string);
    }


	public void update(Observable o, Object arg) {
		
		this.obj=arg;
        
        //When GameState is changed
        if (arg instanceof GameState) {                    
        	this.gameState = (GameState) arg;
            return;
        }	
	}
	
	public Object getReturnedObject() {
		return obj;
	}

	public void receiveCommand(String command) {
		
        try {
        	
            switch (gameState) {
                case LOAD_MAP:
                    mapLoaderController.readCommand(command);
                    break;
                case START_UP:
                    startUpGameController.readCommand(command);
                    break;
                case REINFORCE:
                    reinforceGameController.readCommand(command);
                    break;
                case ATTACK:
                    attackController.readCommand(command);
                    break;
                case FORTIFY:
                    fortifyGameController.readCommand(command);
                    break;
            }

        } catch (Exception e) {
            //displayMessage(e.getMessage());
        	e.printStackTrace();
        }
		
	}
	
    public void addController(List<Controller> list){
        list.forEach(controller -> {
            if(controller instanceof MapLoaderController){
                this.mapLoaderController = controller;
            }
            else if(controller instanceof StartupGameController){
                this.startUpGameController = controller;
            }
            else if(controller instanceof ReinforceGameController){
                this.reinforceGameController = controller;
            }
            else if(controller instanceof FortifyGameController){
                this.fortifyGameController = controller;
            }
            else if(controller instanceof AttackGameController){
                this.attackController = controller;
            }
        });
    }


	@Override
	public void receiveCommand() {}


	public PlayerEditWrapper getPlayerEditWrapper() {
		return playerEditWrapper;
	}


	public String getStrDisplayMessage() {
		return strDisplayMessage;
	}
	
	

}
