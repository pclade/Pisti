package com.example.pisti.deck;

import java.util.ArrayList;
import java.util.Collections;

public class Deck {

    // Suites:
    //  0-12: Spade
    // 13-25: Heart
    // 26-38: Caro
    // 39-51: Cross
    // The first card in a suite is 1 (as) the 2, 3, ... jack, queen, king
    // every card has his weight, starting from 2, 3, ... jack=11, queen=12, king=13, as=14 (weighting depends on game)

    // Limitation: The Deck is designed only for Games with 52 Cards.
    private int deckSize = 52; // 52
    //
    private ArrayList<Integer> list;
    private ArrayList<Card> cardDeck;

    public Deck(){
        list = new ArrayList<>();
        cardDeck = new ArrayList<>();
    }

    public void setDeckSize(int deckSize) {
        this.deckSize = deckSize;
    }

    public void createCards(){
        if(!isEmpty())
            return;
        for (int i = 0; i < getDecSize(); ++i) {
            list.add(i);
            Card card = new Card(i);
            card.setName(getCardsName(card));
            card.setSuite(getCardsSuite(card));
            cardDeck.add(card);
            //TODO
            // Weight should come from Game. Cards could have in every Game another weight.
            // Card card = new Card(i, getCardSuite(i), getCardName(i), getWeight(i));
            // cardDeck.add(card);
        }
    }

    private Suite getCardSuite(int i){
        //TODO: Who should define the Suite?
        // A Deck is a composition of Cards and the deck should know about the Suites.
        // In Swiss-German-Cards for example, there are different suites than Spade, heart, caro and cross: Schild, Schäle, Rose and Eichle.
      return new Suite("Spade");
    }

    private String getCardName(int i){
        //TODO: Who should define the name of a card?
        // A Deck is a composition of Cards and the deck should know about the names of the cards.
        // In Swiss-German-Cards for example, there are different names than Jack, Queen and King: Under, Ober.
      return "Jack";
    }

    public void shuffleDeck(){
//        Collections.shuffle(list);
        Collections.shuffle(cardDeck);
    }

    public ArrayList<Integer> getList(){
        return list;
    }

    public ArrayList<Card> getCardDeck(){
        return cardDeck;
    }

    private String getCardsSuite(Card card){
        if (card.getCardNumber() / 13 == 0) return "♠";
        if (card.getCardNumber() / 13 == 1) return "♥";
        if (card.getCardNumber() / 13 == 2) return "♦";
        if (card.getCardNumber() / 13 == 3) return "♣";
        return "";
    }

    private String getCardsName(Card card) {
        if (card.getCardNumber() % 13 == 0) return "A";
        if (card.getCardNumber() % 13 == 1) return "2";
        if (card.getCardNumber() % 13 == 2) return "3";
        if (card.getCardNumber() % 13 == 3) return "4";
        if (card.getCardNumber() % 13 == 4) return "5";
        if (card.getCardNumber() % 13 == 5) return "6";
        if (card.getCardNumber() % 13 == 6) return "7";
        if (card.getCardNumber() % 13 == 7) return "8";
        if (card.getCardNumber() % 13 == 8) return "9";
        if (card.getCardNumber() % 13 == 9) return "T";
        if (card.getCardNumber() % 13 == 10) return "J";
        if (card.getCardNumber() % 13 == 11) return "Q";
        if (card.getCardNumber() % 13 == 12) return "K";
        return "";
    }

    public Card dealTopCardOnDec(){
        if(isEmpty())
            return null;
        Integer topCard = cardDeck.size()-1;
        Card card = cardDeck.get(topCard);
        cardDeck.remove(card);
        return card;
    }

    public boolean isEmpty(){
        if (cardDeck.size() == 0)
            return true;
        return false;
    }

    public boolean isJack(Card card){
        return card.getName() == "J";
    }
    public boolean isCaro(Card card){
        return card.getSuite() == "♦";
    }
    public boolean isCross(Card card){
        return card.getSuite() == "♣";
    }
    public boolean isTen(Card card){
        return card.getName()=="T";
    }
    public boolean isTwo(Card card){
        return card.getName()=="2";
    }
    public boolean isAs(Card card){
        return card.getName()=="A";
    }
    public Integer getDecSize(){return deckSize;}
}
