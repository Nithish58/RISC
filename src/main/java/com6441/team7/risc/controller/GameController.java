package com6441.team7.risc.controller;

import com6441.team7.risc.api.model.GameState;
import com6441.team7.risc.api.model.MapService;
import com6441.team7.risc.api.model.Player;
import com6441.team7.risc.view.CommandPromptView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;


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
	
	/**
	 * Object startupPhaseController
	 */
	private StartupGameController startupPhaseController;
	/**
	 * Reference to view
	 */
	private CommandPromptView view;
	/**
	 * Reference to mapLoaderController
	 */
	private MapLoaderController mapLoaderController;
	/**
	 * Reference to mapService
	 */
	private MapService mapService;
	/**
	 * List of players playing the game
	 */
	private ArrayList<Player> players;
	/**
	 * Reference to current player
	 */
	private Player currentPlayer;
	/**
	 * Keeps track of current player index
	 * Used to determine current player and switch to next player in player list
	 */
	private int currentPlayerIndex;

	/**
	 * Atomic Boolean that checks whether startUpPhase is over.
	 * If yes, it does not "route" commands to starUpPhase again.
	 * Instead control keeps looping between Reinforcement and Fortification phases.
	 */
	AtomicBoolean boolStartUpPhaseOver;
	/**
	 * boolean that checks whether current phase is startup.
	 * If true, keeps routing commands to startup game controller.
	 * Does not send to another controllers until startup phase is not over.
	 * This is essential for game flow. e.g: cannot start reinforcing until countries have been populated.
	 */
	private boolean boolStartUpPhaseSet=false;
	/**
	 * Atomic Boolean that is set to true when fortification phase is over. It then helps to switch to next player.
	 */
	AtomicBoolean boolFortificationPhaseOver;
	/**
	 * Reference to gamestate in mapservice
	 */
	private GameState gameState;
	/**
	 * Number of players playing the game
	 */
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

		this.players=new ArrayList<Player>();

		this.currentPlayerIndex=0;

		this.boolStartUpPhaseOver=new AtomicBoolean(false);

		this.boolFortificationPhaseOver=new AtomicBoolean(false);


		this.startupPhaseController=new StartupGameController(this.mapLoaderController,this.mapService,
				this.players);


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
			}

			else if(this.mapService.getGameState()==GameState.FORTIFY) {
				switchNextPlayer();

			}

		}

	}

	/**
	 * This method keeps track of the currentPlayerIndex and switches to the next player as soon as a player's
	 * turn is over.
	 *Uses Atomic Boolean boolFortificationPhaseOver to take decisions.
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
	/**
	 * Getter method for view
	 * @return view
	 */
	public CommandPromptView getView() {
		return view;
	}
	
	/**
	 * Setter method for view
	 * @param view
	 */
	public void setView(CommandPromptView v) {
		this.view=v;
		this.startupPhaseController.setView(v);
	}
	
	public StartupGameController getStartupController() {
		return startupPhaseController;
	}
	
	public ArrayList<Player> getPlayerList(){
		return players;
	}
}