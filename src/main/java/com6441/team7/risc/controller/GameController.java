package com6441.team7.risc.controller;


public class GameController {

	private startupGameController startupController;
	private reinforceGameController reinforcementGameController;
	private fortifyGameController fortificationGameController;
	

	
	public GameController() {
		startupController=new startupGameController();
		
		
		
		//reinforcementGameController=new reinforceGameController();
	}
	
	public void startUp(String command){
    System.out.println("start up begins");
	}
}
