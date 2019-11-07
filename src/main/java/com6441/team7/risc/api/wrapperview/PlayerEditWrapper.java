package com6441.team7.risc.api.wrapperview;

import com6441.team7.risc.api.model.Player;

/**
 * a self-defined wrapper for editing player
 */
public class PlayerEditWrapper {

	/**
	 * a reference of player
	 */
	private Player addedPlayer;

	/**
	 * a reference of player
	 */
	private Player removedPlayer;

	/**
	 * constructor to set addPlayer and removePlayer to null
	 */
	public PlayerEditWrapper(){
		this.addedPlayer=null;
		this.removedPlayer=null;
	}

	/**
	 * get addedPlayer
	 * @return player
	 */
	public Player getAddedPlayer() {
		return addedPlayer;
	}

	/**
	 * get removedPlayer
	 * @return player
	 */
	public Player getRemovedPlayer() {
		return removedPlayer;
	}

	/**
	 * set addedPlayer
	 * @param player Player
	 */
	public void setAddedPlayer(Player player) {
		this.addedPlayer=player;
	}

	/**
	 * set removedPlayer
	 * @param player
	 */
	public void setRemovedPlayer(Player player) {
		this.removedPlayer=player;
	}
	
}
