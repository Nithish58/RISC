package com6441.team7.risc.api.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;

import com6441.team7.risc.api.wrapperview.PlayerFortificationWrapper;

/**
 * This is the strategy class for Benevolent players
 * 
 * @author Binsar
 *
 */
public class BenevolentStrategy implements StrategyPlayer {

	private PlayerService playerService;
	private Player player;

	public BenevolentStrategy(PlayerService playerService) {
		this.playerService = playerService;
		this.player = playerService.getCurrentPlayer();
	}

	@Override
	public void reinforce() {

		// It makes no sense to call player's calculate reinf armies method and then
		// pass it as param.
		// Just follow the format we did for attack and fortify and expose the player
		// for reinforcement
		int numArmies = player.calculateReinforcedArmies(player); // Calculate reinforcements
		int minArmy = 0; // Init local variable for minimum number of army
		int minCountryIndex = 0; // init local variable for the index of the country with the smallest num of
									// army

		Country minCountry = null;

		// Get smallest number of armies owned by the player
		for (int i = 0; i < player.getCountryList().size(); i++) {
			if (player.getCountryList().get(i).getSoldiers() < minArmy) {

				minArmy = player.getCountryList().get(i).getSoldiers();

				minCountry = player.getCountryList().get(i);
			}
		}

		// Reinforce the country with the smallest number of soldiers
		player.reinforceArmy(minCountry.getCountryName(), numArmies, playerService.getMapService());

	}

	@Override
	public void attack() {
		playerService.notifyPlayerServiceObservers("Benevolent skips attack phase");

		player.endAttackPhase(playerService);
	}

	@Override
	public void fortify() {
		// Fortification wrapper
		PlayerFortificationWrapper playerFortificationWrapper = new PlayerFortificationWrapper();

		Country weakestCountry = null;

		// get list of player's countries
		ArrayList<Country> weakCountries = player.getCountryList();

		Collections.sort(weakCountries, new Comparator<Country>() {

			@Override
			public int compare(Country c1, Country c2) {

				return c1.getSoldiers().compareTo(c2.getSoldiers());
			}

		});

		// Get smallest number of armies owned by the player
		// Print to check if it's sorted. This will be deleted
		for (Country c : weakCountries) {
			System.out.println(c.getId() + " " + c.getCountryName() + " " + c.getSoldiers());
		}

		// Iterate through the attacker countries and check for adjacency towards enemy
		// country
		Country benefactorCountry = null;

		Set<Integer> weakCountryAdjacencyList = null;

		for (Country c : weakCountries) {
			weakCountryAdjacencyList = playerService.getMapService().getAdjacencyCountries(c.getId());

			while (benefactorCountry == null) {
				for (Integer i : weakCountryAdjacencyList) {
					if (playerService.getMapService().getCountryById(i).get().getPlayer().getName()
							.equals(player.getName()) && playerService.getMapService().getCountryById(i).get().getSoldiers()>1) {

						weakestCountry = c;

						playerService
								.notifyPlayerServiceObservers("weakest country is " + weakestCountry.getCountryName()
										+ " with num of armies is " + weakestCountry.getSoldiers());

						benefactorCountry = playerService.getMapService().getCountryById(i).get();

						playerService.notifyPlayerServiceObservers(
								"benefactor country is " + benefactorCountry.getCountryName());
						break;
					}
				}
				if (benefactorCountry == null) {

					playerService.notifyPlayerServiceObservers("No benefactor country found");

					break;
				}
			}
		}

		playerService.notifyObservers("Weakest country to be fortified is " + weakestCountry.getCountryName());


		// determines whether or not to fortify
		if (benefactorCountry != null) {
			playerFortificationWrapper = new PlayerFortificationWrapper(benefactorCountry, weakestCountry, 1);
			player.fortify(playerService, playerFortificationWrapper);
		} else {
			playerFortificationWrapper = new PlayerFortificationWrapper();
			player.fortify(playerService, playerFortificationWrapper);
		}

	}

}
