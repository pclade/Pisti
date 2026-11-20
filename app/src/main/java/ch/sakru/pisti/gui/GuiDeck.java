package ch.sakru.pisti.gui;

import android.content.Context;
import android.widget.ImageView;

import ch.sakru.cards.Card;
import ch.sakru.cards.Deck;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GuiDeck {

    public ArrayList<ImageView> cardImages;
    private final Map<Integer, Integer> cardIds;

    public GuiDeck(){
        cardImages = new ArrayList<>();
        cardIds = new HashMap<>();
    }

    public void createCards(Context context, Deck deck){
        while (cardImages.size()>0) {cardImages.remove(0);}
        cardIds.clear();
        for(int i = 0; i < deck.getDecSize(); i++){
            ImageView cardImage = new ImageView(context);
            int cardID;
            Card card = deck.getCardDeck().get(i);
            int cardNr = card.getId();
            String cardName = PistiArtwork.drawableName(card);
            cardID = context.getResources().getIdentifier(cardName, "drawable", context.getPackageName());
            cardImage.setImageResource(cardID);
            cardImages.add(cardImage);
            cardIds.put(cardNr, cardID);
        }
    }

    public Integer getCardID(Integer cardNr, Deck deck){
        return cardIds.get(cardNr);
    }


    //public ImageView getCardsImageById(Integer id){
    //    return cardImages.get(id);
    //}
}
