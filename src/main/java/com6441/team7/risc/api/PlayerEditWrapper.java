package com6441.team7.risc.api;

import com6441.team7.risc.api.model.Player;

public class PlayerEditWrapper {

	private Player addedPlayer;
	private Player removedPlayer;
	
	public PlayerEditWrapper(){
		this.addedPlayer=null;
		this.removedPlayer=null;
	}
	
	public Player getAddedPlayer() {
		return addedPlayer;
	}
	
	public Player getRemovedPlayer() {
		return removedPlayer;
	}
	
	public void setAddedPlayer(Player player) {
		this.addedPlayer=player;
	}
	
	public void setRemovedPlayer(Player player) {
		this.removedPlayer=player;
	}
	
}
