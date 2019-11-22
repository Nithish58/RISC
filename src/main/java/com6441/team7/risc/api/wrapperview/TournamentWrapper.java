package com6441.team7.risc.api.wrapperview;

import java.util.ArrayList;

public class TournamentWrapper {
	
	private String[][] arrResults;
	
	private ArrayList<String> mapList;
	
	public TournamentWrapper(String[][] arr, ArrayList<String> mapList) {
		this.arrResults=arr;
		this.mapList=mapList;
	}
	
	public String[][] getTournamentResult(){
		return arrResults;
	}
	
	public ArrayList<String> getMapList(){
		return mapList;
	}
	
}
