package com.example.pisti.gui;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.example.pisti.deck.Deck;

import java.util.ArrayList;

public class GuiDeck {

    public ArrayList<ImageView> cardImages;
    public ArrayList<Integer> cardIds;

    public GuiDeck(){
        cardImages = new ArrayList<ImageView>();
        cardIds = new ArrayList<Integer>();
    }

    public void createCards(Context context, Deck deck){
        while (cardImages.size()>0) {cardImages.remove(0);}
        while (cardIds.size()>0) {cardIds.remove(0);}
        for(Integer i=0; i < deck.getDecSize(); i++){
            ImageView cardImage = new ImageView(context);
            Integer cardID;
            int cardNr = deck.getCardDeck().get(i).getCardNumber();
            String cardName = "c" + String.format("%02d",cardNr);
            cardID = context.getResources().getIdentifier(cardName, "drawable", context.getPackageName());
            cardImage.setImageResource(cardID);
            cardImages.add(cardImage);
            cardIds.add(cardID);
        }
    }

    public Integer getCardID(Integer cardNr, Deck deck){
        return cardIds.get(cardNr);
    }


    //public ImageView getCardsImageById(Integer id){
    //    return cardImages.get(id);
    //}
}
