package com6441.team7.risc.controller;

import static com6441.team7.risc.api.RiscConstants.WHITESPACE;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import com6441.team7.risc.api.model.Country;
import com6441.team7.risc.api.model.GameState;
import com6441.team7.risc.api.model.MapService;
import com6441.team7.risc.api.model.Player;
import com6441.team7.risc.api.model.PlayerService;
import com6441.team7.risc.api.model.RiscCommand;
import com6441.team7.risc.api.wrapperview.PlayerFortificationWrapper;
import com6441.team7.risc.utils.CommonUtils;
import com6441.team7.risc.utils.MapDisplayUtils;
import com6441.team7.risc.view.GameView;

import org.apache.commons.lang3.StringUtils;

/**
 * This class represents the fortification phase It accepts fortification
 * commands from user and some show commands and rejects all other commands It
 * checks whether fortification criteria are met. It then carries out
 * fortification After fortification, it updates gamestate to reinforcement and
 * switches to next player
 */
public class FortifyGameController implements Controller {

	private PlayerService playerService;
	private MapService mapService;
	private GameView phaseView;

	/**
	 * Country from where fortification is start
	 */
	private Country fromCountry;

	/**
	 * country to where fortification is done
	 */
	private Country toCountry;

	/**
	 * player whose fortification is in process
	 */
	private Player player;

	/**
	 * number of soldiers entered by user
	 */
	private int numSoldiers;

	/**
	 * strings arrays in orders
	 */
	private String[] orders;
	/**
	 * Set that keeps track of neighbouring countries of origin country.
	 */
	private Set<Integer> neighbouringCountries;
	/**
	 * set of countries
	 */
	private Set<Country> countryList;

	/**
	 * check validation is met or not
	 */
	private boolean boolValidationMet;

	public FortifyGameController(PlayerService playerService) {
		this.playerService = playerService;
		this.mapService = this.playerService.getMapService();
	}

	public void setView(GameView view) {
		this.phaseView = view;
	}

	// TODO: read command from phaseView and validate command here
	// TODO: if the command is valid, call corresponding method in playerService
	// TODO: if the command is not valid, call phaseView.displayMessage() to show
	// error messages
	@Override
	public void readCommand(String command) throws Exception {


		this.player = playerService.getCurrentPlayer();

		this.orders = command.split("\\s+");

		RiscCommand commandType = RiscCommand.parse(StringUtils.split(command, WHITESPACE)[0]);

		switch (commandType) {

		case FORTIFY:
			determineFortificationAndFortify(command);
			break;

		case SHOW_MAP:
			// startupGameController.showMapFull();
			MapDisplayUtils.showMapFullPopulated(mapService, phaseView);
			break;

		case SHOW_PLAYER:
			// showPlayerFortificationPhase(player);
			MapDisplayUtils.showPlayer(mapService, playerService, phaseView);
			break;

		case SHOW_PLAYER_ALL_COUNTRIES:
			// showPlayerAllCountriesFortification();
			MapDisplayUtils.showPlayerAllCountries(mapService, playerService, phaseView);
			break;

		case SHOW_PLAYER_COUNTRIES:
			// showPlayerCountriesFortification();
			MapDisplayUtils.showPlayerCountries(mapService, playerService, phaseView);
			break;

		case EXIT:
			CommonUtils.endGame(phaseView);
			break;

		default:
			throw new IllegalArgumentException("Cannot recognize this command in Fortification Phase. Try Again");

		} // End of switch

	} // End of readCommand method

	/**
	 * This method determines type of fortification:
	 * <li>It validates the command: if the command format is valid, it check the
	 * command type</li>
	 * <li>if the command is none, it exits reinforce phase</li>
	 * <li>if the command is valid, validate the country info and soldier info, and
	 * move the soldier from these two countries</li>
	 * 
	 * @param command
	 */
	private void determineFortificationAndFortify(String command) {

		this.orders = command.split("\\s+");

		if (orders.length == 2 && orders[1].equalsIgnoreCase("none")) {

			// phaseView.displayMessage("Fortification Phase Over.");

			/*
			 * this.playerService.switchNextPlayer();
			 * this.mapService.setState(GameState.REINFORCE);
			 */

			// FORTIFY CALLED IN PLAYER CLASS
			PlayerFortificationWrapper playerFortificationWrapper = new PlayerFortificationWrapper();

			playerService.getCurrentPlayer().fortify(playerService, playerFortificationWrapper);

		}

		else if (orders.length == 4) {
			
			if(!(mapService.getCountryByName(orders[1]).isPresent()&&
					mapService.getCountryByName(orders[2]).isPresent())) {
				phaseView.displayMessage("Invalid Country Name Entered");
				return;
			}
				

			this.fromCountry = mapService.getCountryByName(orders[1]).get();

			this.toCountry = mapService.getCountryByName(orders[2]).get();
			

			try {
				this.numSoldiers = Integer.parseInt(orders[3]);

			} catch (NumberFormatException e) {

				phaseView.displayMessage("Wrong Number Format. Try Again");
				return;
			}

			// fortify();

			// FORTIFY CALLED IN PLAYER CLASS

			PlayerFortificationWrapper playerFortificationWrapper = new PlayerFortificationWrapper(fromCountry,
					toCountry, numSoldiers);

			// CAN DIRECTLY CALL CURRENT PLAYER...NO NEED TO GO THROUGH MAPSERVICE
			// CAN PASS PLAYERSERVICE AS PARAMETER

			// playerService.fortifyCurrentPlayer(playerFortificationWrapper);

			playerService.getCurrentPlayer().fortify(playerService, playerFortificationWrapper);

		}

		else {
			phaseView.displayMessage("Invalid Fortification Command. Try Again");
			return;
		}

	} // End of method

	
} // End of class




































/*

*//**
	 * After validation comes fortifying show before and after fortification
	 * information of country and soldiers check validation criteria first
	 */
/*
 * public void fortify() {
 * 
 * if(!validateConditions()) return; //If conditions not true, do not proceed
 * 
 * 
 * phaseView.displayMessage("Before Fortification: "+fromCountry.getCountryName(
 * )+":"+ fromCountry.getSoldiers()+" , "+
 * toCountry.getCountryName()+":"+toCountry.getSoldiers());
 * 
 * 
 * fromCountry.removeSoldiers(numSoldiers); toCountry.addSoldiers(numSoldiers);
 * 
 * 
 * phaseView.displayMessage("After Fortification: "+fromCountry.getCountryName()
 * +":"+ fromCountry.getSoldiers()+" , "+
 * toCountry.getCountryName()+":"+toCountry.getSoldiers());
 * 
 * 
 * PlayerFortificationWrapper playerFortificationWrapper =new
 * PlayerFortificationWrapper(fromCountry,toCountry, numSoldiers);
 * 
 * playerService.notifyPlayerServiceObservers(playerFortificationWrapper);
 * 
 * 
 * this.playerService.switchNextPlayer();
 * this.mapService.setState(GameState.REINFORCE);
 * 
 * 
 * }
 * 
 * 
 *//**
	 * This method checks that the following reinforcement criterias are met:
	 * <ul>
	 * <li>Both countries are adjacent</li>
	 * <li>Both countries belong to player</li>
	 * <li>at least 1 player will remain in the source country after
	 * fortification</li>
	 * <ul>
	 */
/*
 * private boolean validateConditions() {
 * 
 * boolValidationMet=true;
 * 
 * checkCountryAdjacency();
 * 
 * if(boolValidationMet) { checkCountriesBelongToCurrentPlayer(); }
 * 
 * if(boolValidationMet) { checkCountryOwnership(); }
 * 
 * if(boolValidationMet) { checkNumSoldiers(); }
 * 
 * return this.boolValidationMet;
 * 
 * }
 * 
 *//**
	 * Check if both countries belong to current player
	 */
/*
 * private void checkCountriesBelongToCurrentPlayer() { Player
 * currentPlayer=playerService.getCurrentPlayer(); String
 * playerName=currentPlayer.getName();
 * 
 * if((!fromCountry.getPlayer().getName().equals(playerName)) ||
 * (!toCountry.getPlayer().getName().equals(playerName))) { phaseView.
 * displayMessage("fromCountry or toCountry does not belong to current player");
 * this.boolValidationMet=false; }
 * 
 * }
 * 
 * 
 *//**
	 * check country has Adjacency
	 */
/*
 * private void checkCountryAdjacency() {
 * 
 * Map<Integer, Set<Integer>> adjacentCountriesList =
 * mapService.getAdjacencyCountriesMap();
 * 
 * Optional<Integer> toId =
 * mapService.findCorrespondingIdByCountryName(toCountry.getCountryName());
 * 
 * Optional<Integer> fromId =
 * mapService.findCorrespondingIdByCountryName(fromCountry.getCountryName());
 * 
 * if(!fromId.isPresent()) {
 * phaseView.displayMessage("Origin country not present");
 * this.boolValidationMet=false; }
 * 
 * 
 * if(!toId.isPresent()) {
 * phaseView.displayMessage("Destination country not present");
 * this.boolValidationMet=false; }
 * 
 * if(boolValidationMet) { neighbouringCountries =
 * adjacentCountriesList.get(fromId.get());
 * 
 * if(!neighbouringCountries.contains(toId.get())) {
 * this.boolValidationMet=false;
 * phaseView.displayMessage("Countries not adjacent to each other"); } }
 * 
 * }
 * 
 * 
 *//**
	 * checks whether the 2 countries are owned by the current player
	 */
/*
 * private void checkCountryOwnership() {
 * 
 * if(!(fromCountry.getPlayer().getName().equalsIgnoreCase
 * (toCountry.getPlayer().getName()))) {
 * 
 * phaseView.displayMessage("Countries do not belong to same player");
 * this.boolValidationMet=false; }
 * 
 * }
 * 
 *//**
	 * check the number of soldiers for the current player Ensures that at least 1
	 * soldier remains in origin country
	 *//*
		 * private void checkNumSoldiers() {
		 * 
		 * if(!(fromCountry.getSoldiers()>numSoldiers)) {
		 * phaseView.displayMessage("Not enough soldiers in origin country");
		 * this.boolValidationMet=false; }
		 * 
		 * if(numSoldiers<1) {
		 * phaseView.displayMessage("Num soldiers must be greater than 0.");
		 * this.boolValidationMet=false; } }
		 * 
		 */