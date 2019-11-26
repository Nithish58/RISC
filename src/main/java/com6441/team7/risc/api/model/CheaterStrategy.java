package com6441.team7.risc.api.model;

import java.util.ArrayList;
import java.util.Set;

import com6441.team7.risc.utils.CommonUtils;

/**
 * This is the strategy class for Cheater players. Implements from 
 * {@link StrategyPlayer}
 * @author Binsar
 *
 */
public class CheaterStrategy implements StrategyPlayer{

	private PlayerService playerService;
	private Player player;
	
	public CheaterStrategy(PlayerService playerService) {
		this.playerService = playerService;
		this.player = playerService.getCurrentPlayer();
		this.playerService.notifyPlayerServiceObservers("Cheater Strategy");
	}
	
	/**
	 * Cheater player doubles the number of armies in all its countries in reinforcement phase.
	 */
	@Override
	public void reinforce() {
		
		for(Country c: player.getCountryPlayerList()) {
			c.setSoldiers(2*c.getSoldiers());			
		}
		
		playerService.notifyPlayerServiceObservers("Cheater's Countries Reinforced by doubling.");
		playerService.evaluateWorldDomination();
		
	}
	
	/**
	 * Cheater player automatically conquers all its neighbors of all its countries in attack phase.
	 */
	@Override
	public void attack() {
		
		ArrayList<Integer> countriesToBeTransferredList=new ArrayList<Integer>();
		
		for(Country c: player.getCountryPlayerList()) {
			
			Set<Integer> fromCountryAdjacencyList = playerService.getMapService()
					.getAdjacencyCountries(c.getId());
			
			for (Integer j : fromCountryAdjacencyList) {
				if (!playerService.getMapService().getCountryById(j).get().getPlayer().getName().
						equals(player.getName())) {
					
					countriesToBeTransferredList.add(j);			
				}
			}			
		}
		
		for(int k=0;k<countriesToBeTransferredList.size();k++) {
			
			transferCountryOwnership(playerService.getMapService().getCountryById(
					countriesToBeTransferredList.get(k)).get());
			
		}
		
		playerService.evaluateWorldDomination();
		
		//Check if any player defeated- remove it
		
		ArrayList<String> playerToBeRemovedList=new ArrayList<String>();
		
		for(int i=0;i<playerService.getPlayerList().size();i++) {
			if(playerService.getPlayerList().get(i).getCountryPlayerList().size()==0) {
				playerToBeRemovedList.add(playerService.getPlayerList().get(i).getName());
			}
		}
		
		for(int i=0;i<playerToBeRemovedList.size();i++) {
			
			//TransferCards as well        	
			
			playerService.removePlayer(playerToBeRemovedList.get(i));
		}
		
		//Check if cheater is winner
		if(player.getCountryPlayerList().size()==playerService.getMapService().getCountries().size()) {
        	if(playerService.getBoolTournamentMode()) {
        		playerService.setBoolPlayerWinner(true);
        		playerService.setPlayerWinner(player);
        		//this.boolAttackMoveRequired=false;
        		return;
        	}
        	
			CommonUtils.endGame(playerService);
		}
		
		playerService.notifyPlayerServiceObservers("Attack Phase Over");
		
		//Set GameState to Fortify after Attack
		this.playerService.getMapService().setState(GameState.FORTIFY);
		
	}
	
	/**
	 * Cheater player doubles armies whose neighbors belong to other players in fortification phase.
	 */
	@Override
	public void fortify() {
		
		for(Country c:player.getCountryPlayerList()) {
			
			Set<Integer> fromCountryAdjacencyList = playerService.getMapService()
					.getAdjacencyCountries(c.getId());
			
			for (Integer j : fromCountryAdjacencyList) {
				if (!playerService.getMapService().getCountryById(j).get().getPlayer().getName().
						equals(player.getName())) {
					
					c.setSoldiers(2*c.getSoldiers());
					
					playerService.notifyPlayerServiceObservers(c.getCountryName()+" has opponent neighbours,"
							+ " doubled to: "+c.getSoldiers());
					
					break; //Already doubled country with foreign neighbours, move to other countries now
				}
			}				
		}
		
		playerService.evaluateWorldDomination();
		playerService.notifyPlayerServiceObservers("End of cheater fortification");
		
		//Switch to next player, set gamestate to reinforce, automate game again
		playerService.switchNextPlayer();
		playerService.getMapService().setState(GameState.REINFORCE);
		playerService.automateGame();
		
		
	}
	
	/**
	 * Change in ownership of country each time its war is won against it.
	 * @param toCountryAttack  Country to be transferred
	 */
    public void transferCountryOwnership(Country toCountryAttack) {

    	//If country already belongs to player, do not transfer again.
    	if(toCountryAttack.getPlayer().getName().
    			equalsIgnoreCase(player.getName())) {
    		return;
    	}
    	
        player.addCountryToPlayerList(toCountryAttack);

        String previousOwnerName=toCountryAttack.getPlayer().getName();
        toCountryAttack.getPlayer().removeCountryFromPlayerList(toCountryAttack);

        toCountryAttack.setPlayer(player);

        playerService.notifyPlayerServiceObservers("Country transferred: "+toCountryAttack.getCountryName()+
        		" (PreviousOwner: "+previousOwnerName+")");
        
    }
	
}
