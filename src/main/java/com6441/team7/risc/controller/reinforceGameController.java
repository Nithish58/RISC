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
     * @param currentPlayer this parameter is the player who is requesting to reinforce new armies.
     * @param mapService the mapService store current information of currentPlayer.
     */
    public reinforceGameController(Player currentPlayer, MapService mapService){
        this.mapService=mapService;
        this.player = currentPlayer;
        this.reinforcedArmiesCount = 0;

        System.out.println("Reinforcement: " + currentPlayer.getName());

        this.mapService.setState(GameState.FORTIFY);
    }

    /**
     * To get total number of reinforced armies of player
     * @return total number of reinforced armies of a player.
     */
    private void getReinforcedArmiesCount(){
        //game rule 1
        this.reinforcedArmiesCount += allCountriesOfPlayer().size()/3;
        //game rule 3
        if (player.hasDifferentCardsCategory() || player.hasSameCardsCategory()){
            this.reinforcedArmiesCount += 5;
            player.removeCards();
        }
        // Game rule 2 continentValue
        for (String item: continentOccuppiedByPlayer()){
            if (listOfCountriesInContinentOfPlayer(item).containsAll(mapService.findCountryByContinentName(item))){
                for (Continent continent:mapService.getContinents()){
                    if (continent.getName().equals(item)){
                        reinforcedArmiesCount += continent.getContinentValue();
                    }
                }
            }

        }
        System.out.println("You have " + reinforcedArmiesCount +"extra armies");
    }

    /**
     * Reinforce the extra armies
     * @param country country where extra armies are added
     * @param num the number of armies
     */
    public void reinforce(Country country, int num){
        getReinforcedArmiesCount();
        while (reinforcedArmiesCount !=0){
            if (allCountriesOfPlayer().contains(country)){
                if (num > reinforcedArmiesCount || num < reinforcedArmiesCount){
                    System.out.println("Sorry, your extra armies number should be in range 1 -"+ reinforcedArmiesCount);
                }else{
                    System.out.println("The "+ country +"has" + country.getSoldiers()+" soldiers");
                    country.addSoldiers(num);
                    System.out.println("After reinforcement, the "+ country +"has" + country.getSoldiers()+" soldiers");
                    reinforcedArmiesCount -= num;
                }
            }else{
                System.out.println("Sorry! couldn't find the country");
            }
            System.out.println("The reinforcement phase completed");
        }
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
        return allCountriesOfPlayer().stream().map(Country::getCountryName).collect(Collectors.toSet());
    }

    /**
     * To store all the countries of specific continents of player.
     * @param continentName continent whose country list has to be found.
     * @return list of countries that is specific continent.
     */
    public List<Country> listOfCountriesInContinentOfPlayer(String continentName ){
        return allCountriesOfPlayer().stream().filter(country -> country.getContinentName().equals(continentName)).collect(Collectors.toList());
    }

}
