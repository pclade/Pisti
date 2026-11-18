package ch.sakru.cards;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** A reusable selection of card faces and stable card IDs, independent of game rules. */
public final class CardSet {
    private static final List<String> FRENCH_SUITS = Arrays.asList("♠", "♥", "♦", "♣");
    private static final List<String> FRENCH_RANKS = Arrays.asList(
            "A", "2", "3", "4", "5", "6", "7", "8", "9", "T", "J", "Q", "K");

    private final List<CardFace> faces;

    private CardSet(List<CardFace> faces) {
        this.faces = Collections.unmodifiableList(new ArrayList<>(faces));
    }

    /** The card IDs match the existing c00 ... c51 image resources. */
    public static CardSet french52() {
        List<CardFace> faces = new ArrayList<>();
        int id = 0;
        for (String suit : FRENCH_SUITS) {
            for (String rank : FRENCH_RANKS) {
                faces.add(new CardFace(id++, rank, suit));
            }
        }
        return new CardSet(faces);
    }

    /** Retain the original IDs when selecting ranks, so existing images still match. */
    public CardSet onlyRanks(String... ranks) {
        if (ranks == null || ranks.length == 0) {
            throw new IllegalArgumentException("Select at least one rank");
        }
        Set<String> selected = new HashSet<>(Arrays.asList(ranks));
        if (selected.size() != ranks.length) {
            throw new IllegalArgumentException("Duplicate rank");
        }
        List<CardFace> chosen = new ArrayList<>();
        Set<String> found = new HashSet<>();
        for (CardFace face : faces) {
            if (selected.contains(face.rank)) {
                chosen.add(face);
                found.add(face.rank);
            }
        }
        if (!found.equals(selected)) {
            throw new IllegalArgumentException("Unknown rank in card set");
        }
        return new CardSet(chosen);
    }

    public int size() {
        return faces.size();
    }

    List<Card> newCards() {
        List<Card> cards = new ArrayList<>(faces.size());
        for (CardFace face : faces) {
            Card card = new Card(face.id);
            card.setName(face.rank);
            card.setSuite(face.suit);
            cards.add(card);
        }
        return cards;
    }

    private static final class CardFace {
        final int id;
        final String rank;
        final String suit;

        CardFace(int id, String rank, String suit) {
            this.id = id;
            this.rank = rank;
            this.suit = suit;
        }
    }
}
