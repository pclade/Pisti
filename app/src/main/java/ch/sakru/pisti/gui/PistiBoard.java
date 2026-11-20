package ch.sakru.pisti.gui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import ch.sakru.cards.Deck;
import ch.sakru.pisti.BuildConfig;
import ch.sakru.pisti.R;
import ch.sakru.pisti.player.Machine;
import ch.sakru.pisti.player.Player;

/** Binds the game board to the existing card and table renderers. */
public final class PistiBoard {
    private final Context context;
    private final TextView machineMemoryDebug;
    private final GuiDeck deckView = new GuiDeck();
    private final GuiTable tableView = new GuiTable();
    private final GuiPlayer humanView = new GuiPlayer();
    private final GuiPlayer machineView = new GuiPlayer();

    @SuppressLint("UseCompatLoadingForDrawables")
    public PistiBoard(Activity activity, View.OnClickListener humanCardClick) {
        context = activity.getApplicationContext();
        machineMemoryDebug = activity.findViewById(R.id.machine_memory_debug);
        if (BuildConfig.DEBUG) {
            machineMemoryDebug.setVisibility(View.VISIBLE);
        }

        RelativeLayout root = activity.findViewById(R.id.root_layout_id);
        root.setBackground(activity.getResources().getDrawable(R.drawable.wood_background));
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        float padding = root.getPaddingLeft() * 2;
        int cardWidth = (int) ((metrics.widthPixels - padding) / 4.0f + 0.5f);

        ImageView settings = activity.findViewById(R.id.iv_settings);
        settings.requestLayout();
        ImageView help = activity.findViewById(R.id.iv_help);
        help.requestLayout();
        help.setY(settings.getY() + settings.getHeight() * metrics.density);

        ArrayList<ImageView> humanCards = new ArrayList<>();
        ArrayList<ImageView> machineCards = new ArrayList<>();
        int[] humanIds = {R.id.iv_card_0, R.id.iv_card_1, R.id.iv_card_2, R.id.iv_card_3};
        int[] machineIds = {R.id.iv_card_back_0, R.id.iv_card_back_1,
                R.id.iv_card_back_2, R.id.iv_card_back_3};
        for (int id : humanIds) {
            ImageView card = activity.findViewById(id);
            sizeCard(card, cardWidth);
            card.setOnClickListener(humanCardClick);
            humanCards.add(card);
        }
        for (int id : machineIds) {
            ImageView card = activity.findViewById(id);
            sizeCard(card, cardWidth);
            machineCards.add(card);
        }

        humanView.createGuiCard();
        humanView.setImageViewFromResources(humanCards);
        machineView.createGuiCard();
        machineView.setHideCards(true);
        machineView.setImageViewFromResources(machineCards);

        tableView.setContext(context);
        tableView.createView();
        ImageView tableCard = activity.findViewById(R.id.iv_table_0);
        sizeCard(tableCard, cardWidth);
        tableView.setImageTable(tableCard);
        ImageView hiddenCard = activity.findViewById(R.id.iv_table_h1);
        hiddenCard.requestLayout();
        hiddenCard.getLayoutParams().width = cardWidth;
        tableView.setImageHiddenCard(hiddenCard);
    }

    private void sizeCard(ImageView card, int width) {
        card.requestLayout();
        card.getLayoutParams().width = width;
        card.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
    }

    public void prepareDeck(Deck deck) {
        deckView.createCards(context, deck);
    }

    public void showHands(Player human, Machine machine, Deck deck) {
        humanView.showCards(human.getHand(), deckView, deck);
        machineView.showCards(machine.getHand(), deckView, deck);
    }

    public void showTableCard(int cardId, Deck deck) {
        tableView.showCard(cardId, deckView, deck);
    }

    public void showHiddenCard() {
        tableView.ShowHiddenCard();
    }

    public void showMachineMemory(Machine machine) {
        if (BuildConfig.DEBUG) {
            machineMemoryDebug.setText(machine.getMemorySummary());
        }
    }

    public GuiTable tableView() {
        return tableView;
    }

    public GuiPlayer humanView() {
        return humanView;
    }

    public GuiPlayer machineView() {
        return machineView;
    }

    public GuiDeck deckView() {
        return deckView;
    }
}
