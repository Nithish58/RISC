package com6441.team7.risc.view;

import com6441.team7.risc.api.model.Country;
import com6441.team7.risc.api.model.Player;
import com6441.team7.risc.api.model.PlayerService;
import static com6441.team7.risc.api.RiscConstants.DOMINATION_VIEW_STRING;


import java.util.Observable;

public class DominationView implements GameView {
    @Override
    public void receiveCommand() {

    }

    @Override
    public void displayMessage(String string) {
    	System.out.println(DOMINATION_VIEW_STRING + string);
    }

    
    @Override
    public void update(Observable o, Object arg) {
    	
        if(o instanceof PlayerService){

            //TODO: o.getTotalArmies();
            //TODO: o.getOccupiedCountriesPercentage();
            //TODO: o.getOccupiedContinents();
            //TODO: call displayMessge to display messages
        }
        
        
        
        
    } //end of update method
    
} //end of class







/*
* if(o instanceof Player) {
* 
* //Player returned as arg when numArmies updated if(arg instanceof Player) {
* String playerName=((Player) arg).getName(); int numArmies=((Player)
* arg).getArmies();
* System.out.println(playerName+" has "+numArmies+" armies."); return; }
* 
* //Country returned as arg when country assigned to player if(arg instanceof
* Country) { String countryName=((Country) arg).getCountryName(); String
* playerName=((Country) arg).getPlayer().getName();
* 
* System.out.println(playerName+" now owns "+countryName); return; }
* 
* }
* 
* 
* if(o instanceof Country) {
* 
* //Country returned as arg when numSoldiers updated if(arg instanceof Country)
* { String countryName=((Country) arg).getCountryName(); int
* numSoldiers=((Country) arg).getSoldiers();
* 
* System.out.println(countryName+" has "+numSoldiers+" soldiers."); }
* 
* }

*/


