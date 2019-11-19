package com6441.team7.risc.api.model;

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
	
	@Override
	public void reinforce() {
		
		//It makes no sense to call player's calculate reinf armies method and then pass it as param.
		//Just follow the format we did for attack and fortify and expose the player for reinforcement
		
		
	}
	
	@Override
	public void attack() {
		System.out.println("Attack");
		
		
	}
	
	@Override
	public void fortify() {


		
	}
	
}
