package com6441.team7.risc.api.wrapperview;

import com6441.team7.risc.api.model.Country;

public class PlayerAttackWrapper {

	Country fromCountry;
	Country toCountry;
	int numDiceAttacker;
	int numDiceDefender;
	boolean boolAllOut;
	boolean boolAttackOver;
	
	String strAttackDisplayMessage="";
	
	public PlayerAttackWrapper(Country from, Country to) {
		this.fromCountry=from;
		this.toCountry=to;
		this.boolAllOut=false;
		
		this.boolAttackOver=false; //for display purposes
	}
	
	public void setNumDiceAttacker(int n) {
		this.numDiceAttacker=n;
	}
	
	public void setNumDiceDefender(int n) {
		this.numDiceDefender=n;
	}
	
	public void setBooleanAllOut() {
		this.boolAllOut=true;
	}
	
	public boolean getBooleanAllOut() {
		return boolAllOut;
	}
	
	public Country getFromCountry() {
		return fromCountry;
	}
	
	public Country getToCountry() {
		return toCountry;
		
	}
	
	public int getNumDiceAttacker() {
		return numDiceAttacker;
	}
	
	public int getNumDiceDefender() {
		return numDiceDefender;
	}
	
	public boolean getBoolAllOut() {
		return boolAllOut;
	}
	
	public boolean getBoolAttackOver() {
		return boolAttackOver;
	}
	
	//Write additional set get Methods here if you want
	public void setAttackDisplayMessage(String str) {
		this.strAttackDisplayMessage = str;
	}
	
	public String getAttackDisplayMessage() {
		return strAttackDisplayMessage;
	}
	
}
