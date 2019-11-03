package com6441.team7.risc.api.wrapperview;

import java.util.ArrayList;

import com6441.team7.risc.api.model.Player;

public class PlayerDominationWrapper {
	
	private String playerName;
	private float percentageMap;
	private int totalNumArmies;
	
	private	ArrayList<String> listContinentsOwned;
	
	public PlayerDominationWrapper(String pName, float percentage, int numArmies) {
		
		this.playerName=pName;
		this.percentageMap=percentage;
		this.totalNumArmies=numArmies;
		
		this.listContinentsOwned=new ArrayList<String>();
		
	}
	
	public void addContinentNameToWrapperList(String continentName) {
		listContinentsOwned.add(continentName);
	}
	
	public String getPlayerDominationMessage() {
		
		String msg=playerName+" controls "+percentageMap+" % of the map and has "
	+totalNumArmies+" total number of armies.";
		
		if(listContinentsOwned.isEmpty()) return msg+="\n "+playerName+" does not own any continent.\n";
		
		msg+="\n "+playerName+" owns:";
		
		for(String strContinents: listContinentsOwned) msg+=" "+strContinents+" ";
		
		return msg+"\n";
		
	}
}
