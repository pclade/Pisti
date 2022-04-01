package com.example.pisti.gui;

import android.widget.ImageView;

import java.util.Scanner;

public class Gui {

    public Gui(){
    }

    public String getInput(){
        Scanner scanner  = new Scanner(System.in);
        return scanner.nextLine();
    }

    public void displayPisti(){
        System.out.println("It's a Pisti");
    }

    public void displayItsATrick() {
        System.out.println("It's a trick");
    }

    public void displayPlayerPoints(String playerName, Integer playerPoints){
      System.out.println(playerName+"'s Points: "+ playerPoints);
    }
    public void displayPlayedCard(String playerName, String suite, String name){
      System.out.println(playerName+" played "+ suite + name);
    }

    public void tellPlayerToPlay(String name, Integer amountOfCards){
        System.out.println(name+", play a card (0-"+ amountOfCards +")");
    }

    public void displayCards(String name, String hand){
        System.out.println(name+"'s hand:" +hand);
    }

    public void displayCard(Integer source, ImageView destination){

    }

    public void displayCard(ImageView source, ImageView destination){
        destination.setImageDrawable(source.getDrawable());
    }

}
