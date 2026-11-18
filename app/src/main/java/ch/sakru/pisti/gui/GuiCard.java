package ch.sakru.pisti.gui;

import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

public class GuiCard {
    private AnimatorSet cardAnimation;
    private View sourceView;

    public GuiCard() {
    }

    public View getSourceView() {
        return this.sourceView;
    }

    public void initAnimation(View sourceView, View destinationView) {
        this.sourceView = sourceView;
        this.sourceView.bringToFront();
        restoreCardPositions();
        ObjectAnimator xAnim = ObjectAnimator.ofFloat(sourceView, View.TRANSLATION_X,
                0f, destinationView.getX() - sourceView.getX());
        ObjectAnimator yAnim = ObjectAnimator.ofFloat(sourceView, View.TRANSLATION_Y,
                0f, destinationView.getY() - sourceView.getY());
        cardAnimation = new AnimatorSet();
        cardAnimation.playTogether(xAnim, yAnim);
        cardAnimation.setDuration(500);
    }

    public void restoreCardPositions() {
        sourceView.setTranslationX(0f);
        sourceView.setTranslationY(0f);
    }

    public void doAnimation() {
        cardAnimation.start();
    }

    public void addListener(AnimatorListenerAdapter animatorListenerAdapter) {
      cardAnimation.addListener(animatorListenerAdapter);
    }

}
