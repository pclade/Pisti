package com.example.pisti;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Application;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.pisti.deck.Card;
import com.example.pisti.deck.Deck;
import com.example.pisti.game.Game;
import com.example.pisti.gui.Gui;
import com.example.pisti.gui.GuiDeck;
import com.example.pisti.gui.GuiPlayer;
import com.example.pisti.gui.GuiTable;
import com.example.pisti.player.Machine;
import com.example.pisti.player.Player;
import com.example.pisti.player.Table;

import java.util.ArrayList;

//public class MainActivity extends AppCompatActivity implements View.OnClickListener {
public class MainActivity extends AppCompatActivity{
    //ImageView iv_card_0, iv_card_1, iv_card_2, iv_card_3;

    //ImageView iv_card_back_0, iv_card_back_1, iv_card_back_2, iv_card_back_3;

    ArrayList<ImageView> cardResourcesP1 = new ArrayList<ImageView>();
    ArrayList<ImageView> cardResourcesP2 = new ArrayList<ImageView>();

    Table table;
    Game game;
    Deck deck;
    Player player1;
    Machine player2;
    //Player currentPlayer;
    ImageView cardToDisplay;
    Gui gui;
    GuiTable guiTable;
    GuiPlayer guiPlayer1;
    GuiPlayer guiPlayer2;
    GuiDeck guiDeck;

    boolean semaphoreMachine;
    //boolean semaphoreTrick;
    AnimatorListenerAdapter animatorListenerAdapter;
    AnimatorListenerAdapter animatorListenerAdapterM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        semaphoreMachine = false;
      //  semaphoreTrick = false;
        // Draw Background
        RelativeLayout relativeLayout = findViewById(R.id.root_layout_id);
        relativeLayout.setBackground(getResources().getDrawable(R.drawable.wood_background));
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        ImageView iv;

        iv = (ImageView)findViewById(R.id.iv_card_0);cardResourcesP1.add(iv);
        iv = (ImageView)findViewById(R.id.iv_card_1);cardResourcesP1.add(iv);
        iv = (ImageView)findViewById(R.id.iv_card_2);cardResourcesP1.add(iv);
        iv = (ImageView)findViewById(R.id.iv_card_3);cardResourcesP1.add(iv);

        for (int i=0;i<4;i++){
            cardResourcesP1.get(i).setOnClickListener(clickListener);
        }

        iv = (ImageView)findViewById(R.id.iv_card_back_0);cardResourcesP2.add(iv);
        iv = (ImageView)findViewById(R.id.iv_card_back_1);cardResourcesP2.add(iv);
        iv = (ImageView)findViewById(R.id.iv_card_back_2);cardResourcesP2.add(iv);
        iv = (ImageView)findViewById(R.id.iv_card_back_3);cardResourcesP2.add(iv);

        // Create game objects
        game = new Game();
        deck = new Deck();
        player1 = new Player();
        player2 = new Machine();
        table = new Table("Table");

        gui = new Gui();
        guiDeck = new GuiDeck();
        guiTable = new GuiTable();
        guiPlayer1 = new GuiPlayer();
        guiPlayer1.createGuiCard();
        guiPlayer2 = new GuiPlayer();
        guiPlayer2.createGuiCard();
        guiPlayer2.setHideCards(true);

        guiTable.setContext(getApplicationContext());
        guiTable.createView();
        guiTable.setImageTable(findViewById(R.id.iv_table_0));

        // Player 1
        player1.setName("Senol");
        game.addPlayer(player1);
        guiPlayer1.setImageViewFromResources(cardResourcesP1);

        // Player 2
        game.addPlayer(player2);
        guiPlayer2.setImageViewFromResources(cardResourcesP2);
        // Table

        deck.createCards();
//        deck.shuffleDeck();

        game.setDeck(deck);
        game.setCardsPoints();

        guiDeck.createCards(getApplicationContext(), deck);

        deck.shuffleDeck();

        game.dealCards(table);
        table.setTopCardOnTable(table.getTheTopCard());
        guiTable.showCard(table.getTheTopCardsId(), guiDeck, deck);

        // Every Player memorizes the top card on the table
        game.addCardToMemoryOfPlayers(table.getTheTopCard());
        // Table has 4 cards dealt, but shows only the last dealt card;
        deal();

        final Button button = findViewById(R.id.points);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //button.setVisibility(View.VISIBLE);
                button.animate().alpha(0).setDuration(2000).withEndAction(new Runnable(){
                    @Override
                    public void run(){
                        //button.setVisibility(View.GONE);

                        game.setPlayerMadeLastTrick(null);
                        deck.createCards();
                        guiDeck.createCards(getApplicationContext(), deck);
                        deck.shuffleDeck();
                        game.setCardsPoints();
                        game.dealCards(table);
                        table.setTopCardOnTable(table.getTheTopCard());
                        guiTable.showCard(table.getTheTopCardsId(), guiDeck, deck);
                        // Every Player memorizes the top card on the table
                        game.addCardToMemoryOfPlayers(table.getTheTopCard());
                        deal();// Code here executes on main thread after user presses button
                    }
                });
            }
        });

        animatorListenerAdapter = new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation){
                guiPlayer1.restoreCardPositions();
                Integer cardNr = 0;
                cardNr = guiPlayer1.getCardNrFromId(guiPlayer1.getSourceView().getId());
                Card playedCard = player1.play(cardNr, table.getTopCardOnTable());
                if(playedCard==null) // Clicked on a previously played card
                    return;
                playP1(playedCard, cardNr);
                // Machine
                semaphoreMachine = true;
                playedCard = player2.playMachine(table.getTopCardOnTable());
                playP2(playedCard, cardNr);
                if(playedCard==null)
                    return;
            }
        };

        animatorListenerAdapterM = new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation){
                guiPlayer2.restoreCardPositions();
                ImageView view = guiPlayer2.cardImages.get(player2.getPlayedCardNrInHand());
                guiTable.showCard(player2.getPlayedCard().getCardNumber(), guiDeck, deck);
                view.setImageResource(android.R.color.transparent);
                //guiPlayer2.cardImages.get(cardNr).setImageResource(android.R.color.transparent);
                play(player2.getPlayedCard(), player2);
                semaphoreMachine = false;
                deal();
            }
        };
    }

    private void playP1(Card playedCard, Integer cardNr){
        if (playedCard == null)
            return;
        //semaphoreTrick = true;
        play(playedCard, player1);
        //guiPlayer1.showCard(playedCard.getCardNumber(), guiDeck, guiTable.imageTable);
        guiTable.showCard(playedCard.getCardNumber(), guiDeck, deck);
        guiPlayer1.cardImages.get(cardNr).setImageResource(android.R.color.transparent);
    }

    private void playP2(Card playedCard, Integer cardNr){
        if(playedCard==null)
            return;
        cardNr = player2.getPlayedCardNrInHand();
        guiPlayer2.showCard(playedCard.getCardNumber(), guiDeck, deck);

        guiPlayer2.initAnimation(guiPlayer2.cardImages.get(cardNr), guiTable.imageTable);
        guiPlayer2.addListener(animatorListenerAdapterM);
        guiPlayer2.doAnimation();
    }

    //@Override
    private View.OnClickListener clickListener = new View.OnClickListener() {
        public void onClick(View view) {
            if(semaphoreMachine==true)
                return;
            guiPlayer1.initAnimation(view, guiTable.imageTable);
            guiPlayer1.addListener(animatorListenerAdapter);
            guiPlayer1.doAnimation();
        }
    };


    private void deal() {
        if (!game.finished()) {
            //***
            if (game.handsEmpty()) {
                game.dealCards(player1);
                game.dealCards(player2);
                // Show on Table
                guiPlayer1.showCards(player1.getHand(), guiDeck, deck);
                guiPlayer2.showCards(player2.getHand(), guiDeck, deck);
            }
        }
        else{
            game.cleanUp(table);
            guiTable.imageTable.setImageResource(android.R.color.transparent);
            player1.calcCardPoints();
            // Show Points
            Button button;
            button = (Button) findViewById(R.id.points);
            player2.calcCardPoints();
            button.setAlpha(1.0f);
            button.setText("P1: "+player1.getPoints()+", P2: "+player2.getPoints());
            button.setVisibility(View.VISIBLE);
            game.cleanMemories();
        }
    }

    private void play(Card playedCard, Player player){
        table.takeCard(playedCard);
        player.addCardToMemory(playedCard);
        table.setTopCardOnTable(playedCard);
        // Check game Rules, if it's a trick.
        if(!game.isTableClean(table)){
            if(game.isItAPisti(table)) {
                gui.displayPisti();
                player.addToWinner(table);
                player.addPoints(10);
                game.setPlayerMadeLastTrick(player);
                //Animation of trick
                if(player==player1) {
                    guiTable.cardAnimation(1000);
                }else{
                    guiTable.cardAnimation(-1000);
                }
            }
            else if(game.isItATrick(table)){
                //semaphoreTrick = true;
                gui.displayItsATrick();
                player.addToWinner(table);
                game.setPlayerMadeLastTrick(player);
                //Animation of trick
                if(player==player1) {
                    guiTable.cardAnimation(1000);
                }else{
                    guiTable.cardAnimation(-1000);
                }
            }
            else{
               // semaphoreTrick = false;
            }
        }
    }
}

