package com6441.team7.risc.api.wrapperview;

import java.util.concurrent.atomic.AtomicBoolean;

import com6441.team7.risc.api.model.Country;

public class PlayerAttackWrapper {

	Country fromCountry;
	Country toCountry;
	int numDiceAttacker;
	int numDiceDefender;
	boolean boolAllOut;
	boolean boolAttackOver;
	
	AtomicBoolean boolDefenderDiceRequired;
	AtomicBoolean boolAttackMoveRequired;
	
	String strAttackDisplayMessage="";
	
	public PlayerAttackWrapper(Country from, Country to) {
		this.fromCountry=from;
		this.toCountry=to;
		this.boolAllOut=false;
		
		this.boolAttackOver=false; //for display purposes in view
		
	//	this.boolDefenderDiceRequired.set(false);
	//	this.boolAttackMoveRequired.set(false);
		
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
	
	public void setBoolAttackMoveRequired(AtomicBoolean b) {
		this.boolAttackMoveRequired=b;
	}
	
	public void setBoolaDefenderDiceRequired(AtomicBoolean b) {
		this.boolDefenderDiceRequired=b;
	}
	
	public AtomicBoolean getBooldAttackMoveRequired() {
		return boolAttackMoveRequired;
	}
	
	public AtomicBoolean getBoolDefenderDiceRequired() {
		return boolDefenderDiceRequired;
	}
}
