package com6441.team7.risc.controller;

import com6441.team7.risc.api.exception.ReinforceParsingException;
import com6441.team7.risc.api.model.*;
import com6441.team7.risc.utils.CommonUtils;
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
 * <ul>
 * <li>Get new armies depending on number of countries the player owned divided by 3, rounded down</li>
 * <li>Get new armies to player according to continent's control value,
 * iff the player own all the countries of an continent.</li>
 * <li></li>
 * </ul>
 * <b>Note: In any case, the minimum number of reinforcement armies is 3. </b>
 * 
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

    /**
     * the value whether the exchange card stage terminates
     */
    private boolean isExchangeCardOver;

    /**
     * constructor
     * @param playerService reference PlayerService
     */
    public ReinforceGameController(PlayerService playerService) {
        this.playerService = playerService;
        isExchangeCardOver = false;

    }

    /**
     * connect the view to the reinforce controller
     * @param view reference the GameView
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
     * @param command reference command
     * @throws Exception on invalid value
     */
    @Override
    public void readCommand(String command) throws Exception {


        Player player = playerService.getCurrentPlayer();
        RiscCommand commandType = RiscCommand.parse(StringUtils.split(command, WHITESPACE)[0]);


        switch (commandType) {
            case REINFORCE:
                reinforce(player, command);
                break;
                
            case EXCHANGE_CARD:
                exchangeCards(player, command);
                break;
                
            case SHOW_CARDS:
            	//showCardsInfo(player.getCardList(),phaseView);
            	//createCardExchangeView();
            	//playerService.deleteObserver(cardExchangeView);
            	break;
                
            case SHOW_PLAYER:
                // showPlayerFortificationPhase(player); (PREVIOUSLY IN BUILD 1)
            	
                MapDisplayUtils.showPlayer(playerService.getMapService(), playerService, phaseView);
                break;
                
            case SHOW_MAP:
                MapDisplayUtils.showMapFullPopulated(playerService.getMapService(), phaseView);
                break;

            case SHOW_PLAYER_ALL_COUNTRIES:
                // showPlayerAllCountriesFortification();
                MapDisplayUtils.showPlayerAllCountries(playerService.getMapService(), playerService, phaseView);
                break;

            case SHOW_PLAYER_COUNTRIES:
                // showPlayerCountriesFortification();
                MapDisplayUtils.showPlayerCountries(playerService.getMapService(), playerService, phaseView);
                break;

            case EXIT:
                CommonUtils.endGame(phaseView);
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
     * @param player reference player
     * @param command reference command
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
     * @param player reference player
     * @param country reference country
     * @param armNum reference num of armies
     */
    public void reinforceArmy(Player player, String country, int armNum){

        if(armNum < 0 || armNum > reinforcedArmies){
            throw new ReinforceParsingException("the number is less than 0 or larger than the number of reinforced solider you have");
        }

        if(notOccupiedByPlayer(player, country)){
            throw new ReinforceParsingException(country + " does not exist or it does not owned by the current player " + player.getName());
        }

        playerService.reinforceArmy(player, country, armNum);
        reinforcedArmies -= armNum;
        phaseView.displayMessage("Now, the left reinforced army is: " + reinforcedArmies);


        if(isReinforceOver()){
            playerService.getMapService().setState(GameState.ATTACK);
            isExchangeCardOver = false;
            return;
        }

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
     * @param player reference player
     * @return number of reinforcer armies
     */
    public int calculateReinforcedArmies(Player player){

        reinforcedArmies += playerService.getConqueredCountriesNumber(player)/3;

        reinforcedArmies += playerService.getReinforcedArmyByConqueredContinents(player);

        if(reinforcedArmies < 3){ reinforcedArmies = 3; }

        return reinforcedArmies;
    }

    /**
     * exchange cards
     * construct card exchange view, subscribe playerService as observer
     * validate exchange commands, if it is not valid, throw an exception
     * else if the command is trade in, call tradeInCards() to exchange soliders
     * else if the command is exchange -none, call tradeNone()
     * @param player reference player
     * @param command reference command
     */
    public void exchangeCards(Player player, String command){
        try{

            createCardExchangeView();

            if(isExchangeCardOver){
                cardExchangeView.displayMessage("the exchange cards stage terminates. enter reinforce command");
                return;
            }

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
            cardExchangeView.displayMessage( e.getMessage());
        }finally {
            cardExchangeView.displayMessage("card exchange view close");
            playerService.deleteObserver(cardExchangeView);
        }

    }


    /**
     * create card exchange view
     * subscribe playerService
     */
    private void createCardExchangeView(){
        cardExchangeView = new CardExchangeView();
        playerService.addObserver(cardExchangeView);
        
        //showCardsInfo(playerService.getCurrentPlayer().getCardList(),cardExchangeView);
    }


    /**
     * if the command is exchangecards -none, if the card number is greater than or equal to 5,
     *  ask player to exchange cards
     *
     * @param player current player
     * @param commands array of strings
     */
    public void tradeNone(Player player, String[] commands){
        if(!commands[1].equalsIgnoreCase("-none")){
            throw new ReinforceParsingException(commands[0] + " " + commands[1] + " is not valid");
        }

        int cardNum = player.getCardList().size();

        if(cardNum >=5){
            phaseView.displayMessage("you must exchange the cards");
        }
        else{
            cardExchangeView.displayMessage("the exchange card phase terminates");
            calculateReinforcedArmies(player);
            phaseView.displayMessage(player.getName() + " get " + reinforcedArmies + " reinforced armies.");
            isExchangeCardOver = true;
        }

    }

    /**
     * display cards owned by the player
     * @param player
     * @param view
     */
    private void  showCardsInfo(List<Card> list, GameView view){
        if (list.isEmpty()){
            view.displayMessage("card list:empty");
            return;
        }

        int count = 1;
        view.displayMessage("card list: ");
        for(Card card: list){
            view.displayMessage(count + ":" + card.getName() + WHITESPACE);
            count ++;
        }
    }


    /**
     * trade in cards from player
     * validate the command, if it is not valid, throw an exception
     * if it is valid, remove corresponding cards from player and display the change on the card exchange view
     * @param player is the player
     * @param cardOne is the first card
     * @param cardTwo is the second card
     * @param cardThree is the third card
     */
    public void tradeInCards(Player player, int cardOne, int cardTwo, int cardThree) {
        int cardSize = player.getCardList().size();

        if(cardOne == cardTwo || cardTwo == cardThree || cardOne == cardThree){
            throw new ReinforceParsingException("card num is not valid");
        }
        if(cardOne > cardSize || cardTwo > cardSize || cardThree > cardSize){
            throw new ReinforceParsingException("card num is not valid");
        }

        Card cardOneName = player.getCardList().get(cardOne - 1);
        Card cardTwoName = player.getCardList().get(cardTwo - 1);
        Card cardThreeName = player.getCardList().get(cardThree - 1);

        List<Card> cardList = new ArrayList<>();
        cardList.add(cardOneName);
        cardList.add(cardTwoName);
        cardList.add(cardThreeName);

        boolean isValid = playerService.isTradeInCardsValid(player,cardList);

        if(!isValid){
            throw new ReinforceParsingException("cards should be all the same or all different.");
        }

        playerService.removeCards(player, cardList);
        reinforcedArmies += playerService.calculateReinforcedArmyByTradingCards(player);
        //cardExchangeView.displayMessage("the reinforced armies received from card exchange is " + player.getTradeInTimes() * 5);
       
        showCardsInfo(player.getCardList(), cardExchangeView);
    }

    /**
     * get number of reinforce armies
     * @return reinforceArmies
     */
    public int getReinforcedArmies(){
        return reinforcedArmies;
    }

    /**
     * check if the exchange cards state is over
     * @return if exchange cards state if over
     */
    public boolean isExchangeCardOver() {
        return isExchangeCardOver;
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