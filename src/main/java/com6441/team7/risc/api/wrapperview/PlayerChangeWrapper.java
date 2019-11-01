package com6441.team7.risc.api.wrapperview;

import com6441.team7.risc.api.model.Player;

public class PlayerChangeWrapper {
	
	private Player currentPlayer;
	
	public PlayerChangeWrapper(Player player) {
		this.currentPlayer=player;
	}
	
	public Player getCurrentPlayer() {
		return currentPlayer;
	}
	

}
