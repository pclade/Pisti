package com.example.pisti.gui;

import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Scanner;

public class Gui {

    private ArrayList<Integer> animationSequence;
    //  1: Player 1 Plays card to Table
    //  2: Player 2 Plays card to Table
    //  3: Player 1 makes a trick
    //  4: Player 2 makes a trick
    //  5: Player 1 or 2 makes a pisti (two animations, trick (3 or 4) and (5) for pisti
    //  7: Round Finished, Show Dialog with points, and wait for continue
    //  8: Game Finished, Show Dialog with points and wait for a "NEW GAME" or "EXIT"
    //  9: It's the first trick in round and it's player1, then show hidden cards to him.
    private ArrayList<View> animationView;

    public Gui(){
        animationSequence = new ArrayList<Integer>();
        animationView = new ArrayList<View>();
    }

    public String getInput(){
        Scanner scanner  = new Scanner(System.in);
        return scanner.nextLine();
    }
/*
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
*/
    public void addAnimation(Integer animation, View view){
        animationSequence.add(animation);
        animationView.add(view);
    }

    public void removeFirstAnimation(){
        animationSequence.remove(0);
        animationView.remove(0);
    }

    public Integer getAnimationSize(){
        return animationSequence.size();
    }

    public Integer getAnimationSequence(Integer animation){
        return animationSequence.get(animation);
    }

    public View getAnimationView(Integer animation){
        return animationView.get(animation);
    }
}
