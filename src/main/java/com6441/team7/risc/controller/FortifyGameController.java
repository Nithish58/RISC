

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

import com6441.team7.risc.utils.MapDisplayUtils;
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
 * After fortification, it updates gamestate to reinforcement
 */
public class FortifyGameController {
	/**
	 * MapService reference
	 */
	private MapService mapService;

	/**
	 * StartupGameController reference
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
	 * number of soldiers entered by user
	 */
	private int numSoldiers;

	/**
	 *  strings arrays in orders
	 */
	private String []orders;
	/**
	 * Set that keeps track of neighbouring countries of origin country.
	 */
	private	Set<Integer> neighbouringCountries;
	/**
	 * set of countries
	 */
	private	Set<Country> countryList;

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
		public FortifyGameController(MapService mapService,
										StartupGameController sgc,
										AtomicBoolean boolFortificationPhaseOver,
										CommandPromptView v) throws IOException{
			this.mapService = mapService;
			this.startupGameController=sgc;
			this.view=v;
			this.boolFortificationPhaseOver=boolFortificationPhaseOver;
			this.boolValidationMet=false;
		}

	/**
	 * reads command from user.
	 * <li>if it is fortify, call determineFortificationAndFortify method</li>
	 * <li>if the command is showMap, call showMapFull()</li>
	 * <li>if the command is showPlayer, call showPlayerFortificationPhase method</li>
	 * <li>if the command is showPlayerAllCountries, call showPlayerAllCountriesFortification();</li>
	 * else any other command type is invalidated
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
	            	
	            case EXIT:
	            	endGame();
	            	break;
	                
	               default:
	            	   this.boolFortificationPhaseOver.set(false);
	            	   throw new IllegalArgumentException
	            	   ("Cannot recognize this command in Fortification Phase. Try Again");
			
			}

		}

	/**
	 * This method determines type of fortification:
	 * <li> It validates the command: if the command format is valid, it check the command type</li>
	 * <li>if the command is none, it exits reinforce phase</li>
	 * <li>if the command is valid, validate the country info and soldier info, and move the soldier from these two countries</li>
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
					this.numSoldiers = Integer.parseInt(orders[3]);
					
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
		 * check validation criteria first
		 */
		public void fortify() {
			
			validateConditions();
			
			if(this.boolValidationMet) {
				
				view.displayMessage("Before Fortification: "+fromCountry.getCountryName()+":"+
						fromCountry.getSoldiers()+" , "+
						toCountry.getCountryName()+":"+toCountry.getSoldiers());
				
				toCountry.addSoldiers(numSoldiers);
				fromCountry.removeSoldiers(numSoldiers);
				
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
	 * checks whether the 2 countrie are owned by the current player
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
	 * Ensures that at least 1 soldier remains in origin country
	 */
	private void checkNumSoldiers() {
			
			if(!(fromCountry.getSoldiers()>numSoldiers && numSoldiers>0)) {
				view.displayMessage("Not enough soldiers in origin country");
				this.boolValidationMet=false;
			}
			
		}

	/**
	 * show current player information, the countries it occupies and corresponding soldiers
	 * @param current player
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
			MapDisplayUtils.showCurrentPlayerMap(mapService, view, player);
	    }
	    
		/**
		 * end the game
		 * called when only 1 player is present.
		 */
		private void endGame() {
	    	view.displayMessage("Game Ends");
	    	System.exit(0);
	    }


	/**
	 * show players and all countries in fortification phase
	 */
	    private void showPlayerAllCountriesFortification() {
	    	MapDisplayUtils.showCurrentPlayerMap(mapService, view, player);
	    }
	    
	    
		
}