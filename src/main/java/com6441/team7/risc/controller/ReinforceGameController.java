package com6441.team7.risc.controller;

import com6441.team7.risc.api.model.Continent;
import com6441.team7.risc.api.model.Country;
import com6441.team7.risc.api.model.GameState;
import com6441.team7.risc.api.model.MapService;
import com6441.team7.risc.api.model.Player;
import com6441.team7.risc.api.model.RiscCommand;
import com6441.team7.risc.view.CommandPromptView;

import static com6441.team7.risc.api.RiscConstants.WHITESPACE;

import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

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
public class ReinforceGameController {
    private Player player;
    private MapService mapService;
    private CommandPromptView view;
    private int reinforcedArmiesCount;
    private StartupGameController startupGameController;


    private String command="";

    /**
     * Getter for the number of reinforcer armies
     * @return amount of reinforced armies
     */
    public int getReinforcedArmiesCountVal() {
        return reinforcedArmiesCount;
    }

    /**
     * Sole constructor
     * @param currentPlayer this parameter is the player who is requesting to reinforce new armies.
     * @param mapService the mapService store current information of currentPlayer.
     */
    public ReinforceGameController(Player currentPlayer, MapService mapService,
                                   StartupGameController sgc, String cmd, CommandPromptView v){
        this.mapService=mapService;
        this.player = currentPlayer;
        this.reinforcedArmiesCount = 0;
        this.command=cmd;

        this.view=v;

        this.startupGameController=sgc;

        getReinforcedArmiesCount();

        int countTurns=0;

        Scanner inputReinforcementScanner=new Scanner(System.in);

        do {

            if(reinforcedArmiesCount==0) break;

            view.displayMessage("You have " + reinforcedArmiesCount +" extra armies");

            if(countTurns!=0) {

                view.displayMessage("You must place all armies to proceed to next phase.");
                String strInput=inputReinforcementScanner.nextLine();
                this.command=new String(strInput);


            }


            readCommand();

            countTurns++;

        }while(reinforcedArmiesCount>0);

        this.mapService.setState(GameState.FORTIFY);

        return;

    }

    private void readCommand() {

        RiscCommand commandType = RiscCommand.parse(StringUtils.split(command, WHITESPACE)[0]);

        switch(commandType) {

            case REINFORCE:

                try {

                    String[] arrCommand =command.split("\\s+");


                    if(arrCommand.length!=3) {
                        view.displayMessage("Invalid Reinforcement Command.");
                    }

                    else {
                        reinforce(arrCommand[1],Integer.parseInt(arrCommand[2]));
                    }
                }

                catch(NumberFormatException e) {}
                break;


            case SHOW_MAP:
                startupGameController.showMapFull();
                break;

            case SHOW_PLAYER:
                showPlayerReinforcementPhase(player);
                break;

            case SHOW_ALL_PLAYERS:
                startupGameController.showAllPlayers();
                break;

            case SHOW_PLAYER_ALL_COUNTRIES:
                showPlayerAllCountriesReinforcement();
                break;

            case SHOW_PLAYER_COUNTRIES:
                showPlayerCountriesReinforcement();
                break;

            default:

                view.displayMessage("Cannot recognize this command in reinforcement. Try Again");

        }

    }


    /**
     * To get total number of reinforced armies of player
     * @return total number of reinforced armies of a player.
     */
    public void getReinforcedArmiesCount(){
        //game rule 1
        this.reinforcedArmiesCount += allCountriesOfPlayer().size()/3;
        //game rule 3
        if (player.meetTradeInCondition()){
            this.reinforcedArmiesCount += 5*player.getTradeInTimes();
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
    }


    /**
     * Reinforce the extra armies
     * @param countryName country where extra armies are added
     * @param num the number of armies
     */
    public void reinforce(String countryName, int num){

        if (mapService.getCountryByName(countryName).isPresent()){

            Country country = mapService.getCountryByName(countryName).get();

            if (allCountriesOfPlayer().contains(country)){

                if (num > reinforcedArmiesCount || num <= 0){

                    view.displayMessage("Sorry, your extra armies number should be in range 1 - "+ reinforcedArmiesCount);

                }else{

                    view.displayMessage("Before reinforcement, "+countryName +" had " +
                            country.getSoldiers()+" soldiers");

                    country.addSoldiers(num);

                    view.displayMessage("After reinforcement, "+ countryName +" has " +
                            country.getSoldiers()+" soldiers");

                    reinforcedArmiesCount -= num;

                }

            }else{
                view.displayMessage(countryName + " belongs to other player.");
            }

        }else{
            view.displayMessage("Sorry, country is not in world map");
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



    private void showPlayerReinforcementPhase(Player p) {
        Collections.sort(p.countryPlayerList, new Comparator<Country>() {

                    @Override
                    public int compare(Country c1, Country c2) {

                        return c1.getContinentName().compareTo(c2.getContinentName());
                    }

                }
        );

        view.displayMessage("Current Player: "+p.getName());

        view.displayMessage("Continent \t\t\t\t Country \t\t\t\t NumArmies");

        for(Country c:p.countryPlayerList) {
            view.displayMessage(c.getContinentName()+"\t\t\t"+c.getCountryName()+"\t\t\t"+c.getSoldiers());
        }
    }

    private void showPlayerCountriesReinforcement() {
        Player currentPlayer=this.player;

        for(Map.Entry<Integer, Set<Integer>> item :
                mapService.getContinentCountriesMap().entrySet()) {

            int key=(int) item.getKey();


            Optional<Continent> optionalContinent=mapService.getContinentById(key);
            Continent currentContinent= (Continent) optionalContinent.get();

            view.displayMessage("\t\t\t\t\t\t\t\t\tContinent "+currentContinent.getName());
            view.displayMessage("\n");

            Set<Integer> value=item.getValue();

            for(Integer i:value) {
                //For Each Country In Continent, Get details + Adjacency Countries
                Optional<Country> optionalCountry=mapService.getCountryById(i);

                Country currentCountry=optionalCountry.get();

                if(currentCountry.getPlayer().getName().equalsIgnoreCase(currentPlayer.getName())) {

                    String strCountryOutput="";

                    strCountryOutput+=currentCountry.getCountryName()+":"+currentCountry.getPlayer().getName()+
                            ", "+currentCountry.getSoldiers()+" soldiers   ";

                    Set<Integer> adjCountryList= mapService.getAdjacencyCountriesMap().get(i);

                    for(Integer j:adjCountryList) {

                        if(mapService.getCountryById(j).get().getPlayer().getName()
                                .equalsIgnoreCase(currentPlayer.getName())){

                            strCountryOutput+=" --> "+mapService.getCountryById(j).get().getCountryName()+
                                    "("+mapService.getCountryById(j).get().getPlayer().getName()+
                                    ":"+mapService.getCountryById(j).get().getSoldiers()+")";
                        }

                    }

                    view.displayMessage(strCountryOutput+"\n");

                }


            }

        }
    }

    private void showPlayerAllCountriesReinforcement() {

        Player currentPlayer=player;

        for(Map.Entry<Integer, Set<Integer>> item :
                mapService.getContinentCountriesMap().entrySet()) {

            int key=(int) item.getKey();


            Optional<Continent> optionalContinent=mapService.getContinentById(key);
            Continent currentContinent= (Continent) optionalContinent.get();

            view.displayMessage("\t\t\t\t\t\t\t\t\tContinent "+currentContinent.getName());
            view.displayMessage("\n");

            Set<Integer> value=item.getValue();

            for(Integer i:value) {
                //For Each Country In Continent, Get details + Adjacency Countries
                Optional<Country> optionalCountry=mapService.getCountryById(i);

                Country currentCountry=optionalCountry.get();

                if(currentCountry.getPlayer().getName().equalsIgnoreCase(currentPlayer.getName())) {

                    String strCountryOutput="";

                    strCountryOutput+=currentCountry.getCountryName()+":"+currentCountry.getPlayer().getName()+
                            ", "+currentCountry.getSoldiers()+" soldiers   ";

                    Set<Integer> adjCountryList= mapService.getAdjacencyCountriesMap().get(i);

                    for(Integer j:adjCountryList) {

                        strCountryOutput+=" --> "+mapService.getCountryById(j).get().getCountryName()+
                                "("+mapService.getCountryById(j).get().getPlayer().getName()+
                                ":"+mapService.getCountryById(j).get().getSoldiers()+")";


                    }

                    view.displayMessage(strCountryOutput+"\n");

                }


            }

        }

    }

    public CommandPromptView getView() {
        return view;
    }

    public void setView(CommandPromptView v) {
        this.view=v;
    }

}