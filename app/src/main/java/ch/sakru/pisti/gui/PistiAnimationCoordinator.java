package ch.sakru.pisti.gui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import ch.sakru.cards.Card;
import ch.sakru.pisti.game.PistiGameController;

/** Serializes card animations, dialogs and the human/machine handoff. */
public final class PistiAnimationCoordinator {
    private static final long OPENING_CARD_PAUSE_MS = 1500L;

    private final AppCompatActivity activity;
    private final PistiGameController game;
    private final PistiBoard board;
    private final Gui queue;
    private boolean busy;
    private boolean machineLeadingNewHand;
    private ImageView pendingHumanCardView;
    private Runnable pendingOpeningMove;

    public PistiAnimationCoordinator(AppCompatActivity activity, PistiGameController game, PistiBoard board) {
        this.activity = activity;
        this.game = game;
        this.board = board;
        this.queue = game.getAnimations();
    }

    public void onHumanCardClicked(ImageView view) {
        // An empty queue means the human turn is ready, even if a transition left busy set.
        if (busy && queue.getAnimationSize() == 0 && pendingOpeningMove == null) {
            busy = false;
            machineLeadingNewHand = false;
            clearPendingHumanCard();
        }
        if (busy) {
            // The new hand is already visible while the machine leads it.
            if (machineLeadingNewHand && pendingOpeningMove == null
                    && pendingHumanCardView == null && game.canPlayHumanCard(view)) {
                pendingHumanCardView = view;
                pendingHumanCardView.setAlpha(0.65f);
            }
            return;
        }
        busy = true;
        if (!game.playHumanCard(view)) {
            busy = false;
            return;
        }
        playNextAnimation();
    }

    public void onRoundStarted() {
        clearPendingHumanCard();
        machineLeadingNewHand = game.machineStartsRound();
        busy = game.machineStartsRound();
    }

    public void destroy() {
        clearPendingHumanCard();
        if (pendingOpeningMove != null) {
            board.tableView().imageTable.removeCallbacks(pendingOpeningMove);
            pendingOpeningMove = null;
        }
    }

    private void clearPendingHumanCard() {
        if (pendingHumanCardView != null) {
            pendingHumanCardView.setAlpha(1f);
            pendingHumanCardView = null;
        }
    }

    private void completeAnimation() {
        queue.removeFirstAnimation();
        playNextAnimation();
    }

    private void animateHumanCard(ImageView view) {
        GuiPlayer humanView = board.humanView();
        humanView.initAnimation(view, board.tableView().imageTable);
        humanView.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                humanView.restoreCardPositions();
                int handIndex = humanView.getCardNrFromView(view);
                Card card = game.getHuman().getPlayedCard();
                board.showTableCard(card.getId(), game.getDeck());
                humanView.cardImages.get(handIndex).setImageResource(android.R.color.transparent);
                completeAnimation();
            }
        });
        humanView.doAnimation();
    }

    private void animateMachineCard() {
        GuiPlayer machineView = board.machineView();
        int handIndex = game.getMachine().getPlayedCardNrInHand();
        Card card = game.getMachine().getPlayedCard();
        machineView.showCard(handIndex, card.getId(), board.deckView(), game.getDeck());
        machineView.initAnimation(machineView.cardImages.get(handIndex), board.tableView().imageTable);
        machineView.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                machineView.restoreCardPositions();
                ImageView playedView = machineView.cardImages.get(game.getMachine().getPlayedCardNrInHand());
                board.showTableCard(game.getMachine().getPlayedCard().getId(), game.getDeck());
                playedView.setImageResource(android.R.color.transparent);
                completeAnimation();
            }
        });
        machineView.doAnimation();
    }

    private void animateTrick(boolean human) {
        GuiTable tableView = board.tableView();
        tableView.initTrickAnimation(human ? 1000 : -1000, 0);
        tableView.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                completeAnimation();
            }
        });
        tableView.doTrickAnimation();
    }

    private void animatePisti(boolean human) {
        GuiTable tableView = board.tableView();
        tableView.initPistiAnimation(human ? 1000 : -1000);
        tableView.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                completeAnimation();
            }
        });
        tableView.doPistiAnimation();
    }

    private void showHiddenCards() {
        new AlertDialog.Builder(activity)
                .setMessage(game.getHiddenCards()).setTitle("HIDDEN CARDS")
                .setCancelable(false)
                .setPositiveButton("CLOSE", (dialog, id) -> completeAnimation())
                .show();
    }

    private void showRoundScore() {
        new AlertDialog.Builder(activity)
                .setMessage(game.getScore()).setTitle("SCORE")
                .setCancelable(false)
                .setPositiveButton("CONTINUE", (dialog, id) -> {
                    queue.removeFirstAnimation();
                    game.nextRound();
                    onRoundStarted();
                    playNextAnimation();
                }).show();
    }

    private void showGameScore() {
        new AlertDialog.Builder(activity)
                .setMessage(game.getScore()).setTitle("SCORE")
                .setCancelable(false)
                .setPositiveButton("NEW GAME", (dialog, id) -> {
                    queue.removeFirstAnimation();
                    game.newGame();
                    onRoundStarted();
                    playNextAnimation();
                })
                .setNegativeButton("EXIT", (dialog, id) -> {
                    System.exit(0);
                    completeAnimation();
                }).show();
    }

    private void pauseForOpeningCard() {
        if (pendingOpeningMove != null) {
            return;
        }
        pendingOpeningMove = () -> {
            pendingOpeningMove = null;
            if (activity.isFinishing() || activity.isDestroyed()) {
                return;
            }
            queue.removeFirstAnimation();
            game.queueMachineTurn();
            playNextAnimation();
        };
        board.tableView().imageTable.postDelayed(pendingOpeningMove, OPENING_CARD_PAUSE_MS);
    }

    private void playNextAnimation() {
        if (queue.getAnimationSize() == 0) {
            // In a machine-led round it leads each new four-card hand.
            if (game.canDeal()) {
                clearPendingHumanCard();
                game.deal();
                if (game.machineStartsRound()) {
                    machineLeadingNewHand = true;
                    game.queueMachineTurn();
                    playNextAnimation();
                    return;
                }
            }
            busy = false;
            machineLeadingNewHand = false;
            ImageView queuedCard = pendingHumanCardView;
            clearPendingHumanCard();
            if (queuedCard != null) {
                queuedCard.performClick();
            }
            return;
        }
        switch (queue.getAnimationSequence(0)) {
            case PLAYER1_PLAY: animateHumanCard((ImageView) queue.getAnimationView(0)); break;
            case PLAYER2_PLAY: animateMachineCard(); break;
            case PLAYER1_TRICK: animateTrick(true); break;
            case PLAYER2_TRICK: animateTrick(false); break;
            case PLAYER1_PISTI: animatePisti(true); break;
            case PLAYER2_PISTI: animatePisti(false); break;
            case SHOW_HIDDEN_CARDS: showHiddenCards(); break;
            case SHOW_ROUND_SCORE: showRoundScore(); break;
            case SHOW_GAME_SCORE: showGameScore(); break;
            case OPENING_CARD_PAUSE: pauseForOpeningCard(); break;
        }
    }
}
