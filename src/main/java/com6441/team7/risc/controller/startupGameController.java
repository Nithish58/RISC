package com6441.team7.risc.controller;

import static com6441.team7.risc.api.RiscConstants.WHITESPACE;
import static java.util.Objects.isNull;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com6441.team7.risc.api.exception.ContinentEditException;
import com6441.team7.risc.api.exception.ContinentParsingException;
import com6441.team7.risc.api.exception.CountryParsingException;
import com6441.team7.risc.api.exception.MissingInfoException;
import com6441.team7.risc.api.exception.NeighborParsingException;
import com6441.team7.risc.api.exception.PlayerEditException;
import com6441.team7.risc.api.model.Continent;
import com6441.team7.risc.api.model.Country;
import com6441.team7.risc.api.model.GameState;
import com6441.team7.risc.api.model.MapService;
import com6441.team7.risc.api.model.Player;
import com6441.team7.risc.api.model.RiscCommand;

public class startupGameController {
	
	private MapLoaderController mapLoaderController;
	
	//private AtomicInteger continentIdGenerator;
    //private AtomicInteger countryIdGenerator;
    
	private boolean boolMapLoaded;
	private boolean boolGamePlayerAdded;
	private boolean boolAllGamePlayersAdded;
	private boolean boolCountriesPopulated;
	
	private boolean[] boolArrayCountriesPlaced;
	
	private AtomicBoolean boolStartUpPhaseOver;
	
	//private Player currentPlayer;
	int currentPlayerIndex;
	
	private MapService mapService;
	
	//private LinkedHashMap<String, Player> players;
	
	private ArrayList<Player> players;
	
	public startupGameController(MapLoaderController mapController ,MapService mapService,
			ArrayList<Player> players) {
		
		this.mapLoaderController=mapController;
		
		this.boolMapLoaded=false;
		
		
		this.boolAllGamePlayersAdded=false;
		this.boolGamePlayerAdded=false;
		this.boolCountriesPopulated=false;
			
		this.mapService=mapService;
		
		this.players=players; 
		
		//this.currentPlayer=currentPlayer;
		
	//	this.continentIdGenerator = new AtomicInteger();
     //   this.countryIdGenerator = new AtomicInteger();
		
	}
	

	public void readCommand(String command, AtomicBoolean boolStartUpPhaseOver) {
		
        RiscCommand commandType = RiscCommand.parse(StringUtils.split(command, WHITESPACE)[0]);

        String[] commands = {};

        if(command.contains("-")){
            command = StringUtils.substringAfter(command, "-");
            commands = StringUtils.split(command, "-");
        }
        
        this.boolStartUpPhaseOver=boolStartUpPhaseOver;

        switch(commandType) {
        	
        case LOAD_MAP:
        	
        	if(!boolMapLoaded) {
        		
        		String fileName=StringUtils.split(command, WHITESPACE)[1];       		
        		loadMap(command);        		
        	}
        	
        	else {
        		
        		if(boolMapLoaded) {
        			
        			String fileName=StringUtils.split(command, WHITESPACE)[1];       		
            		loadMap(command);
        		}
        		else System.out.println("Load Map First");
        	}
        	
        	break;
        	
        case GAME_PLAYER:
        	
        	if(boolMapLoaded && (!boolAllGamePlayersAdded)) {
        		editPlayer(commands);
        	}
        	
        	else {
        		System.out.println("You are past adding phase.All Players Added/Removed");
        	}
        	
        	break;
        	
        case POPULATE_COUNTRY:
        	
        	if(boolMapLoaded) {
        		
        		if(!players.isEmpty()) {
        			
        			boolAllGamePlayersAdded=true;
        			//this.boolStartUpPhaseOver.set(true);
                	populateCountries();               	
        		}
        		
        		else {
        			
        			System.out.println("No Player Added. Add 1 player atleast");
        		}
        	}
        	
        	else {
        		
        		System.out.println("Load a Map first");
        		
        		if(players.isEmpty()) System.out.println("No Player Added. Add 1 player atleast");
        		
        		//System.out.println("Load Map First");
        	}
        	
        	break;
        	
        case PLACE_ARMY:
        	
        	String[] strArr=StringUtils.split(command, WHITESPACE);
        	
        	if(strArr.length!=2) System.out.println("Invalide Placearmy command");
        	else {
        		placeArmy(strArr[1]);
        	}
        	
        	break;
        	
        case PLACE_ALL:
        	placeAll();
        	break;
        	
        case SHOW_PLAYER:
        	showPlayer();
        	break;
        	
        case SHOW_MAP:
        	if(boolCountriesPopulated) showMapFull();
        	
        	else showMap();
        	
        	break;
        	
        default:
            throw new IllegalArgumentException("cannot recognize this command");

        }
        
	}

	
	//CHANGE STATE TO REINFORCEMENT IN MAPSERVICE AFTER END OF LAST METHOD

	//Assign randomly countries to players
	//Calculate number of initial soldiers allocated to players based on numPlayers
	//Assign 1 army to each country and decrement from a player's total
	
	private void populateCountries() {
		
		if(!boolCountriesPopulated) {
			
			int numPlayers=players.size();
			System.out.println("NumPlayers: "+numPlayers);
			
			//CHECK IF ONLY 1 PLAYER: PLAYER WINS
			if(numPlayers==1) {
				System.out.println("PLAYER "+players.get(0).getName()+" WINS");
				endGame();
			}
			
			else if(numPlayers==0) {
				System.out.println("No Players Added. Try again");
			}
			
			else if(numPlayers>9) {
				System.out.println("Player limit exceeded. Cannot Proceed");
			}
			
			else {
				
				int numInitialArmies=determineNumInitialArmies(numPlayers);
				assignInitialArmies(numInitialArmies);
				
				System.out.println(numInitialArmies);
				
				Stack<Country> stackCountry=new Stack<>();
				
				Iterator setIter=mapService.getCountries().iterator();
				
				while(setIter.hasNext()) {
					stackCountry.push((Country) setIter.next());
				}
				
				Collections.shuffle(stackCountry);
				Collections.shuffle(stackCountry);
				Collections.shuffle(stackCountry);
				
				int currentPlayerIndex=0;
				
				while(!stackCountry.isEmpty()) {
					
					Country currentCountry=stackCountry.pop();
					
					currentCountry.setPlayer(players.get(currentPlayerIndex));
					
					currentCountry.addSoldiers(1);
					
					players.get(currentPlayerIndex).reduceArmy(1);
					
					players.get(currentPlayerIndex).countryPlayerList.add(currentCountry);
					
					currentPlayerIndex++;
					
					if(currentPlayerIndex==players.size()) currentPlayerIndex=0;
					
				}
				
				
				System.out.println(mapService.getCountries().size());
				
				for(Country c:mapService.getCountries()) {
					System.out.println(c.getId()+" "+c.getCountryName()+" "+c.getPlayer().getName()
							+" "+c.getSoldiers());
				}
							
				//gameplayer -add keshav -add jenny -remove jenny -add binsar -add jenny -add bikash -add keshav -add lol
				
				System.out.println("Countries Populated. Start placing your armies now.");
				
				this.boolCountriesPopulated=true;
				
				this.boolArrayCountriesPlaced=new boolean[players.size()];
				
				for(Player p:players) {
					System.out.println("Remaining Armies for "+p.getName()
											+": "+p.getArmies());
				}
				
				this.currentPlayerIndex=0;
				System.out.println("\nCurrent Player:"+players.get(this.currentPlayerIndex).getName());
				
			}

		}
		
		else {
			System.out.println("Countries already populated");
		}
		
	}
	
	private int determineNumInitialArmies(int numPlayers) {
		return 40-((numPlayers-2)*5);
	}
	
	private void assignInitialArmies(int numArmies) {
		
		for(Player p: players) {
			p.setArmies(numArmies);
		}
		
	}
	
	
	private void editPlayer(String[] s) {
		
			 Arrays.stream(s).forEach(this::editPlayerFromUserInput);				
	
	}

    private void editPlayerFromUserInput(String s) {
        String[] commands = StringUtils.split(s, " ");
        switch (convertFormat(commands[0])) {
            case "add":
                addPlayer(commands);
                break;
            case "remove":
                removePlayer(commands);
                break;
            default:
                throw new ContinentEditException("The gameplayer command " + s + " is not valid.");
        }
    }
    
    private void addPlayer(String[] s) {
    	
    	if(players.size()<9) {
    		
        	try {
        		
        		String playerName=convertFormat(s[1]);
        		
        		boolean nameFound=false;
        		
        		for(int i=0;i<players.size();i++) {
        			if(players.get(i).getName().equals(playerName)) {
        				nameFound=true;
        				System.out.println("Player Already Exists. Try different name");
        				break;
        			}
        		}
        		
        		if(!nameFound) {
        			players.add(new Player(playerName));
        			System.out.println("Player Added");
        		}
        		
        		
        		
        	}
        	
        	catch(Exception e) {
        		throw new PlayerEditException("gameplayer command: cannot add/remove it is not valid", e);
        	}
    		
    	}
    	
    	else {
    		System.out.println("Limit of 9 players reached.");
    	}
    	

    }
    
    
    private void removePlayer(String[] s) {
    	
    	try {
    		
    		String playerName=convertFormat(s[1]);
    		
    		boolean nameFound=false;
    		
    		for(int i=0;i<players.size();i++) {
    			if(players.get(i).getName().equals(playerName)) {
    				nameFound=true;
    				players.remove(i);
    				System.out.println("Player Removed");
    				break;
    			}
    		}
    		
    		if(!nameFound) System.out.println("gameplayer command: cannot remove, player does not exist");
    		
    	}
    	
    	catch(Exception e) {
    		throw new PlayerEditException("gameplayer command: cannot add/remove it is not valid", e);
    	}
    }
    
    
	Optional<String> loadMap(String s) {
		
	
	        String[] commands = StringUtils.split(s, " ");

	        if (commands.length != 2) {
	        	
	          //  view.displayMessage("The command editmap is not valid");
	           System.out.println("Command LoadMap is not valid"); 
	            return Optional.empty();
	        }

	        String command = commands[0];
	        String path = commands[1];

	        if (command.toLowerCase(Locale.CANADA).equals("loadmap")) {
	            //readFile(path);
	        	mapService.emptyMap();
	        	mapLoaderController.readFile(path);
	        
	        	showMap();
	        	
	        	if(validateMap()) {
	        		boolMapLoaded=true;
	        	}
	        	
	            return Optional.of(path);
	        }

	       // view.displayMessage("The command loadmap is not valid");
	        System.out.println("Command LoadMap is not valid"); 
	        return Optional.empty();

		//NEED TO UPDATE BOOLFILENAME
		
	}
	
   
    private boolean validateMap() {

        if(mapService.isMapValid()){
            //view.displayMessage("map is valid");
        	System.out.println("Map is Valid");
            return true;
        }
        else{
            //view.displayMessage("map is not valid");
        	System.out.println("Map is not Valid");
            return false;
        }
    }

    private void showMap() {
        mapService.printCountryInfo();
        mapService.printContinentInfo();
        mapService.printNeighboringCountryInfo();
    }
	
    private String convertFormat(String name) {
        return StringUtils.deleteWhitespace(name).toLowerCase(Locale.CANADA);
    }

    
    private int endGame() {
    	return 0;
    }
    
    public void showPlayer() {
    	
    	Collections.sort(players.get(currentPlayerIndex).countryPlayerList, new Comparator<Country>() {

			@Override
			public int compare(Country c1, Country c2) {

				return c1.getContinentName().compareTo(c2.getContinentName());
			}
    		
    	}
    	);
    	
    	System.out.println("Current Player: "+players.get(currentPlayerIndex).getName()+
    			" , Num Armies Remaining: "+players.get(currentPlayerIndex).getArmies());
    	
    	System.out.println("Continent \t\t\t\t Country \t\t\t\t NumArmies");
    	
    	for(Country c:players.get(currentPlayerIndex).countryPlayerList) {
    		System.out.println(c.getContinentName()+"\t\t\t"+c.getCountryName()+"\t\t\t"+c.getSoldiers());
    	}
    	
    }
    
    public void placeArmy(String countryName) {
    	
    	boolean countryFound=false;
    	Player currentPlayer=players.get(currentPlayerIndex);
    	
    	//Check if all armies of a specific player placed: if yes: switch to next player, else place army
    	if(!boolArrayCountriesPlaced[currentPlayerIndex]) {
    		
    		for(Country c:currentPlayer.countryPlayerList) {
    		    
        		if(countryName.equalsIgnoreCase(c.getCountryName())) {
        			countryFound=true;
        			
        			currentPlayer.reduceArmy(1);
        			
        			c.addSoldiers(1);		
        			
        		}
        	}
        	
        	if(!countryFound) System.out.println("Wrong Country Name!!");
        	
        	else {
        		
        		if(currentPlayer.getArmies()==0) {
        			boolArrayCountriesPlaced[currentPlayerIndex]=true;
        			System.out.println("All armies placed for "+currentPlayer.getName());
        		}
        		
        		//IF ALL Players have numArmies 0: Startup Phase Over, Switch To Next Phase 
        		boolStartUpPhaseOver.set(true);
        		
        		for(boolean b: boolArrayCountriesPlaced) {
        			
        			if(!b) {
        				boolStartUpPhaseOver.set(false);
        			}    			
        		}
        		
        		if(boolStartUpPhaseOver.get()) {
        			System.out.println("All Armies Placed.\n.");
        			this.mapService.setState(GameState.REINFORCE);
        		}
        		
        		else {
        			//Switch to Next Player
            		switchToNextPlayer();
        		}
        	}
    		
    	}

    	else {
    		switchToNextPlayer();
    	}
	
    }
    
    private void switchToNextPlayer() {
    	if(currentPlayerIndex==(players.size()-1)) currentPlayerIndex=0;
		
		else currentPlayerIndex++;
    	
    	showPlayer();
    	
    }
    
    public void placeAll() {
    	
    	for(Player p:players) {
    		
    		while(p.getArmies()>0) {
    			
    			int randomIndex=ThreadLocalRandom.current().nextInt(0,p.countryPlayerList.size());
    			
    			p.countryPlayerList.get(randomIndex).addSoldiers(1);
    			p.reduceArmy(1);
    			
    		}
    		
    	}
    	System.out.println("All Players Placed.");
    	showAllPlayers();
    	
    	this.boolStartUpPhaseOver.set(true);
    	this.mapService.setState(GameState.REINFORCE);
    }
    
    public void showAllPlayers() {
    	
    	for(Player p:players) {
    		
        	Collections.sort(p.countryPlayerList, new Comparator<Country>() {

    			@Override
    			public int compare(Country c1, Country c2) {

    				return c1.getContinentName().compareTo(c2.getContinentName());
    			}
        		
        	}
        	);
        	
        	System.out.println("Current Player: "+p.getName()+
        			" , Num Armies Remaining: "+p.getArmies());
        	
        	System.out.println("Continent \t\t\t\t Country \t\t\t\t NumArmies");
        	
        	for(Country c :p.countryPlayerList) {
        		System.out.println(c.getContinentName()+"\t\t\t"+c.getCountryName()
        						+"\t\t\t"+c.getSoldiers());
        	}

    	}

    }
    
    public void showMapFull() {
    	
    	for(Map.Entry<Integer, Set<Integer>> item :
    						mapService.getContinentCountriesMap().entrySet()) {
    		
    		int key=(int) item.getKey();
    		   		
    		
    		Optional<Continent> optionalContinent=mapService.getContinentById(key);
    		Continent currentContinent= (Continent) optionalContinent.get();
    		
    		System.out.println("\t\t\t\t\t\t\t\t\tContinent "+currentContinent.getName());
    		System.out.println();
    		
    		Set<Integer> value=item.getValue();
    		
    		for(Integer i:value) {
    			//For Each Country In Continent, Get details + Adjacency Countries
    			Optional<Country> optionalCountry=mapService.getCountryById(i);
    			
    			Country currentCountry=optionalCountry.get();
    			String strCountryOutput="";
    			
    			strCountryOutput+=currentCountry.getCountryName()+" "+currentCountry.getPlayer().getName()+
    					" "+currentCountry.getSoldiers()+" soldiers ";
    			
    			Set<Integer> adjCountryList= mapService.getAdjacencyCountriesMap().get(i);
    			
    			for(Integer j:adjCountryList) {
    				strCountryOutput+=" --> "+mapService.getCountryById(j).get().getCountryName();
    			}
    			System.out.println(strCountryOutput+"\n");
    		}
    		
    		
    	}

    }
    
    
}



/*
Optional<String> readFile(String name){
    try {
        URI uri = Paths.get(name).toUri();
        String file = FileUtils.readFileToString(new File(uri), StandardCharsets.UTF_8.name());
        
        if(parseFile(file)) {
        	boolMapLoaded=true;
        }
      //NEED TO UPDATE BOOLFILENAME
        
        return Optional.of(file);           

    } catch (IOException|NullPointerException e) {
        //createFile(name);
    	System.out.println("File Not Found");
    }

    return Optional.empty();
}

boolean parseFile(String s) {
    String[] parts = StringUtils.split(s, "[");

    try {
        if (parts.length != 5) {
            throw new MissingInfoException("The map is not valid");
        }

        //Clear MapService:
        //this.mapService.clearMapService();;
        
        //parseMapGraphInfo(parts[1]);
        parseRawContinents(parts[2]);
        parseRawCountries(parts[3]);
        parseRawNeighboringCountries(parts[4]);
        
       showMap();
        
        validateMap();

    } catch (Exception e) {
        //view.displayMessage(e.getMessage());
    	System.out.println(e.getMessage());
    	 System.out.println("PROBLEM LOADING FILE");
        boolMapLoaded=false;
        return false;
    }

    return mapService.isStronglyConnected();

}



Set<Continent> parseRawContinents(String part) {
    String continentInfo = StringUtils.substringAfter(part, "]\r\n");

    Set<Continent> continentSet = Optional.of(StringUtils.split(continentInfo, "\r\n"))
            .map(Arrays::stream)
            .orElseGet(Stream::empty)
            .map(this::createContinentFromRaw)
            .collect(Collectors.toSet());

    mapService.addContinent(continentSet);

    return continentSet;

}

private Continent createContinentFromRaw(String s) {

    try {

        String[] continentInfo = StringUtils.split(s, " ");

        if (isNull(continentInfo) || continentInfo.length != 3) {
            throw new ContinentParsingException("continent: " + s + " is not valid ");
        }

        String name = convertFormat(continentInfo[0]);
        int continentValue = Integer.parseInt(continentInfo[1]);
        String color = convertFormat(continentInfo[2]);

        Continent continent = new Continent(continentIdGenerator.incrementAndGet(), name, continentValue);
        continent.setColor(color);

        return continent;

    } catch (NumberFormatException e) {
        throw new ContinentParsingException(e);
    }

}



Set<Country> parseRawCountries(String part) {
    String countryInfo = StringUtils.substringAfter(part, "]\r\n");
    Set<Country> countrySet = Optional.of(StringUtils.split(countryInfo, "\r\n"))
            .map(Arrays::stream)
            .orElseGet(Stream::empty)
            .map(this::createCountryFromRaw)
            .collect(Collectors.toSet());

    mapService.addCountry(countrySet);
    return countrySet;
}



private Country createCountryFromRaw(String s) {
    try {
        String[] countryInfo = StringUtils.split(s, " ");

        if (countryInfo.length != 5) {
            throw new CountryParsingException("country: " + s + " is not valid.");
        }

        int countryId = Integer.parseInt(countryInfo[0]);
        String countryName = convertFormat(countryInfo[1]);
        int continentId = Integer.parseInt(countryInfo[2]);
        int coordinateX = Integer.parseInt(countryInfo[3]);
        int coordinateY = Integer.parseInt(countryInfo[4]);

        if (mapService.continentIdNotExist(continentId)) {
            throw new CountryParsingException("country: " + s + " contains invalid continent information");
        }

        String continentName = mapService.findCorrespondingNameByContidentId(continentId).get();
        Country country = new Country(countryId, countryName, continentId);
        country.setCoordinateX(coordinateX);
        country.setCoordinateY(coordinateY);
        country.setContinentName(continentName);

        return country;

    } catch (NumberFormatException e) {
        throw new CountryParsingException(e);
    }
}



Map<Integer, Set<Integer>> parseRawNeighboringCountries(String part) {

    String borderInfo = StringUtils.substringAfter(part, "]");

    String[] adjacencyInfo = StringUtils.split(borderInfo, "\n\r");
    Map<Integer, Set<Integer>> adjacencyMap = new HashMap<>();

    Arrays.stream(adjacencyInfo)
            .map(this::createAdjacencyCountriesFromRaw)
            .forEach(list -> {
                int countryId = list.get(0);
                Set<Integer> adjacencyId = new HashSet<>(list.subList(1, list.size()));
                adjacencyMap.put(countryId, adjacencyId);
            });

    mapService.addNeighboringCountries(adjacencyMap);

    return adjacencyMap;
}



private List<Integer> createAdjacencyCountriesFromRaw(String s) {
    List<String> adjacency = Arrays.asList(StringUtils.split(s, " "));

    throwWhenNoNeighboringCountry(s, adjacency);
    throwWhenNeighbouringCountriesIdInvalid(s, adjacency);

    return adjacency.stream()
            .map(Integer::parseInt)
            .collect(Collectors.toList());
}

private void throwWhenNeighbouringCountriesIdInvalid(String s, List<String> adjacency) {
    adjacency.stream()
            .map(rawInt -> {
                if (!NumberUtils.isDigits(rawInt)) {
                    throw new NeighborParsingException("adjacency: " + s + " Element " + rawInt + "is not valid");
                }
                return Integer.parseInt(rawInt);
            })
            .filter(this::isCountryIdNotValid)
            .findFirst()
            .ifPresent(invalidId -> {
                throw new NeighborParsingException("adjacency: " + s + " is not valid for the country id does not exist");
            });
}

private void throwWhenNoNeighboringCountry(String s, List<String> adjacency) {
    if (adjacency.size() <= 1) {
        throw new NeighborParsingException("adjacency: " + s + " is not valid for not having adjacent countries ");
    }
}

private boolean isCountryIdNotValid(int id) {
    return !mapService.countryIdExist(id);
}
*/ 
