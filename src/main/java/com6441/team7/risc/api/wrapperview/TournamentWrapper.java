package com6441.team7.risc.api.wrapperview;

import java.util.ArrayList;
/**
 * Class for tournament wrapper
 * @author Keshav
 *
 */
public class TournamentWrapper {
	
	/**
	 * to store results
	 */
	private String[][] arrResults;
	
	/**
	 * to store list of map names
	 */
	private ArrayList<String> mapList;
	
	/**
	 * Constructor for TournamentWrapper
	 * @param arr to store results
	 * @param mapList to store list of map names
	 */
	public TournamentWrapper(String[][] arr, ArrayList<String> mapList) {
		this.arrResults=arr;
		this.mapList=mapList;
	}
	
	/**
	 * getter for tournament results
	 * @return returns a 2D string - arrResults
	 */
	public String[][] getTournamentResult(){
		return arrResults;
	}
	
	/**
	 * getter for mapList
	 * @return returns mapList
	 */
	public ArrayList<String> getMapList(){
		return mapList;
	}
	
}
