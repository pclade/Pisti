package ch.sakru.pisti;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ch.sakru.pisti.gui.Animation;
import ch.sakru.pisti.deck.Card;
import ch.sakru.pisti.deck.Deck;
import ch.sakru.pisti.game.Game;
import ch.sakru.pisti.gui.Gui;
import ch.sakru.pisti.gui.GuiDeck;
import ch.sakru.pisti.gui.GuiPlayer;
import ch.sakru.pisti.gui.GuiTable;
import ch.sakru.pisti.player.Machine;
import ch.sakru.pisti.player.Player;
import ch.sakru.pisti.player.Table;
import ch.sakru.pisti.R;

import java.util.ArrayList;

//public class MainActivity extends AppCompatActivity implements View.OnClickListener {
public class MainActivity extends AppCompatActivity {

    ArrayList<ImageView> cardResourcesP1 = new ArrayList<ImageView>();
    ArrayList<ImageView> cardResourcesP2 = new ArrayList<ImageView>();

    Table table;
    Game game;
    Deck deck;
    Player player1;
    Machine player2;
    Gui gui;
    GuiTable guiTable;
    GuiPlayer guiPlayer1;
    GuiPlayer guiPlayer2;
    GuiDeck guiDeck;

    boolean semaphoreMachine;

    TextView textPisti;
    ImageView settings;
    ImageView help;
    String strScore;

    AnimatorListenerAdapter animatorListenerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        settings = findViewById(R.id.iv_settings);
        settings.setOnClickListener((view) -> {
            showCustomDialog();
        });

        help = findViewById(R.id.iv_help);
        help.setOnClickListener((view) -> {
            startActivity(new Intent(getApplicationContext(), HelpActivity.class));
        });



        semaphoreMachine = false;
        // Draw Background
        RelativeLayout relativeLayout = findViewById(R.id.root_layout_id);
        relativeLayout.setBackground(getResources().getDrawable(R.drawable.wood_background));
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);



        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        DisplayMetrics displayMetrics = new DisplayMetrics();

        display.getRealMetrics(displayMetrics);
        float scale = displayMetrics.density;
        float padding = relativeLayout.getPaddingLeft()*2;
        //size.x = Math.round(displayMetrics.widthPixels / scale)-(padding*2); //paddig: 20dp
        //size.y = Math.round(displayMetrics.heightPixels / scale)-(padding*2);


        ImageView iv_b;
        iv_b = (ImageView) findViewById(R.id.iv_settings);
        iv_b.requestLayout();
        float topHelp = iv_b.getY() + iv_b.getHeight() * scale;

        iv_b = (ImageView) findViewById(R.id.iv_help);
        iv_b.requestLayout();
        iv_b.setY(topHelp);



//        Integer layoutWidth = size.x - 100;
        int layoutWidth =  (int) ((displayMetrics.widthPixels - padding) / 4.0f + 0.5f);

        ImageView iv;

        iv = (ImageView) findViewById(R.id.iv_card_0);
        cardResourcesP1.add(iv);
        iv.requestLayout();
        iv.getLayoutParams().width = layoutWidth;
        iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        iv = (ImageView) findViewById(R.id.iv_card_1);
        cardResourcesP1.add(iv);
        iv.requestLayout();
        iv.getLayoutParams().width = layoutWidth;
        iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        iv = (ImageView) findViewById(R.id.iv_card_2);
        cardResourcesP1.add(iv);
        iv.requestLayout();
        iv.getLayoutParams().width = layoutWidth;
        iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        iv = (ImageView) findViewById(R.id.iv_card_3);
        cardResourcesP1.add(iv);
        iv.requestLayout();
        iv.getLayoutParams().width = layoutWidth;
        iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        for (int i = 0; i < 4; i++) {
            cardResourcesP1.get(i).setOnClickListener(clickListener);
        }

        iv = (ImageView) findViewById(R.id.iv_card_back_0);
        cardResourcesP2.add(iv);
        iv.requestLayout();
        iv.getLayoutParams().width = layoutWidth;
        iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        iv = (ImageView) findViewById(R.id.iv_card_back_1);
        cardResourcesP2.add(iv);
        iv.requestLayout();
        iv.getLayoutParams().width = layoutWidth;
        iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        iv = (ImageView) findViewById(R.id.iv_card_back_2);
        cardResourcesP2.add(iv);
        iv.requestLayout();
        iv.getLayoutParams().width = layoutWidth;
        iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        iv = (ImageView) findViewById(R.id.iv_card_back_3);
        cardResourcesP2.add(iv);
        iv.requestLayout();
        iv.getLayoutParams().width = layoutWidth;
        iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        // Create game objects
        game = new Game();
        game.setStrPisti(getString(R.string.app_name));
        game.setStrTotalPoints(getString(R.string.score_total_points));
        game.setStrTitlePoints(getString(R.string.score_title_points));
        game.setStrThreePoint(getString(R.string.three_point_representation));
        game.setStrComma(getString(R.string.str_comma));
        game.setStrDoublePoint(getString(R.string.str_double_point));
        game.setStrCRLF(getString(R.string.str_CRLF));
        // Create Deck object
        deck = new Deck();
        //TODO: DEBUG
        //deck.setDeckSize(28);
        //
        player1 = new Player();
        player2 = new Machine();
        table = new Table(getString(R.string.table));

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
        guiTable.imageTable.getLayoutParams().width = layoutWidth;
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
        game.setDeck(deck);
        newGame();
        readPreferences();
        //TODO: DEBUG
        //game.setPointsToReach(11);
        //
        textPisti = (TextView) findViewById(R.id.pisti);
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
            gui.addAnimation(Animation.PLAYER1_PLAY, view);
            play(playedCard, player1);
            //
            playedCard = player2.playMachine(table.getTopCardOnTable());
            if (playedCard == null){ // Clicked on a previously played card
                semaphoreMachine = false;
                return;
            }
            gui.addAnimation(Animation.PLAYER2_PLAY, null);
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
        }
    }

    private void playPlayerPisti(Player player){
        if (player == player1) {
            gui.addAnimation(Animation.PLAYER1_PISTI, null);
        } else {
            gui.addAnimation(Animation.PLAYER2_PISTI, null);
        }
    }

    private void playPlayerTrick(Player player){
        if (player == player1) {
            gui.addAnimation(Animation.PLAYER1_TRICK, null);
        } else {
            gui.addAnimation(Animation.PLAYER2_TRICK, null);
        }
    }

    private void playPlayerLastTrick(Player player){
        if (game.getPlayerMadeLastTrick() == player1) {
            gui.addAnimation(Animation.PLAYER1_TRICK, null);
        } else {
            gui.addAnimation(Animation.PLAYER2_TRICK, null);
        }
    }

    private void playShowHiddenCards(Player player){
        if (game.getTrickCount() == 0) {
            game.setTrickCount(1);
            if (player == player1) {
                gui.addAnimation(Animation.SHOW_HIDDEN_CARDS, null);
            }
        }
    }
    private void playGameFinished(Player player){
        // Animate Card to the player with last Tick
        game.setTrickCount(1);
        if(game.getPlayerMadeLastTrick() != null) {
            playPlayerLastTrick(player);
        }
        else{
            // DO NOTHING
        }
        game.cleanUp(table);
        //guiTable.imageTable.setImageResource(android.R.color.transparent);
        player1.calcCardPoints();
        player2.calcCardPoints();
        //String strScore;
        game.setTrickCount(0);
        strScore = game.getScoreString();
        if (game.maxPointsReached()) {
            gui.addAnimation(Animation.SHOW_GAME_SCORE, null);
            game.cleanMemories();
            table.cleanMemories();
            game.cleanPoints();
        } else {
            gui.addAnimation(Animation.SHOW_ROUND_SCORE, null);
            game.cleanMemories();
            table.cleanMemories();
        }
    }

    private void play(Card playedCard, Player player) {
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
                gui.addAnimation(Animation.PISTI, null);
                playPlayerPisti(player);
            } else
            if (game.isItATrick(table)) {
                playPlayerTrick(player);
                playShowHiddenCards(player);
                player.addToWinner(table);
                game.setPlayerMadeLastTrick(player);
            }
            else{ // Game is not finished
                // DO NOTHING
            }
        }
        else{ // Table is clean and the last card is played from Machine
                // DO NOTHING
        }
        if(game.finished()) {
            playGameFinished(player);
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
        table.buildHiddenCardsString();
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

        String player_name_default = getString(R.string.player_name);
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

    private void playAnimation9(View view){
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

    private void playAnimation10(View view){
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


    private void playAnimation7(View view){
        String strHiddenCardsOnTable;
        strHiddenCardsOnTable = table.getHiddenCardsString();
        AlertDialog builder = new AlertDialog.Builder(this)
                .setMessage(strHiddenCardsOnTable).setTitle("HIDDEN CARDS")
                .setCancelable(false)
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

    private void playAnimation6(View view){
        // Show Points
        AlertDialog builder = new AlertDialog.Builder(this)
                .setMessage(strScore).setTitle("SCORE")
                .setCancelable(false)
                .setPositiveButton("CONTINUE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        nextRound();
                        gui.removeFirstAnimation();
                        playNextAnimation();
                    }
                }).show();
    }

    private void playAnimation8(View view){
        AlertDialog builder = new AlertDialog.Builder(this)
                .setMessage(strScore).setTitle("SCORE")
                .setCancelable(false)
                .setPositiveButton("NEW GAME", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        newGame();
                        gui.removeFirstAnimation();
                        playNextAnimation();
                    }
                })
                .setNegativeButton("EXIT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        System.exit(0);
                        gui.removeFirstAnimation();
                        playNextAnimation();
                    }
                }).show();
    }

    public void playNextAnimation() {
        if(gui.getAnimationSize()==0) {
            deal();
            semaphoreMachine = false;
            return;
        }
        switch (gui.getAnimationSequence(0)) {
            case  PLAYER1_PLAY: playAnimation1(gui.getAnimationView(0));  break;
            case  PLAYER2_PLAY: playAnimation2(gui.getAnimationView(0));  break;
            case  PLAYER1_TRICK: playAnimation3(gui.getAnimationView(0));  break;
            case  PLAYER2_TRICK: playAnimation4(gui.getAnimationView(0));  break;
            case  PISTI: playAnimation5(gui.getAnimationView(0));  break;
            case  SHOW_ROUND_SCORE: playAnimation6(gui.getAnimationView(0));  break;
            case  SHOW_HIDDEN_CARDS: playAnimation7(gui.getAnimationView(0));  break;
            case SHOW_GAME_SCORE: playAnimation8(gui.getAnimationView(0)); break;
            case PLAYER1_PISTI: playAnimation9(gui.getAnimationView(0)); break;
            case PLAYER2_PISTI: playAnimation10(gui.getAnimationView(0)); break;
        }
    }
}

