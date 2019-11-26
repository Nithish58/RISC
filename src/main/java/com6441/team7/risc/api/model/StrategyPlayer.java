package com6441.team7.risc.api.model;

/**
 * Strategy interface. Used in strategy pattern, for aggressive, benevolent, cheater, random
 * @author Nithish
 *
 */
public interface StrategyPlayer {
	
	/**
	 * reinforce method, that differs with strategy
	 */
	public void reinforce();
	
	/**
	 * attack method, that differs with strategy
	 */
	public void attack();
	
	/**
	 * fortify method, that differs with strategy
	 */
	public void fortify();
}
