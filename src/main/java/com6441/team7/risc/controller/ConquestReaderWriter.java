package com6441.team7.risc.controller;

import com6441.team7.risc.api.RiscConstants;
import com6441.team7.risc.api.exception.ContinentParsingException;
import com6441.team7.risc.api.exception.CountryParsingException;
import com6441.team7.risc.api.exception.MissingInfoException;
import com6441.team7.risc.api.exception.NeighborParsingException;
import com6441.team7.risc.api.model.*;
import com6441.team7.risc.utils.CommonUtils;
import com6441.team7.risc.view.GameView;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com6441.team7.risc.api.RiscConstants.COMMA;
import static com6441.team7.risc.api.RiscConstants.EOL;
import static com6441.team7.risc.api.RiscConstants.NON_EXIST;
import static java.util.Objects.isNull;

public class ConquestReaderWriter implements IConquestReaderWriter {

    private MapService mapService;
    private GameView view;
    private MapGraph mapGraph;
    /**
     * generates id for continents
     */
    private AtomicInteger continentIdGenerator;

    /**
     * generates id for countries
     */
    private AtomicInteger countryIdGenerator;

    public ConquestReaderWriter(MapService mapService, GameView view) {
        this.mapService = mapService;
        this.view = view;
        this.mapGraph = new MapGraph();
    }


    @Override
    public void showConquestMap() {
        view.displayMessage(getMapGraphString());
        view.displayMessage(getContinentString());
        view.displayMessage(getTerritoryString());
    }

    @Override
    public void saveConquestMap(String fileName) throws IOException {

        StringBuilder stringBuilder =
                new StringBuilder()
                        .append("[Map]")
                        .append(EOL)
                        .append(getMapGraphString())
                        .append("[continents]")
                        .append(EOL)
                        .append(getContinentString())
                        .append(EOL)
                        .append("[Territories]")
                        .append(EOL)
                        .append(getTerritoryString())
                        .append(EOL);

        File file = new File(fileName);
        FileUtils.writeStringToFile(file, stringBuilder.toString(), StandardCharsets.UTF_8.name());
        view.displayMessage("the map is successfully saved.");

    }

    /**
     * get maptGraph in strings
     *
     * @return
     */
    private String getMapGraphString() {
        return mapGraph.getMapGraph();
    }


    /**
     * get continents string
     *
     * @return
     */
    private String getContinentString() {
        StringBuilder stringBuilder = new StringBuilder();
        mapService.getContinents().forEach(continent -> {
            stringBuilder.append(continent.getName()).append("=");
            stringBuilder.append(continent.getContinentValue()).append(EOL);

        });

        return stringBuilder.toString();
    }

    private String getTerritoryString() {
        /**
         * get string of countries
         * @return arrays of string
         */

        StringBuilder stringBuilder = new StringBuilder();
        mapService.getCountries().forEach(country -> {
            stringBuilder.append(country.getCountryName()).append(COMMA);
            stringBuilder.append(country.getCoordinateX()).append(COMMA);
            stringBuilder.append(country.getCoordinateY()).append(COMMA);
            stringBuilder.append(country.getContinentName()).append(COMMA);
        });

        for (Map.Entry<Integer, Set<Integer>> entry : mapService.getAdjacencyCountriesMap().entrySet()) {

            stringBuilder.append(mapService.findCorrespondingNameByCountryId(entry.getKey())).append(COMMA);

            for (Integer integer : entry.getValue()) {
                stringBuilder.append(mapService.findCorrespondingNameByCountryId(integer)).append(COMMA);
            }

            stringBuilder.append(EOL);
        }

        return stringBuilder.toString();
    }



    @Override
    public void readConquestMapFile(String fileName) {
        String file = CommonUtils.readFile(fileName);

        if(file.equalsIgnoreCase(NON_EXIST)){
            createFile(fileName);
            return;
        }

        parseFile(fileName);
    }

    private void createFile(String fileName) {
        File file = new File(fileName);
        try {
            FileUtils.writeStringToFile(file, "", StandardCharsets.UTF_8.name());
            view.displayMessage("a file " + file.getName() + " has been created.");
        } catch (IOException e) {
            view.displayMessage(e.getMessage());
        }
    }

    /**
     * read existing map and create continent, country and its neighbors.
     *
     * @param s exsting map as a string
     * @return
     */
    boolean parseFile(String s) {
        String[] parts = StringUtils.split(s, "[");

        try {
            if (parts.length != 4) {
                throw new MissingInfoException("The map is not valid");
            }

            parseMapGraphInfo(parts[1]);
            parseRawContinents(parts[2]);
            parseRawCountries(parts[3]);
            parseRawNeighboringCountries(parts[3]);

        } catch (Exception e) {
            view.displayMessage(e.getMessage());
            return false;
        }

        return mapService.isStronglyConnected();

    }


    /**
     * add map graph information
     *
     * @param part
     */
    void parseMapGraphInfo(String part) {

        mapGraph.setMapGraph(StringUtils.substringAfter(part, "]\r\n"));
    }


    /**
     * read continent string from existing map and split it with new line,
     * for each line, call createContinentFromRaw to create new continent.
     *
     * @param part continent string
     * @return
     */
    Set<Continent> parseRawContinents(String part) {
        String continentInfo = StringUtils.substringAfter(part, "]\r\n");

        Set<Continent> continentSet = Optional.of(StringUtils.split(continentInfo, EOL))
                .map(Arrays::stream)
                .orElseGet(Stream::empty)
                .map(this::createContinentFromRaw)
                .collect(Collectors.toSet());

        mapService.addContinent(continentSet);

        return continentSet;

    }

    /**
     * read continent string from the existing map file and save each valid continent to the mapService
     * if the continent is not valid, will throw an exception
     *
     * @param s continent string
     * @return
     */
    private Continent createContinentFromRaw(String s) {

        try {

            String[] continentInfo = StringUtils.split(s, RiscConstants.ASSIGNMENT);

            if (isNull(continentInfo) || continentInfo.length != 2) {
                throw new ContinentParsingException("continent: " + s + " is not valid ");
            }

            String name = convertFormat(continentInfo[0]);
            int continentValue = Integer.parseInt(continentInfo[1]);

            return new Continent(continentIdGenerator.incrementAndGet(), name, continentValue);

        } catch (NumberFormatException e) {
            throw new ContinentParsingException(e);
        }

    }

    /**
     * read country string from existing map and split it with new line,
     * for each line, call createCountryFromRaw to create new country.
     *
     * @param
     * @return
     */
    Set<Country> parseRawCountries(String part) {
        String countryInfo = StringUtils.substringAfter(part, "]\r\n");
        Set<Country> countrySet = Optional.of(StringUtils.split(countryInfo, EOL))
                .map(Arrays::stream)
                .orElseGet(Stream::empty)
                .map(this::createCountryFromRaw)
                .collect(Collectors.toSet());

        mapService.addCountry(countrySet);
        return countrySet;
    }

    /**
     * read country string from the existing map file and save each valid country to the mapService
     * if the country is not valid, will throw an exception
     *
     * @param s
     * @return
     */
    private Country createCountryFromRaw(String s) {
        try {
            String[] countryInfo = StringUtils.split(s, RiscConstants.COMMA);

            if (countryInfo.length < 4) {
                throw new CountryParsingException("territories: " + s + " is not valid.");
            }

            int countryId = countryIdGenerator.incrementAndGet();
            String countryName = convertFormat(countryInfo[0]);
            int coordinateX = Integer.parseInt(countryInfo[1]);
            int coordinateY = Integer.parseInt(countryInfo[2]);
            String continentName = convertFormat(countryInfo[3]);


            if (mapService.continentNameNotExist(continentName)) {
                throw new CountryParsingException("territory: " + s + " contains invalid continent information");
            }

            int continentId = mapService.findCorrespondingIdByContinentName(continentName).get();

            Country country = new Country(countryId, countryName, continentId);
            country.setCoordinateX(coordinateX);
            country.setCoordinateY(coordinateY);
            country.setContinentName(continentName);

            return country;

        } catch (NumberFormatException e) {
            throw new CountryParsingException(e);
        }
    }

    /**
     * read strings from the existing file, save neighboring countries info in the mapServer
     *
     * @param part string for borders(neighboring countries)
     * @return
     */
    Map<Integer, Set<Integer>> parseRawNeighboringCountries(String part) {

        String[] adjacencyInfo = StringUtils.split(part, EOL);
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


    /**
     * read neighboring countries id
     *
     * @param s a line for neighboring countries
     * @return
     */
    private List<Integer> createAdjacencyCountriesFromRaw(String s) {

        List<String> adjacency = Arrays.asList(StringUtils.split(s, COMMA));
        List<Integer> list = new ArrayList<>();

        throwWhenNoNeighboringCountry(s, adjacency);
        throwWhenNeighbouringCountriesIdInvalid(s, adjacency.subList(4, adjacency.size()));

        list.add(mapService.findCorrespondingIdByCountryName(adjacency.get(0)).get());

        for (String country : adjacency) {
            list.add(mapService.findCorrespondingIdByCountryName(country).get());

        }
        return list;

    }

    private void throwWhenNeighbouringCountriesIdInvalid(String s, List<String> adjacency) {
        for (String country : adjacency) {
            if (!mapService.findCorrespondingIdByCountryName(country).isPresent()) {
                throw new NeighborParsingException(s + " is not valid");
            }
        }
    }

    private void throwWhenNoNeighboringCountry(String s, List<String> adjacency) {
        if (adjacency.size() < 4) {
            throw new NeighborParsingException(s + " is not valid");
        }
    }


    /**
     * delete whitespace and lower cases the string
     *
     * @param name
     * @return
     */
    private String convertFormat(String name) {
        return StringUtils.deleteWhitespace(name).toLowerCase(Locale.CANADA);
    }


}
