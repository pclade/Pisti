package com.example.pisti.gui;

import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Scanner;

public class Gui {

    private ArrayList<Animation> animationSequence;
    private ArrayList<View> animationView;

    public Gui(){
        animationSequence = new ArrayList<Animation>();
        animationView = new ArrayList<View>();
    }

    public String getInput(){
        Scanner scanner  = new Scanner(System.in);
        return scanner.nextLine();
    }
    public void addAnimation(Animation animation, View view){
        animationSequence.add(animation);
        animationView.add(view);
    }

    public void removeFirstAnimation(){
        animationSequence.remove(0);
        animationView.remove(0);
    }

    public Integer getAnimationSize(){
        return animationSequence.size();
    }

    public Animation getAnimationSequence(Integer animation){
        return animationSequence.get(animation);
    }

    public View getAnimationView(Integer animation){
        return animationView.get(animation);
    }
}
