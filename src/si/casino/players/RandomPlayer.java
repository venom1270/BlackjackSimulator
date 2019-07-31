package si.casino.players;

import si.casino.Hand;
import si.casino.card.Card;
import si.casino.plays.Plays;

import java.util.List;
import java.util.Random;

public class RandomPlayer implements Player {

    private Random random;

    public RandomPlayer() {
        random = new Random();
    }

    @Override
    public Plays makePlay(Hand playerHand, Card faceUpCard) {
        List<Plays> possiblePlays = playerHand.getPossiblePlays();
        return possiblePlays.get(random.nextInt(possiblePlays.size()));
    }

    @Override
    public int getBet(int previousGame, int previousBet) {
        return random.nextInt(1000)+1;
    }

}
