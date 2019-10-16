

package com6441.team7.risc.controller;

import static com6441.team7.risc.api.RiscConstants.WHITESPACE;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.lang3.StringUtils;

import com6441.team7.risc.api.model.Continent;
import com6441.team7.risc.api.model.Country;
import com6441.team7.risc.api.model.GameState;
import com6441.team7.risc.api.model.MapService;
import com6441.team7.risc.api.model.Player;
import com6441.team7.risc.api.model.RiscCommand;
import com6441.team7.risc.view.CommandPromptView;

/**
 * This class represents the fortification phase
 * It accepts fortification commands from user and some show commands and rejects all other commands
 * It checks whether fortification criteria are met.
 * It then carries out fortification
 * After fortification, it changes updates gamestate to reinforcement
 */
public class FortifyGameController {
	
		private MapService mapService;
		
		private StartupGameController startupGameController;
		
		private AtomicBoolean boolFortificationPhaseOver;
		
		private Country fromCountry;
		private Country toCountry;
		private Player player;
		private int num;
		private String []orders;
		GameState fortifyState;
		Set<Integer> neighbouringCountries;
		Set<Country> countryList;
		
		private CommandPromptView view;
		private boolean boolValidationMet;
		
		/*
		 * Constructor with parameters
		 */
		public FortifyGameController(Player player, MapService mapService,
										StartupGameController sgc, String cmd,
										AtomicBoolean boolFortificationPhaseOver,
										CommandPromptView v) throws IOException{
			this.mapService = mapService;
			this.player = player;
			this.startupGameController=sgc;
			this.view=v;
			
			this.boolFortificationPhaseOver=boolFortificationPhaseOver;
			this.boolValidationMet=false;
			
			readCommand(cmd);
			
			
		}
		
		public void readCommand(String command) throws IOException{
			
			this.orders=command.split("\\s+");
			
			RiscCommand commandType = RiscCommand.parse(StringUtils.split(command, WHITESPACE)[0]);
			
			switch(commandType) {
			
				case FORTIFY:
					determineFortificationAndFortify(command);
					break;
				
				case SHOW_MAP:
	                startupGameController.showMapFull();
	                break;

	            case SHOW_PLAYER:
	            	showPlayerFortificationPhase(player);
	                break;

	            case SHOW_PLAYER_ALL_COUNTRIES:
	            	showPlayerAllCountriesFortification();
	            	break;
	            
	            case SHOW_PLAYER_COUNTRIES:
	            	showPlayerCountriesFortification();
	            	break;	            
	                
	               default:
	            	   this.boolFortificationPhaseOver.set(false);
	            	   throw new IllegalArgumentException
	            	   ("Cannot recognize this command in Fortification Phase. Try Again");
			
			}

		}
			
		
		private void determineFortificationAndFortify(String command) {
		
			this.orders=command.split("\\s+");
			
			if(orders.length==2 && orders[1].equalsIgnoreCase("none")) {
				
				view.displayMessage("Fortification Phase Over.");
				
				this.mapService.setState(GameState.REINFORCE);
				this.boolFortificationPhaseOver.set(true);
				
			}
			
			else if(orders.length==4) {
				
				
				this.fromCountry = mapService.getCountryByName(orders[1]).get();
				
				this.toCountry = mapService.getCountryByName(orders[2]).get();
				
				try {
					this.num = Integer.parseInt(orders[3]);
					
				}
				catch(NumberFormatException e) {
					
					view.displayMessage("Wrong Number Format. Try Again");
				}
				
				fortify();
				
			}
			
			else {
				view.displayMessage("Invalid Fortification Command. Try Again");
				this.boolFortificationPhaseOver.set(false);
				return;
			}
						
		}

		/*
		 * 
		 */
		

		
		/*
		 * After validation comes fortifying
		 */
		public void fortify() {
			
			validateConditions();
			
			if(this.boolValidationMet) {
				
				view.displayMessage("Before Fortification: "+fromCountry.getCountryName()+":"+
						fromCountry.getSoldiers()+" , "+
						toCountry.getCountryName()+":"+toCountry.getSoldiers());
				
				toCountry.addSoldiers(num);
				fromCountry.removeSoldiers(num);
				
				view.displayMessage("After Fortification: "+fromCountry.getCountryName()+":"+
						fromCountry.getSoldiers()+" , "+
						toCountry.getCountryName()+":"+toCountry.getSoldiers());
				
				this.boolFortificationPhaseOver.set(true);
				
				this.mapService.setState(GameState.REINFORCE);
				
			}
		}
		

		/**
		 * This method checks that the following reinforcement criterias are met: 
		 * <ul>
		 * <li>Both countries are adjacent </li>
		 * <li>Both countries belong to player</li>
		 * <li>at least 1 player will remain in the source country after fortification</li>
		 * <ul>
		 */
		private boolean validateConditions() {
			
			this.boolValidationMet=true;
			
			checkCountryAdjacency();
			
			if(boolValidationMet) {
				checkCountryOwnership();
			}
						
			if(boolValidationMet) {
				checkNumSoldiers();
			}
			
			return this.boolValidationMet;
			
		}
		
		private void checkCountryAdjacency() {
			
			Map<Integer, Set<Integer>> adjacentCountriesList = mapService.getAdjacencyCountriesMap();
			
			Optional<Integer> toId = mapService.findCorrespondingIdByCountryName(toCountry.getCountryName());
			
			Optional<Integer> fromId = mapService.findCorrespondingIdByCountryName(fromCountry.getCountryName());
			
			if(!fromId.isPresent()) {
				view.displayMessage("Origin country not present");
				this.boolValidationMet=false;
			}

			
			if(!toId.isPresent()) {
				view.displayMessage("Destination country not present");
				this.boolValidationMet=false;
			}
			
			if(boolValidationMet) {
				neighbouringCountries =  adjacentCountriesList.get(fromId.get());
				
				if(!neighbouringCountries.contains(toId.get())) {
					this.boolValidationMet=false;
					view.displayMessage("Countries not adjacent to each other");
				}
			}			
			
		}
		
		private void checkCountryOwnership() {
			
			if(!(fromCountry.getPlayer().getName().equalsIgnoreCase
					(toCountry.getPlayer().getName()))) {
				view.displayMessage("Countries do not belong to same player");
				this.boolValidationMet=false;
			}
			
		}
		
		private void checkNumSoldiers() {
			
			if(!(fromCountry.getSoldiers()>num)) {
				view.displayMessage("Not enough soldiers in origin country");
				this.boolValidationMet=false;
			}
			
		}
		
	    
	    private void showPlayerFortificationPhase(Player p) {
	        Collections.sort(p.countryPlayerList, new Comparator<Country>() {

	                    @Override
	                    public int compare(Country c1, Country c2) {

	                        return c1.getContinentName().compareTo(c2.getContinentName());
	                    }

	                }
	        );

	        view.displayMessage("Current Player: "+p.getName());

	        view.displayMessage("Continent \t\t\t\t Country \t\t\t\t NumArmies");

	        for(Country c:p.countryPlayerList) {
	            view.displayMessage(c.getContinentName()+"\t\t\t"+c.getCountryName()+"\t\t\t"+c.getSoldiers());
	        }
	    }
		
	    
	    private void showPlayerReinforcementPhase(Player p) {
	        Collections.sort(p.countryPlayerList, new Comparator<Country>() {

	                    @Override
	                    public int compare(Country c1, Country c2) {

	                        return c1.getContinentName().compareTo(c2.getContinentName());
	                    }

	                }
	        );

	        view.displayMessage("Current Player: "+p.getName());

	        view.displayMessage("Continent \t\t\t\t Country \t\t\t\t NumArmies");

	        for(Country c:p.countryPlayerList) {
	            view.displayMessage(c.getContinentName()+"\t\t\t"+c.getCountryName()+"\t\t\t"+c.getSoldiers());
	        }
	    }
	    
	    private void showPlayerCountriesFortification() {
	    	Player currentPlayer=this.player;
	    	
	    	for(Map.Entry<Integer, Set<Integer>> item :
	    						mapService.getContinentCountriesMap().entrySet()) {
	    		
	    		int key=(int) item.getKey();
	    		   		
	    		
	    		Optional<Continent> optionalContinent=mapService.getContinentById(key);
	    		Continent currentContinent= (Continent) optionalContinent.get();
	    		
	    		view.displayMessage("\t\t\t\t\t\t\t\t\tContinent "+currentContinent.getName());
	    		view.displayMessage("\n");
	    		
	    		Set<Integer> value=item.getValue();
	    		
	    		for(Integer i:value) {
	    			//For Each Country In Continent, Get details + Adjacency Countries
	    			Optional<Country> optionalCountry=mapService.getCountryById(i);
	    			
	    			Country currentCountry=optionalCountry.get();
	    			
	    			if(currentCountry.getPlayer().getName().equalsIgnoreCase(currentPlayer.getName())) {
	    				
	        			String strCountryOutput="";
	        			
	        			strCountryOutput+=currentCountry.getCountryName()+":"+currentCountry.getPlayer().getName()+
	        					", "+currentCountry.getSoldiers()+" soldiers   ";
	        			
	        			Set<Integer> adjCountryList= mapService.getAdjacencyCountriesMap().get(i);
	        			
	        			for(Integer j:adjCountryList) {
	        				
	        				if(mapService.getCountryById(j).get().getPlayer().getName()
	        						.equalsIgnoreCase(currentPlayer.getName())){
	        	        				
	        	        		strCountryOutput+=" --> "+mapService.getCountryById(j).get().getCountryName()+
	        	        				"("+mapService.getCountryById(j).get().getPlayer().getName()+
	        	        				":"+mapService.getCountryById(j).get().getSoldiers()+")";
	        						}
	        				
	        			}
	        			
	        			view.displayMessage(strCountryOutput+"\n");    				
	    				
	    			}
	    			

	    		}

	    	}
	    }
	    
	    private void showPlayerAllCountriesFortification() {
	    	
	    	Player currentPlayer=player;
	    	
	    	for(Map.Entry<Integer, Set<Integer>> item :
	    						mapService.getContinentCountriesMap().entrySet()) {
	    		
	    		int key=(int) item.getKey();
	    		   		
	    		
	    		Optional<Continent> optionalContinent=mapService.getContinentById(key);
	    		Continent currentContinent= (Continent) optionalContinent.get();
	    		
	    		view.displayMessage("\t\t\t\t\t\t\t\t\tContinent "+currentContinent.getName());
	    		view.displayMessage("\n");
	    		
	    		Set<Integer> value=item.getValue();
	    		
	    		for(Integer i:value) {
	    			//For Each Country In Continent, Get details + Adjacency Countries
	    			Optional<Country> optionalCountry=mapService.getCountryById(i);
	    			
	    			Country currentCountry=optionalCountry.get();
	    			
	    			if(currentCountry.getPlayer().getName().equalsIgnoreCase(currentPlayer.getName())) {
	    				
	        			String strCountryOutput="";
	        			
	        			strCountryOutput+=currentCountry.getCountryName()+":"+currentCountry.getPlayer().getName()+
	        					", "+currentCountry.getSoldiers()+" soldiers   ";
	        			
	        			Set<Integer> adjCountryList= mapService.getAdjacencyCountriesMap().get(i);
	        			
	        			for(Integer j:adjCountryList) {
	        	        				
	        	        		strCountryOutput+=" --> "+mapService.getCountryById(j).get().getCountryName()+
	        	        				"("+mapService.getCountryById(j).get().getPlayer().getName()+
	        	        				":"+mapService.getCountryById(j).get().getSoldiers()+")";
	        						
	        				
	        			}
	        			
	        			view.displayMessage(strCountryOutput+"\n");    				
	    				
	    			}
	    			

	    		}

	    	}

	    }
	    
	    
		
}