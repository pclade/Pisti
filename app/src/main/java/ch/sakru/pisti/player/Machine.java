package ch.sakru.pisti.player;

import ch.sakru.pisti.deck.Card;

import java.util.ArrayList;
import java.util.Collections;

public class Machine extends Player {

    private ArrayList<Card> memoryOfCards;
    private ArrayList<String> machineNames;

    public Machine(){
        memoryOfCards = new ArrayList<>();
        machineNames = new ArrayList<>();
        machineNames.add("Ati");
        machineNames.add("Senol");
        machineNames.add("Ali");
        machineNames.add("Rauf");
        machineNames.add("Yücel");
        machineNames.add("Fatih");
        machineNames.add("Enis");
        machineNames.add("Sükrü");
        machineNames.add("Beyazit");
        machineNames.add("Halil");
        machineNames.add("Yalcin");
        machineNames.add("Faruk");
        machineNames.add("Musa");
        machineNames.add("Ilhan");
        machineNames.add("Dincer");
        machineNames.add("Ergün");
        machineNames.add("Ersel");
        machineNames.add("Hakan");
        machineNames.add("Ilyas");
        machineNames.add("Mithat");
        machineNames.add("Tevfik");
        machineNames.add("Turgut");
        machineNames.add("Ulvi");
        machineNames.add("Yekta");
        Collections.shuffle(machineNames);
    }
    @Override
    public void addCardToMemory(Card card){
        memoryOfCards.add(card);
    }

    /*
    private Card getCardOnTopOfMemory(){
        Integer topCardIndex = memoryOfCards.size();
        if(topCardIndex > 0)
          return memoryOfCards.get(topCardIndex-1);
        return null;
    }

     */

    private Integer getAmountOfCardNameInMemory(Card card){
        Integer amountOfCardNameInMemory = 0;
        for(int i = 0; i< memoryOfCards.size(); i++){
            Card cardInMemory = memoryOfCards.get(i);
            if(cardInMemory.getName() == card.getName())
                amountOfCardNameInMemory++;
        }
        return amountOfCardNameInMemory;
    }

    //Strategy: Play the most played card already, to avoid a trick or ch.sakru.pisti.
    private Card playMostPlayedCard() {
        int indexOfMaxPlayedCard=0;
        ArrayList<Integer> numberOfTimesPlayed;

        numberOfTimesPlayed = new ArrayList<>();
        Card cardToPlay;
        for (int i = 0; i < getHand().size(); i++) {
            Card card = getHand().get(i);
            if((card != null) && (card.getName() != "J")){
                Integer j = getAmountOfCardNameInMemory(card);
                numberOfTimesPlayed.add(j);
                for (Integer m = 0; m < numberOfTimesPlayed.size(); m++) {
                    if (numberOfTimesPlayed.get(m) > numberOfTimesPlayed.get(indexOfMaxPlayedCard)) {
                        indexOfMaxPlayedCard = m;
                    }
                }
            }
            else{
                numberOfTimesPlayed.add(-1);
            }
        }
        setPlayedCardInHand(indexOfMaxPlayedCard);
        cardToPlay = getHand().get(indexOfMaxPlayedCard);
        setPlayedCard(cardToPlay);
        return cardToPlay;
    }

    // Play J only, when its the last option, on a clean desk
    private Card playNextBestCard(){
        Card cardToPlay = null;
        for (Integer i = 0; i < getHand().size(); i++) {
            Card card = getHand().get(i);
            if ((card != null) && (card.getName() != "J")) {
                setPlayedCardInHand(i);
                cardToPlay = card;
                setPlayedCard(cardToPlay);
                return cardToPlay;
            }
        }
        for (Integer i = 0; i < getHand().size(); i++) {
            Card card = getHand().get(i);
            if (card != null)  {
                setPlayedCardInHand(i);
                cardToPlay = card;
                setPlayedCard(cardToPlay);
                return cardToPlay;
            }
        }
        return null;
    }

    // Strategy: If there is only one card on the table, then a ch.sakru.pisti is possible. So look for a match in hand.
    // TODO: if there is a possible pisti with a card which has points, then select this card instead of an other card with the same name, but without points
    // TODO: Example: a pisti with a caro 10 (13 Points) ist more reliable then with cross 10 (10 Points)
    private Card playForTrick(Card topCardOnTable){
        // Maybe it's a ch.sakru.pisti, when there is only one card on the table.
        if(topCardOnTable == null)
            return null;
        Card cardToPlay = null;
        for(Integer i=0; i< getHand().size();i++) {
            Card card = getHand().get(i);
            if ((card != null) && (card.getName() == topCardOnTable.getName())) {
                cardToPlay = card;
                setPlayedCardInHand(i);
                setPlayedCard(cardToPlay);
                return cardToPlay;
            }
        }
        return null;
    }

    private Card playForTrickWithJack(Card topCardOnTable){
        Card cardToPlay = null;
        if(topCardOnTable == null)
            return null;
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
           playedCard = playForTrickWithJack(topCardOnTable);
       if(playedCard == null)
         playedCard = playMostPlayedCard();
       if(playedCard == null)
           playedCard = playNextBestCard();
       playCard(playedCard);
       setPlayedCard(playedCard);
       return playedCard;
    }

    public void setName(String pName){
        for(Integer i=0;i<machineNames.size();i++){
            if(machineNames.get(i) != pName){
                super.setName(machineNames.get(i));
            }
        }
    }
 }
