package com6441.team7.risc.api.model;
import org.jgrapht.Graph;
import org.jgrapht.alg.connectivity.KosarajuStrongConnectivityInspector;
import org.jgrapht.alg.interfaces.StrongConnectivityAlgorithm;
import org.jgrapht.graph.*;

import java.util.*;


public class MapService {

     private Set<Country> countries = new LinkedHashSet<>();
     private Set<Continent> continents = new LinkedHashSet<>();
     private Graph<String, DefaultEdge> directedGraph = new DefaultDirectedGraph<>(DefaultEdge.class);


    public void addEdge(String src, List<String> dest){

        if(!directedGraph.containsVertex(src)){
            directedGraph.addVertex(src);
        }

        for(String country: dest){
            if(!directedGraph.containsVertex(country)){
                directedGraph.addVertex(country);
            }

        }

        for(String country: dest){
            directedGraph.addEdge(src, country);
            directedGraph.addEdge(country, src);
        }

    }

    public void addContinent(Continent continent){
        continents.add(continent);
    }

    public boolean isStrongConnected(){

        StrongConnectivityAlgorithm<String, DefaultEdge> scAlg =
                new KosarajuStrongConnectivityInspector<>(directedGraph);

        List<Graph<String, DefaultEdge>> stronglyConnectedSubgraphs =
                scAlg.getStronglyConnectedComponents();

        long numberOfUnconnected = stronglyConnectedSubgraphs.stream()
                .map(Graph::vertexSet).map(Set::size).filter(n -> n != countries.size()).count();


        return numberOfUnconnected == 0;


    }

    public Set<Continent> getContinentSet() {
        return continents;
    }

    public void setCountries(Set<Country> countries) {
        this.countries = countries;
    }

    public void setContinents(Set<Continent> continents) {
        this.continents = continents;
    }


    public void addCountry(Country country){
        countries.add(country);
    }

    public int getTotalCountry() {
        return countries.size();
    }

    public Set<Country> getCountrySet() {
        return countries;
    }


}
