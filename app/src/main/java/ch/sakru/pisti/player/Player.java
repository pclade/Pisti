package ch.sakru.pisti.player;

import ch.sakru.pisti.deck.Card;

import java.util.ArrayList;

public class Player {
    private String name;
    private ArrayList<Card> hand;
    private ArrayList<Card> winnerCards;
    private Integer amountOfCards;
    private Integer points;
    private Card topCardOnTable = null;
    private Integer playedCardNrInHand;
    private Card playedCard;
    private Integer countOfPisti;
    private boolean threePointsWinner=false;

    public Player() {
        hand = new ArrayList<>();
        winnerCards = new ArrayList<>();
        amountOfCards = 0;
        points = 0;
        countOfPisti = 0;
    }

    private void refreshAmountOfCardsOnHand() {
        amountOfCards = this.getHand().size();
    }
/*
    public Integer getAmountOfCardsOnHand() {
        return amountOfCards;
    }

 */
    public void takeCard(Card card) {
        this.getHand().add(card);
        refreshAmountOfCardsOnHand();
    }

    private Integer getCardIndex(Card card) {
        int i;
        for (i = 0; i < 4; i++) {
            if (this.getHand().get(i) == card)
                return i;
        }
        return 0;
    }

    public void setTopCardOnTable(Card card) {
        topCardOnTable = card;
    }

    public Card getTopCardOnTable() {
        return topCardOnTable;
    }

    public void addPoints(Integer points) {
        this.points += points;
    }

    public ArrayList<Card> getHand() {
        return this.hand;
    }

    public ArrayList<Card> getWinners() {
        return winnerCards;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEmptyHand() {
        boolean isEmpty = true;
        for (int i = 0; i < this.getHand().size(); i++)
            isEmpty = isEmpty && this.getHand().get(i) == null;
        return isEmpty;
    }

    public void cleanHand() {
        while (hand.size() > 0) hand.remove(0);
    }

    public Card playCard(Card playedCard) {
        if(playedCard == null)
            return null;
        hand.set(getCardIndex(playedCard), null);
        setPlayedCard(playedCard);
        return playedCard;
    }

    public void addToWinner(Player player) {
        while (player.getHand().size() > 0) {
            winnerCards.add(player.getHand().get(0));
            player.getHand().remove(0);
        }
    }

    public Integer getAmountOfWinnerCards() {
        return winnerCards.size();
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points=points;
    }

    public void calcCardPoints() {
        for (int i = 0; i < winnerCards.size(); i++) {
            Card card = winnerCards.get(i);
            points += card.getPoint();
        }
    }

    public Card getTheTopCardOnHand() {
        if (isEmptyHand()) return null;
        return this.getHand().get(this.getHand().size() - 1);
    }

    public void addCardToMemory(Card card) {

    }

    public Card play(Integer cardInHand, Card topCardOnTable) {
        // Play card
        setPlayedCardInHand(cardInHand);
        setPlayedCard(hand.get(cardInHand));
        return playCard(playedCard);
    }

    public void setPlayedCardInHand(Integer playedCardNrInHand) {
        this.playedCardNrInHand = playedCardNrInHand;
    }

    public Integer getPlayedCardNrInHand() {
        return playedCardNrInHand;
    }

    public void cleanMemories(){
        while(winnerCards.size()>0){winnerCards.remove(0);}
        cleanHand();
        threePointsWinner = false;
        countOfPisti = 0;
    }
    public void setPlayedCard(Card playedCard){
        this.playedCard = playedCard;
    }

    public Card getPlayedCard(){
        return this.playedCard;
    }

    public boolean isThreePointsWinner(){
        return threePointsWinner;
    }

    public Integer getCountOfPisti(){
        return countOfPisti;
    }

    public void incCountOfPisti(){
        countOfPisti += 1;
    }

    public void addThreePoints(){
        addPoints(3);
        threePointsWinner = true;
    }
}

