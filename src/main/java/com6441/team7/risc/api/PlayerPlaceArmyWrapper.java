package com6441.team7.risc.api;

import com6441.team7.risc.api.model.Country;
import com6441.team7.risc.api.model.Player;

public class PlayerPlaceArmyWrapper {
	
	private Player player;
	private Country country;
	
	public PlayerPlaceArmyWrapper(Player p, Country c){
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
