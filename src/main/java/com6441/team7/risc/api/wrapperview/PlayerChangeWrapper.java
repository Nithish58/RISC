package com6441.team7.risc.api.wrapperview;

import com6441.team7.risc.api.model.Player;

/**
 * a self-defined wrapper for player changes
 */
public class PlayerChangeWrapper {

	/**
	 * a reference of current player
	 */
	private Player currentPlayer;

	/**
	 * the constructor to set current player
	 * @param player reference player
	 */
	public PlayerChangeWrapper(Player player) {
		this.currentPlayer=player;
	}

	/**
	 * get current player
	 * @return player
	 */
	public Player getCurrentPlayer() {
		return currentPlayer;
	}
	
}