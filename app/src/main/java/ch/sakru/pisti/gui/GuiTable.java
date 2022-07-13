package ch.sakru.pisti.gui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ch.sakru.pisti.R;
import ch.sakru.pisti.deck.Deck;

public class GuiTable {

    private Context context;
    public ImageView imageTable;
    public ImageView imageHiddenCard;
    private RelativeLayout layout;
    private float x;
    private float y;
    private ObjectAnimator yAnim;
    private ObjectAnimator yAnimH;
    private GuiTrick guiTrick;

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

    public void setImageHiddenCard(ImageView imageView){
        imageHiddenCard = imageView;
    }

    public ImageView getImageTable(){
        return imageTable;
    }

    public void showCard(Integer cardNr, GuiDeck guiDeck, Deck deck){
        Integer CardId = guiDeck.getCardID(cardNr, deck);
        imageTable.setImageResource(CardId);
        imageTable.bringToFront();
    }

    public void ShowHiddenCard(){
        imageHiddenCard.setVisibility(View.VISIBLE);
    }

    public float getY(){
        return imageTable.getY();
    }
    public float getX(){
        return imageTable.getX();
    }

    public  void addListener(AnimatorListenerAdapter animatorListenerAdapter){
        yAnim.addListener(animatorListenerAdapter);
    }

    public void initTrickAnimation(float screenY, float rotation){
        //guiTrick.initAnimation(sourceView, destinationView);
        float saveX;
        float saveY;
        float saveXh;
        float saveYh;
        //ObjectAnimator yAnim;
        ImageView view;
        ImageView viewHidden;
        view = imageTable;
        saveX = view.getX();
        saveY = view.getY();
        view.setRotation(rotation);

        viewHidden = imageHiddenCard;
        saveXh = viewHidden.getX();
        saveYh = viewHidden.getY();
        viewHidden.setRotation(rotation);

        yAnim = ObjectAnimator.ofFloat(view, "translationY", screenY);
        yAnim.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                // Restore original location of the card
                view.setX(saveX);
                view.setY(saveY);
                view.setRotation(0);
                // END
                imageTable.setImageResource(android.R.color.transparent);
            }
        });
        yAnimH = ObjectAnimator.ofFloat(viewHidden, "translationY", screenY);
        yAnimH.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                // Restore original location of the card
                viewHidden.setX(saveXh);
                viewHidden.setY(saveYh);
                viewHidden.setRotation(0);
                // END
                imageHiddenCard.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void doTrickAnimation(){
        //guiTrick.doAnimation();
        AnimatorSet cardPlayer = new AnimatorSet();
        cardPlayer.play(yAnim);
        cardPlayer.play(yAnimH);
        cardPlayer.setDuration(500);
        cardPlayer.start();
    }

    public void initPistiAnimation(float screenY, float rotation){
        //guiTrick.initAnimation(sourceView, destinationView);
        float saveX;
        float saveY;
        float saveXh;
        float saveYh;
        //ObjectAnimator yAnim;
        ImageView view;
        ImageView viewHidden;
        view = imageTable;
        saveX = view.getX();
        saveY = view.getY();
        view.setRotation(90);

        viewHidden = imageHiddenCard;
        viewHidden.setVisibility(View.VISIBLE);
        saveXh = viewHidden.getX();
        saveYh = viewHidden.getY();
        viewHidden.setRotation(0);

        yAnim = ObjectAnimator.ofFloat(view, "translationY", screenY);
        yAnim.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                // Restore original location of the card
                view.setX(saveX);
                view.setY(saveY);
                view.setRotation(0);
                // END
                imageTable.setImageResource(android.R.color.transparent);
            }
        });
        yAnimH = ObjectAnimator.ofFloat(viewHidden, "translationY", screenY);
        yAnimH.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                // Restore original location of the card
                viewHidden.setX(saveXh);
                viewHidden.setY(saveYh);
                viewHidden.setRotation(0);
                // END
                imageHiddenCard.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void doPistiAnimation(){
        //guiTrick.doAnimation();
        AnimatorSet cardPlayer = new AnimatorSet();
        cardPlayer.play(yAnim);
        cardPlayer.play(yAnimH);
        cardPlayer.setDuration(800);
        cardPlayer.start();
    }


    public void pistiAnimation(TextView view){
        view.setVisibility(View.VISIBLE);
        CountDownTimer timer = new CountDownTimer(500, 500) {
            @Override
            public void onTick(long millisUntilFinished) {
            }
            @Override
            public void onFinish() {
                view.setVisibility(View.GONE); //(or GONE)
            }
        }.start();
    }
}
