package si.casino.players;

import si.casino.Hand;
import si.casino.InputManager;
import si.casino.card.Card;
import si.casino.card.Value;
import si.casino.plays.Plays;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StrategyPlayer implements Player {

    List<String> strategyLines;
    Map<KeyPair, Plays> playStrategy;

    List<String> bettingLines;
    Map<Integer, Float> bettingStrategy;

    private final boolean DEBUG = false;

    public class KeyPair {
        private final Value dealer;
        private final String player;

        public KeyPair(String player, Value dealer) {
            this.dealer = dealer;
            this.player = player;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof KeyPair)) return false;
            KeyPair key = (KeyPair) o;
            return dealer == key.dealer && player.equals(key.player);
        }

        @Override
        public int hashCode() {
            String hash = player + dealer.toString();
            return hash.hashCode();
        }
    }

    public StrategyPlayer(String playStrategyPath, String bettingStrategyPath) {
        strategyLines = InputManager.readLines(playStrategyPath);
        playStrategy = null;
        bettingLines = InputManager.readLines(bettingStrategyPath);
        parsePlayStrategy();
        parseBettingStrategy();
    }

    @Override
    public Plays makePlay(Hand playerHand, Card faceUpCard) {
        if (playStrategy != null) {
            if (DEBUG)
                System.out.println(playerHand.toString());
            Plays chosenPlay = playStrategy.get(new KeyPair(playerHand.toString(), faceUpCard.getValue()));
            if (!playerHand.getPossiblePlays().contains(chosenPlay)) {
                // Handle plays taht are not possible anymore
                // 1. Double down after hitting
                // 2. Split after splitting

                if (chosenPlay == Plays.SPLIT) {
                    if (DEBUG)
                        System.out.println(chosenPlay);
                    chosenPlay = playStrategy.get(new KeyPair(String.valueOf(playerHand.getValue()), faceUpCard.getValue()));
                }

                if (chosenPlay == Plays.DOUBLE_DOWN_OR_HIT) {
                    chosenPlay = Plays.HIT;
                } else if (chosenPlay == Plays.DOUBLE_DOWN_OR_STAND) {
                    chosenPlay = Plays.STAND;
                }
            }
            return chosenPlay;
        } else {
            return null;
        }
    }

    @Override
    public int getBet(int previousGame, int previousBet) {
        float betFactor = bettingStrategy.get(previousGame);
        if (betFactor == -1.0f) {
            // Reset to initial bet
            return 1;
        } else {
            return (int)(previousBet * betFactor);
        }
    }

    private void parsePlayStrategy() {

        // TODO mybe: check header and parse accordingly

        playStrategy = new HashMap<>();

        strategyLines.remove(0);
        for (String line : strategyLines) {
            String[] data = line.split(";");

            int from = 0;
            int to = 0;
            boolean multiple = false;

            if (data[0].endsWith("+")) {
                from = Integer.parseInt(data[0].substring(0, data[0].length()-1));
                to = 20;
                multiple = true;
            } else if (data[0].endsWith("-")) {
                from = 2;
                to = Integer.parseInt(data[0].substring(0, data[0].length()-1));
                multiple = true;
            }

            for (int i = from; i <= to; i++) {
                String value = data[0];
                if (multiple) {
                    value = String.valueOf(i);
                }
                playStrategy.put(new KeyPair(value, Value.ACE), stringToPlays(data[1]));
                playStrategy.put(new KeyPair(value, Value.KING), stringToPlays(data[2]));
                playStrategy.put(new KeyPair(value, Value.QUEEN), stringToPlays(data[2]));
                playStrategy.put(new KeyPair(value, Value.JACK), stringToPlays(data[2]));
                playStrategy.put(new KeyPair(value, Value.TEN), stringToPlays(data[2]));
                playStrategy.put(new KeyPair(value, Value.NINE), stringToPlays(data[3]));
                playStrategy.put(new KeyPair(value, Value.EIGHT), stringToPlays(data[4]));
                playStrategy.put(new KeyPair(value, Value.SEVEN), stringToPlays(data[5]));
                playStrategy.put(new KeyPair(value, Value.SIX), stringToPlays(data[6]));
                playStrategy.put(new KeyPair(value, Value.FIVE), stringToPlays(data[7]));
                playStrategy.put(new KeyPair(value, Value.FOUR), stringToPlays(data[8]));
                playStrategy.put(new KeyPair(value, Value.THREE), stringToPlays(data[9]));
                playStrategy.put(new KeyPair(value, Value.TWO), stringToPlays(data[10]));
            }

        }

    }

    private void parseBettingStrategy() {
        bettingStrategy = new HashMap<>();

        // TODO: read header?? mybe
        bettingLines.remove(0);
        for (String line : bettingLines) {
            String[] data = line.split(";");

            // W=1, L=-1, P=0, DW=2, DL=-2
            bettingStrategy.put(1, stringToBet(data[1]));
            bettingStrategy.put(-1, stringToBet(data[2]));
            bettingStrategy.put(0, stringToBet(data[3]));
            bettingStrategy.put(2, stringToBet(data[4]));
            bettingStrategy.put(-2, stringToBet(data[5]));
        }
    }

    private float stringToBet(String bet) {
        if (bet.equals("R")) {
            return -1.0f;
        } else {
            return Float.valueOf(bet);
        }
    }

    private Plays stringToPlays(String play) {
        switch (play) {
            case "H": return Plays.HIT;
            case "D": return Plays.DOUBLE_DOWN;
            case "Dh": return Plays.DOUBLE_DOWN_OR_HIT;
            case "Ds": return Plays.DOUBLE_DOWN_OR_STAND;
            case "S": return Plays.STAND;
            case "SP": return Plays.SPLIT;
            default:
                System.out.println("ERROR");
                return Plays.STAND;
        }
    }
}
