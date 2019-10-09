package com6441.team7.risc.controller;

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
	
		private Player player;
		private Country fromCountry;
		private Country toCountry;
		private int num;
		GameState fortifyState;
		
		/*
		 * Constructor with parameters
		 */
		public fortifyGameController(Country fromCountry, Country toCountry, int num){
			this.fromCountry = fromCountry;
			this.toCountry = toCountry;
			this.num = num;
		}
		
		/*
		 * Skipping fortify phase
		 */
		public fortifyGameController() {
			this.fortifyState = GameState.REINFORCE;
			//Need to be reinforcement, but to next state.
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
