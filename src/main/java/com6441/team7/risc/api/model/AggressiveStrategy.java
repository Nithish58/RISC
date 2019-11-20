package com6441.team7.risc.api.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

import com6441.team7.risc.api.wrapperview.PlayerFortificationWrapper;

/**
 * This is the strategy class for Aggressive players
 * @author Binsar
 *
 */
public class AggressiveStrategy implements StrategyPlayer{

	private PlayerService playerService;
	private Player player;
	
	public AggressiveStrategy(PlayerService playerService) {
		this.playerService = playerService;
		this.player = playerService.getCurrentPlayer();
	}
	
	/**
	 * In reinforcement phase, aggressive player pick the country with the largest number of armies to be reinforced
	 */
	@Override
	public void reinforce() {
		
		//It makes no sense to call player's calculate reinf armies method and then pass it as param.
		//Just follow the format we did for attack and fortify and expose the player for reinforcement
		int numArmies = player.calculateReinforcedArmies(player); //Calculate reinforcements
		int maxArmy = 0; //Init local variable for maximum number of army
		int maxCountryIndex = 0; //init local variable for the index of the country with the largest num of army
		//Get largest number of armies owned by the player
		for (int i = 0; i < player.getCountryList().size(); i++) {
			if (player.getCountryList().get(i).getSoldiers() > maxArmy) {
				maxArmy = player.getCountryList().get(i).getSoldiers();
				maxCountryIndex = i;
			}
		}
		//Get country with max num of soldiers using maxCountryIndex
		Country maxCountry = player.getCountryList().get(maxCountryIndex);
		//Reinforce the country with the largest number of soldiers
		player.reinforceArmy(maxCountry.getCountryName(), numArmies, playerService.getMapService(), playerService);
	}
	
	@Override
	public void attack() {
		System.out.println("Attack Aggressive");
		
		
	}
	
	@Override
	public void fortify() {
		//Fortification wrapper
		PlayerFortificationWrapper playerFortificationWrapper = new PlayerFortificationWrapper();
		
		//search adjacent countries that belong the same player
		int maxArmy = 0; //Init local variable for maximum number of army
		int maxCountryIndex = 0; //init local variable for the index of the country with the largest num of army
		//Get largest number of armies owned by the player
		for (int i = 0; i < player.getCountryList().size(); i++) {
			if (player.getCountryList().get(i).getSoldiers() > maxArmy) {
				maxArmy = player.getCountryList().get(i).getSoldiers();
				maxCountryIndex = i;
			}
		}
		
		//Get country with max num of soldiers using maxCountryIndex
		Country maxCountry = player.getCountryList().get(maxCountryIndex);
		//Get adjacency list of the country iwth max num of soldiers
		Set<Integer> maxCountryAdjacencyList = playerService.getMapService()
				.getAdjacencyCountries(maxCountry.getId());
		
		//Get adjacent country that is owned by the player
		//if no adjacent country found, invoke fortifynone
		Country neighborMaxCountry = null;
		while (neighborMaxCountry == null) {
			for (Integer i : maxCountryAdjacencyList) {
				if (playerService.getMapService().getCountryById(i).get().getPlayer().getName().equals(player.getName())) {
					neighborMaxCountry = playerService.getMapService().getCountryById(i).get();
					if (neighborMaxCountry.getSoldiers()>1) {
					System.out.println("Owned neighbor country is " + neighborMaxCountry.getCountryName());
					break;
					}
				}
			}
			if (neighborMaxCountry == null) {
				break;
			}
		}
		
		//determines whether or not to fortify
		if (neighborMaxCountry != null ) {
			playerFortificationWrapper.setFromCountry(maxCountry);
			playerFortificationWrapper.setToCountry(neighborMaxCountry);
			playerFortificationWrapper.setNumSoldiers(neighborMaxCountry.getSoldiers()-1);
			player.fortify(playerService, playerFortificationWrapper);
		}
		else {
			player.fortifyNone(playerService);
		}
		
	}
	
}
