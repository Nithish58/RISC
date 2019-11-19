package com6441.team7.risc.api.model;

import java.util.Set;

import com6441.team7.risc.api.wrapperview.PlayerFortificationWrapper;

public class RandomStrategy implements StrategyPlayer{
	
	private PlayerService playerService;
	private Player player;
	
	public RandomStrategy(PlayerService playerService) {
		this.playerService = playerService;
		this.player = playerService.getCurrentPlayer();
	}
	
	@Override
	public void reinforce() {
		
		//It makes no sense to call player's calculate reinf armies method and then pass it as param.
		//Just follow the format we did for attack and fortify and expose the player for reinforcement
		int numArmies = player.calculateReinforcedArmies(player);
		
		Country randomCountry = player.getCountryList().get(0);
		
		player.reinforceArmy(randomCountry.getCountryName(), numArmies, playerService.getMapService());
		
		
	}
	
	@Override
	public void attack() {
		System.out.println("Attack");
		
		
	}
	
	@Override
	public void fortify() {
		
		Country fromCountry = null;
		
		Country toCountry = null;
		
		for ( int i = 0; i < player.getCountryList().size(); i++ ) {
			
			fromCountry=player.getCountryList().get(i);
			
			Set<Integer> fromCountryAdjacencyList = playerService.getMapService()
					.getAdjacencyCountries(fromCountry.getId());
			
			for (Integer j : fromCountryAdjacencyList) {
				if (playerService.getMapService().getCountryById(j).get().getPlayer().getName().equals(player.getName())) {
					toCountry = playerService.getMapService().getCountryById(j).get();
					break;
				}
			}		
		}
		
		fromCountry.setSoldiers(2);
		
		PlayerFortificationWrapper playerFortificationWrapper = new PlayerFortificationWrapper(fromCountry,
				toCountry, 1);
		
		player.fortify(playerService, playerFortificationWrapper);


		
	}
}
