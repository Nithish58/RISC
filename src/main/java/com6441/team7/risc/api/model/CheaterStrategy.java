package com6441.team7.risc.api.model;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Set;

import com6441.team7.risc.api.wrapperview.PlayerFortificationWrapper;
import com6441.team7.risc.api.wrapperview.ReinforcedArmyWrapper;
import com6441.team7.risc.utils.CommonUtils;

/**
 * This is the strategy class for Cheater players
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
	
	
	@Override
	public void reinforce() {
		
		for(Country c: player.getCountryList()) {
			c.setSoldiers(2*c.getSoldiers());			
		}
		
		playerService.evaluateWorldDomination();
		
	}
	
	@Override
	public void attack() {
		
		ArrayList<Integer> countriesToBeTransferredList=new ArrayList<Integer>();
		
		for(Country c: player.getCountryList()) {
			
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
			if(playerService.getPlayerList().get(i).getCountryList().size()==0) {
				playerToBeRemovedList.add(playerService.getPlayerList().get(i).getName());
			}
		}
		
		for(int i=0;i<playerToBeRemovedList.size();i++) {
			playerService.removePlayer(playerToBeRemovedList.get(i));
		}
		
		if(player.getCountryList().size()==playerService.getMapService().getCountries().size()) {
			CommonUtils.endGame(playerService);
		}
		
		playerService.notifyPlayerServiceObservers("Attack Phase Over");
		
		//Set GameState to Fortify after Attack
		this.playerService.getMapService().setState(GameState.FORTIFY);
		
	}
	
	@Override
	public void fortify() {
		
		for(Country c:player.getCountryList()) {			
			
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
