package com.example.pisti.player;

import com.example.pisti.deck.Card;

import java.util.ArrayList;

public class Machine extends Player {

    private ArrayList<Card> memoryOfCards;

    public Machine(){
        setName("Machine");
        memoryOfCards = new ArrayList<>();
    }
    @Override
    public void addCardToMemory(Card card){
        memoryOfCards.add(card);
    }

    private Card getCardOnTopOfMemory(){
        Integer topCardIndex = memoryOfCards.size();
        if(topCardIndex > 0)
          return memoryOfCards.get(topCardIndex-1);
        return null;
    }

    private Integer getAmountOfCardNameInMemory(Card card){
        Integer amountOfCardNameInMemory = 0;
        for(Integer i=0; i< memoryOfCards.size();i++){
            Card cardInMemory = memoryOfCards.get(i);
            if(cardInMemory.getName() == card.getName())
                amountOfCardNameInMemory++;
        }
        return amountOfCardNameInMemory;
    }

    //Strategy: Play the most played card already, to avoid a trick or pisti.
    private Card playMostPlayedCard() {
        Integer numberOfTimesPlayed = 0;
        Card cardToPlay = null;
        for (Integer i = 0; i < getHand().size(); i++) {
            Card card = getHand().get(i);
            if ((card != null) && (numberOfTimesPlayed < getAmountOfCardNameInMemory(card))) {
                setPlayedCardInHand(i);
                cardToPlay = card;
                setPlayedCard(cardToPlay);
                return cardToPlay;
            }
        }
        return null;
    }

    private Card playNextBestCard(){
        Card cardToPlay = null;
        for (Integer i = 0; i < getHand().size(); i++) {
            Card card = getHand().get(i);
            if (card != null) {
                setPlayedCardInHand(i);
                cardToPlay = card;
                setPlayedCard(cardToPlay);
                return cardToPlay;
            }
        }
        return null;
    }

    //Strategy: If there is only one card on the table, then a pisti is possible. So look for a match in hand.
    //TODO if there is a possible pisti with a card which has points, then select this card instead of an other card with the same name, but without points
    //TODO Example: a pisti with a caro 10 (13 Points) ist more reliable then with cross 10 (10 Points)
    private Card playForTrick(Card topCardOnTable){
        // Maybe it's a pisti, when there is only one card on the table.
        Card cardToPlay = null;
        for(Integer i=0; i< getHand().size();i++) {
            Card card = getHand().get(i);
            if ((card != null) && (topCardOnTable != null) && (card.getName() == topCardOnTable.getName())) {
                cardToPlay = card;
                setPlayedCardInHand(i);
                setPlayedCard(cardToPlay);
                return cardToPlay;
            }
        }
        return null;
    }

    private Card playForTrickWithJack(){
        Card cardToPlay = null;
        for(Integer i=0; i< getHand().size();i++) {
            cardToPlay = getHand().get(i);
            if ((cardToPlay!= null) && (cardToPlay.getName() == "J")) {
                setPlayedCardInHand(i);
                setPlayedCard(cardToPlay);
                return cardToPlay;
            }
        }
        return null;
    }

    @Override
    public void takeCard(Card card) {
        super.takeCard(card);
        addCardToMemory(card);
    }


    public Card playMachine(Card topCardOnTable){
       Card playedCard = null;
       playedCard = playForTrick(topCardOnTable);
       if(playedCard == null)
           playedCard = playForTrickWithJack();
       if(playedCard == null)
         playedCard = playMostPlayedCard();
       if(playedCard == null)
           playedCard = playNextBestCard();
       playCard(playedCard);
       setPlayedCard(playedCard);
       return playedCard;
    }
 }
