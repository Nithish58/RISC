package com6441.team7.risc.controller;

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
     * setup method to construct attributes before each test
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        mapService = new MapService();
        playerService = new PlayerService(mapService);
        Player player = new Player("jenny");
        playerService.setCurrentPlayer(player);
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

    @Test
    public void readValidCommand() throws Exception{
        String command = "reinforce siberia 1";
        reinforceGameController.readCommand(command);
    }


    /**
     * sucessfully add a soldier from reinforcement to the country occupied
     * @throws URISyntaxException
     * @throws IOException
     */
    @Test
    public void reinforceArmy() throws URISyntaxException, IOException {
        mockPlayerCountryInformationOne();
        Player player = playerService.getCurrentPlayer();

        reinforceGameController.reinforceArmy(player, "siberia", 1);

        int armyNum = mapService.getCountries().stream()
                .filter(country -> country.getCountryName().equalsIgnoreCase("siberia"))
                .findFirst()
                .get()
                .getSoldiers();

        assertEquals(11, armyNum);

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

        Player player = playerService.getCurrentPlayer();
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

        Player player = playerService.getCurrentPlayer();
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
        Player player = playerService.getCurrentPlayer();
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
        Player player = playerService.getCurrentPlayer();
        List<Card> cardList = new ArrayList<>();
        cardList.add(Card.ARTILLERY);
        cardList.add(Card.CAVALRY);
        cardList.add(Card.INFANTRY);
        player.setCardList(cardList);
    }

    /**
     * mock the player, and occupation data, cardList
     * @throws IOException
     * @throws URISyntaxException
     */
    private void mockPlayerCountryInformationFive() throws IOException, URISyntaxException{
        Player player = playerService.getCurrentPlayer();
        List<Card> cardList = new ArrayList<>();
        cardList.add(Card.ARTILLERY);
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
    public void calculateReinforcedArmies() throws IOException, URISyntaxException {
        mockPlayerCountryInformationOne();
        Player player = playerService.getCurrentPlayer();
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
        Player player = playerService.getCurrentPlayer();
        reinforceGameController.calculateReinforcedArmies(player);
        assertEquals(6, reinforceGameController.getReinforcedArmies());
    }

    /**
     * test the reinforcement army calculation based on card exchanges
     * if the players exchange three same cards, army += tradeTime * 5
     * expect result to be 5
     */
    @Test
    public void exchangeSameCards() throws IOException, URISyntaxException {
        mockPlayerCountryInformationThree();
        Player player = playerService.getCurrentPlayer();
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
        Player player = playerService.getCurrentPlayer();
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
    public void exchangeInvalidCards() throws IOException, URISyntaxException{
        mockPlayerCountryInformationFive();
        Player player = playerService.getCurrentPlayer();
        String command = "exchangecards 1 2 3";
        reinforceGameController.exchangeCards(player, command);
        assertEquals(0, reinforceGameController.getReinforcedArmies());
    }
}