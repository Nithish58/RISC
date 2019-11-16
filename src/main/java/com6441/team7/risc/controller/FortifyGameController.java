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
public class  FortifyGameController implements Controller {

	/**
	 * a reference of playerService
	 */
	private PlayerService playerService;

	/**
	 * a reference of mapService
	 */
	private MapService mapService;

	/**
	 * a reference of GameView
	 */
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

	/**
	 * constructor to set playerService
	 * @param playerService PlayerService
	 */
	public FortifyGameController(PlayerService playerService) {
		this.playerService = playerService;
		this.mapService = this.playerService.getMapService();
	}

	/**
	 * set the view
	 * @param view GameView
	 */
	public void setView(GameView view) {
		this.phaseView = view;
	}


	/**
	 * extends from method in IController
	 * check validity of the commands from player
	 * if the command is valid, it will call corresponding methods
	 * if not, display error messages to the phase view
	 * @param command Command
	 * @throws Exception on invaliad
	 */
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

			// fortify(); (BUILD 1 PREVIOUSLY - LOGIC WAS IN CONTROLLER ITSELF - NOW CONTROLLER MOSTLY DOES ONLY VALIDATION)

			// FORTIFY CALLED IN PLAYER CLASS

			PlayerFortificationWrapper playerFortificationWrapper = new PlayerFortificationWrapper(fromCountry,
					toCountry, numSoldiers);

			playerService.getCurrentPlayer().fortify(playerService, playerFortificationWrapper);

		}

		else {
			phaseView.displayMessage("Invalid Fortification Command. Try Again");
			return;
		}

	} // End of method

	
} // End of class
