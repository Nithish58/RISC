package com6441.team7.risc.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com6441.team7.risc.api.wrapperview.PlayerAttackWrapper;
import com6441.team7.risc.api.wrapperview.PlayerFortificationWrapper;
import com6441.team7.risc.utils.CommonUtils;
import org.apache.commons.collections4.CollectionUtils;

import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com6441.team7.risc.api.RiscConstants.*;

/**
 * store player information
 */
public class Player{

    /**
     * player name
     */
    private String name;

    /**
     * number of army a player has
     */
    private int armies;

    /**
     * list of cards a player has
     */
    private List<Card> cardList;

    /**
     * number of trade-in times
     */
    private int tradeInTimes;

    /**
     * number of card categories
     */
    private static final int CARD_CATEGORY_NUMBER = 3;

	/**
	 * category of the player
	 */
	private PlayerCategory playerCategory;


	/**
     *  a list of country a player has
     */
	@JsonIgnore
    private ArrayList<Country> countryPlayerList;


    /**
     * update countryPlayerList
     * @param mapService stores map information
     */
	public void updateCountryPlayerList(MapService mapService){
		
		
		  for(Country c:mapService.getCountries()) {
		  if(c.getPlayer().getName().equalsIgnoreCase(name)) {
	
		  countryPlayerList.add(c); }
		  }
	    	    		 	    
    }
    /**
     * Constructor for this class
     * @param name Name
     */
    public Player(String name) {
        this.armies = 0;
        this.name = name;
        this.cardList = new ArrayList<>();

        this.tradeInTimes=0;
        this.countryPlayerList=new ArrayList<>();
    }

    /**
     * Constructor for this class
     */
    public Player(){
        this.cardList = new ArrayList<>();
        this.countryPlayerList=new ArrayList<>();
    }

    /**
     * Constructor for this class
     * @param name name of player
     * @param category Player category
     */
	public Player(String name, PlayerCategory category){
    	this.name = name;
    	this.setPlayerCategory(category);
	}
    
    /** get the player category
	 * @return the playerCategory
	 */
	public PlayerCategory getPlayerCategory() {
		return playerCategory;
	}
	
	/** set the player category
	 * @param playerCategory the playerCategory to set
	 */
	public void setPlayerCategory(PlayerCategory playerCategory) {
		this.playerCategory = playerCategory;
	}
	
	/**
     * Setter method for type of player
     * @param strCategory category of player in string format
     */
    public void generatePlayerCategory(String strCategory) {
    	
    	switch(strCategory) {
    		
    		case "aggressive":
    			this.setPlayerCategory(PlayerCategory.AGGRESSIVE);
    			break;
  
    		case "random":
    			this.setPlayerCategory(PlayerCategory.RANDOM);
    			break;
    			
    		case "cheater":
    			this.setPlayerCategory(PlayerCategory.CHEATER);
    			break;
    			
    		case "benevolent":
    			this.setPlayerCategory(PlayerCategory.BENEVOLENT);
    			break;
    			
    		default:
    			this.setPlayerCategory(PlayerCategory.HUMAN);
    			
    			break;
    	}	
    	
    }


    /**
     * get name of player
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * set name of player
     * @param name name to be added
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * add the country to the countryPlayerList
     * @param c country
     */
    public void addCountryToPlayerList(Country c) {

        this.countryPlayerList.add(c);

    }


    /**
     * remove country c from countryPlayerList
     * @param c country
     */
    public void removeCountryFromPlayerList(Country c) {

        String countryName=c.getCountryName();

        for(int i=0;i<countryPlayerList.size();i++) {
            if(countryPlayerList.get(i).getCountryName().equals(countryName)) {
                countryPlayerList.remove(i);
                break;
            }
        }


    }

    /**
     * get country list occupied of the player
     * @return list of country
     */
    @JsonIgnore
    public ArrayList<Country> getCountryPlayerList() {
        return countryPlayerList;
    }


    
    //---------------------------------STRATEGY UTILS------------------------------------------
    
    /**
     * strategy player
     */
    @JsonIgnore
    private StrategyPlayer strategy;
    
    /**
     * sets strategy
     * @param strategy strategy to be set
     */
    @JsonIgnore
    public void setStrategy(StrategyPlayer strategy) {
    	
    	this.strategy = strategy;
    	
    }
    
    /**
     * gets strategy
     * @return returns strategy
     */
    @JsonIgnore
    public StrategyPlayer getStrategy() {
    	return strategy;
    }


    //------------------------------------REINFORCEMENT-----------------------------------------



    /**
     * get number of armies
     * @return armies number
     */
    public int getArmies() {
        return armies;
    }

    /**
     * set armies to player
     * @param armies number of armies
     */
    public void setArmies(int armies) {
        this.armies = armies;
    }

    /**
     * reduce the number of army from player
     * @param number Number
     */
    public void reduceArmy(int number){
        armies -= number;
    }

    /**
     * add the number of army to player
     * @param number Number
     */
    public void addArmy(int number){
        armies += number;
    }


    /**
     * reinforce army to the player of its country occupied
     * @param country Country
     * @param armyNum ArmyNum
     * @param mapService MapService
     */
    public void reinforceArmy(String country, int armyNum, MapService mapService){
        mapService.reinforceArmyToCountry(country, armyNum);
    }

    /**
     * get list of cards
     * @return card list
     */
    public List<Card> getCardList() {
        return cardList;
    }

    /**
     * set card list
     * @param cardList new cardlist
     */
    public void setCardList(List<Card> cardList) {
        this.cardList = cardList;
    }



    /**
     * check if the trade in cards meet the trade in condition
     * @param cardList Card List
     * @return true if valid false if not valid
     */
    public boolean meetTradeInCondition(List<Card> cardList){
        if(hasThreeSameCards(cardList) || hasThreeDifferentCards(cardList)){
            return true;
        }

        return false;
    }



    /**
     * check if the three cards are the same
     * @param cardList Card List
     * @return true if all the cards are the same, false if not
     */
    public boolean hasThreeSameCards(List<Card> cardList){
        if(cardList.get(0).getName().equalsIgnoreCase(cardList.get(1).getName()) &&
                cardList.get(1).getName().equalsIgnoreCase(cardList.get(2).getName())){
            return true;
        }

        return false;
    }

    /**
     * check if the three cards are all different
     * @param cardList Card List
     * @return true if all three cards are different, false if not
     */
    public boolean hasThreeDifferentCards(List<Card> cardList){
        if(!cardList.get(0).getName().equalsIgnoreCase(cardList.get(1).getName()) &&
                !cardList.get(1).getName().equalsIgnoreCase(cardList.get(2).getName()) &&
                !cardList.get(0).getName().equalsIgnoreCase(cardList.get(2).getName())){
            return true;
        }

        return false;
    }

    /**
     * check if the player has same three cards
     * @return true if players has three same cards, false is not
     */
    public boolean hasSameCardsCategory(){
        return Stream.of(Card.ARTILLERY, Card.CAVALRY, Card.INFANTRY)
                .anyMatch(this::hasSameCardCategory);
    }

    /**
     * check if the player has three different cards
     * @return true if players has three different cards, false if not
     */
    public boolean hasDifferentCardsCategory(){
        return new HashSet<>(cardList).size() >= CARD_CATEGORY_NUMBER;
    }



    /**
     * remove trade in cards from the player cards
     * @param list Card List
     */
    public void removeCards(List<Card> list){
        Card cardOne = cardList.stream()
                .filter(card -> card.getName().equalsIgnoreCase(list.get(0).getName()))
                .findFirst().get();

        Card cardTwo = cardList.stream()
                .filter(card -> card.getName().equalsIgnoreCase(list.get(1).getName()))
                .findFirst().get();

        Card cardThree = cardList.stream()
                .filter(card -> card.getName().equalsIgnoreCase(list.get(2).getName()))
                .findFirst().get();


        cardList.remove(cardOne);
        cardList.remove(cardTwo);
        cardList.remove(cardThree);

        tradeInTimes ++;

    }


    /**
     * add card to player
     * @param card drawn/gained
     */
    public void addCard(Card card){
        cardList.add(card);
    }


    /**
     * remove cards from players
     */
    public void removeCards(){
        if(hasSameCardCategory(Card.ARTILLERY)){
            List<Card> list = cardList.stream()
                    .filter(card -> card == Card.ARTILLERY)
                    .limit(CARD_CATEGORY_NUMBER)
                    .collect(Collectors.toList());

            cardList.removeAll(list);
        }

        if(hasSameCardCategory(Card.INFANTRY)){
            List<Card> list = cardList.stream()
                    .filter(card -> card == Card.INFANTRY)
                    .limit(CARD_CATEGORY_NUMBER)
                    .collect(Collectors.toList());

            cardList.removeAll(list);
        }

        if(hasSameCardCategory(Card.CAVALRY)){
            List<Card> list = cardList.stream()
                    .filter(card -> card == Card.CAVALRY)
                    .limit(CARD_CATEGORY_NUMBER)
                    .collect(Collectors.toList());

            cardList.removeAll(list);
        }

        if(hasDifferentCardsCategory()){
            cardList.removeAll(new HashSet<>(cardList));
        }

        tradeInTimes ++;

    }

    /**
     * get trade in times of player
     * @return tradeInTimes
     */
    public int getTradeInTimes() {
        return tradeInTimes;
    }

    /**
     * set trade in times of player
     * @param tradeInTimes tradeinTimes value
     */
    public void setTradeInTimes(int tradeInTimes) {
        this.tradeInTimes = tradeInTimes;
    }

    /**
     * get number of reinforced armies by 5 * tradeInTimes
     * @return int
     */
    public int calculateReinforcedArmyByTradingCards(){
        return tradeInTimes * 5;
    }

    /**
     * check whether player has same card category
     * @param card card
     * @return true or false
     */
    public boolean hasSameCardCategory(Card card){
        return cardList.stream()
                .filter(card::equals)
                .count() == CARD_CATEGORY_NUMBER;
    }

    

    /**
     * Exchange cards for automated strategies
     * @param playerService exchange card on player service
     */
    public void checkAndExchangeCardsForStrategy(PlayerService playerService) {
    	this.playerService=playerService;
    	
    	if(cardList.size()<3) return; //Cannot exchange  	
    	
    	//Create Array containing numOfDifferent Card Types
    	//Count num of cards of each type and place it in array
    	
    	int[] arrCardTypeCount=new int[3];
   
    	Card[] arrCardType=new Card[3];
    	
    	arrCardType[0]=Card.INFANTRY;
    	arrCardType[1]=Card.CAVALRY;
    	arrCardType[2]=Card.ARTILLERY;
    	
    	
    	for(Card c:cardList) {
    		
    		if(c==Card.INFANTRY) arrCardTypeCount[0]++;
    		
    		else if(c==Card.CAVALRY) arrCardTypeCount[1]++;
    		
    		else arrCardTypeCount[2]++;
    	
    	}
    	
    	//Display total cards info:
    	
		String strCardInfo="";
		strCardInfo+=Card.INFANTRY+": "+arrCardTypeCount[0]+"  ";
		strCardInfo+=Card.CAVALRY+": "+arrCardTypeCount[1]+" ";
		strCardInfo+=Card.ARTILLERY+": "+arrCardTypeCount[2]+"\n";
		
		playerService.notifyPlayerServiceObservers(strCardInfo);
    	
    	//Check similar types of cards first...if possible, exchange
    	
    	//Check remainder that will remaining after sets of 3 cards will be exchanged
    	//Find the number of sets of 3 cards that can be exchanged
    	//increase tradeInTimes based on num of exchangeable sets
    	//set new num of cards to remainder
    	
    	int numTradeSetsPossible=0;
    	
    	for(int i=0;i<arrCardTypeCount.length;i++) {
    		
    		int remainderCardsAfterExchange=0;

    		remainderCardsAfterExchange= (arrCardTypeCount[i] % 3);
    		
    		numTradeSetsPossible=(arrCardTypeCount[i]-remainderCardsAfterExchange) / 3;
    		
    		if(numTradeSetsPossible<=0) {
    			playerService.notifyPlayerServiceObservers
    			("No Similar Card Exchange Possible for "+arrCardType[i]);
    			
    			continue; //Go directly to next card type
    		}
    		
    		playerService.notifyPlayerServiceObservers(numTradeSetsPossible
    				+" Similar Card Exchange Sets Possible for "+arrCardType[i]);
    		
    		this.tradeInTimes+=numTradeSetsPossible;
    		
    		//Need to actually trade the cards now
    		//By Removing the cards and returning them to deck
    		
    		//Find index at which card occurs for all n cards to be exchanged
    		//Remove the cards from the player list
    		//Return the removed cards to the deck again
    		
    		for(int j=0;j<(numTradeSetsPossible*3);j++){
    			
    			int indexCard=cardList.indexOf(arrCardType[i]);
    			
    			playerService.returnToDeck(cardList.remove(indexCard));   			
    			
    		}   // End of card exchange for numTradeSets Possible   		
    		
    		arrCardTypeCount[i]=remainderCardsAfterExchange; //Set remainder cards remaining
    		
    	}  //End of card exchange for for similar card types
    	
    	
    	
    	//After checking if similar exchanges possible, now check if different set types possible
    	
    	//Keep on Exchanging 3 different cards at a time until no more possible
    	while(arrCardTypeCount[0]>0 && arrCardTypeCount[1]>0 && arrCardTypeCount[2]>0) {
    		
    		playerService.notifyPlayerServiceObservers("Exchanged 3 different cards.");
    		this.tradeInTimes++;
    		
    		for(int k=0;k<3;k++) {
    			
    			int indexCard=cardList.indexOf(arrCardType[k]);
    			
    			playerService.returnToDeck(cardList.remove(indexCard));   
    			
    			arrCardTypeCount[k]--;
    			
    		}
    		
    	} //End of Card Exchanged for different types as well
    	playerService.notifyPlayerServiceObservers("End Of Card Exchange");
    	
    } //End of card exchanges
    
    
    /**
     * Calculates reinforcement armies based on cards for continents and countries
     * @param playerService gets details from player service
     * @return returns calculated armies
     */
    public int calculateReinforcedArmiesBasedOnCardsContinentsCountries(PlayerService playerService){
    	this.playerService=playerService;
    	
    	int reinforcedArmiesForStrategy=0;

    	//For Cards
    	reinforcedArmiesForStrategy=playerService.calculateReinforcedArmyByTradingCards(this);
    	
    	//For Countries
    	reinforcedArmiesForStrategy += playerService.getConqueredCountriesNumber(this)/3;

    	//For Continents
    	reinforcedArmiesForStrategy += playerService.getReinforcedArmyByConqueredContinents(this);

        if(reinforcedArmiesForStrategy < 3){ reinforcedArmiesForStrategy = 3; }

        return reinforcedArmiesForStrategy;
    }
    
    


    //----------------------------------ATTACK--------------------------------------------------



    /**
     * a reference of attack country
     */
    private Country fromCountryAttack;

    /**
     * a reference of defender country
     */
    private Country toCountryAttack;

    /**
     * number of attacking soldiers
     */
    private int numAttackingSoldiers=0;

    /**
     * number of defending soldiers
     */
    private int numDefendingSoldiers=0;

    /**
     * number of dices from attackers
     */
    private int numDiceAttacker=0;

    /**
     * number of dices from defenders
     */
    private int numDiceDefender=0;

    /**
     * array of dices from attacker
     */
    private int[] attackerDice;

    /**
     * array of dices from defender
     */
    private int[] defenderDice;

    /**
     * a reference of SecureRandom to random dice number
     */
    private SecureRandom diceRandomizer;

    /**
     * a boolean value whether attack is allOut
     */
    private boolean boolAllOut;

    /**
     * a boolean value whether attack is over
     */
    private boolean boolAttackOver;

    /**
     * a boolean value if attackMoveRequired after country conquered
     */
    private boolean boolAttackMoveRequired;

    /**
     * boolean value that determines whether a card needs to be drawn.
     * Set to true when a country is conquered.
     * Used when ending attack phase
     */
    private boolean boolDrawCard=false;

    /**
     * a boolean value if defendDiceRequired
     */
    private AtomicBoolean boolDefendDiceRequired;

    /**
     * a reference of attack player
     */
    private Player attacker;

    /**
     * a reference of defend player
     */
    private Player defender;

    /**
     * a reference of player service
     */
    private PlayerService playerService;

    /**
     * a boolean value if attack is valid
     */
    private boolean boolAttackValidationMet;

    /**
     * a boolean value if country is conquered
     */
    private boolean boolCountryConquered;

    /**
     *  a reference of PlayerAttackWrapper
     */
    private PlayerAttackWrapper playerAttackWrapper;

    /**
     * String that contains information about what is happening in attack phase
     * Sent to observers
     */
    private String strSendAttackInfoToObservers="";

    /**
     * attack method. set the value of attributes
     * check boolean value boolAllOut, if allout, call attackAllOut(). if not, call attackSingle()
     * @param playerService playerService
     * @param playerAttackWrapper playerAttackWrapper
     */
    public void attack(PlayerService playerService, PlayerAttackWrapper playerAttackWrapper){

        this.fromCountryAttack=playerAttackWrapper.getFromCountry();
        this.toCountryAttack=playerAttackWrapper.getToCountry();

        this.attacker=fromCountryAttack.getPlayer();
        this.defender=toCountryAttack.getPlayer();

        this.boolAllOut=playerAttackWrapper.getBooleanAllOut();
        this.boolAttackOver=playerAttackWrapper.getBoolAttackOver();

        this.numDiceAttacker=playerAttackWrapper.getNumDiceAttacker();
        this.numDiceDefender=playerAttackWrapper.getNumDiceDefender();

        //this.boolAttackMoveRequired=playerService.getBoolAttackMoveRequired();
        this.boolDefendDiceRequired=playerAttackWrapper.getBoolDefenderDiceRequired();
        this.boolAttackMoveRequired=false;

        this.numAttackingSoldiers = this.fromCountryAttack.getSoldiers();
        this.numDefendingSoldiers = this.toCountryAttack.getSoldiers();

        this.playerService=playerService;
        
        
        //If boolAllOut is chosen
        //boolAllOut is set to true in playerAttackWrapper by GameController.We retrieve and
        //check this boolean here.
        
        if(boolAllOut) {
            attackAllOut(playerService);
            return;
        }

        attackSingle(playerService);

    }


    /**
     * attack once
     * check if the attack is valid, if yes, roll the dice and compare the results
     * if not, just return
     * Triggers notification to playerservice observers about validation if not passed
     * @param playerService a reference of PlayerService
     */
    public void attackSingle(PlayerService playerService) {

        //check the validity of countries owned by attacker and defender and number of soldiers in attacker's country

        //Send initial attack information
        constructAndSendInitialSingleAttackInformation();

        if (!validateAttackConditions(playerService)) {

            //notify playerService observer if it's not valid
            strSendAttackInfoToObservers+="\nConditions Not Valid. Cannot proceed with attack.";
            playerService.notifyPlayerServiceObservers(strSendAttackInfoToObservers);
            return;
        }

        //Attacker and defender roll their dices
        attackerDice = rollAttackerDice(numDiceAttacker);
        defenderDice = rollDefenderDice(numDiceDefender);

        //Decide the winner
        decideBattleResult(attackerDice, defenderDice);
    }


    /**
     * attack until soldiers from either attacker or defender is out
     * validate the validity of attack, if yes, roll the dice and compare attacking results
     * if not, just return. 
     * Notifies playerservice observers when validation conditions not met
     * @param playerService a reference of PlayerService
     *
     */
    public void attackAllOut(PlayerService playerService) {


        this.numDiceAttacker = MAX_ATTACKER_DICE_NUM;
        this.numDiceDefender = MAX_DEFENDER_DICE_NUM;
        
        

        //while (!checkDefenderPushedOut() || !isAttackerLastManStanding()) {
        while (!defenderPushedOut() || !isAttackerLastManStanding()) {

            //Update numSoldiers everytime attack is being done
            this.numAttackingSoldiers=fromCountryAttack.getSoldiers();
            this.numDefendingSoldiers=toCountryAttack.getSoldiers();

            //Checks the condition of both sides to determine how many number of dices are allowed
            if (this.numAttackingSoldiers <= MAX_ATTACKER_DICE_NUM)
                this.numDiceAttacker = this.numAttackingSoldiers-1;

            if (this.numDefendingSoldiers < MAX_DEFENDER_DICE_NUM)
                this.numDiceDefender = this.numDefendingSoldiers;


            if (!validateAttackConditions(playerService)) {

                //notify playerService observer if it's not valid
                //playerService.notifyObservers(this.playerAttackWrapper);
                strSendAttackInfoToObservers+="\nConditions Not Valid. Cannot proceed with attack.";
                playerService.notifyPlayerServiceObservers(strSendAttackInfoToObservers);
                return;
            }

            //Attacker and defender roll their dices
            attackerDice = rollAttackerDice(numDiceAttacker);
            defenderDice = rollDefenderDice(numDiceDefender);

            //this.boolCountryConquered=false;

            //Decide the winner
            decideBattleResult(attackerDice, defenderDice);

        }
    }


    /**
     * This method decides the result of every battle after both attacker and defender
     * throw their dices.
     * Each side is assigned second dice value to be compared with each other
     * if both sides throw at least two dices.
     * Calls constructAndSendInitialInfo method to trigger notif to playerservice observers.
     * Triggers notif to domination view
     * @param attackerDice attacker's dice
     * @param defenderDice defender's dice
     * 
     */
    public void decideBattleResult(int[] attackerDice, int[] defenderDice) {

        Arrays.sort(attackerDice);
        Arrays.sort(defenderDice);

        int attackerMaxValue = 0;
        int attackerSecondMaxValue = 0;
        int defenderMaxValue = 0;
        int defenderSecondMaxValue = 0;

        // decide the maximum and second maximum(if any) values of attacker's dice
        // based on the number of the dice thrown
        switch (numDiceAttacker) {

            case 3:
                attackerMaxValue = attackerDice[2];
                attackerSecondMaxValue = attackerDice[1];
                break;

            case 2:
                attackerMaxValue = attackerDice[1];
                attackerSecondMaxValue = attackerDice[0];
                break;

            case 1:
                attackerMaxValue = attackerDice[0];
                break;
        }


        // decide the maximum and second maximum(if any) values of defender's dice
        // based on the number of the dice thrown
        switch (numDiceDefender) {
            case 2:
                defenderMaxValue = defenderDice[1];
                defenderSecondMaxValue = defenderDice[0];
                break;
            case 1:
                defenderMaxValue=defenderDice[0];
                break;
        }

        //Reset String
        strSendAttackInfoToObservers="";

        // choose how the battle is decided
        // based on the number of attacker's and defender's dices
        if (((numDiceAttacker == 3) && (numDiceDefender == 2))
                || ((numDiceAttacker == 2) && numDiceDefender == 2)) {

            // Calculate the highest values of the both players' dices
            if (attackerMaxValue > defenderMaxValue) {

                toCountryAttack.removeSoldiers(1);
                strSendAttackInfoToObservers="Defender loses 1 soldier.";

            } else {

                fromCountryAttack.removeSoldiers(1);
                strSendAttackInfoToObservers="Attacker loses 1 soldier.";

            }

            if (attackerSecondMaxValue > defenderSecondMaxValue) {

                toCountryAttack.removeSoldiers(1);
                strSendAttackInfoToObservers+="\nDefender loses 1 soldier.";

            } else {

                fromCountryAttack.removeSoldiers(1);
                strSendAttackInfoToObservers+="\nAttacker loses 1 soldier.";

            }
        }
        // if the attacker or defender has only 1 dice
        else {

            if (attackerMaxValue > defenderMaxValue) {
                toCountryAttack.removeSoldiers(1);
                strSendAttackInfoToObservers="Defender loses 1 soldier.";

            } else {

                fromCountryAttack.removeSoldiers(1);
                strSendAttackInfoToObservers="Attacker loses 1 soldier.";
            }
        }

        constructAndSendAttackBattleMessage(attackerDice,defenderDice);

        //Check if all of the defender's soldiers have been eliminated
        //If the defender lost all soldiers in his/her country, the attacker conquered the country
        //checkDefenderPushedOut();
        
        checkDefenderOwnership();

        playerService.evaluateWorldDomination();
    }

    /**
     * Checks if defending country has been conquered and if defender eliminated from game
     * If defender eliminated from game, triggers notif to dom view
     * @return true if defender country conquered
     */
    public boolean checkDefenderOwnership() {

        this.boolCountryConquered=checkDefenderPushedOut();

        if(boolCountryConquered) {

            this.boolDrawCard=true;

            if(isDefenderEliminatedFromGame()) {

                //transferCardsFromDefenderToAttacker
                transferCardsFromDefenderToAttacker();

                //Remove defender from game
                playerService.removePlayer(defender.getName());

                //Display Domination View by notifying obervers
                playerService.evaluateWorldDomination();
                
                if (checkPlayerWin()) {
                	
                	//If tournament mode is on...game must not end
                	
                	if(playerService.getBoolTournamentMode()) {
                		playerService.setBoolPlayerWinner(true);
                		playerService.setPlayerWinner(attacker);
                		this.boolAttackMoveRequired=false;
                	}
                	
                	else
                		CommonUtils.endGame(playerService);                	
                }
                    

            }
        }

        return true;
    }

    /**
     * Transfers card fron=m defender to attacker WHEN DEFENDER ELIMINATED FROM GAME
     * Triggers notif to show defender cards before transfer
     * Triggers notif to show attack cards after transfer
     */
    public void transferCardsFromDefenderToAttacker() {

        if(defender.getCardList().size()==0) {
            playerService.notifyPlayerServiceObservers("\nDefender has no cards to be transferred.");
            return;
        }

        //Show card details of defender
        showCardsInfoPlayer(defender);

        for(Card card:defender.getCardList()) {
            addCard(card); //add card to playerList
        }

        //Not required as defender will be garbage collected
        defender.getCardList().clear();

        showCardsInfoPlayer(this);

        playerService.notifyPlayerServiceObservers("\nCards transferred.");

    }

    /**
     * display cards owned by the player
     * @param p who's card list we want to view
     */
    private void  showCardsInfoPlayer(Player p){

        if (p.getCardList().isEmpty()){
            playerService.notifyPlayerServiceObservers("Player card list:empty");
            return;
        }

        int count = 1;

        String strCardList=p.getName()+" Card List: ";

        for(Card card: p.getCardList()){
            strCardList+=count + ":" + card.getName() + WHITESPACE;
            count ++;
        }

        playerService.notifyPlayerServiceObservers(strCardList);

    }

    /**
     * Check if all of the defender's soldiers have been eliminated
     *If the defender lost all soldiers in his/her country, the attacker conquered the country
     * @return if true
     * Triggers notif to playerservice observers when country conquered.
     */
    public boolean checkDefenderPushedOut() {
        strSendAttackInfoToObservers="";

        if(defenderPushedOut()) {

            //Need attack move next

            this.boolAttackMoveRequired=true;

            transferCountryOwnershipAfterAttack();

            strSendAttackInfoToObservers+="\nNeed to check player wins, "
                    + "check if defender is eliminated from the game,"
                    + "need to transfer cards\n"
                    + "need to draw card when ending attack phase";


            //notify after attack info to observers
            playerService.notifyPlayerServiceObservers(strSendAttackInfoToObservers);

            if (checkPlayerWin()) {
            	
          	  if(playerService.getBoolTournamentMode()) {
        		  playerService.setBoolPlayerWinner(true);
        		  playerService.setPlayerWinner(attacker);
        		  this.boolAttackMoveRequired=false;
        		  }
        		  
        		  else
        			  CommonUtils.endGame(playerService);
            }
            
            return true;
        }
		 

        strSendAttackInfoToObservers+="\nCountry not conquered.";
        
        //notify after attack info to observers
        playerService.notifyPlayerServiceObservers(strSendAttackInfoToObservers);

        return false;
    }

    /**
     * check if attack has conquered all the countries
     * @return true if player wins
     */
    public boolean checkPlayerWin() {

        if(attacker.getCountryPlayerList().size()==playerService.getMapService()
                .getCountries().size()) {

            strSendAttackInfoToObservers="\n"+attacker.getName()+" Wins";

            playerService.notifyPlayerServiceObservers(strSendAttackInfoToObservers);

            return true;
        }

        strSendAttackInfoToObservers+="\nYou must now transfer some soldiers from attacking country to defending country.";
        return false;
    }

    /**
     * transfer ownership of the country after attack
     */
    public void transferCountryOwnershipAfterAttack() {

    	if(toCountryAttack.getPlayer().getName().
    			equalsIgnoreCase(fromCountryAttack.getPlayer().getName())) {
    		return;
    	}
    	
        fromCountryAttack.getPlayer().addCountryToPlayerList(toCountryAttack);

        toCountryAttack.getPlayer().removeCountryFromPlayerList(toCountryAttack);

        toCountryAttack.setPlayer(fromCountryAttack.getPlayer());

        strSendAttackInfoToObservers+="Country ownership transferred.\n Attacker conquers country.";

    }


    /**
     * validate if the defender occupy 0 country
     * @return true if defender occupy 0 country, false if not
     */
    @JsonIgnore
    public boolean isDefenderEliminatedFromGame() {
        if(Optional.ofNullable(defender).map(Player::getCountryPlayerList).filter(CollectionUtils::isEmpty).isPresent())
            return true;
        return false;
    }



    /**
     * This checks if attacker only has one soldier left in the attacking country
     * Stopping condition 1 for -allout attack
     * @return true if only 1 soldier left in attacking country
     */
    @JsonIgnore
    public boolean isAttackerLastManStanding() {
        if (Optional.ofNullable(fromCountryAttack).map(Country::getSoldiers).filter(soldier -> soldier < MIN_ATTACKING_SOLDIERS).isPresent())
            return true;
        return false;
    }

    /**
     * Checks if defender has no more soldiers remaining in defending country
     * Stopping condition 2 for -allout attack
     * @return true if no soldiers left in defending country
     */
    public boolean defenderPushedOut() {
        return toCountryAttack.getSoldiers().equals(0);
    }


    /**
     * Method called when attackmove called.
     * Moves numSoldiers from attacking country to defeated country if validation check passes.
     * @param numSoldiersTransfer Number of Soldiers Transfered
     * Triggers notif to playerservice using PlayerAttackWrapper (booleAttackMoveOver=true)
     * triggers notif to dom view
     */
    public void attackMove(int numSoldiersTransfer) {

        if(numSoldiersTransfer<fromCountryAttack.getSoldiers() && numSoldiersTransfer>0) {
        	
        	playerService.notifyPlayerServiceObservers("Attack Move Being Done:");
        	
            fromCountryAttack.removeSoldiers(numSoldiersTransfer);
            toCountryAttack.addSoldiers(numSoldiersTransfer);

            //Notify playerService Observers
            PlayerAttackWrapper playerAttackWrapper=new PlayerAttackWrapper(fromCountryAttack,toCountryAttack);
            playerAttackWrapper.setBoolAttackMoveOver(); //Set flag for display in views

            playerService.notifyPlayerServiceObservers(playerAttackWrapper);

            playerService.evaluateWorldDomination();

            //No longer require attackMove command
            //playerService.setBoolAttackMoveRequired(false);
            this.boolAttackMoveRequired=false;
            return;
        }

        playerService.notifyPlayerServiceObservers("Invalid number of soldiers for AttackMove. Try again.");
    }

    /**
     * validate attack conditions
     * @param playerService a reference of PlayerService
     * @return true if valid, false if not valid
     */
    public boolean validateAttackConditions(PlayerService playerService) {

        this.boolAttackValidationMet = true;

        //Clear previous displayMessage and construct invalid conditions again
        strSendAttackInfoToObservers="";

        checkAttackingCountryAdjacency(playerService.getMapService());

        if (boolAttackValidationMet)
            checkCountryBelongToAttacker(playerService);

        if (boolAttackValidationMet)
            checkCountryHostility();

        if (boolAttackValidationMet)
            checkNumAttackingSoldiers();

        if (boolAttackValidationMet)
            checkAttackerMaxDiceNumValidity();

        if (boolAttackValidationMet)
            checkAttackerDiceNumValidity();

        if (boolAttackValidationMet)
            checkAttackerMinDiceNumValidity();

        if (boolAttackValidationMet)
            checkDefenderMaxDiceNumValidity();

        if (boolAttackValidationMet)
            checkDefenderDiceNumValidity();

        if (boolAttackValidationMet)
            checkDefenderMinDiceNumValidity();

        return boolAttackValidationMet;
    }


    /**
     * Method for rolling attacker's dice
     * @param numDiceAttacker number of attacker's dice
     * @return attacker's dice
     */
    public int[] rollAttackerDice(int numDiceAttacker) {
        attackerDice = new int[numDiceAttacker];

        diceRandomizer = new SecureRandom();

        for (int i = 0; i < attackerDice.length; i++) {
            attackerDice[i] = diceRandomizer.nextInt(6)+1;
        }
        return attackerDice;
    }

    /**
     * Method for rolling defender's dice
     * @param numDiceDefender num of defender's dice
     * @return defender's dice
     */
    public int[] rollDefenderDice(int numDiceDefender) {
        defenderDice = new int[numDiceDefender];

        diceRandomizer = new SecureRandom();

        for (int i = 0; i < defenderDice.length; i++) {
            defenderDice[i] = diceRandomizer.nextInt(6)+1;
        }

        return defenderDice;
    }


    /**
     * check if attacking country has Adjacency
     * @param mapService to retrieve from and to countries' info and their adjacent countries
     */
    public void checkAttackingCountryAdjacency(MapService mapService) {

        Map<Integer, Set<Integer>> adjacentCountriesList = mapService.getAdjacencyCountriesMap();

        Optional<Integer> toId = mapService.findCorrespondingIdByCountryName(toCountryAttack.getCountryName());

        Optional<Integer> fromId = mapService.findCorrespondingIdByCountryName(fromCountryAttack.getCountryName());

        if(!fromId.isPresent()) {
            strSendAttackInfoToObservers+="\nOrigin country not present";
            this.boolAttackValidationMet=false;
        }


        if(!toId.isPresent()) {
            strSendAttackInfoToObservers+="\nDestination country not present";
            this.boolAttackValidationMet=false;
        }

        if(boolAttackValidationMet) {
            neighbouringCountries =  adjacentCountriesList.get(fromId.get());

            if(!neighbouringCountries.contains(toId.get())) {

                this.boolAttackValidationMet=false;
                strSendAttackInfoToObservers+="\nCountries not adjacent to each other";
            }
        }

    }

    /**
     * Sets number of dice for attacker. Used in test methods.
     * @param n dices
     */
    public void setNumDiceAttacker(int n) {
        this.numDiceAttacker=n;
    }
    /**
     * Sets number of dice for defender. Used in test methods.
     * @param n dices
     */
    public void setNumDiceDefender(int n) {
        this.numDiceDefender=n;
    }

    /**
     * Check if attacker country actually belongs to the attacker
     * @param playerService to notify observers about game info and retrieve useful info like current player
     */
    public void checkCountryBelongToAttacker(PlayerService playerService) {
        Player currentPlayer=playerService.getCurrentPlayer();
        String playerName=currentPlayer.getName();

        if((!fromCountryAttack.getPlayer().getName().equals(playerName))) {
            //The message will be sent to the playerAttackWrapper when the notification method is created there
            //this.playerAttackWrapper.setAttackDisplayMessage
            strSendAttackInfoToObservers+="\nOrigin country does not belong to current player";
            this.boolAttackValidationMet=false;
        }

    }

    /**
     * checks whether the 2 countries are owned by different players
     */
    public void checkCountryHostility() {

        if(fromCountryAttack.getPlayer().getName().equalsIgnoreCase
                (toCountryAttack.getPlayer().getName())) {

            //The message will be sent to the playerAttackWrapper when the notification method is created there

            strSendAttackInfoToObservers+="\nCountries belong to same player";
            this.boolAttackValidationMet=false;
        }

    }

    /**
     * check the number of soldiers for the attacker
     * Ensures that at least 2 soldier remains in the attacker's origin country
     */
    public void checkNumAttackingSoldiers() {

        if(isAttackerLastManStanding()) {
            //The message will be sent to the playerAttackWrapper when the notification method is created there

            strSendAttackInfoToObservers+="\nNot enough soldiers in origin country";
            this.boolAttackValidationMet=false;
        }
    }

    /**
     * check if attacker throws a valid number of dices
     * it must be less than the maximum allowed number for attacker
     */
    public void checkAttackerMaxDiceNumValidity() {
        if(numDiceAttacker>MAX_ATTACKER_DICE_NUM) {
            //The message will be sent to the playerAttackWrapper when the notification method is created there

            strSendAttackInfoToObservers+="\nAttacker should not throw more than 3 dices";
            this.boolAttackValidationMet=false;
        }
    }

    /**
     * check if attacker throws a number of dices that is less than the number of soldiers in his/her country
     */
    public void checkAttackerDiceNumValidity() {
        if(numDiceAttacker>=fromCountryAttack.getSoldiers()) {
            //The message will be sent to the playerAttackWrapper when the notification method is created there

            strSendAttackInfoToObservers+="\nAttacker number of dices invalid.";
            this.boolAttackValidationMet=false;
        }
    }

    /**
     * check if attacker throws a number of dice that is less than 1
     */
    private void checkAttackerMinDiceNumValidity() {
        if(numDiceAttacker<1) {
            //The message will be sent to the playerAttackWrapper when the notification method is created there

            strSendAttackInfoToObservers+="\nAttacker should throw at least 1 dice";
            this.boolAttackValidationMet=false;
        }
    }

    /**
     * check if defender throws a valid number of dices
     * it must be less or equal than the maximum allowed number for defender
     */
    public void checkDefenderMaxDiceNumValidity() {
        if(numDiceDefender>MAX_DEFENDER_DICE_NUM) {
            //The message will be sent to the playerAttackWrapper when the notification method is created there

            strSendAttackInfoToObservers+="\nDefender should not throw more than 2 dices";
            this.boolAttackValidationMet=false;
            this.boolDefendDiceRequired.set(true);
        }
    }

    /**
     * check if defender throws a number of dices that is less or equal than the number of soldiers in his/her country
     */
    public void checkDefenderDiceNumValidity() {
        if(numDiceDefender>toCountryAttack.getSoldiers()) {
            //The message will be sent to the playerAttackWrapper when the notification method is created there

            strSendAttackInfoToObservers+="\nDefender should throw number of dices"
                    + " that is less or equal than the number of soldiers";
            this.boolAttackValidationMet=false;
            this.boolDefendDiceRequired.set(true);
        }
    }

    /**
     * check if defender throws number of dice that is less than 1
     */
    private void checkDefenderMinDiceNumValidity() {
        if(numDiceDefender<1) {
            //The message will be sent to the playerAttackWrapper when the notification method is created there

            strSendAttackInfoToObservers+="\nDefender should throw at least 1 dice";
            this.boolAttackValidationMet=false;
            this.boolDefendDiceRequired.set(true);
        }
    }

    /**
     * Displays information about attacker and defend dice rolls and attack outcome/
     * Triggers Notification of this information to playerservicer observers.
     * @param attackerDice attacker's dice
     * @param defenderDice defender's dice
     */
    public void constructAndSendAttackBattleMessage(int[] attackerDice, int[] defenderDice) {

        String diceMessage="";

        diceMessage+="Attacker dices: ";

        for(Integer i:attackerDice) {
            diceMessage+=i+" ";
        }
        diceMessage+="\nDefender dices: ";

        for(Integer i:defenderDice) {
            diceMessage+=i+" ";
        }

        diceMessage+="\n";
        strSendAttackInfoToObservers=diceMessage+strSendAttackInfoToObservers; //Already formed during various method calls of decideBattleResult

        //Notify playerservice observers of what happened during battle phase (Concatenated String)

        playerService.notifyPlayerServiceObservers(strSendAttackInfoToObservers);

        //Notify playerservice observers of num armies remaining for attacker and defender

        PlayerAttackWrapper playerAttackWrapper=new PlayerAttackWrapper(fromCountryAttack,toCountryAttack);
        playerAttackWrapper.setBoolAttackOver(); //Set flag for display in views

        playerService.notifyPlayerServiceObservers(playerAttackWrapper);


    }

    /**
     * This method comes up with initial single attack display message
     *  and notifies observers of playerservice about initial attack information
     */
    public void constructAndSendInitialSingleAttackInformation() {

        String fromCountryName=fromCountryAttack.getCountryName();
        String toCountryName=toCountryAttack.getCountryName();
        String attackerName=attacker.getName();
        String defenderName=defender.getName();

        this.strSendAttackInfoToObservers="\n";

        strSendAttackInfoToObservers+=fromCountryName+" ("+attackerName+") wants to attack "+
                toCountryName+" ("+defenderName+")";

        strSendAttackInfoToObservers+="\n"+fromCountryName+" has "+numAttackingSoldiers+" soldiers, "+
                toCountryName+" has "+numDefendingSoldiers+" soldiers.";

        strSendAttackInfoToObservers+="\n"+attackerName+" rolls "+numDiceAttacker+
                " dices, "+defenderName+" rolls "+numDiceDefender+" dices.";

        playerService.notifyPlayerServiceObservers(strSendAttackInfoToObservers);

    }

    /**
     * Getter for boolAttackMoveRequired
     * @return true if attackmove command required
     */
    public boolean getBoolAttackMoveRequired() {
        return boolAttackMoveRequired;
    }

    /**
     * Setter for boolAttackMoveRequired
     * @param b is boolean value
     */
    public void setBoolAttackMoveRequired(boolean b) {
        this.boolAttackMoveRequired=b;
    }

    /**
     * Setter for boolCountryConquered
     * @param b is boolean value
     */
    public void setBoolCountryConquered(boolean b) {
        this.boolCountryConquered=b;
    }

    /**
     * Getter for boolCountryConquered
     * @return boolean value
     * Notifies observers about card drawn
     */
    public boolean getBoolCountryConquered() {
        return boolCountryConquered;
    }

    /**
     * Called when attack -noattack valid command entered
     * Draws card from deck and gives to attacker
     * @param playerService reference
     * Triggers notification about what card is drawn
     */
    public void endAttackPhase(PlayerService playerService) {

    	playerService.notifyPlayerServiceObservers("Attack Phase Ended");
    	
        //Check if card needs to be drawn-set to true when country conquered
        if(boolDrawCard) {
            Card c=playerService.drawFromDeck();
            String strMessage = "";
            if (c == null) {
            	strMessage="No card is drawn as the deck is empty.";
            }
            else {
            	addCard(c);
            	strMessage="Card drawn: "+c.getName();
            }
            this.boolDrawCard=false;
            playerService.notifyPlayerServiceObservers(strMessage);
        }


        resetBooleanValues();
        
        //Set gamestate to fortify
        playerService.getMapService().setState(GameState.FORTIFY);

    }

    /**
     * Resetting boolean values for the next attack phase of the player
     */
    public void resetBooleanValues() {
        this.boolDrawCard=false;
        this.boolAttackMoveRequired=false;
        this.boolAttackOver=false;
        this.boolCountryConquered=false;

    }


    //--------------------------------------FORTIFICATION--------------------------------------------------

    /**
     * a reference of from Country of fortification
     */
    private Country fromCountryFortify;

    /**
     * a reference of to country of fortification
     */
    private Country toCountryFortify;

    /**
     * number of fortified soldiers
     */
    private int numSoldiersToFortify;

    /**
     * Set that keeps track of neighbouring countries of origin country.
     */
    private	Set<Integer> neighbouringCountries;

    /**
     * a boolean value if fortification is valid
     */
    private boolean boolFortifyValidationMet;

    /**
     * a reference of PlayerFortificationWrapper
     */
    private PlayerFortificationWrapper playerFortificationWrapper;


    /**
     * fortify soldiers of the country
     * if fortificationNone is true, call fortifyNone()
     * check if conditions of ownership, adjacency and numSoldiers are valid
     * if yes, implement fortification and notify observers
     * if not, notify observers with error messages
     * @param playerService PlayerService
     * @param playerFortificationWrapper PlayerFortification Wrapper
     * Triggers notification for validation messages and successful fortification
     * Triggers notification to domination view
     */
    public void fortify(PlayerService playerService, PlayerFortificationWrapper playerFortificationWrapper) {

        this.playerFortificationWrapper=playerFortificationWrapper;
        this.fromCountryFortify=this.playerFortificationWrapper.getCountryFrom();
        this.toCountryFortify=this.playerFortificationWrapper.getCountryTo();
        this.numSoldiersToFortify=this.playerFortificationWrapper.getNumSoldiers();

        //Checks if boolean fortificationNone is true...calls fortifyNone method.

        if(this.playerFortificationWrapper.getBooleanFortificationNone()) {
            fortifyNone(playerService);
            return;
        }

        //Check if conditions of ownership, adjacency and numSoldiers are valid
        if(!validateFortifyConditions(playerService)) {

            //Notify playerService Observers about validation error message
            playerService.notifyPlayerServiceObservers(this.playerFortificationWrapper);

            return;
        }


        //Actual Fortification
        fromCountryFortify.removeSoldiers(numSoldiersToFortify);
        toCountryFortify.addSoldiers(numSoldiersToFortify);

        //Notifying Observers of PlayerService
        this.playerFortificationWrapper=new PlayerFortificationWrapper(fromCountryFortify, toCountryFortify,
                numSoldiersToFortify);
        this.playerFortificationWrapper.setFortificationDisplayMessage("Fortification Success");

        playerService.notifyPlayerServiceObservers(this.playerFortificationWrapper);

        playerService.evaluateWorldDomination();

        //Switch to Next player and Change State to Reinforcement
        playerService.switchNextPlayer();
        playerService.getMapService().setState(GameState.REINFORCE);

        playerService.showCardsInfo(playerService.getCurrentPlayer());

        playerService.automateGame();

    }

    /**
     * Method called when fortify none is chosen
     * It just switches to next player and changes game state to reinforcement again.
     * @param playerService to notify observers about game info and retrieve useful info like current player
     */
    public void fortifyNone(PlayerService playerService) {

        //Notify playerService observers that fortification phase is over
        playerFortificationWrapper.setFortificationDisplayMessage("Fortification Phase is over.");
        playerService.notifyPlayerServiceObservers(playerFortificationWrapper);

        playerService.switchNextPlayer();
        playerService.getMapService().setState(GameState.REINFORCE);
        playerService.showCardsInfo(playerService.getCurrentPlayer());
        
        playerService.automateGame();
    }

    /**
     * This method checks that the following reinforcement criterias are met:
     * <ul>
     * <li>Both countries are adjacent </li>
     * <li>Both countries belong to player</li>
     * <li>at least 1 player will remain in the source country after fortification</li>
     * </ul>
     * @param playerService PlayerService
     * @return boolFortifyValidationMet
     */
    public boolean validateFortifyConditions(PlayerService playerService) {

        this.boolFortifyValidationMet=true;

        checkCountryAdjacencyForFortification(playerService.getMapService());

        if(boolFortifyValidationMet) {
            checkCountriesBelongToCurrentPlayer(playerService);
        }

        if(boolFortifyValidationMet) {
            checkCountryOwnership();
        }

        if(boolFortifyValidationMet) {
            checkNumSoldiers();
        }

        return this.boolFortifyValidationMet;

    }


    /**
     * check country has Adjacency
     * @param mapService to retrieve from and to countries' info and their adjacent countries
     */
    public void checkCountryAdjacencyForFortification(MapService mapService) {

        Map<Integer, Set<Integer>> adjacentCountriesList = mapService.getAdjacencyCountriesMap();

        Optional<Integer> toId = mapService.findCorrespondingIdByCountryName(toCountryFortify.getCountryName());

        Optional<Integer> fromId = mapService.findCorrespondingIdByCountryName(fromCountryFortify.getCountryName());

        if(!fromId.isPresent()) {
            this.playerFortificationWrapper.setFortificationDisplayMessage
                    ("Origin country not present");
            this.boolFortifyValidationMet=false;
        }


        if(!toId.isPresent()) {
            this.playerFortificationWrapper.setFortificationDisplayMessage
                    ("Destination country not present");
            this.boolFortifyValidationMet=false;
        }

        if(boolFortifyValidationMet) {
            neighbouringCountries =  adjacentCountriesList.get(fromId.get());

            if(!neighbouringCountries.contains(toId.get())) {
                this.boolFortifyValidationMet=false;
                this.playerFortificationWrapper.setFortificationDisplayMessage
                        ("Countries not adjacent to each other");
            }
        }

    }


    /**
     * checks whether the 2 countries are owned by the current player
     */
    public void checkCountryOwnership() {

        if(!(fromCountryFortify.getPlayer().getName().equalsIgnoreCase
                (toCountryFortify.getPlayer().getName()))) {

            this.playerFortificationWrapper.setFortificationDisplayMessage
                    ("Countries do not belong to same player");
            this.boolFortifyValidationMet=false;
        }

    }

    /**
     * Check if both countries belong to current player
     * @param playerService to notify observers about game info and retrieve useful info like current player
     */
    public void checkCountriesBelongToCurrentPlayer(PlayerService playerService) {
        Player currentPlayer=playerService.getCurrentPlayer();
        String playerName=currentPlayer.getName();

        if((!fromCountryFortify.getPlayer().getName().equals(playerName))
                || (!toCountryFortify.getPlayer().getName().equals(playerName))) {
            this.playerFortificationWrapper.setFortificationDisplayMessage
                    ("fromCountry or toCountry does not belong to current player");
            this.boolFortifyValidationMet=false;
        }

    }

    /**
     * check the number of soldiers for the current player
     * Ensures that at least 1 soldier remains in origin country
     */
    public void checkNumSoldiers() {

        if(!(fromCountryFortify.getSoldiers()>numSoldiersToFortify)) {
            this.playerFortificationWrapper.setFortificationDisplayMessage
                    ("Not enough soldiers in origin country");
            this.boolFortifyValidationMet=false;
        }

        if(numSoldiersToFortify<1) {
            this.playerFortificationWrapper.setFortificationDisplayMessage
                    ("Num soldiers must be greater than 0.");
            this.boolFortifyValidationMet=false;
        }
    }



    /**
     * check if the two players are the same
     * @param o Object o
     * @return Object that checks name equality
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(name, player.name);
    }

    /**
     * extend and override hashCode
     * @return Object with name hash
     */
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    /**
     * extend and override toString() method
     * @return name
     */
    @Override
    public String toString(){
        return name;
    }

    /**
     * method to create countryPlayerList
     */
    public void instantiatePlayerCountryListForLoading() {
    	this.countryPlayerList=new ArrayList<Country>();
    }

    /**
     * get the boolean value boolDrawCard
     * @return boolDrawCard
     */
	public boolean isBoolDrawCard() {
		return boolDrawCard;
	}

    /**
     * set the boolean value boolDrawCard
     * @param boolDrawCard true if draws a card, false if not
     */
	public void setBoolDrawCard(boolean boolDrawCard) {
		this.boolDrawCard = boolDrawCard;
	}

}  //End of Player Class

