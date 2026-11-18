package ch.sakru.pisti.player;

import ch.sakru.pisti.deck.Card;
import ch.sakru.pisti.game.Game;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;

public class MachineTest {
    private static Card card(int number, String name) {
        Card card = new Card(number);
        card.setName(name);
        return card;
    }

    @Test
    public void visibleCardsInfluenceChoiceWithoutCountingTheSameCardTwice() {
        Game game = new Game();
        Machine machine = new Machine();
        game.addPlayer(new Player());
        game.addPlayer(machine);

        Card two = card(1, "2");
        Card three = card(2, "3");
        machine.takeCard(two);
        machine.takeCard(three);
        machine.takeCard(card(3, "4"));
        machine.takeCard(card(4, "J"));

        // The machine already knows its own two. Observing it again must not
        // outweigh two different threes played by the human.
        game.addCardToMemoryOfPlayers(two);
        game.addCardToMemoryOfPlayers(two);
        game.addCardToMemoryOfPlayers(card(15, new String("3")));
        game.addCardToMemoryOfPlayers(card(28, "3"));

        assertSame(three, machine.playMachine(null));
    }

    @Test
    public void knowledgeFromPreviousDeckDoesNotAffectNextRound() {
        Game game = new Game();
        Machine machine = new Machine();
        game.addPlayer(machine);
        machine.takeCard(card(1, "2"));
        machine.takeCard(card(2, "3"));
        game.addCardToMemoryOfPlayers(card(14, "2"));
        game.addCardToMemoryOfPlayers(card(27, "2"));

        game.cleanMemories();

        Card three = card(2, "3");
        machine.takeCard(three);
        machine.takeCard(card(1, "2"));
        assertSame(three, machine.playMachine(null));
    }

    @Test
    public void matchingRankWorksForDifferentStringInstances() {
        Machine machine = new Machine();
        Card match = card(7, new String("7"));
        machine.takeCard(card(1, "2"));
        machine.takeCard(match);
        machine.takeCard(card(3, "4"));
        machine.takeCard(card(10, "J"));

        assertSame(match, machine.playMachine(card(20, new String("7"))));
        assertEquals(Integer.valueOf(1), machine.getPlayedCardNrInHand());
    }

    @Test
    public void keepsJackOnEmptyTableWhileOtherCardRemains() {
        Machine machine = new Machine();
        machine.takeCard(card(10, new String("J")));
        Card five = card(5, "5");
        machine.takeCard(five);

        assertSame(five, machine.playMachine(null));
    }

    @Test
    public void computerNameDiffersFromHumanName() {
        Machine machine = new Machine();
        machine.setName(new String("Senol"));

        assertNotEquals("Senol", machine.getName());
    }
}
