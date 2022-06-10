package ch.sakru.pisti.gui;

import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

public class GuiTrick {

    private float saveX;
    private float saveY;
    private ObjectAnimator xAnim;
    private ObjectAnimator yAnim;
    private View sourceView;


    public void addListener(AnimatorListenerAdapter animatorListenerAdapter) {
        yAnim.addListener(animatorListenerAdapter);
    }

    public void initAnimation(View sourceView, View destinationView) {
        this.sourceView = sourceView;
        this.sourceView.bringToFront();
        saveX = sourceView.getX();
        saveY = sourceView.getY();
        xAnim = ObjectAnimator.ofFloat(sourceView, "translationX", destinationView.getX() - sourceView.getX());
        yAnim = ObjectAnimator.ofFloat(sourceView, "translationY", destinationView.getY() - sourceView.getY());
    }

    public void doAnimation() {
        AnimatorSet cardPlayer = new AnimatorSet();
        cardPlayer.play(xAnim).with(yAnim);
        cardPlayer.setDuration(500);
        cardPlayer.start();
    }
}
