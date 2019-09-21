package com6441.team7.risc.api.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Country {
    private String name;
    private Integer cordinateX;
    private Integer cordinateY;
    private Continent continent;
    private Player player;
    private Integer soldier;
    private List<String> adjacentCountryName = new ArrayList<>();

    public Country(){}

    public Country(String name){
        this.name = name;
    }

    public Country setName(String name) {
        this.name = name;
        return this;
    }

    public Country setCordinateX(int cordinateX) {
        this.cordinateX = cordinateX;
        return this;
    }

    public Country setCordinateY(int cordinateY) {
        this.cordinateY = cordinateY;
        return this;
    }



    public Country setContinent(String continent) {

        this.continent = new Continent(continent);
        return this;
    }


    public String getName() {
        return name;
    }

    public int getCordinateX() {
        return cordinateX;
    }

    public int getCordinateY() {
        return cordinateY;
    }

    public Continent getContinent() {
        return continent;
    }

    public Country setAdjacentCountryName(List<String> countryName) {
        this.adjacentCountryName = countryName.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toList());

        return this;
    }

    public List<String> getAdjacentCountryName() {
        return adjacentCountryName;
    }

    public void placeSoldier(int soldier){
        this.soldier += soldier;
    }
    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setSoldier(int soldier) {
        this.soldier = soldier;
    }

    public Player getPlayer() {
        return player;
    }

    public int getSoldier() {
        return soldier;
    }

    public void addSoldier(int number){
        this.soldier += number;
    }
}
