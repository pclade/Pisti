package ch.sakru.pisti.gui;

import android.animation.AnimatorListenerAdapter;
import android.view.View;
import android.widget.ImageView;
import ch.sakru.pisti.deck.Card;
import ch.sakru.pisti.deck.Deck;
import ch.sakru.pisti.R;

import java.util.ArrayList;

public class GuiPlayer {

    private boolean hideCards=false;
    private GuiCard guiCard;

    public ArrayList<ImageView> cardImages;
    Integer amountOfCards;

    public GuiPlayer(){
        amountOfCards = 4;
        cardImages = new ArrayList<ImageView>();
    }

    public void setImageViewFromResources(ArrayList<ImageView> cImages){
        ImageView imageView;
        for(Integer i=0; i<amountOfCards;i++){
            imageView = cImages.get(i) ;
            cardImages.add(imageView);
        }
    }

    public void showCards(ArrayList<Card> hand, GuiDeck guiDeck, Deck deck){
        for(Integer i=0; i<amountOfCards;i++) {
            Integer cardNrInHand = hand.get(i).getCardNumber();
            ImageView ivOfCardInHand = cardImages.get(i);
            //DEBUG:
            //hideCards = false;
            if(hideCards==true){
                ivOfCardInHand.setImageResource(R.drawable.red_card_back);
            }else {
                ivOfCardInHand.setImageResource(guiDeck.getCardID(cardNrInHand, deck));
            }
        }
    }

    public Integer getCardNrFromId(Integer cardId){
        for(Integer i=0; i<amountOfCards;i++) {
            ImageView ivOfCardInHand = cardImages.get(i);
            if(ivOfCardInHand.getId() == cardId){
                return i;
            }
        }
        return 0;
    }

    public Integer getCardNrFromView(ImageView iv){
        for(Integer i=0; i<amountOfCards;i++) {
            ImageView ivOfCardInHand = cardImages.get(i);
            if(ivOfCardInHand == iv){
                return i;
            }
        }
        return 0;
    }

    public void showCard(Integer cardNrInHand, Integer cardNr, GuiDeck guiDeck, Deck deck) {
        ImageView ivOfCardInHand = cardImages.get(cardNrInHand);
        ivOfCardInHand.setImageResource(guiDeck.getCardID(cardNr, deck));
    }

    public void setHideCards(boolean hideCards){
        this.hideCards = hideCards;
    }

    public void createGuiCard(){
        guiCard = new GuiCard();
    }

    public void initAnimation(View sourceView, View destinationView){
        guiCard.initAnimation(sourceView, destinationView);
    }

    public void restoreCardPositions(){
        guiCard.restoreCardPositions();
    }

    public void doAnimation(){
        guiCard.doAnimation();
    }

    public  void addListener(AnimatorListenerAdapter animatorListenerAdapter){
        guiCard.addListener(animatorListenerAdapter);
    }

}
