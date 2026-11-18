package ch.sakru.cards;

import org.junit.Test;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CardSetTest {
    @Test
    public void standardDeckKeepsPistiCardFacesAndIds() {
        Deck deck = new Deck(CardSet.french52());
        deck.createCards();

        assertEquals(Integer.valueOf(52), deck.getDecSize());
        assertFace(deck.getCardDeck().get(0), 0, "A", "♠");
        assertFace(deck.getCardDeck().get(35), 35, "T", "♦");
        assertFace(deck.getCardDeck().get(51), 51, "K", "♣");

        deck.createCards();
        assertEquals(52, deck.getCardDeck().size());
    }

    @Test
    public void twentyFourCardSelectionRetainsTheOriginalImageIds() {
        CardSet sixtySixCards = CardSet.french52().onlyRanks("9", "J", "Q", "K", "T", "A");
        Deck deck = new Deck(sixtySixCards);
        deck.createCards();

        assertEquals(Integer.valueOf(24), deck.getDecSize());
        Set<Integer> ids = new HashSet<>();
        for (Card card : deck.getCardDeck()) {
            assertTrue(ids.add(card.getCardNumber()));
        }
        assertFace(deck.getCardDeck().get(0), 0, "A", "♠");
        assertFace(deck.getCardDeck().get(1), 8, "9", "♠");
        assertFace(deck.getCardDeck().get(6), 13, "A", "♥");
        assertFace(deck.getCardDeck().get(23), 51, "K", "♣");

        for (int i = 0; i < 24; i++) {
            deck.dealTopCardOnDec();
        }
        assertTrue(deck.isEmpty());
        deck.createCards();
        assertEquals(24, deck.getCardDeck().size());
    }

    @Test
    public void shuffleCanBeReproducedForSimulation() {
        Deck first = new Deck();
        Deck second = new Deck();
        first.createCards();
        second.createCards();
        first.shuffleDeck(new Random(1234));
        second.shuffleDeck(new Random(1234));

        for (int i = 0; i < first.getDecSize(); i++) {
            assertEquals(first.dealTopCardOnDec().getCardNumber(),
                    second.dealTopCardOnDec().getCardNumber());
        }
    }

    private static void assertFace(Card card, int id, String rank, String suit) {
        assertEquals(Integer.valueOf(id), card.getCardNumber());
        assertEquals(rank, card.getName());
        assertEquals(suit, card.getSuite());
    }
}
