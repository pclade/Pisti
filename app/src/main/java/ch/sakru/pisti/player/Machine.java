package ch.sakru.pisti.player;

import ch.sakru.pisti.deck.Card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Machine extends Player {

    private static final String[] RANKS = {
            "A", "2", "3", "4", "5", "6", "7", "8", "9", "T", "J", "Q", "K"
    };

    // Only cards revealed on the table are recorded here. The machine also knows
    // its current hand, which is considered separately when choosing a card.
    private final Map<Integer, Card> memoryOfCards;
    private ArrayList<String> machineNames;

    public Machine(){
        memoryOfCards = new HashMap<>();
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
        if (card != null) {
            memoryOfCards.put(card.getCardNumber(), card);
        }
    }

    private int getAmountOfCardNameInMemory(Card card){
        int count = 0;
        for (Card knownCard : memoryOfCards.values()) {
            if (card.getName().equals(knownCard.getName())) {
                count++;
            }
        }
        for (Card handCard : getHand()) {
            if (handCard != null && card.getName().equals(handCard.getName())) {
                count++;
            }
        }
        return count;
    }

    /** Counts publicly revealed ranks in this deck, from ace (1) to king (13). */
    public String getMemorySummary() {
        int[] counts = new int[RANKS.length];
        for (Card knownCard : memoryOfCards.values()) {
            for (int i = 0; i < RANKS.length; i++) {
                if (RANKS[i].equals(knownCard.getName())) {
                    counts[i]++;
                    break;
                }
            }
        }

        StringBuilder summary = new StringBuilder();
        for (int i = 0; i < counts.length; i++) {
            if (i > 0) {
                summary.append(", ");
            }
            summary.append(counts[i]).append('x').append(i + 1);
        }
        return summary.toString();
    }

    // Play a rank with the most known cards, reducing the chance of an opposing match.
    private Card playMostPlayedCard() {
        int bestIndex = -1;
        int bestCount = -1;
        for (int i = 0; i < getHand().size(); i++) {
            Card card = getHand().get(i);
            if (card != null && !"J".equals(card.getName())) {
                int count = getAmountOfCardNameInMemory(card);
                if (count > bestCount) {
                    bestIndex = i;
                    bestCount = count;
                }
            }
        }
        if (bestIndex < 0) {
            return null;
        }
        setPlayedCardInHand(bestIndex);
        Card cardToPlay = getHand().get(bestIndex);
        setPlayedCard(cardToPlay);
        return cardToPlay;
    }

    // Play J only, when its the last option, on a clean desk
    private Card playNextBestCard(){
        Card cardToPlay = null;
        for (Integer i = 0; i < getHand().size(); i++) {
            Card card = getHand().get(i);
            if ((card != null) && (!"J".equals(card.getName()))) {
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
        // Maybe it's a Pisti, when there is only one card on the table.
        if(topCardOnTable == null)
            return null;
        Card cardToPlay = null;
        for(Integer i=0; i< getHand().size();i++) {
            Card card = getHand().get(i);
            if ((card != null) && card.getName().equals(topCardOnTable.getName())) {
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
            if ((cardToPlay!= null) && "J".equals(cardToPlay.getName())) {
                setPlayedCardInHand(i);
                setPlayedCard(cardToPlay);
                return cardToPlay;
            }
        }
        return null;
    }

    @Override
    public void cleanMemories() {
        super.cleanMemories();
        memoryOfCards.clear();
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
        for (String candidate : machineNames) {
            if (!candidate.equals(pName)) {
                super.setName(candidate);
                return;
            }
        }
    }
 }
