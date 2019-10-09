package com6441.team7.risc.controller;

import static com6441.team7.risc.api.RiscContants.WHITESPACE;
import static java.util.Objects.isNull;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
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
	
	private AtomicInteger continentIdGenerator;
    private AtomicInteger countryIdGenerator;
	
	
	private boolean boolMapLoaded;
	
	
	private boolean boolGamePlayerAdded;
	private boolean boolAllGamePlayersAdded;
	
	private AtomicBoolean boolStartUpPhaseOver;
	
	private Player currentPlayer;
	
	private MapService mapService;
	
	//private LinkedHashMap<String, Player> players;
	
	private ArrayList<Player> players;
	
	public startupGameController(MapService mapService,
			ArrayList<Player> players, Player currentPlayer) {
		
		this.boolMapLoaded=false;
		
		
		boolAllGamePlayersAdded=false;
		boolGamePlayerAdded=false;
			
		this.mapService=mapService;
		
		this.players=players; 
		this.currentPlayer=currentPlayer;
		
		this.continentIdGenerator = new AtomicInteger();
        this.countryIdGenerator = new AtomicInteger();
		
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
        		System.out.println("All Players Added/Removed");
        	}
        	
        	break;
        	
        case POPULATE_COUNTRY:
        	
        	if(boolMapLoaded) {
        		
        		if(!players.isEmpty()) {
        			
        			boolAllGamePlayersAdded=true;
        			this.boolStartUpPhaseOver.set(true);
                	populateCountries();               	
        		}
        		
        		else {
        			
        			System.out.println("No Player Added. Add 1 player atleast");
        		}
        	}
        	
        	else {
        		
        		if(players.isEmpty()) System.out.println("No Player Added. Add 1 player atleast");
        		
        		//System.out.println("Load Map First");
        	}
        	
        	break;
        	
        case PLACE_ARMY:
        	break;
        	
        case PLACE_ALL:
        	break;
        	
        default:
            throw new IllegalArgumentException("cannot recognize this command");
        	
        
        }
        
	}

	
	//CHANGE STATE TO REINFORCEMENT IN MAPSERVICE AFTER END OF LAST METHOD

	private void populateCountries() {
		// TODO Auto-generated method stub
		
		//CHECK IF ONLY 1 PLAYER: PLAYER WINS
		System.out.println("Countries Populated");
		
		this.mapService.setState(GameState.REINFORCE);
		
		this.currentPlayer=players.get(0);
		System.out.println("Current Player:"+currentPlayer.getName());
		
	}
	
	
	private void editPlayer(String[] s) {
		// TODO Auto-generated method stub
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
    		throw new PlayerEditException("gameplayer command: cannot add it is not valid", e);
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
    		throw new PlayerEditException("gameplayer command: cannot add it is not valid", e);
    	}
    }
    
    
	/* ADAPTED JENNY CODE */

	
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
	            readFile(path);
	            return Optional.of(path);
	        }

	       // view.displayMessage("The command loadmap is not valid");
	        System.out.println("Command LoadMap is not valid"); 
	        return Optional.empty();

		//NEED TO UPDATE BOOLFILENAME
		
	}

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
            this.mapService.clearMapService();;
            
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
    
    private void showMap() {
        mapService.printCountryInfo();
        mapService.printContinentInfo();
        mapService.printNeighboringCountryInfo();
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


	
    private String convertFormat(String name) {
        return StringUtils.deleteWhitespace(name).toLowerCase(Locale.CANADA);
    }

}
