package com6441.team7.risc.controller;

import com6441.team7.risc.api.exception.ReinforceParsingException;
import com6441.team7.risc.api.model.*;
import com6441.team7.risc.utils.MapDisplayUtils;
import com6441.team7.risc.view.*;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

import static com6441.team7.risc.api.RiscConstants.WHITESPACE;

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
    private int reinforcedArmies;

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
    	
    	this.playerService.getMapService().setState(GameState.ATTACK);
    	
        createCardExchangeView();

        Player player = playerService.getCurrentPlayer();
        showCardsInfo(player, cardExchangeView);

        RiscCommand commandType = RiscCommand.parse(StringUtils.split(command, WHITESPACE)[0]);


        switch (commandType) {
            case REINFORCE:
                reinforce(player, command);
                break;
            case EXCHANGE_CARD:
                exchangeCards(player, command, cardExchangeView);
                break;
            case SHOW_MAP:
                showMap();
                break;
            default:
                throw new IllegalArgumentException("cannot recognize this command");
        }
    }

    private void createCardExchangeView(){
        cardExchangeView = new CardExchangeView();
        playerService.addObserver(cardExchangeView);

    }


    public void showMap(){
        MapDisplayUtils.showFullMap(playerService.getMapService(), phaseView);
    }

    public void reinforce(Player player, String command){
        try{
            String[] commands = StringUtils.split(command, WHITESPACE);

            if(commands.length != 3){
                throw new ReinforceParsingException(command + " is not valid.");
            }

            String country = commands[1];
            int armyNum = Integer.parseInt(commands[2]);

            reinforceArmy(player, country, armyNum);

        } catch (Exception e){
            phaseView.displayMessage("from phase view: " + e.getMessage());
        }
    }


    public void reinforceArmy(Player player, String country, int armNum){

        reinforcedArmies = calculateReinforcedArmies(player);

        if(isReinforceOver()){
            playerService.getMapService().setState(GameState.ATTACK);
            return;
        }

        if(armNum < 0 || armNum > reinforcedArmies){
            throw new ReinforceParsingException("the number is less than 0 or larger than the number of reinforced solider you have");
        }

        if(notOccupiedByPlayer(player, country)){
            throw new ReinforceParsingException(country + " does not exist or it does not owned by the current player " + player.getName());
        }

        playerService.reinforceArmy(country, armNum);
        reinforcedArmies -= armNum;

    }

    private boolean isReinforceOver(){
        return reinforcedArmies == 0;
    }


    private boolean notOccupiedByPlayer(Player player, String country){
        return !playerService.getConqueredContries(player).contains(convertFormat(country));
    }

    public int calculateReinforcedArmies(Player player){
        int num = 0;
        num += playerService.getConqueredCountriesNumber(player)/3;

        num += playerService.getConqueredContinentNumber(player);

        if(num < 3){ num = 3; }

        return num;
    }

    public void exchangeCards(Player player, String command, GameView view){
        try{

            String[] commands = StringUtils.split(command, WHITESPACE);

            if(commands.length == 4){
                int cardOne = Integer.parseInt(commands[1]);
                int cardTwo = Integer.parseInt(commands[2]);
                int cardThree = Integer.parseInt(commands[3]);

                tradeInCards(player, cardOne, cardTwo, cardThree);
            }

            else if(commands.length == 2){
                //tradeNone();
            }
            else{
                throw new ReinforceParsingException(command + " is not valid.");
            }

        } catch (Exception e){
            phaseView.displayMessage("from phase view: " + e.getMessage());
        }

    }

    private void showCardsInfo(Player player, GameView view){
        List<String> cardsInfo = playerService.showCardsInfo(player);

        int count = 1;
        for(String card: cardsInfo){
            view.displayMessage(count + ":" + card + WHITESPACE);
            count ++;
        }
    }

    public void tradeInCards(Player player, int cardOne, int cardTwo, int cardThree) {
        int cardSize = player.getCardList().size();

        if(cardOne > cardSize || cardTwo > cardSize || cardThree > cardSize){
            throw new ReinforceParsingException("card num is not valid");
        }

        String cardOneName = player.getCardList().get(cardOne - 1);
        String cardTwoName = player.getCardList().get(cardTwo - 1);
        String cardThreeName = player.getCardList().get(cardThree - 1);

        List<String> cardList = new ArrayList<>();
        cardList.add(cardOneName);
        cardList.add(cardTwoName);
        cardList.add(cardThreeName);

        boolean isValid = playerService.isTradeInCardsValid(player,cardList);

        if(!isValid){
            throw new ReinforceParsingException("cards should be all the same or all different.");
        }

        playerService.removeCards(player, cardList);
        reinforcedArmies += player.getTradeInTimes() * 5;
    }


    private String convertFormat(String name) {
        return StringUtils.deleteWhitespace(name).toLowerCase(Locale.CANADA);
    }
}