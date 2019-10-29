

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

import com6441.team7.risc.api.model.PlayerService;
import com6441.team7.risc.view.GameView;
import org.apache.commons.lang3.StringUtils;

/**
 * This class represents the fortification phase
 * It accepts fortification commands from user and some show commands and rejects all other commands
 * It checks whether fortification criteria are met.
 * It then carries out fortification
 * After fortification, it updates gamestate to reinforcement
 */
public class FortifyGameController implements Controller{


	private PlayerService playerService;
	private GameView phaseView;

	public FortifyGameController(PlayerService playerService){
		this.playerService = playerService;
	}

	public void setView(GameView view){
	    this.phaseView = view;
    }

    //TODO: read command from phaseView and validate command here
    //TODO: if the command is valid, call corresponding method in playerService
    //TODO: if the command is not valid, call phaseView.displayMessage() to show error messages
	@Override
	public void readCommand(String command) throws Exception {

	}
}