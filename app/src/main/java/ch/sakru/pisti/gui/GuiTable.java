package ch.sakru.pisti.gui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
//import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;

// import ch.sakru.pisti.R;
import ch.sakru.pisti.deck.Deck;

public class GuiTable {

    private Context context;
    public ImageView imageTable;
    public ImageView imageHiddenCard;
    // private RelativeLayout layout;
    // private float x;
    // private float y;
    private AnimatorSet cardAnimation;
    private AnimatorListenerAdapter completionListener;
    // private GuiTrick guiTrick;

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

    //public ImageView getImageTable(){
    //    return imageTable;
    //}

    public void showCard(Integer cardNr, GuiDeck guiDeck, Deck deck){
        Integer CardId = guiDeck.getCardID(cardNr, deck);
        imageTable.setImageResource(CardId);
        imageTable.bringToFront();
    }

    public void ShowHiddenCard(){
        resetCardPositions();
        imageHiddenCard.setVisibility(View.VISIBLE);
    }

    public void resetCardPositions() {
        imageTable.setTranslationX(0f);
        imageTable.setTranslationY(0f);
        imageTable.setRotation(0f);
        imageHiddenCard.setTranslationX(0f);
        imageHiddenCard.setTranslationY(0f);
        imageHiddenCard.setRotation(0f);
    }

    //public float getY(){
//        return imageTable.getY();
  //  }
    //public float getX(){
      //  return imageTable.getX();
   // }

    public  void addListener(AnimatorListenerAdapter animatorListenerAdapter){
        completionListener = animatorListenerAdapter;
    }

    public void initTrickAnimation(float screenY, float rotation){
        initCardAnimation(screenY, rotation, false, 500);
    }

    public void doTrickAnimation(){
        cardAnimation.start();
    }

    public void initPistiAnimation(float screenY){
        initCardAnimation(screenY, 90f, true, 850);
    }

    public void doPistiAnimation(){
        cardAnimation.start();
    }

    private void initCardAnimation(float screenY, float rotation, boolean showHiddenCard, long duration) {
        resetCardPositions();
        imageTable.setRotation(rotation);
        if (showHiddenCard) {
            imageHiddenCard.setVisibility(View.VISIBLE);
        }

        ObjectAnimator openCard = ObjectAnimator.ofFloat(imageTable, View.TRANSLATION_Y, 0f, screenY);
        ObjectAnimator hiddenCard = ObjectAnimator.ofFloat(imageHiddenCard, View.TRANSLATION_Y, 0f, screenY);
        cardAnimation = new AnimatorSet();
        cardAnimation.playTogether(openCard, hiddenCard);
        cardAnimation.setDuration(duration);
        cardAnimation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                resetCardPositions();
                imageTable.setImageResource(android.R.color.transparent);
                imageHiddenCard.setVisibility(View.INVISIBLE);

                // Advance the queue only after both cards have returned to their layout positions.
                AnimatorListenerAdapter listener = completionListener;
                completionListener = null;
                if (listener != null) {
                    listener.onAnimationEnd(animation);
                }
            }
        });
    }


    /* public void pistiAnimation(TextView view){
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
    }*/
}
