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

import ch.sakru.pisti.deck.Deck;

public class GuiTable {

    private Context context;
    public ImageView imageTable;
    private RelativeLayout layout;
    private float x;
    private float y;
    private ObjectAnimator yAnim;
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

    public ImageView getImageTable(){
        return imageTable;
    }

    public void showCard(Integer cardNr, GuiDeck guiDeck, Deck deck){
        Integer CardId = guiDeck.getCardID(cardNr, deck);
        imageTable.setImageResource(CardId);
        imageTable.bringToFront();
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

    public void initAnimation(float screenY, float rotation){
        //guiTrick.initAnimation(sourceView, destinationView);
        float saveX;
        float saveY;
        //ObjectAnimator yAnim;
        ImageView view;
        view = imageTable;
        saveX = view.getX();
        saveY = view.getY();
        view.setRotation(rotation);
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
    }

    public void doAnimation(){
        //guiTrick.doAnimation();
        AnimatorSet cardPlayer = new AnimatorSet();
        cardPlayer.play(yAnim);
        cardPlayer.setDuration(500);
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
