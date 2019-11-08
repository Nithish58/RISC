package com6441.team7.risc.api.wrapperview;

import java.util.ArrayList;

import com6441.team7.risc.api.model.Player;

/**
 * a self-defined wrapper for player conquering information shown in domination view
 */
public class PlayerDominationWrapper {

	/**
	 * a string of player name
	 */
	private String playerName;

	/**
	 * a float number to show occupation percentage
	 */
	private float percentageMap;

	/**
	 * int number of total number of armies
	 */
	private int totalNumArmies;

	/**
	 * a list of countries name occupied by the player
	 */
	private	ArrayList<String> listContinentsOwned;

	/**
	 * constructor set player name, occupation percentage and total number of armies
	 * @param pName reference player name
	 * @param percentage reference percentage of player's dominated territory
	 * @param numArmies reference num of armies
	 */
	public PlayerDominationWrapper(String pName, float percentage, int numArmies) {
		
		this.playerName=pName;
		this.percentageMap=percentage;
		this.totalNumArmies=numArmies;
		
		this.listContinentsOwned=new ArrayList<String>();
		
	}

	/**
	 * add continent name to list of listContinentsOwned
	 * @param continentName reference continent name
	 */
	public void addContinentNameToWrapperList(String continentName) {
		listContinentsOwned.add(continentName);
	}

	/**
	 * get the string to show in the domination view
	 * @return string
	 */
	public String getPlayerDominationMessage() {
		
		String msg=playerName+" controls "+percentageMap+" % of the map and has "
	+totalNumArmies+" total number of armies.";
		
		if(listContinentsOwned.isEmpty()) return msg+="\n "+playerName+" does not own any continent.\n";
		
		msg+="\n "+playerName+" owns:";
		
		for(String strContinents: listContinentsOwned) msg+=" "+strContinents+" ";
		
		return msg+"\n";
		
	}
}
