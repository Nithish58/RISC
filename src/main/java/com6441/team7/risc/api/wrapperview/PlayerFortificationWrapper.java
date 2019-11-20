package com6441.team7.risc.api.wrapperview;

import com6441.team7.risc.api.model.Country;

/**
 * a self-defined wrapper for fortification phase
 */
public class PlayerFortificationWrapper {

	/**
	 * a refenrence of country
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
	 * @param str String
	 */
	public void setFortificationDisplayMessage(String str) {
		this.displayMessage=str;
	}

	/**
	 * set from country
	 * @param c country
	 */
	public void setFromCountry(Country c) {
		this.fromCountry=c;
	}

	/**
	 * set to country
	 * @param c country
	 */
	public void setToCountry(Country c) {
		this.toCountry=c;
	}
	
	/**
	 * set num of soldier
	 * @param n num of soldiers
	 */
	public void setNumSoldiers(int n) {
		this.numSoldiers=n;
	}
	
	/**
	 * set boolean of fortificationNone
	 * @param b boolean
	 */
	public void setBooleanFortificationNon(boolean b) {
		this.fortifyNone = b;
	}
	
}
