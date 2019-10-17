

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
	/**
	 * MapService objects
	 */
	private MapService mapService;

	/**
	 * StartupGameController objects
	 */
	private StartupGameController startupGameController;

	/**
	 * to check fortification phase gets over
	 */
	private AtomicBoolean boolFortificationPhaseOver;

	/**
	 * Country from where fortification is start
	 */
	private Country fromCountry;

	/**
	 * country to where fortification is done
	 */
	private Country toCountry;

	/**
	 * player whose fortification is in process
	 */
	private Player player;

	/**
	 * number
	 */
	private int num;

	/**
	 *  strings arrays in orders
	 */
	private String []orders;
		GameState fortifyState;
		Set<Integer> neighbouringCountries;
		Set<Country> countryList;

	/**
	 * command Prompt view object
	 */
	private CommandPromptView view;

	/**
	 * check validation is met or not
	 */
	private boolean boolValidationMet;
		
	/**
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

	/**
	 * read command from user, if it is fortify, call determineFortificationAndFortify method
	 * if the command is showMap, call showMapFull()
	 * if the command is showPlayer, call showPlayerFortificationPhase method
	 * if the command is showPlayerAllCountries, call showPlayerAllCountriesFortification();
	 * else throw exception
	 * @param command
	 * @throws IOException
	 */
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

	/**
	 * validate the command, if the command format is valid, check the command type
	 * if the command is none, do nothing and exit reinforce phase
	 * if the command is country and num, validat the country info and soldier info,
	 * move the soldier from these two countries
	 * @param command
	 */
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

		
		/**
		 * After validation comes fortifying
		 * show before and after fortification information of country and soldiers
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

	/**
	 * check country has Adjacency
	 */
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

	/**
	 * check the country owned by the current player
	 */
	private void checkCountryOwnership() {
			
			if(!(fromCountry.getPlayer().getName().equalsIgnoreCase
					(toCountry.getPlayer().getName()))) {
				view.displayMessage("Countries do not belong to same player");
				this.boolValidationMet=false;
			}
			
		}

	/**
	 * check the number of soldiers for the current player
	 */
	private void checkNumSoldiers() {
			
			if(!(fromCountry.getSoldiers()>num)) {
				view.displayMessage("Not enough soldiers in origin country");
				this.boolValidationMet=false;
			}
			
		}

	/**
	 * show current player information, the countries it occupies and corresponding soldiers
	 * @param p player
	 */
	private void showPlayerFortificationPhase(Player p) {
	        Collections.sort(p.countryPlayerList, new Comparator<Country>() {

	                    @Override
	                    public int compare(Country c1, Country c2) {

	                        return c1.getContinentName().compareTo(c2.getContinentName());
	                    }

	                }
	        );

	        view.displayMessage("Current Player: "+p.getName());

	        for(Country c:p.countryPlayerList) {
	            view.displayMessage(c.getContinentName()+"\t"+c.getCountryName()+"\t"+c.getSoldiers());
	        }
	    }
		


	/**
	 * show current player and information of all countries player owns
	 *
	 */
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
	        			
	        			strCountryOutput+=currentCountry.getCountryName().toUpperCase()+":"+currentCountry.getPlayer().getName().toUpperCase()+
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


	/**
	 * show players and all countries in fortification phase
	 */
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
	        			
	        			strCountryOutput+=currentCountry.getCountryName().toUpperCase()+":"+currentCountry.getPlayer().getName().toUpperCase()+
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