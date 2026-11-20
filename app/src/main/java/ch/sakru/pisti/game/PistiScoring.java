package ch.sakru.pisti.game;

import ch.sakru.cards.Card;

/** Pisti-specific card points; cards-core has no knowledge of game scores. */
public final class PistiScoring {
    private PistiScoring() {
    }

    public static int pointsFor(Card card) {
        if ("♦".equals(card.getSuit()) && "T".equals(card.getRank())) {
            return 3;
        }
        if ("♣".equals(card.getSuit()) && "2".equals(card.getRank())) {
            return 2;
        }
        if ("A".equals(card.getRank()) || "J".equals(card.getRank())) {
            return 1;
        }
        return 0;
    }
}
