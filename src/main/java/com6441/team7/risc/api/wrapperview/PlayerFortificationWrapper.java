package com6441.team7.risc.api.wrapperview;

import com6441.team7.risc.api.model.Country;

public class PlayerFortificationWrapper {
	
	Country fromCountry;
	Country toCountry;
	int numSoldiers;
	
	boolean fortifyNone;
	
	String displayMessage="";
	
	public PlayerFortificationWrapper() {
		this.fortifyNone=true;
	}
	
	public PlayerFortificationWrapper(Country from, Country to, int n) {
		this.fromCountry=from;
		this.toCountry=to;
		this.numSoldiers=n;
		this.fortifyNone=false;
	}
	
	public Country getCountryFrom() {
		return fromCountry;
	}
	
	public Country getCountryTo() {
		return toCountry;
	}
	
	public int getNumSoldiers() {
		return numSoldiers;
	}
	
	public boolean getBooleanFortificationNone() {
		return fortifyNone;
	}
	
	public String getFortificationDisplayMessage() {
		return displayMessage;
	}
	
	public void setFortificationDisplayMessage(String str) {
		this.displayMessage=str;
	}
	
	public void setFromCountry(Country c) {
		this.fromCountry=c;
	}
	
	public void setToCountry(Country c) {
		this.toCountry=c;
	}
	
}
