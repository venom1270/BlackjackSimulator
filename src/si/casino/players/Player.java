package si.casino.players;

import si.casino.Hand;
import si.casino.card.Card;
import si.casino.plays.Plays;

public interface Player {

    Plays makePlay(Hand playerHand, Card faceUpCard);

    int getBet(int previousGame, int previusBet);

}
