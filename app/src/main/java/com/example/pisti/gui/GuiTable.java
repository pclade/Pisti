package com.example.pisti.gui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.pisti.R;
import com.example.pisti.deck.Card;
import com.example.pisti.deck.Deck;

import java.lang.annotation.IncompleteAnnotationException;
import java.util.ArrayList;

public class GuiTable {

    private Context context;
    public ImageView imageTable;
    private RelativeLayout layout;
    private float x;
    private float y;

    public GuiTable(){
    }

    public void setContext(Context context){
        this.context = context;
    }

    public void createView(){
        imageTable = new ImageView(context);
    }

    public void setImageTable(ImageView imageView){
        imageTable = imageView;
    }

    public ImageView getImageTable(){
        return imageTable;
    }

    public void showCard(Integer cardNr, GuiDeck guiDeck, Deck deck){
        imageTable.setImageResource(guiDeck.getCardID(cardNr, deck));
    }

    public float getY(){
        return imageTable.getY();
    }
    public float getX(){
        return imageTable.getX();
    }

    public void cardAnimation(float screenY){
        //Animation of trick
        float saveX;
        float saveY;
        ObjectAnimator yAnim;
        ImageView view;
        view = imageTable;
        saveX = view.getX();
        saveY = view.getY();
        yAnim = ObjectAnimator.ofFloat(view, "translationY", screenY);
        yAnim.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                // Restore original location of the card
                view.setX(saveX);
                view.setY(saveY);
                // END
                imageTable.setImageResource(android.R.color.transparent);
            }
        });
        // Make an animation set and play X and Y animations together
        AnimatorSet cardPlayer = new AnimatorSet();
        cardPlayer.play(yAnim);
        cardPlayer.setDuration(500);
        cardPlayer.start();
    }
}
