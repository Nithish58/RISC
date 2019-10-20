package com6441.team7.risc.utils;

import com6441.team7.risc.api.model.Country;
import com6441.team7.risc.api.model.MapService;
import com6441.team7.risc.api.model.Player;
import com6441.team7.risc.view.GameView;

import java.util.Optional;
import java.util.Set;
import java.util.function.BiPredicate;

import static com6441.team7.risc.api.RiscConstants.NEWLINE;
import static com6441.team7.risc.utils.NotnullUtils.toStream;
import static java.util.Objects.requireNonNull;

public final class MapDisplayUtils {
    private static final BiPredicate<Country, Player> countryBelongsToPlayer = ((country, player) -> country.getPlayer().equals(player));
    private static final BiPredicate<Country, Player> anyPlayers = (country, player) -> true;

    private MapDisplayUtils(){}

    public static void showCurrentPlayerMap(MapService mapService, GameView gameView, Player currentPlayer) {
        requireNonNull(mapService);
        requireNonNull(gameView);
        requireNonNull(currentPlayer);

        mapService.getContinentCountriesMap().forEach((key, value) -> {
            displayContinentInfo(gameView, mapService, key);
            displayCountryInfo(gameView, mapService, value, countryBelongsToPlayer, currentPlayer);

        });
    }

    public static void showFullMap(MapService mapService, GameView gameView) {
        requireNonNull(mapService);
        requireNonNull(gameView);

        mapService.getContinentCountriesMap().forEach((key, value) -> {
            displayContinentInfo(gameView, mapService, key);
            displayCountryInfo(gameView, mapService, value, anyPlayers, null);

        });
    }
    private static void displayContinentInfo(GameView gameView, MapService mapService, Integer continentId) {
        mapService.getContinentById(continentId)
                .ifPresent(continent -> {
                    gameView.displayMessage("\t\t\t\t\t\t\t\t\tContinent "+continent.getName()+NEWLINE);
                });
    }

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
}
