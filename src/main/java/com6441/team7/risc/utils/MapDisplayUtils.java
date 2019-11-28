package com6441.team7.risc.utils;

import com6441.team7.risc.api.model.Continent;
import com6441.team7.risc.api.model.Country;
import com6441.team7.risc.api.model.MapService;
import com6441.team7.risc.api.model.Player;
import com6441.team7.risc.api.model.PlayerService;
import com6441.team7.risc.view.GameView;

import java.util.Optional;
import java.util.Set;
import java.util.function.BiPredicate;

import static com6441.team7.risc.api.RiscConstants.NEWLINE;
import static com6441.team7.risc.utils.NotnullUtils.toStream;
import static java.util.Objects.requireNonNull;

import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

/**
 * a util class to show players, occupation, soldiers information.
 */
public final class MapDisplayUtils {

	/**
	 * boolean value if country belongs to the player
	 */
    private static final BiPredicate<Country, Player> countryBelongsToPlayer = ((country, player) -> country.getPlayer().equals(player));

   
	/**
	 * boolean value anyPlayer set to true
	 */
	private static final BiPredicate<Country, Player> anyPlayers = (country, player) -> true;

	/**
	 * private constructor
	 */
	private MapDisplayUtils(){}

	/**
	 * show current players map information
	 * @param mapService reference
	 * @param gameView reference
	 * @param currentPlayer reference
	 */
    public static void showCurrentPlayerMap(MapService mapService, GameView gameView, Player currentPlayer) {
        requireNonNull(mapService);
        requireNonNull(gameView);
        requireNonNull(currentPlayer);

        mapService.getContinentCountriesMap().forEach((key, value) -> {
            displayContinentInfo(gameView, mapService, key);
            displayCountryInfo(gameView, mapService, value, countryBelongsToPlayer, currentPlayer);

        });
    }

	/**
	 * show map information to the view including player, occupied countries, corresponding continents and soldiers
	 * @param mapService reference
	 * @param gameView receives GameView
	 */
	public static void showFullMap(MapService mapService, GameView gameView) {
        requireNonNull(mapService);
        requireNonNull(gameView);

        mapService.getContinentCountriesMap().forEach((key, value) -> {
            displayContinentInfo(gameView, mapService, key);
            displayCountryInfo(gameView, mapService, value, anyPlayers, null);

        });
    }

	/**
	 * display continent information
	 * @param gameView display information on view.
	 * @param mapService map details are taken from here
	 * @param continentId required continent Id for its details
	 */
	private static void displayContinentInfo(GameView gameView, MapService mapService, Integer continentId) {
        mapService.getContinentById(continentId)
                .ifPresent(continent -> {
                    gameView.displayMessage("\t\t\t\t\t\t\t\t\tContinent "+continent.getName()+NEWLINE);
                });
    }

	/**
	 * display country information
	 * @param gameView reference
	 * @param mapService reference
	 * @param countriesIds reference
	 * @param predicate of country and player
	 * @param current currentplayer
	 */
    private static void displayCountryInfo(GameView gameView, MapService mapService, Set<Integer> countriesIds, BiPredicate<Country, Player> predicate, Player current) {
        countriesIds.stream()
                .map(mapService::getCountryById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(country -> predicate.test(country, current))
                .forEach(country -> {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(country.getCountryName());
                    toStream(mapService.getAdjacencyCountries(country.getId()))
                            .map(mapService::getCountryById)
                            .filter(Optional::isPresent)
                            .map(Optional::get)
                            .map(Country::getCountryName)
                            .forEach(countryName -> stringBuilder.append(" --> ").append(countryName));
                    gameView.displayMessage(stringBuilder.toString() + NEWLINE);
                });
    }
    
    

	/**
	 * show map information including countries, continents, and neighboring countries
	 * @param mapService reference
	 */
	public static void showMap(MapService mapService) {
        mapService.printCountryInfo();
        mapService.printContinentInfo();
        mapService.printNeighboringCountryInfo();
    }
	
	/**
	 * Used after countries have been populated
	 * print map in a format of continent, countries belong to the continent
	 * for each country, print the players that occupy, the number of soldiers and
	 * its neighboring countries
	 * @param mapService reference
	 * @param view reference
	 */
	public static void showMapFullPopulated(MapService mapService, GameView view) {
    	
    	for(Map.Entry<Integer, Set<Integer>> item :
    						mapService.getContinentCountriesMap().entrySet()) {
    		
    		int key=(int) item.getKey();
    		   		
    		
    		Optional<Continent> optionalContinent=mapService.getContinentById(key);
    		Continent currentContinent= (Continent) optionalContinent.get();
    		
    		view.displayMessage("\t\t\t\t\t\t\t\t\tContinent "+currentContinent.getName());
    		view.displayMessage("\n");
    		
    		Set<Integer> value=item.getValue();
    		
    		for(Integer i:value) {
    			//For Each Country In Continent, Get details + Adjacency Countries
    			Optional<Country> optionalCountry=mapService.getCountryById(i);
    			
    			Country currentCountry=optionalCountry.get();
    			String strCountryOutput="";
    			
    			strCountryOutput+=currentCountry.getCountryName().toUpperCase()+":"+currentCountry.getPlayer().getName().toUpperCase()+
    					", "+currentCountry.getSoldiers()+" soldiers   ";
    			
    			Set<Integer> adjCountryList= mapService.getAdjacencyCountriesMap().get(i);
    			
    			for(Integer j:adjCountryList) {
    				strCountryOutput+=" --> "+mapService.getCountryById(j).get().getCountryName()+
    						"("+mapService.getCountryById(j).get().getPlayer().getName()+
    						":"+mapService.getCountryById(j).get().getSoldiers()+")";
    			}
    			
    			view.displayMessage(strCountryOutput+"\n");
    		}

    	}

    }
	
	
    /**
     * Used before populating countries. Does not display any ownership information
     * show map information with continents, each country relates to continents and its neighboring countries
     * @param mapService reference
	 * @param view reference
     */
    public static void showMapFullUnpopulated(MapService mapService, GameView view) {

    	for(Map.Entry<Integer, Set<Integer>> item :
    						mapService.getContinentCountriesMap().entrySet()) {

    		int key=(int) item.getKey();


    		Optional<Continent> optionalContinent=mapService.getContinentById(key);
    		Continent currentContinent= (Continent) optionalContinent.get();

    		view.displayMessage("\t\t\t\t\t\t\t\t\tContinent "+currentContinent.getName()+"\n");

    		Set<Integer> value=item.getValue();

    		for(Integer i:value) {
    			//For Each Country In Continent, Get details + Adjacency Countries
    			Optional<Country> optionalCountry=mapService.getCountryById(i);

    			Country currentCountry=optionalCountry.get();
    			String strCountryOutput="";

    			strCountryOutput+=currentCountry.getCountryName();

    			Set<Integer> adjCountryList= mapService.getAdjacencyCountriesMap().get(i);

    			if(adjCountryList != null) {
                    for (Integer j : adjCountryList) {
                        strCountryOutput += " --> " + mapService.getCountryById(j).get().getCountryName();
                    }
                }

    			view.displayMessage(strCountryOutput+"\n");
    		}

    	}

    }
    
    
	/**
	 * show countries occupied by the current player and adjacent countries occupied by player only
	 * this is useful in the fortification phase when player wants to know only about its countries
	*  @param mapService reference
	*  @param playerService reference
	 * @param view reference
	 */
	public static void showPlayerCountries(MapService mapService, PlayerService playerService, GameView view) {
    	
    	Player currentPlayer=playerService.getCurrentPlayer();
    	
    	if(currentPlayer==null) {
    		view.displayMessage("Add a player before calling showplayercountries command");
    		return;
    	}
    	
    	for(Map.Entry<Integer, Set<Integer>> item :
    						mapService.getContinentCountriesMap().entrySet()) {
    		
    		int key=(int) item.getKey();
    		   		
    		
    		Optional<Continent> optionalContinent=mapService.getContinentById(key);
    		Continent currentContinent= (Continent) optionalContinent.get();
    		
    		view.displayMessage("\t\t\t\t\t\t\t\t\tContinent "+currentContinent.getName());
    		view.displayMessage("\n");
    		
    		Set<Integer> value=item.getValue();
    		
    		for(Integer i:value) {
    			
    			//For Each Country In Continent, Get details + Adjacency Countries
    			Optional<Country> optionalCountry=mapService.getCountryById(i);
    			
    			Country currentCountry=optionalCountry.get();
    			
    			if(currentCountry.getPlayer().getName().equalsIgnoreCase(currentPlayer.getName())) {
    				
        			String strCountryOutput="";
        			
        			strCountryOutput+=currentCountry.getCountryName().toUpperCase()+":"+currentCountry.getPlayer().getName().toUpperCase()+
        					", "+currentCountry.getSoldiers()+" soldiers   ";
        			
        			Set<Integer> adjCountryList= mapService.getAdjacencyCountriesMap().get(i);
        			
        			for(Integer j:adjCountryList) {
        				
        				if(mapService.getCountryById(j).get().getPlayer().getName()
        						.equalsIgnoreCase(currentPlayer.getName())){
        	        				
        	        		strCountryOutput+=" --> "+mapService.getCountryById(j).get().getCountryName()+
        	        				"("+mapService.getCountryById(j).get().getPlayer().getName()+
        	        				":"+mapService.getCountryById(j).get().getSoldiers()+")";
        						}
        				
        			}
        			
        			view.displayMessage(strCountryOutput+"\n");    				
    				
    			}
    			

    		}

    	}

    }
    
    
	/**
	 * show current player name, number of armies, continent,
	 * countries, and number of armies on each country
	 * @param mapService reference
	 * @param playerService reference
	 * @param view reference
	 */
	public static void showPlayer(MapService mapService, PlayerService playerService, GameView view) {
		
		Player currentPlayer=playerService.getCurrentPlayer();
		
		if(currentPlayer==null) {
			view.displayMessage("Add a player before calling showplayer command");
			return;
		}
    	
    	Collections.sort(currentPlayer.getCountryPlayerList(), new Comparator<Country>() {

			@Override
			public int compare(Country c1, Country c2) {

				return c1.getContinentName().compareTo(c2.getContinentName());
			}
    		
    	}
    	);
    	
    	view.displayMessage("Current Player: "+currentPlayer.getName()+
    			" , Num Armies Remaining to be placed: "+currentPlayer.getArmies());
    	
    	for(Country c : currentPlayer.getCountryPlayerList()) {
    		view.displayMessage(c.getContinentName()+"\t"+c.getCountryName()+"\t"+c.getSoldiers());
    	}
    	
    }
    
	
	
	/**
	 * show players information including current player name, number of armies, continent,
	 * countries, and number of armies on each country
	 * @param mapService reference
	 * @param playerService reference
	 * @param view reference
	 */
	public static void showAllPlayers(MapService mapService, PlayerService playerService, GameView view) {
    	
    	for(Player p: playerService.getPlayerList()) {
    		
        	Collections.sort(p.getCountryPlayerList(), new Comparator<Country>() {

    			@Override
    			public int compare(Country c1, Country c2) {

    				return c1.getContinentName().compareTo(c2.getContinentName());
    			}
        		
        	}
        	);
        	
        	view.displayMessage("\n Player: "+p.getName()+
        			" Num Armies Remaining: "+p.getArmies());
        	
        	for(Country c :p.getCountryPlayerList()) {
        		view.displayMessage(c.getContinentName()+"\t"+c.getCountryName()
        						+"\t"+c.getSoldiers());
        	}

    	}

    }
	
	
	
	/**
	 * show all countries occupied by current player and  information about all adjacent countries.
	 * This is useful in the attack phase when player wants to know which countries it owns and which countries it can attack.
	 * @param mapService reference
	 * @param playerService reference
	 * @param view reference
	 */
	public static void showPlayerAllCountries(MapService mapService, PlayerService playerService,
																	GameView view) {
    	
    	Player currentPlayer=playerService.getCurrentPlayer();
    	
    	if(currentPlayer==null) {
    		view.displayMessage("Add a player before calling showplayerallcountries command");
    		return;
    	}
    	
    	for(Map.Entry<Integer, Set<Integer>> item :
    						mapService.getContinentCountriesMap().entrySet()) {
    		
    		int key=(int) item.getKey();
    		   		
    		
    		Optional<Continent> optionalContinent=mapService.getContinentById(key);
    		Continent currentContinent= (Continent) optionalContinent.get();
    		
    		view.displayMessage("\t\t\t\t\t\t\t\t\tContinent "+currentContinent.getName());
    		view.displayMessage("\n");
    		
    		Set<Integer> value=item.getValue();
    		
    		for(Integer i:value) {
    			//For Each Country In Continent, Get details + Adjacency Countries
    			Optional<Country> optionalCountry=mapService.getCountryById(i);
    			
    			Country currentCountry=optionalCountry.get();
    			
    			if(currentCountry.getPlayer().getName().equalsIgnoreCase(currentPlayer.getName())) {
    				
        			String strCountryOutput="";
        			
        			strCountryOutput+=currentCountry.getCountryName().toUpperCase()+":"+currentCountry.getPlayer().getName().toUpperCase()+
        					", "+currentCountry.getSoldiers()+" soldiers   ";
        			
        			Set<Integer> adjCountryList= mapService.getAdjacencyCountriesMap().get(i);
        			
        			for(Integer j:adjCountryList) {
        	        				
        	        		strCountryOutput+=" --> "+mapService.getCountryById(j).get().getCountryName()+
        	        				"("+mapService.getCountryById(j).get().getPlayer().getName()+
        	        				":"+mapService.getCountryById(j).get().getSoldiers()+")";
        						
        				
        			}
        			
        			view.displayMessage(strCountryOutput+"\n");    				
    				
    			}
    			

    		}

    	}

    }
    
}
