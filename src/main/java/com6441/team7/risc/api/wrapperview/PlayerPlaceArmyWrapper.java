package com6441.team7.risc.api.wrapperview;

import com6441.team7.risc.api.model.Country;
import com6441.team7.risc.api.model.Player;

/**
 * a self-defined wrapper for placing army
 */
public class PlayerPlaceArmyWrapper {

	/**
	 * a reference of player
	 */
	private Player player;

	/**
	 * a reference of country
	 */
	private Country country;

	/**
	 * constructor of PlayerPlaceArmyWrapper
	 * @param p Player
	 * @param c Country
	 */
	public PlayerPlaceArmyWrapper(Player p, Country c){
		this.player=p;
		this.country=c;
	}

	/**
	 * get the player
	 * @return player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * get the country
	 * @return country
	 */
	public Country getCountry() {
		return country;
	}
	

}
