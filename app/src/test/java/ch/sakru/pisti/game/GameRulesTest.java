package ch.sakru.pisti.game;

import ch.sakru.cards.Card;
import ch.sakru.cards.Deck;
import ch.sakru.pisti.player.Table;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GameRulesTest {
    private static Card card(int number, String name, String suite) {
        Card card = new Card(number);
        card.setName(name);
        card.setSuite(suite);
        return card;
    }

    @Test
    public void twoEqualRanksMakePistiEvenWithDifferentStringInstances() {
        Game game = new Game();
        Table table = new Table("table");
        table.takeCard(card(1, new String("5"), "♠"));
        table.takeCard(card(14, new String("5"), "♥"));

        assertTrue(game.isItAPisti(table));
    }

    @Test
    public void jackTakesTrickEvenWithDynamicallyCreatedRank() {
        Game game = new Game();
        game.setDeck(new Deck());
        Table table = new Table("table");
        table.takeCard(card(1, "5", "♠"));
        table.takeCard(card(10, new String("J"), "♣"));

        assertFalse(game.isItAPisti(table));
        assertTrue(game.isItATrick(table));
    }

    @Test
    public void specialCardsScoreWithDynamicallyCreatedNamesAndSuits() {
        Deck deck = new Deck();
        deck.createCards();
        Card diamondTen = deck.getCardDeck().get(35);
        diamondTen.setSuite(new String("♦"));
        diamondTen.setName(new String("T"));
        Card clubTwo = deck.getCardDeck().get(40);
        clubTwo.setSuite(new String("♣"));
        clubTwo.setName(new String("2"));

        Game game = new Game();
        game.setDeck(deck);
        game.setCardsPoints();

        assertEquals(Integer.valueOf(3), diamondTen.getPoint());
        assertEquals(Integer.valueOf(2), clubTwo.getPoint());
    }
}
