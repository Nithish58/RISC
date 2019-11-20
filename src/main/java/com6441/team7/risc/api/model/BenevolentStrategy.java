package com6441.team7.risc.api.model;

import java.util.Set;

import com6441.team7.risc.api.wrapperview.PlayerFortificationWrapper;

/**
 * This is the strategy class for Benevolent players
 * @author Binsar
 *
 */
public class BenevolentStrategy implements StrategyPlayer{

	private PlayerService playerService;
	private Player player;
	
	public BenevolentStrategy(PlayerService playerService) {
		this.playerService = playerService;
		this.player = playerService.getCurrentPlayer();
	}
	
	@Override
	public void reinforce() {
		
		//It makes no sense to call player's calculate reinf armies method and then pass it as param.
		//Just follow the format we did for attack and fortify and expose the player for reinforcement
		int numArmies = player.calculateReinforcedArmies(player); //Calculate reinforcements
		int minArmy = 0; //Init local variable for minimum number of army
		int minCountryIndex = 0; //init local variable for the index of the country with the smallest num of army
		//Get smallest number of armies owned by the player
		for (int i = 0; i < player.getCountryList().size(); i++) {
			if (player.getCountryList().get(i).getSoldiers() < minArmy) {
				minArmy = player.getCountryList().get(i).getSoldiers();
				minCountryIndex = i;
			}
		}
		//Get country with min num of soldiers using maxCountryIndex
		Country minCountry = player.getCountryList().get(minCountryIndex);
		//Reinforce the country with the smallest number of soldiers
		player.reinforceArmy(minCountry.getCountryName(), numArmies, playerService.getMapService());
		
	}
	
	@Override
	public void attack() {
		playerService.notifyPlayerServiceObservers("Benevolent skips attack phase");		
	}
	
	@Override
	public void fortify() {
		//Fortification wrapper
		PlayerFortificationWrapper playerFortificationWrapper = new PlayerFortificationWrapper();
		
		//search adjacent countries that belong the same player
		int minArmy = 999999; //Init local variable for maximum number of army
		int minCountryIndex = 0; //init local variable for the index of the country with the largest num of army
		//Get largest number of armies owned by the player
		for (int i = 0; i < player.getCountryList().size(); i++) {
			if (player.getCountryList().get(i).getSoldiers() < minArmy) {
				minArmy = player.getCountryList().get(i).getSoldiers();
				minCountryIndex = i;
			}
		}
		
		//Get country with max num of soldiers using maxCountryIndex
		Country minCountry = player.getCountryList().get(minCountryIndex);
		//Get adjacency list of the country with min num of soldiers
		Set<Integer> minCountryAdjacencyList = playerService.getMapService()
				.getAdjacencyCountries(minCountry.getId());
		
		//Get adjacent country that is owned by the player
		//if no adjacent country found, invoke fortifynone
		Country neighborMinCountry = null;
		while (neighborMinCountry == null) {
			for (Integer i : minCountryAdjacencyList) {
				if (playerService.getMapService().getCountryById(i).get().getPlayer().getName().equals(player.getName())) {
					neighborMinCountry = playerService.getMapService().getCountryById(i).get();
					if (neighborMinCountry.getSoldiers()>1) {
					System.out.println("Owned neighbor country is " + neighborMinCountry.getCountryName());
					break;
					}
				}
			}
			if (neighborMinCountry == null) {
				break;
			}
		}
		
		//determines whether or not to fortify
		if (neighborMinCountry != null ) {
			playerFortificationWrapper.setFromCountry(neighborMinCountry);
			playerFortificationWrapper.setToCountry(minCountry);
			playerFortificationWrapper.setNumSoldiers(neighborMinCountry.getSoldiers()-1);
			playerFortificationWrapper.setBooleanFortificationNone(false);
			player.fortify(playerService, playerFortificationWrapper);
		}
		else {
			player.fortifyNone(playerService);
		}

		
	}
	
}
