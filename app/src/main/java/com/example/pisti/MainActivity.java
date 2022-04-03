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

    Button button_points;
    Button button_newgame;
    Button button_exit;

    TextView textPisti;
    ImageView settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        settings = findViewById(R.id.iv_settings);
        settings.setOnClickListener((view) -> {showCustomDialog();});


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
        Integer layoutWidth = size.x-100;

        ImageView iv;

        iv = (ImageView)findViewById(R.id.iv_card_0);cardResourcesP1.add(iv);
        Integer originalWidth = iv.getLayoutParams().width;
        iv.requestLayout();
        iv.getLayoutParams().width= layoutWidth / 4;
        iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        iv = (ImageView)findViewById(R.id.iv_card_1);cardResourcesP1.add(iv);
        iv.requestLayout();iv.getLayoutParams().width= layoutWidth / 4;iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        iv = (ImageView)findViewById(R.id.iv_card_2);cardResourcesP1.add(iv);
        iv.requestLayout();iv.getLayoutParams().width= layoutWidth / 4;iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        iv = (ImageView)findViewById(R.id.iv_card_3);cardResourcesP1.add(iv);
        iv.requestLayout();iv.getLayoutParams().width= layoutWidth / 4;iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        for (int i=0;i<4;i++){
            cardResourcesP1.get(i).setOnClickListener(clickListener);
        }

        iv = (ImageView)findViewById(R.id.iv_card_back_0);cardResourcesP2.add(iv);
        iv.requestLayout();iv.getLayoutParams().width= layoutWidth / 4;iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        iv = (ImageView)findViewById(R.id.iv_card_back_1);cardResourcesP2.add(iv);
        iv.requestLayout();iv.getLayoutParams().width= layoutWidth / 4;iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        iv = (ImageView)findViewById(R.id.iv_card_back_2);cardResourcesP2.add(iv);
        iv.requestLayout();iv.getLayoutParams().width= layoutWidth / 4;iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        iv = (ImageView)findViewById(R.id.iv_card_back_3);cardResourcesP2.add(iv);
        iv.requestLayout();iv.getLayoutParams().width= layoutWidth / 4;iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

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
        guiTable.imageTable.getLayoutParams().width= layoutWidth / 4;
        guiTable.imageTable.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

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
        readPreferences();
/*
        buttonPisti = findViewById(R.id.pisti);
        buttonPisti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonPisti.animate().alpha(0).setDuration(2000);
            }
        });
*/
        textPisti = (TextView)findViewById(R.id.pisti);

/*
        button_points = findViewById(R.id.points);
        button_points.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //button.setVisibility(View.VISIBLE);
                button_points.animate().alpha(0).setDuration(2000).withEndAction(new Runnable(){
                    @Override
                    public void run(){
                        nextRound();
                    }
                });
            }
        });

        button_newgame = findViewById(R.id.new_game);
        button_newgame.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                button_exit.animate().alpha(0).setDuration(2000);
                button_newgame.animate().alpha(0).setDuration(2000).withEndAction(new Runnable(){
                    @Override
                    public void run(){
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

        button_exit = findViewById(R.id.exit);
        button_exit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                button_exit.animate().alpha(0).setDuration(2000).withEndAction(new Runnable(){
                    @Override
                    public void run(){
                        System.exit(0);
                    }
                });
            }
        });
*/
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
        guiPlayer2.showCard(cardNr, playedCard.getCardNumber(), guiDeck, deck);

        guiPlayer2.initAnimation(guiPlayer2.cardImages.get(cardNr), guiTable.imageTable);
        guiPlayer2.addListener(animatorListenerAdapterM);
        guiPlayer2.doAnimation();
//        deal();
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
            // Animate Card to the player with last Tick
            /*
            if(game.getPlayerMadeLastTrick() == player1){
                guiTable.cardAnimation(1000);
            }else{
                guiTable.cardAnimation(-1000);
            }
*/
            game.cleanUp(table);
            guiTable.imageTable.setImageResource(android.R.color.transparent);
            player1.calcCardPoints();
            player2.calcCardPoints();
            String strScore;
            strScore = player1.getName() + ": "+player1.getPoints()+ "\n" +
                    player2.getName() + ": "+player2.getPoints()+ "\n";
            if(game.maxPointsReached()) {
                //button_newgame = (Button) findViewById(R.id.new_game);
                //button_newgame.setAlpha(1.0f);


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
/*
                GuiDialogContinue guiDialogContinue = new GuiDialogContinue();
                guiDialogContinue.scoreString = player1.getName() + ": "+player1.getPoints()+ "\n" +
                                                player2.getName() + ": "+player2.getPoints()+ "\n" +
                                                "Click CONTINUE for new game";

                button_newgame.setText("YOU: " + player1.getPoints() + ", ME: " + player2.getPoints() + "\n Click for new game");
                button_newgame.setVisibility(View.VISIBLE);

                button_exit = (Button) findViewById(R.id.exit);
                button_exit.setAlpha(1.0f);
                button_exit.setVisibility(View.VISIBLE);
*/
                game.cleanMemories();
                game.cleanPoints();
            }
            else {
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
//                button_points = (Button) findViewById(R.id.points);
//                button_points.setAlpha(1.0f);
//                button_points.setText("YOU: " + player1.getPoints() + ", ME: " + player2.getPoints()+"\n Click for continue");
//                button_points.setVisibility(View.VISIBLE);
                game.cleanMemories();
            }
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
                guiTable.pistiAnimation(textPisti);
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
            else if(game.finished()){
                // Animate Card to the player with last Tick
                if(game.getPlayerMadeLastTrick() == player1){
                    guiTable.cardAnimation(1000);
                }else{
                    guiTable.cardAnimation(-1000);
                }
            }
        }
    }

    public void nextRound(){
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

    public void newGame(){
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

    public void readPreferences(){
        Context context =  getApplicationContext();
        SharedPreferences sharedPref = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        String player_name_default = getResources().getString(R.string.player_name);
        String player_name = sharedPref.getString(getString(R.string.player_name), player_name_default);

        boolean max_51 = sharedPref.getBoolean(getString(R.string.max_51),true);
        boolean max_101 = sharedPref.getBoolean(getString(R.string.max_101),false);
        boolean max_151 = sharedPref.getBoolean(getString(R.string.max_151),false);

        player1.setName(player_name);
        player2.setName("Machine");
        if(max_51) {
            game.setPointsToReach(51);
        }else if (max_101){
            game.setPointsToReach(101);
        }else if (max_151){
            game.setPointsToReach(151);
        }
        //TODO: DEBUGGING
        //game.setPointsToReach(5);

    }

    public void showCustomDialog(){
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_settings);

        readPreferences();

        final EditText nameEt = dialog.findViewById(R.id.username);
        final RadioButton max51Et = dialog.findViewById(R.id.max_51);
        final RadioButton max101Et = dialog.findViewById(R.id.max_101);
        final RadioButton max151Et = dialog.findViewById(R.id.max_151);

        nameEt.setText(player1.getName());
        switch (game.getPointsToReach()){
            case  51: max51Et.setChecked(true);max51Et.setActivated(true);break;
            case 101: max101Et.setChecked(true);max51Et.setActivated(true);break;
            case 151: max151Et.setChecked(true);max51Et.setActivated(true);break;
        }
        Button button_save = dialog.findViewById(R.id.button_save);
        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String player_name = nameEt.getText().toString();
                boolean max51 = max51Et.isChecked();
                boolean max101 = max101Et.isChecked();
                boolean max151 = max151Et.isChecked();

                Context context =  getApplicationContext();
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
}

