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
    public reinforceGameController(Player player, MapService mapService){
        this.mapService =  mapService;
        this.player = player;
        this.reinforcedArmiesCount = 0;

        System.out.println("Reinforcement:" + this.player.getName());

        this.mapService.setState(GameState.FORTIFY);
    }

    /**
     *
     * @return total number of reinforced armies of a player.
     */
    public int getReinforcedArmiesCount(){
        //game rule 1
        this.reinforcedArmiesCount += allCountriesOfPlayer().size()/3;
        //game rule 3
        if (player.hasDifferentCardsCategory() || player.hasSameCardsCategory()){
            this.reinforcedArmiesCount += 5;
            player.removeCards();
        }

        return this.reinforcedArmiesCount;
    }

    /**
     * To know all the countries a player have
     * @return list of all countries of player
     */
    private List<Country> allCountriesOfPlayer(){
        return mapService.getCountries().stream().filter(country ->country.getPlayer().getName().
                equals(player.getName())).collect(Collectors.toList());

    }

    /**
     * TO know all the continent a player have
     * @return list of continents in which player country is located
     */
    private Set<String> continentOccuppiedByPlayer(){
        return allCountriesOfPlayer().stream().map(Country::getContinentName).collect(Collectors.toSet());

    }
    public List<Country> listOfCountriesInContinent(Continent continent){
        return mapService.getCountries().stream().filter(country -> country.getContinentName().equals(continent.getName())).collect(Collectors.toList());
    }

}
