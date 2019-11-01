package com6441.team7.risc.api.wrapperview;

import com6441.team7.risc.api.model.Country;
import com6441.team7.risc.api.model.Player;

public class PlayerInitialCountryAssignmentWrapper {
	
	private Player player;
	private Country country;
	
	public PlayerInitialCountryAssignmentWrapper(Player p, Country c){
		this.player=p;
		this.country=c;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public Country getCountry() {
		return country;
	}
	
}
