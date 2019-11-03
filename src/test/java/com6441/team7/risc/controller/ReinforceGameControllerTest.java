package com6441.team7.risc.controller;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
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
     * @throws Exception
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
     * @return
     * @throws Exception
     */
    private String getFile() throws Exception{
        URI uri = getClass().getClassLoader().getResource("jenny.map").toURI();
        return FileUtils.readFileToString(new File(uri), StandardCharsets.UTF_8);
    }


    /**
     * read an invalid command for reinforcement
     * expects the test to throw an IllegalArgumentException
     * @throws Exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void readInvalidCommand() throws Exception {
        String command = "invalid command";
        reinforceGameController.readCommand(command);
    }


    /**
     * sucessfully add a soldier from reinforcement to the country occupied
     * @throws URISyntaxException
     * @throws IOException
     */
    @Test
    public void reinforceValidArmyNumberToValidCountry() throws URISyntaxException, IOException {
        mockPlayerCountryInformationOne();

        reinforceGameController.calculateReinforcedArmies(player);

        reinforceGameController.reinforceArmy(player, "siberia", 1);

        int armyNum = mapService.getCountries().stream()
                .filter(country -> country.getCountryName().equalsIgnoreCase("siberia"))
                .findFirst()
                .get()
                .getSoldiers();

        assertEquals(11, armyNum);

    }

    /**
     * if player reinforce armies to the country that is occupied by other player
     * expect: throw an ReinforceParsingException
     * @throws URISyntaxException
     * @throws IOException
     */
    @Test(expected = ReinforceParsingException.class)
    public void reinforceValidNumberToCountryOccupiedByOtherPlayer() throws URISyntaxException, IOException{
        mockPlayerCountryInformationOne();
        reinforceGameController.calculateReinforcedArmies(player);
        reinforceGameController.reinforceArmy(player, "worrick", 3);
    }

    @Test(expected = ReinforceParsingException.class)
    public void reinforceArmyNumberGreaterThanActualReinforcedArmies() throws URISyntaxException, IOException{
        mockPlayerCountryInformationOne();
        reinforceGameController.calculateReinforcedArmies(player);
        reinforceGameController.reinforceArmy(player, "siberia", 100);
    }

    /**
     * occupy a country with certain number of soldiers number by the player
     * @param player
     * @param name
     * @param number
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
     * @throws IOException
     * @throws URISyntaxException
     */
    private void mockPlayerCountryInformationOne() throws IOException, URISyntaxException {
        MapLoaderController mapLoaderController = new MapLoaderController(mapService);
        URI uri = getClass().getClassLoader().getResource("jenny.map").toURI();
        String file = FileUtils.readFileToString(new File(uri), StandardCharsets.UTF_8);
        mapLoaderController.parseFile(file);

        Player player2 = new Player("jake");
        occupyCountry(player, "siberia", 10);
        occupyCountry(player, "yazteck", 20);
        occupyCountry(player2, "worrick", 10);
    }

    /**
     * mock the player, and occupation data
     * @throws IOException
     * @throws URISyntaxException
     */
    private void mockPlayerCountryInformationTwo() throws IOException, URISyntaxException{
        MapLoaderController mapLoaderController = new MapLoaderController(mapService);
        URI uri = getClass().getClassLoader().getResource("jenny.map").toURI();
        String file = FileUtils.readFileToString(new File(uri), StandardCharsets.UTF_8);
        mapLoaderController.parseFile(file);

        occupyCountry(player, "siberia", 10);
        occupyCountry(player, "yazteck", 20);
        occupyCountry(player, "worrick", 10);
    }

    /**
     * mock the player, and occupation data, cardList
     * @throws IOException
     * @throws URISyntaxException
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
     * @throws IOException
     * @throws URISyntaxException
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
     * @throws IOException
     * @throws URISyntaxException
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
     * @throws IOException
     * @throws URISyntaxException
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
     * test the reinforcement army calculation based on rule 1 and rule 3
     * the player occupy 2 countries and did not occupy a whole continent
     * rule 1: armyNum += country/3
     * rule 3: armyNum < 3, armyNum will be three
     * expect the reinforceArmy number be 3
     * @throws IOException
     * @throws URISyntaxException
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
     * @throws IOException
     * @throws URISyntaxException
     */
    @Test
    public void calculateReinforceArmiesWithWholeContinentOccupied() throws IOException, URISyntaxException {
        mockPlayerCountryInformationTwo();
        reinforceGameController.calculateReinforcedArmies(player);
        assertEquals(6, reinforceGameController.getReinforcedArmies());
    }

    /**
     * test the reinforcement army calculation based on card exchanges
     * if the players exchange three same cards, army += tradeTime * 5
     * expect result to be 5
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
     * @throws IOException
     * @throws URISyntaxException
     */
    @Test
    public void exchangeNoneWithFiveCards() throws IOException, URISyntaxException{
        mockPlayerCountryInformationSix();
        String command = "exchangecards -none";
        reinforceGameController.exchangeCards(player, command);
        assertFalse(reinforceGameController.isExchangeCardOver());
    }
}