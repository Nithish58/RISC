package com6441.team7.risc.controller;

import com6441.team7.risc.api.exception.ReinforceParsingException;
import com6441.team7.risc.api.model.*;
import com6441.team7.risc.utils.MapDisplayUtils;
import com6441.team7.risc.view.*;
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
    /**
     * the reference of playerService
     */
    private PlayerService playerService;

    /**
     * the reference of phaseView
     */
    private GameView phaseView;

    /**
     * the reference of cardExchangeView which will be constructed locally
     */
    private GameView cardExchangeView;

    /**
     * the number of reinforced armies
     */
    private int reinforcedArmies;

    private boolean isExchangeCardOver;

    /**
     * constructor
     * @param playerService
     */
    public ReinforceGameController(PlayerService playerService) {
        this.playerService = playerService;
        isExchangeCardOver = false;

    }

    /**
     * connect the view to the reinforce controller
     * @param view
     */
    public void setView(GameView view){
        this.phaseView = view;
    }

    /**
     * receive commands from phase view
     * check the command type, if it is reinforce, call reinforce()
     * if it is exchange card, call exchangeCards()
     * if it is show map, call showmap()
     * else the command is not valid, will throw an exception
     * @param command
     * @throws Exception
     */
    @Override
    public void readCommand(String command) throws Exception {
    	
    	this.playerService.getMapService().setState(GameState.ATTACK);
    	
        Player player = playerService.getCurrentPlayer();

        RiscCommand commandType = RiscCommand.parse(StringUtils.split(command, WHITESPACE)[0]);


        switch (commandType) {
            case REINFORCE:
                reinforce(player, command);
                break;
            case EXCHANGE_CARD:
                exchangeCards(player, command);
                break;
            case SHOW_MAP:
                showMap();
                break;
            default:
                throw new IllegalArgumentException("cannot recognize this command");
        }
    }


    /**
     * show map information
     */
    public void showMap(){
        MapDisplayUtils.showFullMap(playerService.getMapService(), phaseView);
    }

    /**
     * validate reinforce command, if the command is valid, call reinforceArmy to put extra armies on countries occupied
     * if the command is not valid, throw an exception and display error message to phaseView
     * @param player
     * @param command
     */
    public void reinforce(Player player, String command){
        try{

            if(!isExchangeCardOver){
                phaseView.displayMessage("exchange cards first before reinforcement");
                return;
            }

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


    /**
     * calculate the reinforced armies, if the army number is 0, reinforce stage is over.
     * else if the army number is not valid, will throw an exception
     * else will reinforce army on the country specified and reduce reinforced army number
     * @param player
     * @param country
     * @param armNum
     */
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

        playerService.reinforceArmy(player, country, armNum);
        reinforcedArmies -= armNum;

    }

    /**
     * if the reinforced army number is reduced to 0
     * @return true if the reinforceArmies to 0
     */
    private boolean isReinforceOver(){
        return reinforcedArmies == 0;
    }


    /**
     * check if the country is occupied by the player
     * @param player
     * @param country
     * @return
     */
    private boolean notOccupiedByPlayer(Player player, String country){
        return !playerService.getConqueredContries(player).contains(convertFormat(country));
    }

    /**
     * caculate the number of reinforced armies
     * rule 1: all conquered countries divided by 3
     * rule 2: get the power of the continent if it is occupied by the player
     * rule 3: if the total number of reinforced armies is less than 3, make it three
     * @param player
     * @return
     */
    public int calculateReinforcedArmies(Player player){

        reinforcedArmies += playerService.getConqueredCountriesNumber(player)/3;

        reinforcedArmies += playerService.getReinforcedArmyByConqueredContinents(player);

        if(reinforcedArmies < 3){ reinforcedArmies = 3; }

        return reinforcedArmies;
    }

    /**
     * exchange cards
     * construct card exchange view
     * validate exchange commands, if it is not valid, throw an exception
     * else if the command is trade in, call tradeInCards() to exchange soliders
     * else if the command is exchange -none, call tradeNone()
     * @param player
     * @param command
     */
    public void exchangeCards(Player player, String command){
        try{
            createCardExchangeView();
            showCardsInfo(player, cardExchangeView);

            String[] commands = StringUtils.split(command, WHITESPACE);

            if(commands.length == 4){
                int cardOne = Integer.parseInt(commands[1]);
                int cardTwo = Integer.parseInt(commands[2]);
                int cardThree = Integer.parseInt(commands[3]);

                tradeInCards(player, cardOne, cardTwo, cardThree);
            }

            else if(commands.length == 2){
                tradeNone(player, commands);
            }
            else{
                throw new ReinforceParsingException(command + " is not valid.");
            }

        } catch (Exception e){
            phaseView.displayMessage("from phase view: " + e.getMessage());
        }finally {
            cardExchangeView.displayMessage("card exchange view close");
        }

    }


    /**
     * create card exchange view
     * subscribe playerService
     */
    private void createCardExchangeView(){
        cardExchangeView = new CardExchangeView();
        playerService.addObserver(cardExchangeView);

    }


    /**
     * if the command is exchangecards -none, if the card number is greater than 5, ask player to exchange cards
     *
     * @param player
     * @param commands
     */
    public void tradeNone(Player player, String[] commands){
        if(commands[1].equalsIgnoreCase("-none")){
            throw new ReinforceParsingException(commands[1] + " is not valid");
        }

        int cardNum = player.getCardList().size();

        if(cardNum >=5){
            phaseView.displayMessage("you must exchange the cards");
        }
        else{
            isExchangeCardOver = true;
        }

    }

    /**
     * display cards owned by the player
     * @param player
     * @param view
     */
    private void  showCardsInfo(Player player, GameView view){
        List<String> cardsInfo = playerService.showCardsInfo(player);

        int count = 1;
        for(String card: cardsInfo){
            view.displayMessage(count + ":" + card + WHITESPACE);
            count ++;
        }
    }


    /**
     * trade in cards from player
     * validate the command, if it is not valid, throw an exception
     * if it is valid, remove corresponding cards from player and display the change on the card exchange view
     * @param player
     * @param cardOne
     * @param cardTwo
     * @param cardThree
     */
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

    /**
     * get number of reinforce armies
     * @return reinforceArmies
     */
    public int getReinforcedArmies(){
        return reinforcedArmies;
    }

    /**
     * make the string lower cases and remove white spaces
     * @param name
     * @return
     */
    private String convertFormat(String name) {
        return StringUtils.deleteWhitespace(name).toLowerCase(Locale.CANADA);
    }


}