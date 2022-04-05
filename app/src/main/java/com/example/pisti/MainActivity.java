package com.example.pisti;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import java.util.concurrent.Semaphore;

//public class MainActivity extends AppCompatActivity implements View.OnClickListener {
public class MainActivity extends AppCompatActivity {
    //ImageView iv_card_0, iv_card_1, iv_card_2, iv_card_3;

    //ImageView iv_card_back_0, iv_card_back_1, iv_card_back_2, iv_card_back_3;

    ArrayList<ImageView> cardResourcesP1 = new ArrayList<ImageView>();
    ArrayList<ImageView> cardResourcesP2 = new ArrayList<ImageView>();

    //boolean playAnimationAfterBusiness = false;
    //View viewToAnimate;
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

    TextView textPisti;
    ImageView settings;

    AnimatorListenerAdapter animatorListenerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        settings = findViewById(R.id.iv_settings);
        settings.setOnClickListener((view) -> {
            showCustomDialog();
        });


        semaphoreMachine = false;
        //  semaphoreTrick = false;
        // Draw Background
        RelativeLayout relativeLayout = findViewById(R.id.root_layout_id);
        relativeLayout.setBackground(getResources().getDrawable(R.drawable.wood_background));
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        Integer layoutWidth = size.x - 100;

        ImageView iv;

        iv = (ImageView) findViewById(R.id.iv_card_0);
        cardResourcesP1.add(iv);
        Integer originalWidth = iv.getLayoutParams().width;
        iv.requestLayout();
        iv.getLayoutParams().width = layoutWidth / 4;
        iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        iv = (ImageView) findViewById(R.id.iv_card_1);
        cardResourcesP1.add(iv);
        iv.requestLayout();
        iv.getLayoutParams().width = layoutWidth / 4;
        iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        iv = (ImageView) findViewById(R.id.iv_card_2);
        cardResourcesP1.add(iv);
        iv.requestLayout();
        iv.getLayoutParams().width = layoutWidth / 4;
        iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        iv = (ImageView) findViewById(R.id.iv_card_3);
        cardResourcesP1.add(iv);
        iv.requestLayout();
        iv.getLayoutParams().width = layoutWidth / 4;
        iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        for (int i = 0; i < 4; i++) {
            cardResourcesP1.get(i).setOnClickListener(clickListener);
        }

        iv = (ImageView) findViewById(R.id.iv_card_back_0);
        cardResourcesP2.add(iv);
        iv.requestLayout();
        iv.getLayoutParams().width = layoutWidth / 4;
        iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        iv = (ImageView) findViewById(R.id.iv_card_back_1);
        cardResourcesP2.add(iv);
        iv.requestLayout();
        iv.getLayoutParams().width = layoutWidth / 4;
        iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        iv = (ImageView) findViewById(R.id.iv_card_back_2);
        cardResourcesP2.add(iv);
        iv.requestLayout();
        iv.getLayoutParams().width = layoutWidth / 4;
        iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        iv = (ImageView) findViewById(R.id.iv_card_back_3);
        cardResourcesP2.add(iv);
        iv.requestLayout();
        iv.getLayoutParams().width = layoutWidth / 4;
        iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

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
        guiTable.imageTable.requestLayout();
        guiTable.imageTable.getLayoutParams().width = layoutWidth / 4;
        guiTable.imageTable.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        // Player 1
        player1.setName("Senol");
        game.addPlayer(player1);
        guiPlayer1.setImageViewFromResources(cardResourcesP1);

        // Player 2
        player2.setName(player1.getName());
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
        table.buildHiddenCardsString();

        table.setTopCardOnTable(table.getTheTopCard());
        guiTable.showCard(table.getTheTopCardsId(), guiDeck, deck);

        // Every Player memorizes the top card on the table
        game.addCardToMemoryOfPlayers(table.getTheTopCard());
        // Table has 4 cards dealt, but shows only the last dealt card;
        deal();
        readPreferences();
        textPisti = (TextView) findViewById(R.id.pisti);



/*
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
*/
        /*
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

         */
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        public void onClick(View view) {
            if (semaphoreMachine == true)
                return;
            semaphoreMachine = true;
            Integer cardNr = guiPlayer1.getCardNrFromView((ImageView) view);
            Card playedCard = player1.play(cardNr, table.getTopCardOnTable());
            if (playedCard == null) {// Clicked on a previously played card
                semaphoreMachine = false;
                return;
            }
            while (gui.getAnimationSize() > 0) {
                gui.removeFirstAnimation();
            }
            gui.addAnimation(1, view);
            play(playedCard, player1);
            //
            playedCard = player2.playMachine(table.getTopCardOnTable());
            if (playedCard == null){ // Clicked on a previously played card
                semaphoreMachine = false;
                return;
            }
            gui.addAnimation(2, null);
            play(playedCard, player2);
            playNextAnimation();
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
        } else {
            game.cleanUp(table);
            guiTable.imageTable.setImageResource(android.R.color.transparent);
            player1.calcCardPoints();
            player2.calcCardPoints();
            String strScore;
            game.setTrickCount(0);
            strScore = player1.getName() + ", total points: " + player1.getPoints() + "\n" +
                    "Points: " + player1.getCardsWithPointsString() + "\n" +
                    "\n" +
                    player2.getName() + ", total points: " + player2.getPoints() + "\n" +
                    "Points: " + player2.getCardsWithPointsString() + "\n";
            if (game.maxPointsReached()) {
                AlertDialog builder = new AlertDialog.Builder(this)
                        .setMessage(strScore).setTitle("SCORE")
                        .setCancelable(false)
                        .setPositiveButton("NEW GAME", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                newGame();
                            }
                        })
                        .setNegativeButton("EXIT", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                System.exit(0);
                            }
                        }).show();
                game.cleanMemories();
                game.cleanPoints();
            } else {
                // Show Points
                AlertDialog builder = new AlertDialog.Builder(this)
                        .setMessage(strScore).setTitle("SCORE")
                        .setCancelable(false)
                        .setPositiveButton("CONTINUE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                nextRound();
                            }
                        }).show();
                game.cleanMemories();
            }
        }
    }

    private void play(Card playedCard, Player player) {
        //TODO: Test Animation
        table.takeCard(playedCard);
        player.addCardToMemory(playedCard);
        table.setTopCardOnTable(playedCard);

        // Check game Rules, if it's a trick.
        if (!game.isTableClean(table)) {
            if (game.isItAPisti(table)) {
                player.incCountOfPisti();
                player.addToWinner(table);
                player.addPoints(10);
                game.setPlayerMadeLastTrick(player);
                //Animation of trick
                gui.addAnimation(5, null);
                if (player == player1) {
                    gui.addAnimation(11, null);
                } else {
                    gui.addAnimation(12, null);
                }
            } else if (game.isItATrick(table)) {
                //semaphoreTrick = true;
                if (player == player1) {
                    gui.addAnimation(3, null);
                } else {
                    gui.addAnimation(4, null);
                }
                if (game.getTrickCount() == 0) {
                    game.setTrickCount(1);
                    if (player == player1) {
                        //It's the first trick, show hidden cards to player1
                        gui.addAnimation(9, null);
                    }
                }
                player.addToWinner(table);
                game.setPlayerMadeLastTrick(player);
            } else if (game.finished()) {
                // Animate Card to the player with last Tick
                game.setTrickCount(1);
                if (game.getPlayerMadeLastTrick() == player1) {
                    gui.addAnimation(3, null);
                } else {
                    gui.addAnimation(4, null);
                }
            }
        }
    }

    public void nextRound() {
        game.setPlayerMadeLastTrick(null);
        game.setTrickCount(0);
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

    public void newGame() {
        game.setPlayerMadeLastTrick(null);
        game.setTrickCount(0);
        deck.createCards();
        guiDeck.createCards(getApplicationContext(), deck);
        deck.shuffleDeck();
        game.setCardsPoints();
        game.dealCards(table);
        table.buildHiddenCardsString();
        table.setTopCardOnTable(table.getTheTopCard());
        guiTable.showCard(table.getTheTopCardsId(), guiDeck, deck);
        // Every Player memorizes the top card on the table
        game.addCardToMemoryOfPlayers(table.getTheTopCard());
        deal();// Code here executes on main thread after user presses button
    }

    public void readPreferences() {
        Context context = getApplicationContext();
        SharedPreferences sharedPref = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        String player_name_default = getResources().getString(R.string.player_name);
        String player_name = sharedPref.getString(getString(R.string.player_name), player_name_default);

        boolean max_51 = sharedPref.getBoolean(getString(R.string.max_51), true);
        boolean max_101 = sharedPref.getBoolean(getString(R.string.max_101), false);
        boolean max_151 = sharedPref.getBoolean(getString(R.string.max_151), false);

        player1.setName(player_name);
        player2.setName(player_name);
        if (max_51) {
            game.setPointsToReach(51);
        } else if (max_101) {
            game.setPointsToReach(101);
        } else if (max_151) {
            game.setPointsToReach(151);
        }
        //TODO: DEBUGGING
        //game.setPointsToReach(5);

    }

    public void showCustomDialog() {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_settings);

        readPreferences();

        final TextView versionEt = dialog.findViewById(R.id.version);
        versionEt.setText("Version: "+ BuildConfig.VERSION_NAME);
        final EditText nameEt = dialog.findViewById(R.id.username);
        final RadioButton max51Et = dialog.findViewById(R.id.max_51);
        final RadioButton max101Et = dialog.findViewById(R.id.max_101);
        final RadioButton max151Et = dialog.findViewById(R.id.max_151);

        nameEt.setText(player1.getName());
        switch (game.getPointsToReach()) {
            case 51:
                max51Et.setChecked(true);
                max51Et.setActivated(true);
                break;
            case 101:
                max101Et.setChecked(true);
                max51Et.setActivated(true);
                break;
            case 151:
                max151Et.setChecked(true);
                max51Et.setActivated(true);
                break;
        }
        Button button_save = dialog.findViewById(R.id.button_save);
        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String player_name = nameEt.getText().toString();
                boolean max51 = max51Et.isChecked();
                boolean max101 = max101Et.isChecked();
                boolean max151 = max151Et.isChecked();

                Context context = getApplicationContext();
                SharedPreferences sharedPref = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.player_name), player_name);
                editor.putBoolean(getString(R.string.max_51), max51);
                editor.putBoolean(getString(R.string.max_101), max101);
                editor.putBoolean(getString(R.string.max_151), max151);
                editor.apply();
                readPreferences();
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    private void playAnimation1(View view) {
        guiPlayer1.initAnimation(view, guiTable.imageTable);
        animatorListenerAdapter = new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                guiPlayer1.restoreCardPositions();
                Integer cardNrInHand = guiPlayer1.getCardNrFromView((ImageView) view);
                Card playedCard = player1.getPlayedCard();
                Integer cardNr = playedCard.getCardNumber();
                guiTable.showCard(cardNr, guiDeck, deck);
                guiPlayer1.cardImages.get(cardNrInHand).setImageResource(android.R.color.transparent);
                gui.removeFirstAnimation();
                playNextAnimation();
            }
        };
        guiPlayer1.addListener(animatorListenerAdapter);
        guiPlayer1.doAnimation();
        // After Animation

    }

    private void playAnimation2(View view) {
        Integer cardNr = player2.getPlayedCardNrInHand();
        Card playedCard = player2.getPlayedCard();
        guiPlayer2.showCard(cardNr, playedCard.getCardNumber(), guiDeck, deck);
        guiPlayer2.initAnimation(guiPlayer2.cardImages.get(cardNr), guiTable.imageTable);
        animatorListenerAdapter = new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                guiPlayer2.restoreCardPositions();
                ImageView view = guiPlayer2.cardImages.get(player2.getPlayedCardNrInHand());
                guiTable.showCard(player2.getPlayedCard().getCardNumber(), guiDeck, deck);
                view.setImageResource(android.R.color.transparent);
                gui.removeFirstAnimation();
                playNextAnimation();
            }
        };

        guiPlayer2.addListener(animatorListenerAdapter); //M
        guiPlayer2.doAnimation();
    }

    private void playAnimation3(View view){
        guiTable.initAnimation(1000, 0);
        animatorListenerAdapter = new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                gui.removeFirstAnimation();
                playNextAnimation();
            }
        };
        guiTable.addListener(animatorListenerAdapter);
        guiTable.doAnimation();
    }

    private void playAnimation4(View view){
        guiTable.initAnimation(-1000, 0);
        animatorListenerAdapter = new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                gui.removeFirstAnimation();
                playNextAnimation();
            }
        };
        guiTable.addListener(animatorListenerAdapter);
        guiTable.doAnimation();
    }

    private void playAnimation11(View view){
        guiTable.initAnimation(1000, 90);
        animatorListenerAdapter = new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                gui.removeFirstAnimation();
                playNextAnimation();
            }
        };
        guiTable.addListener(animatorListenerAdapter);
        guiTable.doAnimation();
    }

    private void playAnimation12(View view){
        guiTable.initAnimation(-1000, 90);
        animatorListenerAdapter = new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                gui.removeFirstAnimation();
                playNextAnimation();
            }
        };
        guiTable.addListener(animatorListenerAdapter);
        guiTable.doAnimation();
    }


    private void playAnimation9(View view){
        String strHiddenCardsOnTable;
        strHiddenCardsOnTable = table.getHiddenCardsString();
        AlertDialog builder = new AlertDialog.Builder(this)
                .setMessage(strHiddenCardsOnTable).setTitle("HIDDEN CARDS")
                //.setCancelable(false)
                .setPositiveButton("CLOSE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        gui.removeFirstAnimation();
                        playNextAnimation();
                    }
                }).show();

    }

    private void playAnimation5(View view){
        guiTable.pistiAnimation(textPisti);
        gui.removeFirstAnimation();
        playNextAnimation();
    }

    public void playNextAnimation() {
        if(gui.getAnimationSize()==0) {
            deal();
            semaphoreMachine = false;
            return;
        }
        switch (gui.getAnimationSequence(0)) {
            case 1:
                playAnimation1(gui.getAnimationView(0));
                break;
            case 2:
                playAnimation2(gui.getAnimationView(0));
                break;
            case 3:
                playAnimation3(gui.getAnimationView(0));
                break;
            case 4:
                playAnimation4(gui.getAnimationView(0));
                break;
            case 5:
                playAnimation5(gui.getAnimationView(0));
                break;
            case 9:
                playAnimation9(gui.getAnimationView(0));
                break;
            case 11:
                playAnimation11(gui.getAnimationView(0));
                break;
            case 12:
                playAnimation12(gui.getAnimationView(0));
                break;
        }
    }
}

