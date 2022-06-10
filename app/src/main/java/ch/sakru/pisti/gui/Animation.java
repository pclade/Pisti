package ch.sakru.pisti.gui;

public enum Animation {
    PLAYER1_PLAY,
    PLAYER2_PLAY,
    PLAYER1_TRICK,
    PLAYER2_TRICK,
    PISTI,
    SHOW_ROUND_SCORE,
    SHOW_GAME_SCORE,
    SHOW_HIDDEN_CARDS,
    PLAYER1_PISTI,
    PLAYER2_PISTI
}

//  1: Player 1 Plays card to Table
//  2: Player 2 Plays card to Table
//  3: Player 1 makes a trick
//  4: Player 2 makes a trick
//  5: Player 1 or 2 makes a ch.sakru.pisti (two animations, trick (3 or 4) and (5) for ch.sakru.pisti
//  6: Show score after a round (game is not yet finished). Only CONTINUE is possible
//  7: Game Finished, Show Dialog with points and wait for a "NEW GAME" or "EXIT"
//  8: It's the first trick in round and it's player1, then show hidden cards to him.
//  9: Player 1 Pisti animation
// 10: Player 2 Pisti animation
