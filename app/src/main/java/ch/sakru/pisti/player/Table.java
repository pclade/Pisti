package ch.sakru.pisti.player;

import ch.sakru.pisti.deck.Card;

public class Table extends Player{

    private String strHiddenCards;

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
        return strHiddenCards;
    }
    public void buildHiddenCardsString(){
        strHiddenCards = "";
        for(Integer i=0; i<3;i++){
            strHiddenCards += getHand().get(i).getSuite()+getHand().get(i).getName()+" ";
        }
    }

}
