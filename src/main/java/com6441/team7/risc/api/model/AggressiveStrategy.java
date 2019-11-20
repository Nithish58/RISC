package com6441.team7.risc.api.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

import com6441.team7.risc.api.wrapperview.PlayerAttackWrapper;
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

		//get country with maximum number of armies
		Country maxCountry = findMaxCountry();
		
		//Reinforce the country with the largest number of soldiers
		player.reinforceArmy(maxCountry.getCountryName(), numArmies, playerService.getMapService());
	}
	
	@Override
	public void attack() {
		playerService.notifyPlayerServiceObservers("Attack Aggressive");
		
		//get country with maximum number of armies
		Country attackerCountry = findMaxCountry();
		
		//get countries adjacent to maxCountry to be attacked
		//if no adjacent country found, invoke attack noattack
		Set<Integer> attackerCountryAdjacencyList = playerService.getMapService()
				.getAdjacencyCountries(attackerCountry.getId());
		Country defenderCountry = null;
		while (defenderCountry == null) {
			for (Integer i : attackerCountryAdjacencyList) {
				if (!playerService.getMapService().getCountryById(i).get().getPlayer().getName().equals(player.getName())) {
					defenderCountry = playerService.getMapService().getCountryById(i).get();
					playerService.notifyPlayerServiceObservers("defender country is " + defenderCountry.getCountryName());
					break;
				}
			}
			if (defenderCountry == null) {
				break;
			}
		}
		
		//Attack the defender country
		//Attack wrapper
		PlayerAttackWrapper playerAttackWrapper = new PlayerAttackWrapper(attackerCountry, defenderCountry);
		
		//Set allout to true
		playerAttackWrapper.setBooleanAllOut();
		
		//Call the attack function
		player.attack(playerService, playerAttackWrapper);
		
		// If attacker is left with one soldier, stop the attack
		if (player.isAttackerLastManStanding()) {
			player.endAttackPhase(playerService);
		} 
		// If the defender is pushed out of the country, do attackmove and then endAttackPhase
		else if (player.defenderPushedOut()) {
			player.attackMove(1);
			player.endAttackPhase(playerService);
		}
	}
	
	@Override
	public void fortify() {
		//Fortification wrapper
		PlayerFortificationWrapper playerFortificationWrapper = new PlayerFortificationWrapper();
		
		//search adjacent countries that belong the same player
		int maxArmy = 0; //Init local variable for maximum number of army
		int maxCountryIndex = 0; //init local variable for the index of the country with the largest num of army
		
		//Get largest number of armies owned by the player
		//get country with maximum number of armies
		Country maxCountry = findMaxCountryForFortification();
		
		//Get adjacency list of the country with max num of soldiers
		Set<Integer> maxCountryAdjacencyList = playerService.getMapService()
				.getAdjacencyCountries(maxCountry.getId());
		
		//Get adjacent country that is owned by the player
		//if no adjacent country found, invoke fortifynone
		Country neighborMaxCountry = null;
		while (neighborMaxCountry == null) {
			for (Integer i : maxCountryAdjacencyList) {
				if (playerService.getMapService().getCountryById(i).get().getPlayer().getName().equals(player.getName())) {
					neighborMaxCountry = playerService.getMapService().getCountryById(i).get();
					playerService.notifyPlayerServiceObservers("Owned neighbor country is " + neighborMaxCountry.getCountryName());
					break;
				}
			}
			if (neighborMaxCountry == null) {
				break;
			}
		}
		
		//determines whether or not to fortify
		if (neighborMaxCountry != null && maxCountry.getSoldiers()>1) {
			playerFortificationWrapper.setFromCountry(maxCountry);
			playerFortificationWrapper.setToCountry(neighborMaxCountry);
			playerFortificationWrapper.setNumSoldiers(maxCountry.getSoldiers()-1);
			playerFortificationWrapper.setBooleanFortificationNone(false);
			player.fortify(playerService, playerFortificationWrapper);
		}
		else {
			player.fortifyNone(playerService);
		}
		
	}
	
	public Country findMaxCountry() {
		int maxArmy = 0; //Init local variable for maximum number of army
		int maxCountryIndex = 0; //init local variable for the index of the country with the largest num of army
		
		//initialize attacker's country
		
		//Get country with max num of soldiers using maxCountryIndex
		Country maxCountry = null;
		
		//initialize defenders' country
		Country defenderCountry = null;
		
		//initialize adjacency list for attacker's country
		Set<Integer> maxCountryAdjacencyList;
		
		//Get largest number of armies owned by the player
		for (int i = 0; i < player.getCountryList().size(); i++) {
			if (player.getCountryList().get(i).getSoldiers() > maxArmy) {
				maxArmy = player.getCountryList().get(i).getSoldiers();
				//get countries adjacent to maxCountry to be attacked
				//if no adjacent country found, invoke attack noattack
				maxCountryAdjacencyList = playerService.getMapService()
						.getAdjacencyCountries(player.getCountryList().get(i).getId());
				while (defenderCountry == null) {
					for (Integer index : maxCountryAdjacencyList) {
						if (!playerService.getMapService().getCountryById(index).get().getPlayer().getName().equals(player.getName())) {
							//set attacker's country
							maxCountry = player.getCountryList().get(i);
							playerService.notifyPlayerServiceObservers("Attacker country is "+maxCountry);
							defenderCountry = playerService.getMapService().getCountryById(index).get();
							playerService.notifyPlayerServiceObservers("Defender country is " + defenderCountry.getCountryName());
							break;
						}
					}
					if (defenderCountry == null) {
						break;
					}
				}
			}
		}
		
		return maxCountry;
	}
	
	public Country findMaxCountryForFortification() {
		int maxArmy = 0; //Init local variable for maximum number of army
		int maxCountryIndex = 0; //init local variable for the index of the country with the largest num of army
		
		//initialize attacker's country
		
		//Get country with max num of soldiers using maxCountryIndex
		Country maxCountry = null;
		
		//Get largest number of armies owned by the player
		for (int i = 0; i < player.getCountryList().size(); i++) {
			if (player.getCountryList().get(i).getSoldiers() > maxArmy) {
				maxArmy = player.getCountryList().get(i).getSoldiers();
				maxCountryIndex = i;
			}
		}
		
		//Get country with max num of soldiers using maxCountryIndex
		maxCountry = player.getCountryList().get(maxCountryIndex);
		
		return maxCountry;
	}
	
}
