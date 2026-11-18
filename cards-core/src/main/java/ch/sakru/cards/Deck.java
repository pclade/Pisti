package ch.sakru.cards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/** A shuffled, drawable deck assembled from a reusable card set. */
public class Deck {
    private final CardSet cardSet;
    private final ArrayList<Card> cardDeck = new ArrayList<>();

    public Deck() {
        this(CardSet.french52());
    }

    public Deck(CardSet cardSet) {
        if (cardSet == null) {
            throw new IllegalArgumentException("Card set is required");
        }
        this.cardSet = cardSet;
    }

    /** Creates a new deck only when the old deck has been exhausted. */
    public void createCards() {
        if (isEmpty()) {
            cardDeck.addAll(cardSet.newCards());
        }
    }

    public void shuffleDeck() {
        shuffleDeck(new Random());
    }

    /** Explicit randomness makes complete games reproducible in tests and simulations. */
    public void shuffleDeck(Random random) {
        Collections.shuffle(cardDeck, random);
    }

    public ArrayList<Card> getCardDeck() {
        return cardDeck;
    }

    public Card dealTopCardOnDec() {
        if (isEmpty()) {
            return null;
        }
        return cardDeck.remove(cardDeck.size() - 1);
    }

    public boolean isEmpty() {
        return cardDeck.isEmpty();
    }

    /** Number of faces in the card set, even after dealing. */
    public Integer getDecSize() {
        return cardSet.size();
    }
}
