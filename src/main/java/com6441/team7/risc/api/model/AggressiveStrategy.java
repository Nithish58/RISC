package com6441.team7.risc.api.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com6441.team7.risc.api.wrapperview.PlayerAttackWrapper;
import com6441.team7.risc.api.wrapperview.PlayerFortificationWrapper;

/**
 * This is the strategy class for Aggressive players. Implements from 
 * {@link StrategyPlayer}
 * 
 * @author Binsar
 *
 */
public class AggressiveStrategy implements StrategyPlayer {

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
	 * default constructor
	 */
	public AggressiveStrategy(){}


	/**
	 * {@link AggressiveStrategy} class constructor 
	 * @param playerService PlayerService to be passed for details.
	 */
	public AggressiveStrategy(PlayerService playerService) {
		this.playerService = playerService;
		this.player = playerService.getCurrentPlayer();
		this.playerService.notifyPlayerServiceObservers("Aggressive Strategy");
	}

	/**
	 * In reinforcement phase, aggressive player pick the country with the largest
	 * number of armies to be reinforced.
	 */
	@Override
	public void reinforce() {

		//Check And Exchange Cards
		player.checkAndExchangeCardsForStrategy(playerService);
				
		//Then Calculate Total Num Armies
		int numReinforcementArmies=player.calculateReinforcedArmiesBasedOnCardsContinentsCountries(playerService);
				
		// get country with maximum number of armies
		Country maxCountry = findMaxCountry();
		
		playerService.notifyPlayerServiceObservers(maxCountry.getCountryName()+" has "+maxCountry.getSoldiers()+ " soldier(s)"
				+ " and will receive "+numReinforcementArmies+" reinforcement(s)");

		//player.reinforceArmy(randomCountry.getCountryName(), numArmies, playerService.getMapService());
		playerService.reinforceArmy(player, maxCountry.getCountryName(), numReinforcementArmies);
		
		//End Reinforcement Phase and Move to Attack Phase
		playerService.getMapService().setState(GameState.ATTACK);
	}
	
	/**
	 * In attack phase, aggressive player attacks with its strongest player until it cannot anymore.
	 */
	@Override
	public void attack() {
		
		// get country with maximum number of armies
		Country attackerCountry = null;
		Country defenderCountry=null;

		// get list of player's countries
		ArrayList<Country> attackerCountries = player.getCountryPlayerList();

		Collections.sort(attackerCountries, new Comparator<Country>() {

			@Override
			public int compare(Country c1, Country c2) {

				return c2.getSoldiers().compareTo(c1.getSoldiers());
			}

		});
		
		//Print to check if it's sorted.
		playerService.notifyPlayerServiceObservers("Sorted Country List Descending numSoldiers:");
		for(Country c : attackerCountries) {
			playerService.notifyPlayerServiceObservers(c.getCountryName()+" "+c.getSoldiers()+" soldiers.");
		}
		
		if(attackerCountries.size()<=0) {
			playerService.notifyPlayerServiceObservers("No attack possible");
			player.endAttackPhase(playerService);
			return;
		}
		
		Country attackCountry=null;
		ArrayList<Country> possibleTargetCountries=new ArrayList<Country>();
		
		Set<Integer> attackCountryAdjacencyList;
		
		boolean boolTargetFound=false;
		
		//Choose highest country to be attacker country which has atleast 1 target
		for(Country c:attackerCountries) {
			
			//Get adjacency list of highest country
			//Check if that country has a target
			//If not, move to next highest country
			attackCountryAdjacencyList = playerService.getMapService()
					.getAdjacencyCountries(c.getId());
			
			boolTargetFound=false;
						
			
			for (Integer i : attackCountryAdjacencyList) {
				if (!playerService.getMapService().getCountryById(i).get().getPlayer().getName()
						.equals(player.getName())) {
					
					attackerCountry = c;
					boolTargetFound=true;
					
					break;
				}
			}
			
			if(boolTargetFound) {
				break;
			}
			playerService.notifyPlayerServiceObservers(c.getCountryName()+" does not have any targets.");
		}
		
		if((!boolTargetFound) || attackerCountry==null) {
			playerService.notifyPlayerServiceObservers("No attack possible as no targets found.");
			player.endAttackPhase(playerService);
			return;
		}
		
		//Target Countries Found
		attackCountryAdjacencyList=playerService.getMapService()
				.getAdjacencyCountries(attackerCountry.getId());
		
		for(Integer j:attackCountryAdjacencyList) {
			
			//This country is adjacent and not owned by attacker, it can be attacked
			if (!playerService.getMapService().getCountryById(j).get().getPlayer().getName()
					.equals(player.getName())) {
				
				defenderCountry=playerService.getMapService().getCountryById(j).get();
						
				// Attack the defender country
				// Attack wrapper
				PlayerAttackWrapper playerAttackWrapper = new PlayerAttackWrapper(attackerCountry, defenderCountry);
				
				// Set allout to true
				playerAttackWrapper.setBooleanAllOut();					
				
				playerService.notifyPlayerServiceObservers("Attacker Country: "+attackerCountry.getCountryName()+
						" "+attackerCountry.getSoldiers()+" soldiers.");
				playerService.notifyPlayerServiceObservers("Defender Country: "+defenderCountry.getCountryName()+
						" "+defenderCountry.getSoldiers()+" soldiers.");
				
				// Call the attack function
				player.attack(playerService, playerAttackWrapper);
				
				//If country conquered...attackmove
				if(player.getBoolAttackMoveRequired()) {
					
					//Move max num of soldiers to conquered country
					
					player.attackMove(Math.max(1, (attackerCountry.getSoldiers()-1)));
				}			
			}  //End of If -- Go To next target country 
			
		}  //End of For -- Finished Looping through all target countries
		
		//After Attacking all targets, end the attack
		player.endAttackPhase(playerService);
		
	}

	/**
	 * In fortify phase, aggressive player aggregates its armies into current strongest country.
	 */
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

		//FORTIFY NONE IF MAX COUNTRY IS NULL
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

					neighborMaxCountry = playerService.getMapService().getCountryById(i).get();
					
					playerService.notifyPlayerServiceObservers(
							"From neighbor country: " + neighborMaxCountry.getCountryName());
					playerService.notifyPlayerServiceObservers("To Country: " + maxCountry.getCountryName());
					
					neighborCountryFound = true;
					break;
				}
			}

			if (!neighborCountryFound) {
				playerService.notifyPlayerServiceObservers("No adjacent countries found for fortification");
				playerFortificationWrapper = new PlayerFortificationWrapper();
				player.fortify(playerService, playerFortificationWrapper);
				return;
			}
			
			//Fortification conditions met...can fortify
			
			if (neighborMaxCountry != null && neighborMaxCountry.getSoldiers() > 1) {
				
				int numArmiesToBeFortified=neighborMaxCountry.getSoldiers()-1;
				
				playerFortificationWrapper = new PlayerFortificationWrapper(neighborMaxCountry, maxCountry, numArmiesToBeFortified);
				player.fortify(playerService, playerFortificationWrapper);
				
			}
			
			//Fortification conditions not met...therefore we don't fortify
			else {
				playerService.notifyPlayerServiceObservers("From Country does not have enough soldiers to move.");
				playerFortificationWrapper = new PlayerFortificationWrapper();
				player.fortify(playerService, playerFortificationWrapper);
			}			
		}
	}

	/**
	 * Finds strongest country, that is country with largest number of armies
	 * @return returns the strongest country
	 */
	public Country findMaxCountry() {
		int maxArmy = -1; // Init local variable for maximum number of army
		int maxCountryIndex = 0; // init local variable for the index of the country with the largest num of army

		// initialize attacker's country

		// Get country with max num of soldiers using maxCountryIndex
		Country maxCountry = null;

		// Get largest number of armies owned by the player
		for (int i = 0; i < player.getCountryPlayerList().size(); i++) {
			if (player.getCountryPlayerList().get(i).getSoldiers() > maxArmy) {
				
				//Get number of soldiers owned by the country
				maxArmy = player.getCountryPlayerList().get(i).getSoldiers();
				
				// Get country with max num of soldiers using maxCountryIndex
				maxCountry = player.getCountryPlayerList().get(i);
			}
		}

		return maxCountry;
	}
	
	/**
	 * This is for calculating aggressive country's reinforcements
	 * @param player receives player whose reinforcement armies need to be calculated
	 * @return number of reinforced armies calculated.
	 */
    public int calculateReinforcedAggressiveArmies(Player player){

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























































