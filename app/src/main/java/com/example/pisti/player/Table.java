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

    public String getHiddenCardsString(){
        String strHiddenCards;
        strHiddenCards = "";
        for(Integer i=0; i<3;i++){
            strHiddenCards += getHand().get(i).getSuite()+getHand().get(i).getName()+" ";
        }
        return strHiddenCards;
    }
}
