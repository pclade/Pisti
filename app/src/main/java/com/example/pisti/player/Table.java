package com.example.pisti.player;

import com.example.pisti.deck.Card;

public class Table extends Player{

    public Table(String name){
        this.setName(name);
    }

    public Card getTheTopCard(){
        return getTheTopCardOnHand();
    }

    public Integer getTheTopCardsId(){
        return getTheTopCard().getCardNumber();
    }
}
