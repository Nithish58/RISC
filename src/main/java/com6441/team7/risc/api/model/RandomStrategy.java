package com6441.team7.risc.api.model;

import java.util.Random;
import java.util.Set;

import com6441.team7.risc.api.wrapperview.PlayerAttackWrapper;
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

		int numArmies = player.calculateReinforcedArmies(player);
		
		Random rn=new Random();
		
		Country randomCountry = player.getCountryList().get(rn.nextInt(player.getCountryList().size()));
		
		//player.reinforceArmy(randomCountry.getCountryName(), numArmies, playerService.getMapService());
		playerService.reinforceArmy(player, randomCountry.getCountryName(), numArmies);
		
	}
	
	@Override
	public void attack() {		
		
		Country randomFromAttackCountry=null;
		Country randomToAttackCountry=null;
		
		//Loop through every country in player list
		//Find adjacency country list
		//Find adjacent country that does not belong to player
		//Attack that adjacent country
		for(Country c:player.getCountryList()) {
			
			boolean boolTargetFound=false;
			
			Set<Integer> toCountryAdjacencyList = playerService.getMapService()
					.getAdjacencyCountries(c.getId());
			
			for (Integer j : toCountryAdjacencyList) {
				if (!playerService.getMapService().getCountryById(j).get().getPlayer().getName().equals(player.getName())) {
					
					randomToAttackCountry = playerService.getMapService().getCountryById(j).get();
					boolTargetFound=true;
					randomFromAttackCountry=c;
					
					break;
				}
			}		
			
			if(boolTargetFound) {
				
				PlayerAttackWrapper playerAttackWrapper=new PlayerAttackWrapper(randomFromAttackCountry,randomToAttackCountry);
				playerAttackWrapper.setNumDiceAttacker(1);
				playerAttackWrapper.setNumDiceDefender(1);
				
				player.attack(playerService,playerAttackWrapper);
				
				if(player.getBoolAttackMoveRequired()) player.attackMove(1);
				
				player.endAttackPhase(playerService);
				
				return;				
			}
			
		}	
		
		//Attack Not Possible since no targets found
		player.endAttackPhase(playerService);
		
	}
	
	@Override
	public void fortify() {
		
		Country fromCountry = null;
		
		Country toCountry = null;
		
		boolean boolFortificationConditionMet=false;
		
		for ( int i = 0; i < player.getCountryList().size(); i++ ) {
			
			fromCountry=player.getCountryList().get(i);
			
			Set<Integer> fromCountryAdjacencyList = playerService.getMapService()
					.getAdjacencyCountries(fromCountry.getId());
			
			for (Integer j : fromCountryAdjacencyList) {
				if (playerService.getMapService().getCountryById(j).get().getPlayer().getName().equals(player.getName())) {
					toCountry = playerService.getMapService().getCountryById(j).get();
					boolFortificationConditionMet=true;
					break;
				}
			}		
		}
		
		//Actual Fortification
		PlayerFortificationWrapper playerFortificationWrapper;
		if(fromCountry.getSoldiers()>1 && boolFortificationConditionMet) {			
			playerFortificationWrapper = new PlayerFortificationWrapper(fromCountry,
					toCountry, 1);			
		}
		else {			
			//By default boolFortifyNone is set to true
			playerFortificationWrapper=new PlayerFortificationWrapper();			
		}
		
		
		player.fortify(playerService, playerFortificationWrapper);
		
	}
}
