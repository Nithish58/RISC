package com6441.team7.risc.api.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;

import com6441.team7.risc.api.wrapperview.PlayerAttackWrapper;
import com6441.team7.risc.api.wrapperview.PlayerFortificationWrapper;

/**
 * This is the strategy class for Aggressive players
 * 
 * @author Binsar
 *
 */
public class AggressiveStrategy implements StrategyPlayer {

	private PlayerService playerService;
	private Player player;

	public AggressiveStrategy(PlayerService playerService) {
		this.playerService = playerService;
		this.player = playerService.getCurrentPlayer();
	}

	/**
	 * In reinforcement phase, aggressive player pick the country with the largest
	 * number of armies to be reinforced
	 */
	@Override
	public void reinforce() {

		// It makes no sense to call player's calculate reinf armies method and then
		// pass it as param.
		// Just follow the format we did for attack and fortify and expose the player
		// for reinforcement
		int numArmies = player.calculateReinforcedArmies(player); // Calculate reinforcements

		// get country with maximum number of armies
		Country maxCountry = findMaxCountry();

		// Reinforce the country with the largest number of soldiers
		player.reinforceArmy(maxCountry.getCountryName(), numArmies, playerService.getMapService());
	}

	@Override
	public void attack() {
		playerService.notifyPlayerServiceObservers("Attack Aggressive");

		// get country with maximum number of armies
		Country attackerCountry = null;

		// get list of player's countries
		ArrayList<Country> attackerCountries = player.getCountryList();

		Collections.sort(attackerCountries, new Comparator<Country>() {

			@Override
			public int compare(Country c1, Country c2) {

				return c2.getSoldiers().compareTo(c1.getSoldiers());
			}

		});
		
		//Print to check if it's sorted. This will be deleted
		for(Country c : attackerCountries) {
			System.out.println(c.getId()+" "+c.getCountryName()+" "+c.getSoldiers());
		}

		//Iterate through the attacker countries and check for adjacency towards enemy country
		Country defenderCountry = null;
		
		Set<Integer> attackerCountryAdjacencyList = null;
		
		for(Country c : attackerCountries) {
			attackerCountryAdjacencyList = playerService.getMapService()
			.getAdjacencyCountries(c.getId());
			
			while (defenderCountry == null) {
				for (Integer i : attackerCountryAdjacencyList) {
					if (!playerService.getMapService().getCountryById(i).get().getPlayer().getName()
							.equals(player.getName())) {
						
						attackerCountry = c;
						
						playerService.notifyPlayerServiceObservers(
								"attacker country is " + attackerCountry.getCountryName()+
								" with num of armies is " +attackerCountry.getSoldiers());
						
						defenderCountry = playerService.getMapService().getCountryById(i).get();
						
						playerService.notifyPlayerServiceObservers(
								"defender country is " + defenderCountry.getCountryName());
						break;
					}
				}
				if (defenderCountry == null) {
					
					playerService.notifyPlayerServiceObservers("No defender country found");
					
					break;
				}
			}
		}
		
		// check if no attackerCountry with max num of soldiers and enemy country in the
		// adjacency list found
		// if it's null, end attack phase and switch to next player
		if (defenderCountry == null)
			player.endAttackPhase(playerService);

		else {

			// Attack the defender country
			// Attack wrapper
			PlayerAttackWrapper playerAttackWrapper = new PlayerAttackWrapper(attackerCountry, defenderCountry);

			// Set allout to true
			playerAttackWrapper.setBooleanAllOut();

			// Check if attacker only has one soldier left in the country
			if (attackerCountry.getSoldiers() > 1) {
				// Call the attack function
				player.attack(playerService, playerAttackWrapper);

				// If attacker is left with one soldier, stop the attack
				if (player.isAttackerLastManStanding()) {
					player.endAttackPhase(playerService);
				}
				// If the defender is pushed out of the country, do attackmove and then
				// endAttackPhase
				else if (player.getBoolAttackMoveRequired()) {
					player.attackMove(1);
					player.endAttackPhase(playerService);
				}
			} else {
				player.endAttackPhase(playerService);
			}
		}
	}

	@Override
	public void fortify() {
		// Fortification wrapper
		PlayerFortificationWrapper playerFortificationWrapper;

		// search adjacent countries that belong the same player
		int maxArmy = 0; // Init local variable for maximum number of army
		int maxCountryIndex = 0; // init local variable for the index of the country with the largest num of army

		// Get largest number of armies owned by the player
		// get country with maximum number of armies
		Country maxCountry = findMaxCountry();

		if (maxCountry == null) {
			playerFortificationWrapper = new PlayerFortificationWrapper();
			player.fortify(playerService, playerFortificationWrapper);
		}

		else {
			// Get adjacency list of the country with max num of soldiers
			Set<Integer> maxCountryAdjacencyList = playerService.getMapService()
					.getAdjacencyCountries(maxCountry.getId());

			// Get adjacent country that is owned by the player
			// if no adjacent country found, invoke fortifynone

			boolean neighborCountryFound = false;

			Country neighborMaxCountry = null;

			for (Integer i : maxCountryAdjacencyList) {
				if (playerService.getMapService().getCountryById(i).get().getPlayer().getName()
						.equals(player.getName())) {
					playerService.notifyPlayerServiceObservers("From Country " + maxCountry.getCountryName());
					neighborMaxCountry = playerService.getMapService().getCountryById(i).get();
					playerService.notifyPlayerServiceObservers(
							"To neighbor country is " + neighborMaxCountry.getCountryName());
					neighborCountryFound = true;
					break;
				}
			}

			if (!neighborCountryFound) {
				playerFortificationWrapper = new PlayerFortificationWrapper();
				player.fortify(playerService, playerFortificationWrapper);
				return;
			}

			// determines whether or not to fortify
			if (neighborMaxCountry != null && maxCountry.getSoldiers() > 1) {
				playerFortificationWrapper = new PlayerFortificationWrapper(maxCountry, neighborMaxCountry, 1);
				player.fortify(playerService, playerFortificationWrapper);
			} else {
				playerFortificationWrapper = new PlayerFortificationWrapper();
				player.fortify(playerService, playerFortificationWrapper);
			}
		}
	}


	public Country findMaxCountry() {
		int maxArmy = 0; // Init local variable for maximum number of army
		int maxCountryIndex = 0; // init local variable for the index of the country with the largest num of army

		// initialize attacker's country

		// Get country with max num of soldiers using maxCountryIndex
		Country maxCountry = null;

		// Get largest number of armies owned by the player
		for (int i = 0; i < player.getCountryList().size(); i++) {
			if (player.getCountryList().get(i).getSoldiers() > maxArmy) {
				maxArmy = player.getCountryList().get(i).getSoldiers();
				// Get country with max num of soldiers using maxCountryIndex
				maxCountry = player.getCountryList().get(i);
			}
		}

		return maxCountry;
	}

}
