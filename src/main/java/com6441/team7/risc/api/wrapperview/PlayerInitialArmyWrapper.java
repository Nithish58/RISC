package com6441.team7.risc.api.wrapperview;

import com6441.team7.risc.api.model.Player;

/**
 * a self-defined wrapper for placing initial army
 */
public class PlayerInitialArmyWrapper {

	/**
	 * a reference of player
	 */
	private Player player;

	/**
	 * constructor
	 * @param p player
	 */
	public PlayerInitialArmyWrapper(Player p) {
		this.player=p;
	}

	/**
	 * get the player
	 * @return player
	 *
	 */
	public Player getPlayer() {
		return player;
	}

}
