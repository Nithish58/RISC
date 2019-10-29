package com6441.team7.risc.api.model;

import org.apache.commons.collections4.queue.CircularFifoQueue;

import java.util.Observable;
import java.util.Observer;

public class PlayerService extends Observable {

    private MapService mapService;

    //to store newly added player to the list
    private CircularFifoQueue<Player> playerList;

    public PlayerService(MapService mapService){

        this.mapService = mapService;
        playerList = new CircularFifoQueue<>();
    }

    public MapService getMapService() {
        return mapService;
    }

    @Override
    public void addObserver(Observer observer) {

        super.addObserver(observer);
    }

    public void addPlayer(String name){
        //TODO: add a player in the playerList
        notifyObservers();
    }

    public void removePlayer(String name){
        //TODO: remove a player in the playerList
        notifyObservers();
    }

    public String getPlayerList(){
        return null;
    }

    public String getCurrentPlayerName(){
        return null;
    }

    public String getConqueredContries(){ return null;}


    public boolean isPlayerValid(){ return false; }




}
