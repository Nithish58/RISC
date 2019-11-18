package com6441.team7.risc.view;

import java.util.List;
import java.util.Observable;

import com6441.team7.risc.api.model.GameState;
import com6441.team7.risc.controller.*;
import com6441.team7.risc.controller.MapLoaderAdapter;

/**
 * The test class for phase view
 */
public class PhaseViewTest implements GameView{

    /**
     * Controller object for mapLoader controller
     */
    private Controller mapLoaderController;

    /**
     * Controller object for startUp controller
     */
    private Controller startUpGameController;

    /**
     * Controller object for reinforce controller
     */
    private Controller reinforceGameController;

    /**
     * Controller object for Fortify controller
     */
    private Controller fortifyGameController;

    /**
     * Controller object for attack controller
     */
    private Controller attackController;

    /**
     * an object of GameState class
     */
    private GameState gameState;

    /**
     * an instance of Object
     */
    private Object obj;

    /**
     * Empty string which display message
     */
    private String strDisplayMessage="";

    /**
     * method to display message
     * @param string String
     */
    public void displayMessage(String string) {
    	this.strDisplayMessage=string;
        System.out.println(string);
    }

    /**
     * To update the change in state
     * @param o Observable instance
     * @param arg Object argument
     */
	public void update(Observable o, Object arg) {
		
		this.obj=arg;
        
        //When GameState is changed
        if (arg instanceof GameState) {                    
        	this.gameState = (GameState) arg;
            return;
        }	
	}

    /**
     * To return an instance of Objects
     * @return Object instance
     */
	public Object getReturnedObject() {
		return obj;
	}

    /**
     * this method receives command and change gamestate as per command if its valid
     * @param command user command
     */
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
        	e.printStackTrace();
        }
		
	}

    /**
     * This method add list of controller to various controller after verifying instances of controller
     * @param list list of controller
     */
    public void addController(List<Controller> list){
        list.forEach(controller -> {
            if(controller instanceof MapLoaderAdapter){
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

    
    /**
     * Read command method. Not used here for testing
     */
	@Override
	public void receiveCommand() {}

    /**
     * Method to get display message
     * @return string
     */
	public String getStrDisplayMessage() {
		return strDisplayMessage;
	}
}
