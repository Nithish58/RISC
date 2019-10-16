

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

import com6441.team7.risc.api.model.Country;
import com6441.team7.risc.api.model.GameState;
import com6441.team7.risc.api.model.MapService;
import com6441.team7.risc.api.model.Player;
import com6441.team7.risc.api.model.RiscCommand;

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
		
		private boolean boolValidationMet;
		
		/*
		 * Constructor with parameters
		 */
		public FortifyGameController(Player player, MapService mapService,
										StartupGameController sgc, String cmd,
										AtomicBoolean boolFortificationPhaseOver) throws IOException{
			this.mapService = mapService;
			this.player = player;
			this.startupGameController=sgc;
			
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

	            case SHOW_ALL_PLAYERS:
	                startupGameController.showAllPlayers();
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
				
				System.out.println("Fortification Phase Over.");
				
				this.mapService.setState(GameState.REINFORCE);
				this.boolFortificationPhaseOver.set(true);
				
			}
			
			else if(orders.length==4) {
				
				
				this.fromCountry = mapService.getCountryByName(orders[1]).get();
				
				this.toCountry = mapService.getCountryByName(orders[2]).get();
				
				System.out.println(fromCountry.getCountryName());
				System.out.println(toCountry.getCountryName());
				
				try {
					this.num = Integer.parseInt(orders[3]);
					
				}
				catch(NumberFormatException e) {
					
					System.out.println("Wrong Number Format. Try Again");
				}
				
				fortify();
				
			}
			
			else {
				System.out.println("Invalid Fortification Command. Try Again");
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
				
				System.out.println("Before Fortification: "+fromCountry.getCountryName()+":"+
						fromCountry.getSoldiers()+" , "+
						toCountry.getCountryName()+":"+toCountry.getSoldiers());
				
				toCountry.addSoldiers(num);
				fromCountry.removeSoldiers(num);
				
				System.out.println("After Fortification: "+fromCountry.getCountryName()+":"+
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
				System.out.println("Origin country not present");
				this.boolValidationMet=false;
			}

			
			if(!toId.isPresent()) {
				System.out.println("Destination country not present");
				this.boolValidationMet=false;
			}
			
			if(boolValidationMet) {
				neighbouringCountries =  adjacentCountriesList.get(fromId.get());
				
				if(!neighbouringCountries.contains(toId.get())) {
					this.boolValidationMet=false;
					System.out.println("Countries not adjacent to each other");
				}
			}			
			
		}
		
		private void checkCountryOwnership() {
			
			if(!(fromCountry.getPlayer().getName().equalsIgnoreCase
					(toCountry.getPlayer().getName()))) {
				System.out.println("Countries do not belong to same player");
				this.boolValidationMet=false;
			}
			
		}
		
		private void checkNumSoldiers() {
			
			if(!(fromCountry.getSoldiers()>num)) {
				System.out.println("Not enough soldiers in origin country");
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

	        System.out.println("Current Player: "+p.getName());

	        System.out.println("Continent \t\t\t\t Country \t\t\t\t NumArmies");

	        for(Country c:p.countryPlayerList) {
	            System.out.println(c.getContinentName()+"\t\t\t"+c.getCountryName()+"\t\t\t"+c.getSoldiers());
	        }
	    }
		
		
		
}