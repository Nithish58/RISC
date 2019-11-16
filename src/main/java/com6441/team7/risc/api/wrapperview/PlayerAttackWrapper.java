package com6441.team7.risc.api.wrapperview;

import java.util.concurrent.atomic.AtomicBoolean;

import com6441.team7.risc.api.model.Country;

/**
 * a self-defined wrapper for attack phase
 */
public class PlayerAttackWrapper {

	/**
	 * a reference of country
	 */
	Country fromCountry;

	/**
	 * a reference of country
	 */
	Country toCountry;

	/**
	 * the number of dice from attacker
	 */
	int numDiceAttacker;

	/**
	 * the number of dice from defender
	 */
	int numDiceDefender;

	/**
	 * boolean value to validate if allout
	 */
	boolean boolAllOut;

	/**
	 * boolean value to validate if attack is over
	 * Used for display purposes
	 */
	boolean boolAttackOver;
	
	/**
	 * boolean value to validate if attackmove is over
	 * Used for display purposes
	 */
	boolean boolAttackMoveOver;



	AtomicBoolean boolDefenderDiceRequired;
	AtomicBoolean boolAttackMoveRequired;

	/**
	 * string to display attack messages
	 */
	String strAttackDisplayMessage="";

	/**
	 * constructor of PlayerAttackWrapper to set from country and to country
	 * @param from country
	 * @param to country
	 */
	public PlayerAttackWrapper(Country from, Country to) {
		this.fromCountry=from;
		this.toCountry=to;
		this.boolAllOut=false;
		
		this.boolAttackOver=false; //for display purposes in view
		
	}

	/**
	 * set number of dice from Attacker
	 * @param n reference number of attacker's dice
	 */
	public void setNumDiceAttacker(int n) {
		this.numDiceAttacker=n;
	}

	/**
	 * set number of dice from defender
	 * @param n reference num of defender's dice
	 */
	public void setNumDiceDefender(int n) {
		this.numDiceDefender=n;
	}

	/**
	 * set boolean value allout to true
	 */
	public void setBooleanAllOut() {
		this.boolAllOut=true;
	}

	/**
	 * get booleanAllOut value
	 * @return boolean
	 */
	public boolean getBooleanAllOut() {
		return boolAllOut;
	}

	/**
	 * get fromCountry
	 * @return country
	 */
	public Country getFromCountry() {
		return fromCountry;
	}

	/**
	 * get toCountry
	 * @return country
	 */
	public Country getToCountry() {
		return toCountry;
		
	}

	/**
	 * get number of dice from attacker
	 * @return numDiceAttacker
	 */
	public int getNumDiceAttacker() {
		return numDiceAttacker;
	}

	/**
	 * get number of dice from defender
	 * @return numDiceDefender
	 */
	public int getNumDiceDefender() {
		return numDiceDefender;
	}

	/**
	 * get boolean value of boolAllOut
	 * @return boolAllOut
	 */
	public boolean getBoolAllOut() {
		return boolAllOut;
	}

	/**
	 * get boolean value of boolAttackOver
	 * @return boolAttackOver
	 */
	public boolean getBoolAttackOver() {
		return boolAttackOver;
	}
	/**
	 * Setter for boolAttackOver
	 * Alsways sets to true
	 */
	public void setBoolAttackOver() {
		this.boolAttackOver=true;
	}
	
	/**
	 * get boolean value of boolAttackMoveOver
	 * @return boolAttackOver
	 */
	public boolean getBoolAttackMoveOver() {
		return boolAttackMoveOver;
	}
	
	/**
	 * Setter for boolAttackMoveOver
	 * Always sets to true
	 */
	public void setBoolAttackMoveOver() {
		this.boolAttackMoveOver=true;
	}

	/**
	 * set attackDisplayMessage
	 * @param str reference message string
	 */
	public void setAttackDisplayMessage(String str) {
		this.strAttackDisplayMessage = str;
	}

	/**
	 * get string of attackDisplayMessage
	 * @return strAttackDisplayMessage
	 */
	public String getAttackDisplayMessage() {
		return strAttackDisplayMessage;
	}

	/**
	 * set boolAttackMovedRequired
	 * @param b boolean
	 */
	public void setBoolAttackMoveRequired(AtomicBoolean b) {
		this.boolAttackMoveRequired=b;
	}

	/**
	 * set boolDefenderDiceRequired value
	 * @param b boolean
	 */
	public void setBoolaDefenderDiceRequired(AtomicBoolean b) {
		this.boolDefenderDiceRequired=b;
	}

	/**
	 * get boolAttackMoveRequired value
	 * @return boolAttackMoveRequired
	 */
	public AtomicBoolean getBooldAttackMoveRequired() {
		return boolAttackMoveRequired;
	}

	/**
	 * get boolDefenderDiceRequired value
	 * @return boolDefenderDiceRequired
	 */
	public AtomicBoolean getBoolDefenderDiceRequired() {
		return boolDefenderDiceRequired;
	}
}
