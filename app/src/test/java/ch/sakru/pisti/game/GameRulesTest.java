package ch.sakru.pisti.game;

import ch.sakru.cards.Card;
import ch.sakru.cards.CardSet;
import ch.sakru.cards.Deck;
import ch.sakru.pisti.player.Player;
import ch.sakru.pisti.player.Table;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GameRulesTest {
    private static Card card(int number, String name, String suite) {
        return new Card(number, name, suite);
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
        Card diamondTen = card(35, new String("T"), new String("♦"));
        Card clubTwo = card(40, new String("2"), new String("♣"));
        Player winner = new Player();
        winner.getWinners().add(diamondTen);
        winner.getWinners().add(clubTwo);

        assertEquals(3, PistiScoring.pointsFor(diamondTen));
        assertEquals(2, PistiScoring.pointsFor(clubTwo));
        winner.calcCardPoints();
        assertEquals(Integer.valueOf(5), winner.getPoints());
    }

    @Test
    public void completeFrenchDeckRetainsThirteenPistiCardPoints() {
        Deck deck = new Deck(CardSet.french52());
        deck.createCards();
        int total = 0;
        for (Card card : deck.getCardDeck()) {
            total += PistiScoring.pointsFor(card);
        }
        assertEquals(13, total);
    }
}
