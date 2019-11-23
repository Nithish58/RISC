package com6441.team7.risc.api.model;

import java.util.Collections;
import java.util.Random;
import java.util.Set;

import com6441.team7.risc.api.wrapperview.PlayerAttackWrapper;
import com6441.team7.risc.api.wrapperview.PlayerFortificationWrapper;
import com6441.team7.risc.utils.MapDisplayUtils;

public class RandomStrategy implements StrategyPlayer{
	
	private PlayerService playerService;
	private Player player;
	
	public RandomStrategy(PlayerService playerService) {
		this.playerService = playerService;
		this.player = playerService.getCurrentPlayer();
		this.playerService.notifyPlayerServiceObservers("Random Strategy");
	}
	
	@Override
	public void reinforce() {
		
		//Check And Exchange Cards
		player.checkAndExchangeCardsForStrategy(playerService);
		
		//Then Calculate Total Num Armies
		int numReinforcementArmies=player.calculateReinforcedArmiesBasedOnCardsContinentsCountries(playerService);
		
		//Then Reinforce Random Country
		
		Random rn=new Random();
		
		Country randomCountry = player.getCountryList().get(rn.nextInt(player.getCountryList().size()));
		
		//player.reinforceArmy(randomCountry.getCountryName(), numArmies, playerService.getMapService());
		playerService.reinforceArmy(player, randomCountry.getCountryName(), numReinforcementArmies);
		
		//End Fortification and Move to Attack Phase
		playerService.getMapService().setState(GameState.ATTACK);
		
	}
	
	@Override
	public void attack() {		
		
		Country randomFromAttackCountry=null;
		Country randomToAttackCountry=null;
		
		//Loop through every country in player list
		//Find adjacency country list
		//Find adjacent country that does not belong to player
		//Attack that adjacent country
		
		Collections.shuffle(player.getCountryList());
		
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
				
				//Decide Random num times attack (1-5)
				int numTimesAttack=new Random().nextInt(5)+1;
				
				
				for(int i=0;i<numTimesAttack;i++) {
				
					PlayerAttackWrapper playerAttackWrapper=new PlayerAttackWrapper(randomFromAttackCountry,randomToAttackCountry);
					playerAttackWrapper.setNumDiceAttacker(1);
					playerAttackWrapper.setNumDiceDefender(1);
					
					player.attack(playerService,playerAttackWrapper);
					
					//If country counquered after attack: Move soldier to conquered country
					//Then stop attack as future attacks wont be possible on same owner country
					if(player.getBoolAttackMoveRequired()) {
						player.attackMove(1);
						break;
					}
					
				}				
				
				player.endAttackPhase(playerService);
				
				return;				
			}
			
		}	
		
		//Attack Not Possible since no targets found
		playerService.notifyPlayerServiceObservers("No Attack Targets Found.");
		player.endAttackPhase(playerService);
		
	}
	
	@Override
	public void fortify() {
		
		Country fromCountry = null;
		
		Country toCountry = null;
		
		boolean boolFortificationConditionMet=false;
		
		//Loop through player list
		//Choose adjacent country which is eligible to be reinforced
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
			
			if(boolFortificationConditionMet) break;
		}
		
		//Actual Fortification	
		PlayerFortificationWrapper playerFortificationWrapper;
		
		if(fromCountry.getSoldiers()>1 && boolFortificationConditionMet) {
			playerFortificationWrapper = new PlayerFortificationWrapper(fromCountry,
					toCountry, 1);			
		}
		else {			
			//By default boolFortifyNone is set to true
			playerService.notifyPlayerServiceObservers("Fortify NONE");
			playerFortificationWrapper=new PlayerFortificationWrapper();
			
		}
		
		
		player.fortify(playerService, playerFortificationWrapper);
		
	}
}
