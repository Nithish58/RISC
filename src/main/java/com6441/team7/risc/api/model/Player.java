package com6441.team7.risc.api.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import java.security.SecureRandom;

import com6441.team7.risc.api.wrapperview.PlayerAttackWrapper;
import com6441.team7.risc.api.wrapperview.PlayerFortificationWrapper;

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
     *  a list of country a player has
     */
    public ArrayList<Country> countryPlayerList;

    /**
     * constructor
     * @param name
     */
    public Player(String name) {
        this.armies = 0;
        this.name = name;
        this.cardList = new ArrayList<>();

        this.tradeInTimes=0;
        this.countryPlayerList=new ArrayList<>();
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
     * Minimum allowed number of attacking armies in a country
     */
    private static final int MIN_ATTACKING_SOLDIERS=2;

    /**
     * Maximum allowed number of dice(s) for attacker to roll 
     */
    private static final int MAX_ATTACKER_DICE_NUM=3;
    
    /**
     * Maximum allowed number of dice(s) for defender to roll
     */
    private static final int MAX_DEFENDER_DICE_NUM=2;


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
     * @param number
     */
    public void reduceArmy(int number){
        armies -= number;
    }

    /**
     * add the number of army to player
     * @param number
     */
    public void addArmy(int number){
    	armies += number;
    }

    /**
     * get list of cards
     * @return card list
     */
    //jenny: modified methods
    public List<String> getCardList() {
        return cardList.stream()
                .map(Card::getName)
                .collect(Collectors.toList());
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
     * @param cardList
     * @return true if valid false if not valid
     */
    public boolean meetTradeInCondition(List<String> cardList){
        if(hasThreeSameCards(cardList) || hasThreeDifferentCards(cardList)){
            return true;
        }

        return false;
    }



    /**
     * check if the three cards are the same
     * @param cardList
     * @return true if all the cards are the same, false if not
     */
    private boolean hasThreeSameCards(List<String> cardList){
        if(cardList.get(0).equalsIgnoreCase(cardList.get(1)) &&
           cardList.get(1).equalsIgnoreCase(cardList.get(2))){
            return true;
        }

        return false;
    }

    /**
     * check if the three cards are all different
     * @param cardList
     * @return true if all three cards are different, false if not
     */
    private boolean hasThreeDifferentCards(List<String> cardList){
        if(!cardList.get(0).equalsIgnoreCase(cardList.get(1)) &&
            !cardList.get(1).equalsIgnoreCase(cardList.get(2)) &&
            !cardList.get(0).equalsIgnoreCase(cardList.get(2))){
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
     * @param list
     */
    public void removeCards(List<String> list){
        Card cardOne = cardList.stream()
                .filter(card -> card.getName().equalsIgnoreCase(list.get(0)))
                .findFirst().get();

        Card cardTwo = cardList.stream()
                .filter(card -> card.getName().equalsIgnoreCase(list.get(1)))
                .findFirst().get();

        Card cardThree = cardList.stream()
                .filter(card -> card.getName().equalsIgnoreCase(list.get(2)))
                .findFirst().get();


        cardList.remove(cardOne);
        cardList.remove(cardTwo);
        cardList.remove(cardThree);

        tradeInTimes ++;

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
     * check whether player has same card category
     * @param card card
     * @return true or false
     */
    private boolean hasSameCardCategory(Card card){
        return cardList.stream()
                .filter(card::equals)
                .count() == CARD_CATEGORY_NUMBER;
    }




    public void addCountryToPlayerList(Country c) {
    	
    	this.countryPlayerList.add(c);
    }
    
    public void removeCountryFromPlayerList(Country c) {}
    
    public ArrayList<Country> getCountryList() {
    	return countryPlayerList;
    }
    

    
    

    
    
    //----------------------------------ATTACK--------------------------------------------------
    
    //Start listing private members for attack here binsar and then continue in this region.
    
    private Country fromCountryAttack;
    private Country toCountryAttack;
    private int numDiceAttacker=0;
    private int numDiceDefender=0;
    private int[] attackerDice;
    private int[] defenderDice;
    private SecureRandom diceRandomizer;
    private boolean boolAllOut;
    private boolean boolAttackOver;
    
    private boolean boolAttackValidationMet;
    
    private PlayerAttackWrapper playerAttackWrapper;
    
    public void attack(PlayerService playerService, PlayerAttackWrapper playerAttackWrapper){
    		this.fromCountryAttack=playerAttackWrapper.getFromCountry();
    		this.toCountryAttack=playerAttackWrapper.getToCountry();
    		
    		this.boolAllOut=playerAttackWrapper.getBooleanAllOut();
    		this.boolAttackOver=playerAttackWrapper.getBoolAttackOver();
    		
    		if(boolAllOut) {
    			attackAllOut();
    			return;
    		}
    	
			this.numDiceAttacker=playerAttackWrapper.getNumDiceAttacker();
			this.numDiceDefender=playerAttackWrapper.getNumDiceDefender();
			
			//Continue or you can change the structure of functions etc if you want.
			// DO NOT CHANGE IN ATTACK CONTROLLER...You just have to code here.
			//						GOOD LUCK BINSAR!!!
			
			//check the validity of countries owned by attacker and defender and number of soldiers in attacker's country
			System.out.println("Before: ");
			System.out.println("Name of soldiers owned by "+this.fromCountryAttack.getPlayer().getName()+" : "+this.fromCountryAttack.getPlayer().getArmies());
			System.out.println(this.fromCountryAttack.getCountryName()+": "+this.fromCountryAttack.getSoldiers());
    		System.out.println(this.toCountryAttack.getCountryName()+": "+this.toCountryAttack.getSoldiers());
    		
			if (!validateAttackConditions(playerService)) {
				
				//notify playerService observer if it's not valid
				playerService.notifyObservers(this.playerAttackWrapper);
				
				return;
			}
			
			//Attacker and defender roll their dices
			attackerDice = rollAttackerDice(numDiceAttacker);
			defenderDice = rollDefenderDice(numDiceDefender);
    		
			//Decide the winner
			decideBattleResult(attackerDice, defenderDice);
    		System.out.println("After: ");
    		System.out.println(this.fromCountryAttack.getCountryName()+": "+this.fromCountryAttack.getSoldiers());
    		System.out.println(this.toCountryAttack.getCountryName()+": "+this.toCountryAttack.getSoldiers());
    }        
    
    public void attackAllOut() {}
    
    public void attackMove() {
    	
    }
    
    /**
     * Method for rolling attacker's dice
     * @param numDiceAttacker
     * @return
     */
    public int[] rollAttackerDice(int numDiceAttacker) {
    	attackerDice = new int[numDiceAttacker];
    	diceRandomizer = new SecureRandom();
    	for (int i = 0; i < attackerDice.length; i++) {
    		attackerDice[i] = diceRandomizer.nextInt(5)+1;
    		System.out.println(attackerDice[i]);
    	}
    	return attackerDice;
    }
    
    /**
     * Method for rolling defender's dice
     * @param numDiceDefender
     * @return
     */
    public int[] rollDefenderDice(int numDiceDefender) {
    	defenderDice = new int[numDiceDefender];
    	diceRandomizer = new SecureRandom();
    	for (int i = 0; i < defenderDice.length; i++) {
    		defenderDice[i] = diceRandomizer.nextInt(5)+1;
    		System.out.println(defenderDice[i]);
    	}
    	return defenderDice;
    }
    
    /**
     * This method decides the result of every battle after both attacker and defender
     * throw their dices. 
     * Each side is assigned second dice value to be compared with each other
     * if both sides throw at least two dices.
     * @param attackerDice
     * @param defenderDice
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
		}

		// decide the maximum and second maximum(if any) values of defender's dice
		// based on the number of the dice thrown
		switch (numDiceDefender) {
		case 2:
			defenderMaxValue = defenderDice[1];
			defenderSecondMaxValue = defenderDice[0];
			break;
		case 1:
			defenderMaxValue = defenderDice[0];
		}

		// choose how the battle is decided
		// based on the number of attacker's and defender's dices
		if (((numDiceAttacker == 3) && (numDiceDefender == 2)) 
				|| ((numDiceAttacker == 2) && numDiceDefender == 2)) {
			// Calculate the highest values of the both players' dices
			if (attackerMaxValue > defenderMaxValue) {
				toCountryAttack.removeSoldiers(1);
				//Notify Observers of Player Service
				this.playerAttackWrapper = new PlayerAttackWrapper(fromCountryAttack, toCountryAttack);
				System.out.println(fromCountryAttack.getPlayer().getName()+
						" : Neutralized a soldier belonging to "+toCountryAttack.getPlayer().getName());
			} else {
				fromCountryAttack.removeSoldiers(1);
				//Notify Observers of Player Service
				this.playerAttackWrapper = new PlayerAttackWrapper(fromCountryAttack, toCountryAttack);
				System.out.println(fromCountryAttack.getPlayer().getName()+
						" : Lost a soldier");
			}

			if (attackerSecondMaxValue > defenderSecondMaxValue) {
				toCountryAttack.removeSoldiers(1);
				//Notify Observers of Player Service
				this.playerAttackWrapper = new PlayerAttackWrapper(fromCountryAttack, toCountryAttack);
				System.out.println(fromCountryAttack.getPlayer().getName()+
						" : Neutralized a soldier belonging to "+toCountryAttack.getPlayer().getName());
			} else {
				fromCountryAttack.removeSoldiers(1);
				//Notify Observers of Player Service
				this.playerAttackWrapper = new PlayerAttackWrapper(fromCountryAttack, toCountryAttack);
				System.out.println(fromCountryAttack.getPlayer().getName()+
						" : Lost a soldier");
			}
		}
		// if the defender's country only have one soldier
		else {
			if (attackerMaxValue > defenderMaxValue) {
				toCountryAttack.removeSoldiers(1);
				//Notify Observers of Player Service
				this.playerAttackWrapper = new PlayerAttackWrapper(fromCountryAttack, toCountryAttack);
				System.out.println(fromCountryAttack.getPlayer().getName()+
						" : Neutralized a soldier belonging to "+toCountryAttack.getPlayer().getName());
			} else {
				fromCountryAttack.removeSoldiers(1);
				//Notify Observers of Player Service
				this.playerAttackWrapper = new PlayerAttackWrapper(fromCountryAttack, toCountryAttack);
				System.out.println(fromCountryAttack.getPlayer().getName()+
						" : Lost a soldier");
			}
		}
		
		//Check if all of the defender's soldiers have been eliminated
		//If the defender lost all soldiers in his/her country, the attacker conquered the country
		if (toCountryAttack.getSoldiers()==0)
			toCountryAttack.setPlayer(fromCountryAttack.getPlayer());
	}

    
    private boolean validateAttackConditions(PlayerService playerService) {
    	this.boolAttackValidationMet = true;
    	
    	checkAttackingCountryAdjacency(playerService.getMapService());
    	
    	if (boolAttackValidationMet)
    		checkCountryBelongToAttacker(playerService);
    	
    	if (boolAttackValidationMet)
    		checkCountryHostility();
    	
    	if (boolAttackValidationMet)
    		checkNumAttackingSoldiers();
    	
    	if (boolAttackValidationMet)
    		checkAttackerDiceNumValidity();
    	
    	if (boolAttackValidationMet)
    		checkDefenderMaxDiceNumValidity();
    	
    	if (boolAttackValidationMet)
    		checkDefenderDiceNumValidity();
    	
    	return boolAttackValidationMet;
    }
    
	/**
	 * Check if attacker country actually belongs to the attacker
	 * @param playerService to notify observers about game info and retrieve useful info like current player 
	 */
	private void checkCountryBelongToAttacker(PlayerService playerService) {
		Player currentPlayer=playerService.getCurrentPlayer();
		String playerName=currentPlayer.getName();
		
		if((!fromCountryAttack.getPlayer().getName().equals(playerName))) {
			//The message will be sent to the playerAttackWrapper when the notification method is created there
			//this.playerAttackWrapper.setAttackDisplayMessage
			System.out.println("Origin country does not belong to current player");
			this.boolAttackValidationMet=false;
		}
		
	}
	
	/**
	 * checks whether the 2 countries are owned by different players
	 */
	private void checkCountryHostility() {
			
			if(fromCountryAttack.getPlayer().getName().equalsIgnoreCase
					(toCountryAttack.getPlayer().getName())) {
				
				//The message will be sent to the playerAttackWrapper when the notification method is created there
				//this.playerAttackWrapper.setAttackDisplayMessage
				System.out.println("Countries belong to same player");
				this.boolAttackValidationMet=false;
			}
			
		}
	
	/**
	 * check the number of soldiers for the attacker
	 * Ensures that at least 2 soldier remains in the attacker's origin country
	 */
	private void checkNumAttackingSoldiers() {
			
			if(fromCountryAttack.getSoldiers()<MIN_ATTACKING_SOLDIERS) {
				//The message will be sent to the playerAttackWrapper when the notification method is created there
				//this.playerAttackWrapper.setAttackDisplayMessage
				System.out.println("Not enough soldiers in origin country");
				this.boolAttackValidationMet=false;
			}
		}
	
	/**
	 * check if attacker throws a valid number of dices
	 */
	private void checkAttackerDiceNumValidity() {
		if(numDiceAttacker>MAX_ATTACKER_DICE_NUM && numDiceAttacker>=fromCountryAttack.getSoldiers()) {
			//The message will be sent to the playerAttackWrapper when the notification method is created there
			//this.playerAttackWrapper.setAttackDisplayMessage
			System.out.println("Attacker should not throw more than 3 dices and the number of dices should be less than the number of soldiers");
			this.boolAttackValidationMet=false;
		}
	}
	
	/**
	 * check if defender throws a valid number of dices
	 * it must be less or equal than the maximum allowed number for defender
	 */
	private void checkDefenderMaxDiceNumValidity() {
		if(numDiceDefender>MAX_DEFENDER_DICE_NUM) {
			//The message will be sent to the playerAttackWrapper when the notification method is created there
			//playerAttackWrapper.setAttackDisplayMessage
			System.out.println("Defender should not throw more than 2 dices");
			this.boolAttackValidationMet=false;
		}
	}
	
	/**
	 * check if defender throws a number of dices that is less or equal than the number of soldiers in his/her country
	 */
	private void checkDefenderDiceNumValidity() {
		if(numDiceDefender>toCountryAttack.getSoldiers()) {
			//The message will be sent to the playerAttackWrapper when the notification method is created there
			//playerAttackWrapper.setAttackDisplayMessage
			System.out.println("Defender should throw less or equal than the number of soldiers");
			this.boolAttackValidationMet=false;
		}
	}
	
	/**
	 * check if attacking country has Adjacency
	 * @param mapservice to retrieve from and to countries' info and their adjacent countries
	 */
	private void checkAttackingCountryAdjacency(MapService mapService) {
			
			Map<Integer, Set<Integer>> adjacentCountriesList = mapService.getAdjacencyCountriesMap();
			
			Optional<Integer> toId = mapService.findCorrespondingIdByCountryName(toCountryAttack.getCountryName());
			
			Optional<Integer> fromId = mapService.findCorrespondingIdByCountryName(fromCountryAttack.getCountryName());
			
			if(!fromId.isPresent()) {
				//this.playerAttackWrapper.setAttackDisplayMessage
				System.out.println("Origin country not present");
				this.boolAttackValidationMet=false;
			}

			
			if(!toId.isPresent()) {
				//this.playerAttackWrapper.setAttackDisplayMessage
				System.out.println("Destination country not present");
				this.boolAttackValidationMet=false;
			}
			
			if(boolAttackValidationMet) {
				neighbouringCountries =  adjacentCountriesList.get(fromId.get());
				
				if(!neighbouringCountries.contains(toId.get())) {
					this.boolAttackValidationMet=false;
					//this.playerAttackWrapper.setAttackDisplayMessage
					System.out.println("Countries not adjacent to each other");
				}
			}			
			
		}
	
	
    
    //--------------------------------------FORTIFICATION--------------------------------------------------
    
    // Private members for Fortification
    private Country fromCountryFortify;
    private Country toCountryFortify;
    private int numSoldiersToFortify;
    
	/**
	 * Set that keeps track of neighbouring countries of origin country.
	 */
	private	Set<Integer> neighbouringCountries;
    
    private boolean boolFortifyValidationMet;
    
    private PlayerFortificationWrapper playerFortificationWrapper;
    
    
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
		this.playerFortificationWrapper.setFortificationDisplayMessage("success");
		
		playerService.notifyPlayerServiceObservers(this.playerFortificationWrapper);
		
		playerService.evaluateWorldDomination();
		
		//Switch to Next player and Change State to Reinforcement
		playerService.switchNextPlayer();
		playerService.getMapService().setState(GameState.REINFORCE);
		
    	
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

    }
    
	/**
	 * This method checks that the following reinforcement criterias are met: 
	 * <ul>
	 * <li>Both countries are adjacent </li>
	 * <li>Both countries belong to player</li>
	 * <li>at least 1 player will remain in the source country after fortification</li>
	 * <ul>
	 */
	private boolean validateFortifyConditions(PlayerService playerService) {
		
		this.boolFortifyValidationMet=true;
		
		checkCountryAdjacency(playerService.getMapService());
		
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
	 * @param mapservice to retrieve from and to countries' info and their adjacent countries
	 */
	private void checkCountryAdjacency(MapService mapService) {
			
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
	private void checkCountryOwnership() {
			
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
	private void checkCountriesBelongToCurrentPlayer(PlayerService playerService) {
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
	private void checkNumSoldiers() {
			
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
     * @param o
     * @return
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
     * @return
     */
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    /**
     * extend and override toString() method
     * @return
     */
    @Override
    public String toString(){
        return name;
    }
    
   
}  //End of Player class

