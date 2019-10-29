//package com6441.team7.risc.controller;
//
//import static org.junit.Assert.*;
//
//import java.io.File;
//import java.net.URI;
//import java.nio.charset.StandardCharsets;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.atomic.AtomicBoolean;
//
//import org.apache.commons.io.FileUtils;
//import org.junit.After;
//import org.junit.AfterClass;
//import org.junit.Before;
//import org.junit.BeforeClass;
//import org.junit.FixMethodOrder;
//import org.junit.Test;
//import org.junit.runners.MethodSorters;
//
//import com6441.team7.risc.api.model.Card;
//import com6441.team7.risc.api.model.MapService;
//import com6441.team7.risc.api.model.Player;
//import com6441.team7.risc.view.CommandPromptView;
//
///**
// * This test class handles reinforcement phase testing.
// * It has following fields:
// * <ul>
// * <li><b>ReinforceGameController reinforcementController</b>
// * <li><b>Player playerDummy1</b>
// * <li><b>Player playerDummy2</b>
// * <li><b>StartupGameController startup</b>
// * <li><b>MapLoaderController mapController</b>
// * <li><b>MapService mapService</b>
// * <li><b>CommandPromptView cmdView</b>
// * <li><b>ArrayList<Player> players</b>
// * <li><b>AtomicBoolean phase</b>
// * <li>int testNo</b>
// * <li><b>String file</b>
// * <li><b>String loadmapcmd</b>
// * <li><b>String cmd</b>
// * <li><b>int expectedReinforcement</i>
// * </ul>
// *
// */
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//public class ReinforceGameControllerTest {
//	static ReinforceGameController reinforcementController;
//	static Player playerDummy1, playerDummy2;
//	static StartupGameController startup;
//	static MapLoaderController mapController;
//	static GameController game;
//	static MapService mapService;
//	static CommandPromptView cmdView;
//	static ArrayList<Player> players;
//	static AtomicBoolean phase;
//	String file, loadmapcmd;
//	String cmd="";
//	int expectedReinforcement;
//
//	/**
//	 * Upon the creation of the class, MapService, MapLoaderController,
//	 * CommandPromptView, and StartupGameController objects are instantiated.
//	 * The MapController object also set the game view to the CommandPromptView object.
//	 * Player class is instantiated as an array list called players.
//	 * An AtomicBoolean variable called phase is initialized as false.
//	 * @throws Exception on invalid value
//	 */
//	@BeforeClass
//	public static void setUpBeforeClass() throws Exception {
//		mapService = new MapService();
//		mapController = new MapLoaderController(mapService);
//		game = new GameController(mapController, mapService);
//		cmdView = new CommandPromptView(mapController, game);
//		mapController.setView(cmdView);
//		game.setView(cmdView);
//		players = game.getPlayerList();
//		startup = game.getStartupController();
//		phase = new AtomicBoolean(false);
//	}
//
//	/**
//	 * This method is called after all test cases have been performed.
//	 * @throws Exception
//	 */
//	@AfterClass
//	public static void tearDownAfterClass() throws Exception {
//	}
//
//	/**
//	 * Before any test method is invoked, the followings are defined:
//	 * <ul>
//	 * <li><i>map loading is performed by invoking the <i>readCommand</i> from the <i>startup</i> object.</i>
//	 * <li>Two <i>Player</i> objects are instantiated as <i>playerDummy1</i> and <i>playerDummy2</i>.</li>
//	 * <li>The aforementioned <i>Player</i> objects are added to the <i>players<i> array list.</li>
//	 * <li><i>populatecountries</i>, <i>showmap</i>, and <i>placeall</i> are performed.</li>
//	 * <li>cmd is set as <i>reinforce siberia 7</i> and is a param for constructing <i>ReinforceGameController</i></li>
//	 * <li>A <i>ReinforceGameController</i> object is instantiated as <i>reinforcementController</i> with <i>playerDummy1</i>,
//	 * <i>mapService</i>, <i>startup</i>, and <i>cmd</i> as params.</li>
//	 * <li><i>getReinforcedArmiesCount()</i> from the <i>reinforcementController</i> is performed to calculate the number of reinforcements.</li>
//	 * <li><i>expectedReinforcement</i> is set to 7 because the subject player owns 22 countries in this scenario and according
//	 * to rule 1 about reinforcement calculation, the number of reinforcement for each player is the number of the territories owned
//	 * divided by 3.
//	 * </ul>
//	 * @throws Exception
//	 */
//	@Before
//	public void setUp() throws Exception {
//		startup.readCommand("loadmap ameroki.map", phase);
//		playerDummy1 = new Player("John Doe");
//		playerDummy2 = new Player("Jane Doe");
//		players.add(playerDummy1); //first player is added to the players array list
//		players.add(playerDummy2); //second player is addesd to the players array list
//		startup.readCommand("populatecountries", phase);
//		startup.readCommand("showmap", phase);
//		startup.readCommand("placeall", phase);
//		cmd="reinforce siberia 7"; //sets the cmd param
//
//		reinforcementController = new ReinforceGameController(playerDummy1, mapService, startup, cmd,cmdView);
//		reinforcementController.getReinforcedArmiesCount();
//		expectedReinforcement=7;
//
//	}
//
//	/**
//	 * This method is performed after every test is performed.
//	 * @throws Exception
//	 */
//	@After
//	public void tearDown() throws Exception {
//	}
//
//	/**
//	 * This is the test case for calculating number of reinforcements.
//	 * The actual value is retrieved by performing the <i>getReinforcedArmiesCountVal()</i> method
//	 * from the <i>reinforcementController</i> object.
//	 * The test passes if the returned value is the same as the value of <i>expectedReinforcement</i>.
//	 */
//	@Test
//	public void test001_getReinforcedArmiesCount() {
//		assertSame(expectedReinforcement, reinforcementController.getReinforcedArmiesCountVal());
//	}
//
//
//
//}
