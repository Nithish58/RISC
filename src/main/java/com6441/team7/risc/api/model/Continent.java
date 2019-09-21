package com6441.team7.risc.api.model;

public class Continent {
    private String name;
    private int power;
    private int numberOfCountry;

    public Continent(String name, int power) {
        this.name = name;
        this.power = power;
        this.numberOfCountry = 0;
    }

    public Continent(){}

    public Continent(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public int getNumberOfCountry() {
        return numberOfCountry;
    }

    public void setNumberOfCountry(int numberOfCountry) {
        this.numberOfCountry = numberOfCountry;
    }

    public void addCountry(){
        numberOfCountry++;
    }

    @Override
    public String toString() {
        return "Continent{" +
                "name='" + name + '\'' +
                ", power=" + power +
                '}';
    }
}
