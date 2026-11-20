package ch.sakru.pisti.game;

import android.content.Context;
import android.widget.ImageView;

import ch.sakru.cards.Card;
import ch.sakru.cards.CardSet;
import ch.sakru.cards.Deck;
import ch.sakru.pisti.R;
import ch.sakru.pisti.gui.Animation;
import ch.sakru.pisti.gui.Gui;
import ch.sakru.pisti.gui.PistiBoard;
import ch.sakru.pisti.player.Machine;
import ch.sakru.pisti.player.Player;
import ch.sakru.pisti.player.Table;
import ch.sakru.pisti.settings.PistiSettings;

/** Coordinates rounds and turns; the board renders them and the animation queue presents them. */
public final class PistiGameController {
    private final Game game = new Game();
    private final Deck deck = new Deck(CardSet.french52());
    private final Player human = new Player();
    private final Machine machine = new Machine();
    private final Table table;
    private final Gui animations = new Gui();
    private final PistiBoard board;
    private boolean machineStartsRound;
    private String score;

    public PistiGameController(Context context, PistiBoard board) {
        this.board = board;
        table = new Table(context.getString(R.string.table));
        game.setStrPisti(context.getString(R.string.app_name));
        game.setStrTotalPoints(context.getString(R.string.score_total_points));
        game.setStrTitlePoints(context.getString(R.string.score_title_points));
        game.setStrThreePoint(context.getString(R.string.three_point_representation));
        game.setStrComma(context.getString(R.string.str_comma));
        game.setStrDoublePoint(context.getString(R.string.str_double_point));
        game.setStrCRLF(context.getString(R.string.str_CRLF));
        game.addPlayer(human);
        game.addPlayer(machine);
        game.setDeck(deck);
    }

    public void applySettings(PistiSettings settings) {
        human.setName(settings.getPlayerName());
        machine.setName(settings.getPlayerName());
        game.setPointsToReach(settings.getPointsToReach());
    }

    public void newGame() {
        machineStartsRound = false;
        startRound();
    }

    public void nextRound() {
        machineStartsRound = !machineStartsRound;
        startRound();
    }

    private void startRound() {
        game.setPlayerMadeLastTrick(null);
        game.setTrickCount(0);
        deck.createCards();
        board.prepareDeck(deck);
        deck.shuffleDeck();
        game.dealCards(table);
        table.buildHiddenCardsString();
        table.setTopCardOnTable(table.getTheTopCard());
        board.showTableCard(table.getTheTopCardsId(), deck);
        board.showHiddenCard();
        game.addCardToMemoryOfPlayers(table.getTheTopCard());
        board.showMachineMemory(machine);
        if (deal() && machineStartsRound) {
            animations.addAnimation(Animation.OPENING_CARD_PAUSE, null);
        }
    }

    public boolean deal() {
        if (canDeal()) {
            game.dealCards(human);
            game.dealCards(machine);
            board.showHands(human, machine, deck);
            return true;
        }
        return false;
    }

    public boolean canDeal() {
        return !game.finished() && game.handsEmpty();
    }

    public boolean canPlayHumanCard(ImageView view) {
        int index = board.humanView().getCardNrFromView(view);
        return !game.finished() && human.getHand().get(index) != null;
    }

    public boolean playHumanCard(ImageView view) {
        int index = board.humanView().getCardNrFromView(view);
        Card card = human.play(index, table.getTopCardOnTable());
        if (card == null) {
            return false;
        }
        while (animations.getAnimationSize() > 0) {
            animations.removeFirstAnimation();
        }
        animations.addAnimation(Animation.PLAYER1_PLAY, view);
        play(card, human);
        // When the machine leads, its fourth card was already played.
        if (!game.finished() && !machine.isEmptyHand()) {
            queueMachineTurn();
        }
        return true;
    }

    public void queueMachineTurn() {
        Card card = machine.playMachine(table.getTopCardOnTable());
        if (card == null) {
            throw new IllegalStateException("Machine has no card for its turn");
        }
        animations.addAnimation(Animation.PLAYER2_PLAY, null);
        play(card, machine);
    }

    private void play(Card card, Player player) {
        table.takeCard(card);
        game.addCardToMemoryOfPlayers(card);
        board.showMachineMemory(machine);
        table.setTopCardOnTable(card);

        if (!game.isTableClean(table)) {
            if (game.isItAPisti(table)) {
                player.incCountOfPisti();
                player.addToWinner(table);
                player.addPoints(10);
                game.setPlayerMadeLastTrick(player);
                animations.addAnimation(player == human ? Animation.PLAYER1_PISTI : Animation.PLAYER2_PISTI, null);
            } else if (game.isItATrick(table)) {
                animations.addAnimation(player == human ? Animation.PLAYER1_TRICK : Animation.PLAYER2_TRICK, null);
                if (game.getTrickCount() == 0) {
                    game.setTrickCount(1);
                    if (player == human) {
                        animations.addAnimation(Animation.SHOW_HIDDEN_CARDS, null);
                    }
                }
                player.addToWinner(table);
                game.setPlayerMadeLastTrick(player);
            }
        }
        if (game.finished()) {
            finishRound();
        }
    }

    private void finishRound() {
        game.setTrickCount(1);
        if (game.getPlayerMadeLastTrick() != null) {
            animations.addAnimation(game.getPlayerMadeLastTrick() == human
                    ? Animation.PLAYER1_TRICK : Animation.PLAYER2_TRICK, null);
        }
        game.cleanUp(table);
        human.calcCardPoints();
        machine.calcCardPoints();
        game.setTrickCount(0);
        score = game.getScoreString();
        if (game.maxPointsReached()) {
            animations.addAnimation(Animation.SHOW_GAME_SCORE, null);
            game.cleanMemories();
            table.cleanMemories();
            game.cleanPoints();
        } else {
            animations.addAnimation(Animation.SHOW_ROUND_SCORE, null);
            game.cleanMemories();
            table.cleanMemories();
        }
    }

    public boolean machineStartsRound() {
        return machineStartsRound;
    }

    public String getScore() {
        return score;
    }

    public String getHiddenCards() {
        return table.getHiddenCardsString();
    }

    public Deck getDeck() {
        return deck;
    }

    public Player getHuman() {
        return human;
    }

    public Machine getMachine() {
        return machine;
    }

    public Gui getAnimations() {
        return animations;
    }
}
