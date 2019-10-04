package com6441.team7.risc.view;

import java.util.Scanner;

public class CommandPromptView {
    private Scanner scanner = new Scanner(System.in);

    public CommandPromptView(){
        //System.out.println("welcome to the risk game. Enter the map file you would like to play: ");
    }

    public String receiveCommand(){
        return scanner.nextLine();
    }

    public void displayMessage(String string){
        System.out.println(string);
    }
}
