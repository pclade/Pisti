package com.example.pisti.game;

import com.example.pisti.deck.Card;
import com.example.pisti.deck.Deck;
import com.example.pisti.player.Player;

import java.util.ArrayList;

public class Game {
    // The Game defines the Players, the Dec, the Cards etc.
    // It defines also the rules of the Game.
    // TO DO: In a next Step, there should be an abstract Game Class, which generalizes the concrete games.
    // The first Game to implement is "Pisti".
    // Deck: 52 Cards (french)
    // Suites: Spade, Heart, Caro, Cross
    // Wights: All cards have same weights=0
    // Points: Caro 10 = 10 Points, Cross 2 = 2 Points, Jacks = 1 Point, As = 1 Point. At the end of the Dec, the player with most cards win, gets +3 points.
    // Player : 2
    // Hands: Initially 4 Cards per player.
    // Start: Four cards on the table. Last one open.
    // Deal: 4 Cards per Player are dealt.
    // Round: Players take turns playing their cards, until all 4 Cards are played. After that, 4 new cards are dealt, until the deck is empty. After that, the cards are shuffled and dealt again, until the game finishes
    // Trick: Made with the same card as the one lying on the table, independent of the suite, or with a Jack anytime.
    // Pisti: Is a special Trick, made if only one card is lying on the table. This Trick counts 10 Points.
    // Finish: The Points are counted, after all the Rounds, until a deck finishes. One of the players must have 51 or more points, to finish the game. The winner ist the player with the highest points.
    // Example for Finish: Before last deck=> 49-33, after the last deck=> 65 - 87

    private String gameName;
    private ArrayList<Player> players;
    private Integer currentPlayer;
    private Deck deck;
    private Player playerMadeLastTrick;
    private Integer amountOfCardsToDeal=4;
    private Integer pointsToReach = 51; //TODO: Implement an outer game loop, until points are reached at the end of an outer loop.

    public Game(){
      players = new ArrayList<>(0);
    }

    public Game(String gameName){
      this.gameName = gameName;
    }

    public Integer getNumberOfPlayers(){
        return players.size();
    }
    public void addPlayer(Player player){
        players.add(player);
    }

    public void setDeck(Deck deck){
        this.deck = deck;
    }

    public Deck getDeck() {
        return deck;
    }

    public void dealCards(Player player){
        player.cleanHand();
        for(Integer i=0; i< amountOfCardsToDeal; i++){
            Card card = deck.dealTopCardOnDec();
            player.takeCard(card);
        }
    }

    public Player getFirstPlayer(){
        currentPlayer = 0;
        return players.get(0);
    }

    public Player getNextPlayer(){
        if(currentPlayer == 0){
            currentPlayer++;
        }
        else
            currentPlayer = 0;
        return players.get(currentPlayer);
    }

    public Player getPlayerByNumber(Integer pNr){
      return players.get(pNr);
    }

    public boolean isItAPisti(Player table){
        int maxCardOnTable = table.getHand().size();
        if(maxCardOnTable == 2){
            if(table.getHand().get(maxCardOnTable-1).getName() == table.getHand().get(maxCardOnTable-2).getName()){
                return true;
            }
            else{
                return false;
            }
        }
        return false;
    }

    public boolean isItATrick(Player table){
        int maxCardOnTable = table.getHand().size();
        if(maxCardOnTable >= 2){
          if(deck.isJack(table.getHand().get(maxCardOnTable-1))){
              return true;
          }
          else
          if(table.getHand().get(maxCardOnTable-1).getName() == table.getHand().get(maxCardOnTable-2).getName()){
              return true;
          }
          else{
              return false;
          }
        }
        // Table was empty, it's the first card after a trick
        return false;
    }

    public boolean isTableClean(Player table){
        int maxCardOnTable = table.getHand().size();
        if(maxCardOnTable < 2){
            return true;
        }
        return false;
    }

    public boolean handsEmpty(){
        boolean result = true;
        for(Integer i=0; i< getNumberOfPlayers(); i++){
            Player player = players.get(i);
            result = result && player.isEmptyHand();
        }
        return result;
    }

    public boolean finished(){
        if(!deck.isEmpty())
          return false;
        if(!handsEmpty())
            return false;
        return true;
    }

    public void cleanUp(Player table){
      Integer amountOfPlayers = players.size();
      if(playerMadeLastTrick != null) {
          playerMadeLastTrick.addToWinner(table);
          //for (Integer i = 0; i < amountOfPlayers; i++) {
          //    Player player = players.get(i);
          //    player.calcCardPoints();
          //}
      }
      // the player with the most cards gets 3 extra Points
      Player playerWithMostCards = null;
      Integer mostCards = 0;
      Integer amountOfWinnerCardsOfPlayer = 0;
      for(Integer i=0; i< amountOfPlayers; i++) {
          Player player = players.get(i);
          amountOfWinnerCardsOfPlayer = player.getAmountOfWinnerCards();
          if(amountOfWinnerCardsOfPlayer == mostCards){
              mostCards = amountOfWinnerCardsOfPlayer;
              playerWithMostCards = null;
          }
          else
          if(amountOfWinnerCardsOfPlayer > mostCards){
              mostCards = amountOfWinnerCardsOfPlayer;
              playerWithMostCards = player;
          }
      }
      if(playerWithMostCards != null) {
          playerWithMostCards.addPoints(3);
      }
    }

    public void cleanMemories(){
        Integer amountOfPlayers = players.size();
        for(Integer i=0; i< amountOfPlayers; i++) {
            Player player = players.get(i);
            player.cleanMemories();
        }
    }
    public void cleanPoints(){
        Integer amountOfPlayers = players.size();
        for(Integer i=0; i< amountOfPlayers; i++) {
            Player player = players.get(i);
            player.setPoints(0);
        }
   }


    public void setPlayerMadeLastTrick(Player player){
        playerMadeLastTrick = player;
    }

    public void setCardsPoints(){
      Integer amountOfCards = deck.getDecSize();
      for(Integer i=0; i< amountOfCards; i++) {
          Card card = deck.getCardDeck().get(i);
          if(deck.isCaro(card) && deck.isTen(card)){
              card.setPoint(3);
          }
          else
          if(deck.isCross(card) && deck.isTwo(card)){
              card.setPoint(2);
          }
          else
          if(deck.isAs(card)){
              card.setPoint(1);
          }
          else
          if(deck.isJack(card)){
              card.setPoint(1);
          }
      }
    }

    public void addCardToMemoryOfPlayers(Card card){
        for(Integer i=0; i< getNumberOfPlayers(); i++) {
            Player player = players.get(i);
            player.addCardToMemory(card);
        }
    }

    public boolean maxPointsReached(){
        Integer amountOfPlayers = players.size();
        for(Integer i=0; i< amountOfPlayers; i++) {
            Player player = players.get(i);
            if(player.getPoints() >= pointsToReach){
                return true;
            }
        }
        return false;
    }
}
