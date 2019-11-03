package com6441.team7.risc.controller;

import static com6441.team7.risc.api.RiscConstants.WHITESPACE;

import org.apache.commons.lang3.StringUtils;

import com6441.team7.risc.api.model.GameState;
import com6441.team7.risc.api.model.PlayerService;
import com6441.team7.risc.api.model.RiscCommand;
import com6441.team7.risc.view.GameView;

public class AttackGameController implements Controller {

    private PlayerService playerService;
    private GameView phaseView;

    public AttackGameController(PlayerService playerService){

        this.playerService = playerService;
    }

    public void setView(GameView view){
        this.phaseView = view;
    }


    //TODO: receive command from phaseView and validate the command
    //TODO: if the command is valid, call corresponding method in PlayerService.class
    //TODO: if the command is not valid, call phaseView.displayMessage() to display error message
    @Override
    public void readCommand(String command) throws Exception {

    	this.playerService.getMapService().setState(GameState.FORTIFY);
    	
    	RiscCommand commandType = RiscCommand.parse(StringUtils.split(command, WHITESPACE)[0]);
    	
    	
    	switch(commandType) {
    	
    	case ATTACK:
    		
    		
    		
    		break;
    	
    	case DEFEND:
    		break;
    		
    	case ATTACKMOVE:
    		break;
    	
    	default:
            throw new IllegalArgumentException("cannot recognize this command");
    	
    	}
    	
    	
    	
    	
    	this.playerService.getMapService().setState(GameState.FORTIFY);
    	
    }
}
