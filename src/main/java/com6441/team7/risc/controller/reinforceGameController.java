package com6441.team7.risc.controller;

import com6441.team7.risc.api.model.Continent;
import com6441.team7.risc.api.model.Country;
import com6441.team7.risc.api.model.GameState;
import com6441.team7.risc.api.model.MapService;
import com6441.team7.risc.api.model.Player;
import com6441.team7.risc.view.CommandPromptView;

import java.util.*;
import java.util.stream.Collectors;

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
public class reinforceGameController {
    private Player player;
    private MapService mapService;
    private CommandPromptView view;
    private int reinforcedArmiesCount;

    /**
     * Sole constructor
     * @param player this parameter is the player who is requesting to reinforce new armiees.
     */
    public reinforceGameController(Player currentPlayer, MapService mapService){
       // this.mapService = new MapService();
    	this.mapService=mapService;
        this.player = currentPlayer;
        this.reinforcedArmiesCount = 0;
        
        System.out.println("Reinforcement:" + currentPlayer.getName());
        
        this.mapService.setState(GameState.FORTIFY);
    }

    /**
     *
     * @return total number of reinforced armies of a player.
     */
    public int getReinforcedArmiesCount(){
        //game rule 1
        this.reinforcedArmiesCount += allCountriesOfPlayer().size()/3;


        return this.reinforcedArmiesCount;
    }

    /**
     *
     * @return list of all countries of player
     */
    public List<Country> allCountriesOfPlayer(){
        List<Country> playerCountries = mapService.getCountries().stream().filter(country ->
                country.getPlayer().getName().equals(player.getName())).collect(Collectors.toList());
        return playerCountries;

    }


}
