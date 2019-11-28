package com6441.team7.risc.view;

import com6441.team7.risc.api.model.Country;
import com6441.team7.risc.api.model.Player;
import com6441.team7.risc.api.model.PlayerService;
import com6441.team7.risc.api.wrapperview.PlayerDominationWrapper;

import static com6441.team7.risc.api.RiscConstants.DOMINATION_VIEW_STRING;

import java.util.ArrayList;
import java.util.Observable;

/**
 * This view displays message when occupations data of the player changes
 */
public class DominationView implements GameView {

    /**
     * extends method from GameView to receiveCommand
     */
    @Override
    public void receiveCommand() {

    }


    /**
     * extends method from GameView to displayMessage
     * @param string String
     */
    @Override
    public void displayMessage(String string) {
    	System.out.println(DOMINATION_VIEW_STRING + string);
    }


    /**
     * when the total armies, percentage of occupied countries, occupied continents of the player has been changed,
     * the update() will call and display changes to the domination view
     * @param o object o
     * @param arg argument
     */
    @Override
    public void update(Observable o, Object arg) {
    	
        if(o instanceof PlayerService){
        	
        	if(arg instanceof ArrayList) {
        		ArrayList<PlayerDominationWrapper> list=((ArrayList<PlayerDominationWrapper>)arg);
        		
        		for(PlayerDominationWrapper item:list)
        		displayMessage(item.getPlayerDominationMessage());
        		
        	}
        	
        }
        
        
        
        
    } //end of update method
    
} //end of class


