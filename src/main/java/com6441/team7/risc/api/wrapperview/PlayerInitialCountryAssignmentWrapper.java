package com6441.team7.risc.api.wrapperview;

import com6441.team7.risc.api.model.Country;
import com6441.team7.risc.api.model.Player;

/**
 * a self-defined wrapper for assigning country to player
 */
public class PlayerInitialCountryAssignmentWrapper {

	/**
	 * a reference of player
	 */
	private Player player;

	/**
	 *  a reference of country
	 */
	private Country country;

	/**
	 * constructor
	 * @param p player
	 * @param c country
	 */
	public PlayerInitialCountryAssignmentWrapper(Player p, Country c){
		this.player=p;
		this.country=c;
	}

	/**
	 * get player
	 * @return player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * get country
	 * @return country
	 */
	public Country getCountry() {
		return country;
	}
	
}
