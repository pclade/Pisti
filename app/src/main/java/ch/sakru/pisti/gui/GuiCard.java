package ch.sakru.pisti.gui;

import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

public class GuiCard {
    private float saveX;
    private float saveY;
    private ObjectAnimator xAnim;
    private ObjectAnimator yAnim;
    private View sourceView;

    public GuiCard() {
    }

    public void setSaveX(float saveX) {
        this.saveX = saveX;
    }
    public float getSaveX() {
        return saveX;
    }
    public void setSaveY(float saveY) {
        this.saveY = saveY;
    }
    public float getSaveY() {
        return saveY;
    }
    public View getSourceView() {
        return this.sourceView;
    }

    public void initAnimation(View sourceView, View destinationView) {
        this.sourceView = sourceView;
        this.sourceView.bringToFront();
        saveX = sourceView.getX();
        saveY = sourceView.getY();
        xAnim = ObjectAnimator.ofFloat(sourceView, "translationX", destinationView.getX() - sourceView.getX());
        yAnim = ObjectAnimator.ofFloat(sourceView, "translationY", destinationView.getY() - sourceView.getY());
    }

    public void restoreCardPositions() {
        sourceView.setX(saveX);
        sourceView.setY(saveY);
    }

    public void doAnimation() {
        AnimatorSet cardPlayer = new AnimatorSet();
        cardPlayer.play(xAnim).with(yAnim);
        cardPlayer.setDuration(500);
        cardPlayer.start();
    }

    public void addListener(AnimatorListenerAdapter animatorListenerAdapter) {
      yAnim.addListener(animatorListenerAdapter);
    }

}