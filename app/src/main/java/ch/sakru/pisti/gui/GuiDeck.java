package ch.sakru.pisti.gui;

import android.content.Context;
import android.widget.ImageView;

import ch.sakru.pisti.deck.Deck;

import java.util.ArrayList;

public class GuiDeck {

    public ArrayList<ImageView> cardImages;
    public ArrayList<Integer> cardIds;

    public GuiDeck(){
        cardImages = new ArrayList<>();
        cardIds = new ArrayList<>();
    }

    public void createCards(Context context, Deck deck){
        while (cardImages.size()>0) {cardImages.remove(0);}
        while (cardIds.size()>0) {cardIds.remove(0);}
        for(int i = 0; i < deck.getDecSize(); i++){
            ImageView cardImage = new ImageView(context);
            int cardID;
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
