package com6441.team7.risc.api.wrapperview;

import com6441.team7.risc.api.model.Country;

/**
 * a self-defined wrapper for fortification phase
 */
public class PlayerFortificationWrapper {

	/**
	 * a reference of country
	 */
	Country fromCountry;

	/**
	 * a reference of country
	 */
	Country toCountry;

	/**
	 * soldier number
	 */
	int numSoldiers;

	/**
	 * boolean value to validate if fortifyNone
	 */
	boolean fortifyNone;

	/**
	 * a string of display message
	 */
	String displayMessage="";

	/**
	 * constructor to set fortifyNone to true
	 */
	public PlayerFortificationWrapper() {
		this.fortifyNone=true;
	}

	/**
	 * constructor to set from country, to country and soldier number
	 * @param from country
	 * @param to country
	 * @param n soldier number
	 */
	public PlayerFortificationWrapper(Country from, Country to, int n) {
		this.fromCountry=from;
		this.toCountry=to;
		this.numSoldiers=n;
		this.fortifyNone=false;
	}

	/**
	 * get from country
	 * @return country
	 */
	public Country getCountryFrom() {
		return fromCountry;
	}

	/**
	 * get to country
	 * @return country
	 */
	public Country getCountryTo() {
		return toCountry;
	}

	/**
	 * return soldier number
	 * @return int
	 */
	public int getNumSoldiers() {
		return numSoldiers;
	}

	/**
	 * get fortifyNone boolean value
	 * @return fortifyNone
	 */
	public boolean getBooleanFortificationNone() {
		return fortifyNone;
	}

	/**
	 * get displayMessage
	 * @return displayMessage
	 */
	public String getFortificationDisplayMessage() {
		return displayMessage;
	}

	/**
	 * set display message
	 * @param string String
	 */
	public void setFortificationDisplayMessage(String string) {
		this.displayMessage=string;
	}

	/**
	 * setter for fromCountry
	 * @param country country
	 */
	public void setFromCountry(Country country) {
		this.fromCountry=country;
	}

	/**
	 * set to country
	 * @param country country
	 */
	public void setToCountry(Country country) {
		this.toCountry=country;
	}

	/**
	 * setter for numSoldiers
	 * @param soldiers number of soldiers
	 */
	public void setNumSoldiers(int soldiers) {
		this.numSoldiers=soldiers;
		
	}

	public void setBooleanFortificationNone(boolean bool) {
		this.fortifyNone=bool;
		
	}
	
}
