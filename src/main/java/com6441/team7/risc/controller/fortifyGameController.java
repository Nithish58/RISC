package com6441.team7.risc.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com6441.team7.risc.api.model.Country;
import com6441.team7.risc.api.model.GameState;
import com6441.team7.risc.api.model.MapService;
import com6441.team7.risc.api.model.Player;

/**
 * <h1>The fortify phase</>
 * This class implements fortification phase of the game
 * It takes two commands:
 * <code>fortify none</code>
 * <code>fortify fromcountry tocountry num</code>
 */
public class fortifyGameController {
	
		private MapService mapService;
		private Country fromCountry;
		private Country toCountry;
		private Player player;
		private Integer num;
		private String []orders;
		GameState fortifyState;
		Set<Integer> neighbouringCountries;
		Set<Country> countryList;
		
		/*
		 * Constructor with parameters
		 */
		public fortifyGameController(Player player, MapService mapService){
			this.mapService = mapService;
			this.player = player;
		}
		
		public void readCommand(String command) throws IOException {
			this.orders = command.split(" ");
			if (orders[1].equals(new String("none"))){
				fortifyState = GameState.REINFORCE;
			} else {
				this.fromCountry = mapService.getCountryByName(orders[1]).get();
				this.toCountry = mapService.getCountryByName(orders[2]).get();
				this.num = Integer.parseInt(orders[3]);
			}
		}
		
		/*
		 * 
		 */
		public void validation() {
			/*
			 * Validity checks: 
			 * -Game State
			 * -Both countries are adjacent 
			 * -Both countries belong to player
			 * -minimum num of armies/soldiers in source country
			 */
			
			
			// Is the exception correct? 
			try {
				assert(fortifyState.getName() == GameState.FORTIFY.toString());

			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("Not right Game state");
			}
			
			try {
				Map<Integer, Set<Integer>> adjacentCountriesList = mapService.getAdjacencyCountriesMap();
				Optional<Integer> toId = mapService.findCorrespondingIdByCountryName(toCountry.toString());
				Optional<Integer> fromId = mapService.findCorrespondingIdByCountryName(fromCountry.toString());
				neighbouringCountries =  adjacentCountriesList.get(fromId);
				assert(neighbouringCountries.contains(toId));
			} catch (Exception e) {
				System.out.println("Countries not adjacent to each other");
			}
			
			
			try {
				assert(fromCountry.getPlayer()==toCountry.getPlayer());
			} catch (Exception e) {
				System.out.println("Check if both are same player's countries");
			}
			
			try {
				assert(fromCountry.getSoldiers()>1);
			} catch (Exception e) {
				System.out.println("Not enough soldiers in source country");
			}
		}
		
		/*
		 * After validation comes fortifying
		 */
		public void fortify() {
			validation();
			toCountry.setSoldiers(fromCountry.getSoldiers() - num);
		}

}