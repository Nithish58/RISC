package com6441.team7.risc.controller;

import com6441.team7.risc.api.model.Country;
import com6441.team7.risc.api.model.MapLoader;
import com6441.team7.risc.api.model.MapService;
import com6441.team7.risc.api.model.Player;
import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.util.*;

import java.util.stream.Collectors;
import java.util.stream.Stream;


public class GameController {
    private static Logger logger = LogManager.getLogger(MapLoader.class);
    private MapService mapService;
    private CircularFifoQueue<Player> playerList = new CircularFifoQueue<>();
    private Player humanPlayer;

    public GameController(MapService mapService) {
        this.mapService = mapService;
    }

    public void play(Scanner scanner){
        startup(scanner);
        reinforce(scanner);

    }


    public void startup(Scanner scanner) {


        System.out.println("Type in the number of playerList for the game?");
        try {
            int playerNumber = Integer.parseInt(scanner.next());
            System.out.println("enter the name of each player. First name is yourself");
            scanner.nextLine();

            List<Player> list = Stream.generate(scanner::nextLine)
                    .limit(playerNumber)
                    .map(Player::new)
                    .collect(Collectors.toList());

            playerList.addAll(list);
            humanPlayer = playerList.get(0);
        }catch (NumberFormatException e){
            logger.error("cannot parse the string to integer");
            System.exit(126);
        }


        setInitialArmies();
        assignCountry(scanner);

    }

    private void reinforce(Scanner scanner){

        while(!endOfReinforce()){
            Player player = decidePlayerTurn().get();
            calculateArmy(scanner, player);

            if(player == humanPlayer){
                placeArmyOnMap(player, scanner);
            }else{
                randomlyPlaceArmyOnMap(player);
            }
        }


    }

    private boolean endOfReinforce(){
        return playerList.stream().noneMatch(player -> player.getArmies() > 0);

    }
    private void randomlyPlaceArmyOnMap(Player player) {

        while(player.getArmies() > 0){
            System.out.println(player.getName() + " has " + player.getArmies() + " soldiers.");
            int soldierToBePlaced = (int) (Math.random() * player.getArmies() + 1);
            Random rand = new Random();
            int size = (int)mapService.getCountrySet().stream()
                    .filter(country -> country.getPlayer() == player)
                    .count();

            Optional<Country> c = mapService.getCountrySet().stream()
                    .filter(country -> country.getPlayer() == player)
                    .skip(rand.nextInt(size - 1))
                    .findAny();

            c.get().addSoldier(soldierToBePlaced);
            player.reduceArmy(soldierToBePlaced);
            System.out.println(player.getName() + " puts " + soldierToBePlaced + " soldiers on the country " +c.get().getName());
            offerPlayerOwnCountry(player);
        }

        System.out.print("\n\n");
    }

    private void placeArmyOnMap(Player player, Scanner scanner){

        try{
            while(player.getArmies() > 0) {
                offerPlayerOwnCountry(player);
                System.out.println(player.getName() + " has " + player.getArmies() + " soldiers.");
                System.out.println("enter your country you would like to put soldiers. For example, Japan 3");

                String[] info = scanner.nextLine().split(" ");
                String rawCountry = info[0].toLowerCase();
                int soldierToBePlaced = Integer.parseInt(info[1]);

                Set<Country> countrySet = mapService.getCountrySet().stream()
                        .filter(country -> country.getPlayer() == player)
                        .filter(country -> country.getName().equalsIgnoreCase(rawCountry))
                        .collect(Collectors.toSet());

                if(soldierToBePlaced > player.getArmies()){
                    System.out.println("The soldiers to put exceed the number of soldiers you have");
                    continue;
                }

                if (countrySet.size() == 0) {
                    System.out.println("Please enter your country you occupy.");
                    continue;
                }

                mapService.getCountrySet().stream()
                        .filter(country -> country.getName().equalsIgnoreCase(rawCountry))
                        .forEach(country -> country.addSoldier(soldierToBePlaced));

                player.reduceArmy(soldierToBePlaced);
                System.out.println(player.getName() + " put " + soldierToBePlaced + " soldiers on the country " + rawCountry);

            }

            offerPlayerOwnCountry(player);
        }
        catch (Exception e){
            logger.error(e.getMessage());
            System.exit(126);
        }

    }

    private void offerPlayerOwnCountry(Player player){
        System.out.println("The country for " + player.getName());
          mapService.getCountrySet().stream()
                .filter(country -> country.getPlayer() == player)
                .forEach(country -> System.out.println(country.getName() + " : " + country.getSoldier()));
    }

    private void calculateArmy(Scanner scanner, Player player){
        int number = 0;

        number += tradeInCards(scanner, player);

        number += calculateSoldierOnOccupyingCountry(player);

        if(number < 3){
            number = 3;
        }

        player.addArmy(number);
        System.out.println(player.getName() + " get " + number + " extra soldiers.");
    }


    private int calculateSoldierOnOccupyingCountry(Player player){

        long result = mapService.getCountrySet().stream()
                .filter(country -> country.getPlayer() == player)
                .count()/3;

        return (int)Math.floor(result);
    }

    private int tradeInCards(Scanner scanner, Player player){

        int numberOfCards = player.getCardList().size();
        int soldierNumber = 0;

        if(numberOfCards < 3){
            return soldierNumber;
        }

        if(player == humanPlayer) {
            if (numberOfCards < 5 && player.meetTradeInCondition()) {
                System.out.println("Would you like to trade in cards?(y/n)");
                String reply = scanner.nextLine();

                if ("y".equalsIgnoreCase(reply)) {
                    player.removeCards();
                    soldierNumber += player.getTradeInTimes() * 5;
                }
            }
        }else{
            if(numberOfCards < 5 && player.meetTradeInCondition()){
                player.removeCards();
                soldierNumber += player.getTradeInTimes() * 5;
            }
        }

        while(numberOfCards >=5){
            System.out.println("You must trade in cards.");
            player.removeCards();
            soldierNumber += player.getTradeInTimes() * 5;
            numberOfCards = player.getCardList().size();
        }

        return soldierNumber;

    }

    private void setInitialArmies() {

        int numberOfArmiesEachPlayer = 40 - (playerList.size() - 2) * 5;

        if (numberOfArmiesEachPlayer <= 0) {
            logger.error("The game cannot be exceed 8 person");
            System.exit(126);
        }

        playerList.forEach(player -> player.setArmies(numberOfArmiesEachPlayer));
        System.out.println("The number of soldiers assigned to each player is: " + numberOfArmiesEachPlayer);

    }

    private void assignCountry(Scanner scanner) {

        while (!isAllCountriesOccupies()) {
            Player player = decidePlayerTurn().get();
            if (isHumanPlayer(player)) {
                occupyCountry(player, scanner);
            } else {
                randomlyAssignCountry(player);
            }
        }
    }

    private boolean isAllCountriesOccupies() {
        return mapService.getCountrySet()
                .stream().noneMatch(country -> country.getPlayer() == null);
    }

    private boolean isHumanPlayer(Player player) {
        return player == humanPlayer;
    }

    private Optional<Player> decidePlayerTurn() {
        try {
            Player player = playerList.poll();
            playerList.add(player);
            return Optional.of(player);

        } catch (NullPointerException e) {
            logger.error(e.getMessage());
            System.exit(126);
        }

        return Optional.empty();
    }


    private void offerAvailableCountryInfo() {
        System.out.println("The available countries to be occupied are:");
        mapService.getCountrySet().stream()
                .filter(this::isNotOccupied)
                .forEach(country -> System.out.print(country.getName() + ", "));
        System.out.println("\n");
    }


    private void occupyCountry(Player player, Scanner scanner) {

        if (player.getArmies() > 0) {
            System.out.println("It is " + player.getName() + " turn.");
            offerAvailableCountryInfo();
        } else {
            System.out.println("You are out of the soldiers");
            return;
        }

        System.out.println("choose the country you would like to occupy");

        while (true) {
            String country = scanner.nextLine();
            boolean isCountryValid = validateCountry(country);

            if (isCountryValid) {
                setPlayersToCountry(country, player);
                player.reduceArmy(1);
                System.out.println(player.getName() + " occupied " + country.toLowerCase() + "\n");
                break;
            } else {
                System.out.println("The country you type is not valid, type again: country you would like to occupy");
            }
        }



    }

    private void randomlyAssignCountry(Player player) {
        Optional<Country> optional = mapService.getCountrySet().stream()
                .filter(country -> country.getPlayer() == null)
                .findAny();

        optional.ifPresent(country -> {
            country.setPlayer(player);
            country.setSoldier(1);
            System.out.println(player.getName() + " occupied the country " + country.getName() + "\n");

        });
    }

    private void setPlayersToCountry(String rawCountry, Player player) {
        mapService.getCountrySet().stream()
                .filter(country -> country.getName().equalsIgnoreCase(rawCountry))
                .forEach(country -> {
                            country.setPlayer(player);
                            country.setSoldier(1);
                        }
                );
    }

    private boolean validateCountry(String text) {
        return mapService.getCountrySet().stream()
                .filter(this::isNotOccupied)
                .map(Country::getName)
                .map(String::toLowerCase)
                .filter(country -> country.equalsIgnoreCase(text))
                .collect(Collectors.toSet())
                .contains(text.toLowerCase());
    }


    private boolean isNotOccupied(Country country) {
        return country.getPlayer() == null;
    }

}
