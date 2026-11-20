package ch.sakru.pisti.gui;

import ch.sakru.cards.Card;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PistiArtworkTest {
    @Test
    public void resourcesDependOnFaceNotCardId() {
        assertEquals("c00", PistiArtwork.drawableName(new Card(700, "A", "♠")));
        assertEquals("c08", PistiArtwork.drawableName(new Card(8, "9", "♠")));
        assertEquals("c35", PistiArtwork.drawableName(new Card(35, "T", "♦")));
        assertEquals("c51", PistiArtwork.drawableName(new Card(51, "K", "♣")));
    }
}
