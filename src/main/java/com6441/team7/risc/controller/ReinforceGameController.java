package com6441.team7.risc.controller;

import com6441.team7.risc.api.model.*;
import com6441.team7.risc.view.*;

import java.util.*;

/**
 * <h1>The Reinforcement phase</h1>
 * This class basically implements the Reinforcement phase of the game.
 * This phase helps in getting and placing new armies before attack and fortification phase
 * There are two task in this phase. First to find correct number of reinforcement armies according to risk rules
 * and second to place the all the reinforcement armies on the map
 * <p>
 * The player gets new armies depending on Risk Rules:
 * <li>Get new armies depending on number of countries the player owned divided by 3, rounded down<./li>
 * <li>Get new armies to player according to continent's control value,
 * iff the player own all the countries of an continent.</li>
 * <li></li>
 * <b>Note: In any case, the minimum number of reinforcement armies is 3. </b>
 * </p>
 */
public class ReinforceGameController implements Controller{
    private PlayerService playerService;
    private GameView phaseView;
    private GameView cardExchangeView;


    public ReinforceGameController(PlayerService playerService) {
        this.playerService = playerService;

    }

    public void setView(GameView view){
        this.phaseView = view;
    }

    //TODO: read command from phaseView and validate command here
    //TODO: if the command is valid, call corresponding method in playerService
    //TODO: cardExchangeView could be constructed locally in the method
    @Override
    public void readCommand(String command) throws Exception {

    }
}