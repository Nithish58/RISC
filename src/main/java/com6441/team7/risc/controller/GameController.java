package com6441.team7.risc.controller;

import static com6441.team7.risc.api.RiscConstants.WHITESPACE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.lang3.StringUtils;

import com6441.team7.risc.api.model.Country;
import com6441.team7.risc.api.model.GameState;
import com6441.team7.risc.api.model.MapService;
import com6441.team7.risc.api.model.Player;
import com6441.team7.risc.api.model.RiscCommand;
import com6441.team7.risc.view.CommandPromptView;


/**
 * This GameController class instantiates the 4 other game controllers which each represent a phase.
 * The 4 other controllers are: startup controller, reinforcement controller,
 *  attack controller, fortification controller.
 *  <ul>
 *  <li>This class checks the current game state and "routes" user input to one of the controllers respectively.</li>
 *  <li>Basically this class controls the flow of the game.</li>
 *  <li>This class also keeps track of current player and keeps switching to next player when a player's turn is over.</li>
 *  </ul>
 *
 * @author Keshav
 *
 */
public class GameController {

	private StartupGameController startupPhaseController;
	private ReinforceGameController reinforcementGameController;
	private FortifyGameController fortificationGameController;

	private CommandPromptView view;

	private MapLoaderController mapLoaderController;

	private MapService mapService;

	private ArrayList<Player> players;

	private Player currentPlayer;

	private int currentPlayerIndex;


	AtomicBoolean boolStartUpPhaseOver;
	private boolean boolStartUpPhaseSet=false;
	AtomicBoolean boolFortificationPhaseOver;

	private GameState gameState;

	private int numPlayers;

	/**
	 * This is the constructor of GameController class.
	 * @param mapController Represents the mapLoaderController.
	 * @param mapService Takes as Reference the main map address.
	 */
	public GameController(MapLoaderController mapController,MapService mapService) {

		this.mapLoaderController=mapController;

		this.mapService=mapService;
		this.gameState=this.mapService.getGameState();

		//this.players=new LinkedHashMap<String,Player>();
		this.players=new ArrayList<Player>();

		this.currentPlayerIndex=0;

		this.boolStartUpPhaseOver=new AtomicBoolean(false);

		this.boolFortificationPhaseOver=new AtomicBoolean(false);


		this.startupPhaseController=new StartupGameController(this.mapLoaderController,this.mapService,
				this.players);

		//reinforcementGameController=new reinforceGameController();
		//fortificationGameController=new fortifyGameController();

	}

	/**
	 * This is the method that "routes" user input to the respective phases depending on gamestate.
	 * <ul>
	 * <li>It first initialises game state to startup by default as this state is compulsory.</li>
	 * <li>It then checks whether startup state is over whenever it receives a command.</li>
	 * <li>If yes, gameflow moves to the next state.</li>
	 * <li>Else it continues sending commands to startup as startup needs to be over for game to proceed to next state.</li>
	 * <li> Game State then Loops through reinforcement, attack and fortification phase until game gets over.</li>
	 * <li>
	 * </ul>
	 * @param command Accepts a String of command as parameter and then splits it to evaluate and redirect that command to one state.
	 *
	 */
	public void readCommand(String command) throws IOException {

		if(!boolStartUpPhaseOver.get()) {


			if(!boolStartUpPhaseSet) {
				this.mapService.setState(GameState.START_UP);
				this.boolStartUpPhaseSet=true;
			}

			startupPhaseController.readCommand(command, this.boolStartUpPhaseOver);
		}

		else {

			if(this.mapService.getGameState()==GameState.REINFORCE) {

				//reinforcementGameController.readCommand(command)

				this.currentPlayer=players.get(currentPlayerIndex);

				reinforcementGameController=new ReinforceGameController(this.currentPlayer,
						this.mapService,
						startupPhaseController,
						command,
						view);

			}

			else if(this.mapService.getGameState()==GameState.FORTIFY) {

				fortificationGameController=new FortifyGameController(this.currentPlayer,
						this.mapService,
						this.startupPhaseController,
						command,
						this.boolFortificationPhaseOver);

				switchNextPlayer();

			}

		}

	}

	/**
	 * This method keeps track of the currentPlayerIndex and switches to the next player as soon as a player's
	 * turn is over.
	 *
	 */

	private void switchNextPlayer() {

		if(boolFortificationPhaseOver.get()) {

			if(currentPlayerIndex==players.size()-1) {
				currentPlayerIndex=0;
			}

			else currentPlayerIndex++;

			this.currentPlayer=players.get(currentPlayerIndex);
			view.displayMessage("\nPlayer Turn: "+currentPlayer.getName());

		}

		boolFortificationPhaseOver.set(false);

	}

	public CommandPromptView getView() {
		return view;
	}

	public void setView(CommandPromptView v) {
		this.view=v;
	}

}