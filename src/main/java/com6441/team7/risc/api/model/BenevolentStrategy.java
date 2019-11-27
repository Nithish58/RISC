package com6441.team7.risc.api.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com6441.team7.risc.api.wrapperview.PlayerFortificationWrapper;

/**
 * This is the strategy class for Benevolent players. Implements from 
 * {@link StrategyPlayer}
 * 
 * @author Binsar
 *
 */
public class BenevolentStrategy implements StrategyPlayer {

	/**
	 * player service for specific player functions
	 */
	@JsonIgnore
	private PlayerService playerService;
	
	/**
	 * Player object for player list and details.
	 */
	@JsonIgnore
	private Player player;

	/**
	 * A default constructor for benevolent strategy
	 */
	public BenevolentStrategy() {}
	
	/**
	 * {@link BenevolentStrategy} class constructor 
	 * @param playerService PlayerService to be passed for details.
	 */
	public BenevolentStrategy(PlayerService playerService) {
		this.playerService = playerService;
		this.player = playerService.getCurrentPlayer();
		this.playerService.notifyPlayerServiceObservers("Benevolent Strategy");
	}

	/**
	 * In reinforcement phase, benevolent player protects its weakest country.
	 */
	@Override
	public void reinforce() {

		//Check And Exchange Cards
		player.checkAndExchangeCardsForStrategy(playerService);
		
		//Then Calculate Total Num Armies
		int numReinforcementArmies=player.calculateReinforcedArmiesBasedOnCardsContinentsCountries(playerService);

		// get list of player's countries and sort it
		ArrayList<Country> weakCountries = player.getCountryPlayerList();

		Collections.sort(weakCountries, new Comparator<Country>() {

			@Override
			public int compare(Country c1, Country c2) {

				return c1.getSoldiers().compareTo(c2.getSoldiers());
			}

		});
		
		Country weakestCountry = weakCountries.get(0);
		
		playerService.notifyPlayerServiceObservers(weakestCountry.getCountryName()+" has "+weakestCountry.getSoldiers()+ " soldier(s)"
				+ " and will receive "+numReinforcementArmies+" reinforcement(s)");

		//Reinforce country with smallest num soldiers
		playerService.reinforceArmy(player, weakestCountry.getCountryName(), numReinforcementArmies);
		
		//End Fortification and Move to Attack Phase
		playerService.getMapService().setState(GameState.ATTACK);

	}

	/**
	 * In attack phase, benevolent player never attacks
	 */
	@Override
	public void attack() {
		playerService.notifyPlayerServiceObservers("Benevolent ends attack phase");

		player.endAttackPhase(playerService);
	}

	/**
	 * In fortification phase, benevolent player fortifies its current weakest that can be fortified country and moves to next phase.
	 */
	@Override
	public void fortify() {
		// Fortification wrapper
		PlayerFortificationWrapper playerFortificationWrapper = new PlayerFortificationWrapper();

		Country weakestCountry = null;

		// get list of player's countries
		ArrayList<Country> weakCountries = player.getCountryPlayerList();

		Collections.sort(weakCountries, new Comparator<Country>() {

			@Override
			public int compare(Country c1, Country c2) {

				return c1.getSoldiers().compareTo(c2.getSoldiers());
			}

		});

		// Get smallest number of armies owned by the player
		// Print to check if it's sorted.
		playerService.notifyPlayerServiceObservers("Countries with numSoldiers sorted in ascending order:");
		for (Country c : weakCountries) {
			playerService.notifyPlayerServiceObservers(c.getCountryName() + " " + c.getSoldiers()+" soldiers");
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

					playerService.notifyPlayerServiceObservers("No benefactor country found for "+c.getCountryName());

					break;
				}
			}
		}


		// determines whether or not to fortify
		if (benefactorCountry != null) {
			playerService.notifyObservers("Weakest country to be fortified is " + weakestCountry.getCountryName());
			
			playerFortificationWrapper = new PlayerFortificationWrapper(benefactorCountry, weakestCountry, 1);
			
			player.fortify(playerService, playerFortificationWrapper);
		} else {
			playerFortificationWrapper = new PlayerFortificationWrapper();
			
			player.fortify(playerService, playerFortificationWrapper);
		}

	}
	
	/**
	 * This is for calculating benevolent country's reinforcements
	 * @param player receives player whose reinforcement armies need to be calculated.
	 * @return number of calculated reinforced armies
	 */
    public int calculateReinforcedBenevolentArmies(Player player){

    	int reinforcedArmies = 0;
    	
        reinforcedArmies += playerService.getConqueredCountriesNumber(player)/3;

        reinforcedArmies += playerService.getReinforcedArmyByConqueredContinents(player);

        if(reinforcedArmies < 3){ reinforcedArmies = 3; }

        return reinforcedArmies;
    }


	/**
	 * get reference of playerService
	 * @return playService
	 */
	@JsonIgnore
	public PlayerService getPlayerService() {
		return playerService;
	}

	/**
	 * set value of playerService
	 * @param playerService stores player information
	 */
	@JsonIgnore
	public void setPlayerService(PlayerService playerService) {
		this.playerService = playerService;
	}

	/**
	 * get the object of player
	 * @return player
	 */
	@JsonIgnore
	public Player getPlayer() {
		return player;
	}


	/**
	 * set the player
	 * @param player player
	 */
	@JsonIgnore
	public void setPlayer(Player player) {
		this.player = player;
	}
}
