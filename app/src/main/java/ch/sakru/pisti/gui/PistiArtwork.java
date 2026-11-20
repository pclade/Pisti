package ch.sakru.pisti.gui;

import ch.sakru.cards.Card;

/** Maps French card faces to Pisti's historical c00..c51 Android resources. */
final class PistiArtwork {
    private static final String[] SUITS = {"♠", "♥", "♦", "♣"};
    private static final String[] RANKS = {
            "A", "2", "3", "4", "5", "6", "7", "8", "9", "T", "J", "Q", "K"
    };

    private PistiArtwork() {
    }

    static String drawableName(Card card) {
        for (int suit = 0; suit < SUITS.length; suit++) {
            if (SUITS[suit].equals(card.getSuit())) {
                for (int rank = 0; rank < RANKS.length; rank++) {
                    if (RANKS[rank].equals(card.getRank())) {
                        int imageNumber = suit * RANKS.length + rank;
                        return imageNumber < 10 ? "c0" + imageNumber : "c" + imageNumber;
                    }
                }
            }
        }
        throw new IllegalArgumentException("No Pisti image for " + card.getSuit() + card.getRank());
    }
}
