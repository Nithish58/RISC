package com6441.team7.risc.api.model;

import java.lang.reflect.Array;
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
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import java.security.SecureRandom;

import com6441.team7.risc.api.wrapperview.PlayerAttackWrapper;
import com6441.team7.risc.api.wrapperview.PlayerFortificationWrapper;
import com6441.team7.risc.utils.CommonUtils;
import com6441.team7.risc.view.GameView;

import static com6441.team7.risc.api.RiscConstants.MAX_ATTACKER_DICE_NUM;
import static com6441.team7.risc.api.RiscConstants.MAX_DEFENDER_DICE_NUM;
import static com6441.team7.risc.api.RiscConstants.MIN_ATTACKING_SOLDIERS;
import static com6441.team7.risc.api.RiscConstants.WHITESPACE;

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
	public ArrayList<Country> getCountryList() {
    	return countryPlayerList;
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
	 * if not, just return
	 * @param playerService a reference of PlayerService
	 */
	public void attackAllOut(PlayerService playerService) {
    	
    	
    	this.numDiceAttacker = MAX_ATTACKER_DICE_NUM;
    	this.numDiceDefender = MAX_DEFENDER_DICE_NUM;

    	//while (!checkDefenderPushedOut() || !isAttackerLastManStanding()) {
    	while (!defenderPushedOut() || !isAttackerLastManStanding()) {
    		
    		//Update numSoldiers everytime attack is being done
    		this.numAttackingSoldiers=fromCountryAttack.getSoldiers();
    		this.numDefendingSoldiers=toCountryAttack.getSoldiers();
    		
		//	playerService.evaluateWorldDomination();
    		
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
    		
			this.boolCountryConquered=false;
			
			//Decide the winner
			decideBattleResult(attackerDice, defenderDice);
			
    	}
    }   

    
    /**
     * This method decides the result of every battle after both attacker and defender
     * throw their dices. 
     * Each side is assigned second dice value to be compared with each other
     * if both sides throw at least two dices.
     * @param attackerDice attacker's dice
     * @param defenderDice defender's dice
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
			//defenderMaxValue = defenderDice[0];
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
	 * Checks if defending country has been conquered
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
				

			}
		}
		
		return true;
	}
	
	/**
	 * Transfers card fron defender to attacker WHEN DEFENDER ELIMINATED FROM GAME
	 */
	public void transferCardsFromDefenderToAttacker() {
		
		/*
		 * for(int i=0;i<4;i++) { defender.addCard(Card.INFANTRY); }  //FOR TESTING ONLY
		 */
		
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
     * @param player who's card list we want to view
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
	 */
	public boolean checkDefenderPushedOut() {
		strSendAttackInfoToObservers="";
		//if (!(fromCountryAttack.getPlayer().getName().equals(toCountryAttack.getPlayer().getName())) && toCountryAttack.getSoldiers()==0)
		//if ((toCountryAttack.getSoldiers().equals(0))) {
			
		if(defenderPushedOut()) {
		
			//Need attack move next
			//playerService.setBoolAttackMoveRequired(true);
			this.boolAttackMoveRequired=true;
			
			transferCountryOwnershipAfterAttack();
					
			if (checkPlayerWin())
				CommonUtils.endGame(playerService);
				
			
	
			
			strSendAttackInfoToObservers+="\nNeed to check player wins, "
					+ "check if defender is eliminated from the game,"
					+ "need to transfer cards\n"
					+ "need to draw card when ending attack phase";
			
			
			//notify after attack info to observers
			playerService.notifyPlayerServiceObservers(strSendAttackInfoToObservers);
			
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
		 
		if(attacker.getCountryList().size()==playerService.getMapService()
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
		
		fromCountryAttack.getPlayer().addCountryToPlayerList(toCountryAttack);
		
		toCountryAttack.getPlayer().removeCountryFromPlayerList(toCountryAttack);

		toCountryAttack.setPlayer(fromCountryAttack.getPlayer());		

		strSendAttackInfoToObservers+="Country ownership transferred.\n Attacker conquers country.";
		
		//Hardcoded to test exchangercards functions during build 2 demo
		//Attacker allocated 9 cards whenever it conquers country, just to test exchangecards
		for ( int i = 0 ; i < 3; i++) {
			addCard(Card.INFANTRY);
			addCard(Card.ARTILLERY);
			addCard(Card.CAVALRY);
		}
	}
	

	/**
	 * validate if the defender occupy 0 country
	 * @return true if defender occupy 0 country, false if not
	 */
	public boolean isDefenderEliminatedFromGame() {
		if(defender.getCountryList().size()==0)
			return true;
		return false;
	}

	
	
	/**
	 * This checks if attacker only has one soldier left in the attacking country
	 * Stopping condition 1 for -allout attack
	 * @return true if only 1 soldier left in attacking country
	 */
	public boolean isAttackerLastManStanding() {
		if (fromCountryAttack.getSoldiers()<MIN_ATTACKING_SOLDIERS)
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
	 */
    public void attackMove(int numSoldiersTransfer) {
    	
    	if(numSoldiersTransfer<fromCountryAttack.getSoldiers() && numSoldiersTransfer>0) {
    		
    		fromCountryAttack.removeSoldiers(numSoldiersTransfer);
    		toCountryAttack.addSoldiers(numSoldiersTransfer);
    		
	    	playerService.evaluateWorldDomination();	
	    	
    		//No longer require attackMove command
    		//playerService.setBoolAttackMoveRequired(false);
	    	this.boolAttackMoveRequired=false;
    		return;
    	}
    	
    	playerService.notifyPlayerServiceObservers("Invalid number of soldiers. Try again.");
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
					//this.playerAttackWrapper.setAttackDisplayMessage
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
				//this.playerAttackWrapper.setAttackDisplayMessage
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
				//this.playerAttackWrapper.setAttackDisplayMessage
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
			//this.playerAttackWrapper.setAttackDisplayMessage
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
			//this.playerAttackWrapper.setAttackDisplayMessage
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
			//this.playerAttackWrapper.setAttackDisplayMessage
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
			//playerAttackWrapper.setAttackDisplayMessage
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
			//playerAttackWrapper.setAttackDisplayMessage
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
			//playerAttackWrapper.setAttackDisplayMessage
			strSendAttackInfoToObservers+="\nDefender should throw at least 1 dice";
			this.boolAttackValidationMet=false;
			this.boolDefendDiceRequired.set(true);
		}
	}
	
    /**
	 * Displays information about attacker and defend dice rolls and attack outcome/
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
	 *  and notifies observers of playerservice
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
	 */
	public boolean getBoolCountryConquered() {
		return boolCountryConquered;
	}
	
	public void endAttackPhase(PlayerService playerService) {
		
		//Check if card needs to be drawn
		if(boolDrawCard) {
			Card c=playerService.drawFromDeck();
			addCard(c);
			this.boolDrawCard=false;
			String strMessage="Card drawn: "+c.getName();
			playerService.notifyPlayerServiceObservers(strMessage);
		}
		
		
		resetBooleanValues();
		
		//playerService.getMapService().setState(GameState.FORTIFY);
	}
	
	/**
	 * Resetting boolean values for the next attack phase of the player
	 */
	public void resetBooleanValues() {
		this.boolDrawCard=false;
		this.boolAttackMoveRequired=false;
		this.boolAttackOver=false;
		this.boolCountryConquered=false;
		//this.boolDefendDiceRequired.set(false);
	}
	
    
    //--------------------------------------FORTIFICATION--------------------------------------------------
    
    // Private members for Fortification

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
		this.playerFortificationWrapper.setFortificationDisplayMessage("success");
		
		playerService.notifyPlayerServiceObservers(this.playerFortificationWrapper);
		
		playerService.evaluateWorldDomination();
		
		//Switch to Next player and Change State to Reinforcement
		playerService.switchNextPlayer();
		playerService.getMapService().setState(GameState.REINFORCE);
		
		playerService.showCardsInfo(playerService.getCurrentPlayer());
		
    	
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
    
   
}  //End of Player class

