package com6441.team7.risc.controller;

import com6441.team7.risc.api.exception.ReinforceParsingException;
import com6441.team7.risc.api.model.*;
import com6441.team7.risc.view.PhaseView;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Test class for testing methods of ReinforceGameController class
 */
public class ReinforceGameControllerTest {

    /**
     * a reference of reinforcedGameController
     */
    private ReinforceGameController reinforceGameController;

    /**
     * a reference of playerService
     */
    private PlayerService playerService;

    /**
     * a reference of mapService
     */
    private MapService mapService;

    /**
     * a reference of current player
     */
    private Player player;

    /**
     * setup method to construct attributes before each test
     * @throws Exception on invalid
     */
    @Before
    public void setUp() throws Exception {
        mapService = new MapService();
        playerService = new PlayerService(mapService);
        player = new Player("jenny");
        PhaseView phaseView = new PhaseView();
        reinforceGameController = new ReinforceGameController(playerService);
        reinforceGameController.setView(phaseView);
    }


    /**
     * read a valid domination map
     * @return returns string format of file name
     * @throws Exception on invalid
     */
    private String getFile() throws Exception{
        URI uri = getClass().getClassLoader().getResource("jenny.map").toURI();
        return FileUtils.readFileToString(new File(uri), StandardCharsets.UTF_8);
    }


    /**
     * read an invalid command for reinforcement
     * expects the test to throw an IllegalArgumentException
     * @throws Exception on invalid
     */
    @Test(expected = IllegalArgumentException.class)
    public void readInvalidCommand() throws Exception {
        String command = "invalid command";
        reinforceGameController.readCommand(command);
    }


    /**
     * sucessfully add a soldier from reinforcement to the country occupied
     * @throws URISyntaxException on invalid URI
     * @throws IOException on invalid IO
     */
    @Test
    public void reinforceValidArmyNumberToValidCountry() throws URISyntaxException, IOException {
        mockPlayerCountryInformationOne();

        reinforceGameController.calculateReinforcedArmies(player);

        reinforceGameController.reinforceArmy(player, "a1", 1);

        int armyNum = mapService.getCountries().stream()
                .filter(country -> country.getCountryName().equalsIgnoreCase("a1"))
                .findFirst()
                .get()
                .getSoldiers();

        assertEquals(11, armyNum);

    }

    /**
     * if player reinforce armies to the country that is occupied by other player
     * expect: throw an ReinforceParsingException
     * @throws URISyntaxException on invalid URI on invalid URI
     * @throws IOException on invalid IO on invalid IO 
     */
    @Test(expected = ReinforceParsingException.class)
    public void reinforceValidNumberToCountryOccupiedByOtherPlayer() throws URISyntaxException, IOException{
        mockPlayerCountryInformationOne();
        reinforceGameController.calculateReinforcedArmies(player);
        reinforceGameController.reinforceArmy(player, "worrick", 3);
    }

    /**
     * if the player reinforce armies to the countries which the number is larger than the army he gets in reinforcement phase
     * expect: throw an ReinforceParsingException
     * @throws URISyntaxException on invalid URI
     * @throws IOException on invalid IO
     */
    @Test(expected = ReinforceParsingException.class)
    public void reinforceArmyNumberGreaterThanActualReinforcedArmies() throws URISyntaxException, IOException{
        mockPlayerCountryInformationOne();
        reinforceGameController.calculateReinforcedArmies(player);
        reinforceGameController.reinforceArmy(player, "siberia", 100);
    }

    /**
     * if the player reinforce negative armies to the countries
     * expect: throw an ReinforceParsingException
     * @throws URISyntaxException on invalid URI
     * @throws IOException on invalid IO
     */
    @Test(expected = ReinforceParsingException.class)
    public void reinforceNegativeArmyNumber() throws URISyntaxException, IOException{
        mockPlayerCountryInformationOne();
        reinforceGameController.calculateReinforcedArmies(player);
        reinforceGameController.reinforceArmy(player, "siberia", -1);
    }

    /**
     * occupy a country with certain number of soldiers number by the player
     * @param player whose armies are used to occupy
     * @param name name of country to be occupies
     * @param number number of armies
     */
    private void occupyCountry(Player player, String name, int number){
        mapService.getCountries().stream()
                .filter(country -> country.getCountryName().equalsIgnoreCase(name))
                .findFirst()
                .ifPresent(country -> {
                    country.setPlayer(player);
                    country.setSoldiers(number);
                });
    }

    /**
     * mock the player, and occupation information
     * one continents with three countries
     * player jenny occupy 2 countries and player jake occupy 1 country
     * @throws IOException on invalid IO
     * @throws URISyntaxException on invalid URI
     */
    private void mockPlayerCountryInformationOne() throws IOException, URISyntaxException {
        MapLoaderController mapLoaderController = new MapLoaderController(mapService);
        mapLoaderController.readFile("conquest_test.map");

        Player player2 = new Player("jake");
        occupyCountry(player, "a1", 10);
        occupyCountry(player, "b1", 20);
        occupyCountry(player2, "b2", 10);
        occupyCountry(player2, "b3", 10);
    }

    /**
     * mock the player, and occupation data
     * @throws IOException on invalid IO
     * @throws URISyntaxException on invalid URI
     */
    private void mockPlayerCountryInformationTwo() throws IOException, URISyntaxException{
        MapLoaderController mapLoaderController = new MapLoaderController(mapService);
        mapLoaderController.readFile("conquest_test.map");

        occupyCountry(player, "a1", 10);
        occupyCountry(player, "b1", 20);
        occupyCountry(player, "b2", 10);
        occupyCountry(player, "b3", 10);
    }

    /**
     * mock the player, and occupation data, cardList
     * @throws IOException on invalid IO
     * @throws URISyntaxException on invalid URI
     */
    private void mockPlayerCountryInformationThree() throws IOException, URISyntaxException{

        List<Card> cardList = new ArrayList<>();
        cardList.add(Card.ARTILLERY);
        cardList.add(Card.ARTILLERY);
        cardList.add(Card.ARTILLERY);
        player.setCardList(cardList);
    }

    /**
     * mock the player, and occupation data, cardList
     * @throws IOException on invalid IO
     * @throws URISyntaxException on invalid URI
     */
    private void mockPlayerCountryInformationFour() throws IOException, URISyntaxException{
        List<Card> cardList = new ArrayList<>();
        cardList.add(Card.ARTILLERY);
        cardList.add(Card.CAVALRY);
        cardList.add(Card.INFANTRY);
        player.setCardList(cardList);
    }

    /**
     * mock the player, and three cards the player has with two cards the same
     * @throws IOException on invalid IO
     * @throws URISyntaxException on invalid URI
     */
    private void mockPlayerCountryInformationFive() throws IOException, URISyntaxException{
        List<Card> cardList = new ArrayList<>();
        cardList.add(Card.ARTILLERY);
        cardList.add(Card.ARTILLERY);
        cardList.add(Card.INFANTRY);
        player.setCardList(cardList);
    }

    /**
     * mock the player, and five cards the player has
     * @throws IOException on invalid IO
     * @throws URISyntaxException on invalid URI
     */
    private void mockPlayerCountryInformationSix() throws IOException, URISyntaxException{
        List<Card> cardList = new ArrayList<>();
        cardList.add(Card.ARTILLERY);
        cardList.add(Card.INFANTRY);
        cardList.add(Card.CAVALRY);
        cardList.add(Card.ARTILLERY);
        cardList.add(Card.INFANTRY);

        player.setCardList(cardList);
    }

    /**
     * mock the player, and ten cards the player has
     * @throws IOException on invalid IO
     * @throws URISyntaxException on invalid URI
     */
    private void mockPlayerCountryInformationSeven() throws IOException, URISyntaxException{
        List<Card> cardList = new ArrayList<>();
        cardList.add(Card.ARTILLERY);
        cardList.add(Card.INFANTRY);
        cardList.add(Card.CAVALRY);
        cardList.add(Card.ARTILLERY);
        cardList.add(Card.INFANTRY);
        cardList.add(Card.CAVALRY);

        cardList.add(Card.ARTILLERY);
        cardList.add(Card.INFANTRY);
        cardList.add(Card.CAVALRY);
        cardList.add(Card.INFANTRY);


        player.setCardList(cardList);
    }




    /**
     * test the reinforcement army calculation based on rule 1 and rule 3
     * the player occupy 2 countries and did not occupy a whole continent
     * rule 1: armyNum += country/3
     * rule 3: armyNum less than 3, armyNum will be three
     * expect the reinforceArmy number be 3
     * @throws IOException on invalid IO
     * @throws URISyntaxException on invalid URI
     */
    @Test
    public void calculateReinforcedArmiesWithCountriesOccupied() throws IOException, URISyntaxException {
        mockPlayerCountryInformationOne();
        reinforceGameController.calculateReinforcedArmies(player);
        assertEquals(3, reinforceGameController.getReinforcedArmies());
    }

    /**
     * test the reinforcement army calculation based on rule 1 and rule 2
     * the player occupy all 3 countries in the continent azio, which continent value is 5
     * rule 1: army += country / 3
     * rule 3: army += continent value (5)
     * expect the reinforceArmy number be 6
     * @throws IOException on invalid IO on IO error
     * @throws URISyntaxException on invalid URI on URI being invalid
     */
    @Test
    public void calculateReinforceArmiesWithWholeContinentOccupied() throws IOException, URISyntaxException {
        mockPlayerCountryInformationTwo();
        reinforceGameController.calculateReinforcedArmies(player);
        assertEquals(18, reinforceGameController.getReinforcedArmies());
    }

    
    /**
     * test the reinforcement army calculation based on card exchanges
     * if the players exchange three same cards, army += tradeTime * 5
     * expect result to be 5
     * @throws IOException on invalid IO on IO error
     * @throws URISyntaxException on invalid URI on invalid URI
     */
    @Test
    public void exchangeThreeSameCards() throws IOException, URISyntaxException {
        mockPlayerCountryInformationThree();
        String command = "exchangecards 1 2 3";
        reinforceGameController.exchangeCards(player, command);
        assertEquals(5, reinforceGameController.getReinforcedArmies());
    }

    /**
     * test the reinforcement army calculation based on card exchanges
     * if the players exchange three different cards, army += tradeTime * 5
     * expect result to be 5
     * @throws IOException on invalid IO on IO error
     * @throws URISyntaxException on invalid URI on invalid URI
     */
    @Test
    public void exchangeThreeDifferentCards() throws IOException, URISyntaxException {
        mockPlayerCountryInformationFour();
        String command = "exchangecards 1 2 3";
        reinforceGameController.exchangeCards(player, command);
        assertEquals(5, reinforceGameController.getReinforcedArmies());
    }

    /**
     * test the reinforcement army calculation based on card exchanges
     * if the players exchange Two sames cards and 1 different card(artillery, artillery, infantry) different cards
     * expect reinforced number to be 0
     * @throws IOException on invalid IO on IO error
     * @throws URISyntaxException on invalid URI on invalid URI
     */
    @Test
    public void exchangeTwoSameCardsOneDifferentCard() throws IOException, URISyntaxException{
        mockPlayerCountryInformationFive();
        String command = "exchangecards 1 2 3";
        reinforceGameController.exchangeCards(player, command);
        assertEquals(0, reinforceGameController.getReinforcedArmies());
    }

    /**
     * test when users have equal or greater than 5 cards but choose not to exchange cards
     * exect the card exchange state to keep false
     * @throws IOException on invalid IO on invalid IO
     * @throws URISyntaxException on invalid URI on invalid URI
     */
    @Test
    public void exchangeNoneWithFiveCards() throws IOException, URISyntaxException{
        mockPlayerCountryInformationSix();
        String command = "exchangecards -none";
        reinforceGameController.exchangeCards(player, command);
        assertFalse(reinforceGameController.isExchangeCardOver());
    }


    /**
     * test when users trade cards in second time
     * expect 1st time trade in would be 5, and 2nd time trade in would be 10, 3rd time trade in would be 15
     * @throws IOException on invalid IO
     * @throws URISyntaxException on invalid URI
     */
    @Test
    public void exchangeCardsWithFiveCards() throws IOException, URISyntaxException{
        mockPlayerCountryInformationSeven();
        String command = "exchangecards 1 2 3";

        reinforceGameController.exchangeCards(player, command);
        assertEquals(playerService.calculateReinforcedArmyByTradingCards(player), 5);

        reinforceGameController.exchangeCards(player, command);
        assertEquals(playerService.calculateReinforcedArmyByTradingCards(player), 10);

        reinforceGameController.exchangeCards(player, command);
        assertEquals(playerService.calculateReinforcedArmyByTradingCards(player), 15);

    }
}