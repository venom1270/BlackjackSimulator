package si.casino.players;

import si.casino.Hand;
import si.casino.InputManager;
import si.casino.card.Card;
import si.casino.card.Value;
import si.casino.plays.Plays;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StrategyPlayer implements Player{

    List<String> strategyLines;
    Map<KeyPair, Plays> strategy;

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

    public StrategyPlayer(String path) {
        strategyLines = InputManager.readStrategy(path);
        strategy = null;
        parseStrategy();
    }

    @Override
    public Plays makePlay(Hand playerHand, Card faceUpCard) {
        if (strategy != null) {
            System.out.println(playerHand.toString());
            Plays chosenPlay = strategy.get(new KeyPair(playerHand.toString(), faceUpCard.getValue()));
            if (!playerHand.getPossiblePlays().contains(chosenPlay)) {
                // Handle plays taht are not possible anymore
                // 1. Double down after hitting
                // 2. Split after splitting
                if (chosenPlay == Plays.DOUBLE_DOWN) {
                    chosenPlay = Plays.HIT;
                } else if (chosenPlay == Plays.SPLIT) {
                    System.out.println(chosenPlay);
                    chosenPlay = strategy.get(new KeyPair(String.valueOf(playerHand.getValue()), faceUpCard.getValue()));
                }

            }
            return chosenPlay;
        } else {
            return null;
        }
    }

    private void parseStrategy() {

        // TODO mybe: check header and parse accordingly

        strategy = new HashMap<>();

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
                strategy.put(new KeyPair(value, Value.ACE), stringToPlays(data[1]));
                strategy.put(new KeyPair(value, Value.KING), stringToPlays(data[2]));
                strategy.put(new KeyPair(value, Value.QUEEN), stringToPlays(data[2]));
                strategy.put(new KeyPair(value, Value.JACK), stringToPlays(data[2]));
                strategy.put(new KeyPair(value, Value.TEN), stringToPlays(data[2]));
                strategy.put(new KeyPair(value, Value.NINE), stringToPlays(data[3]));
                strategy.put(new KeyPair(value, Value.EIGHT), stringToPlays(data[4]));
                strategy.put(new KeyPair(value, Value.SEVEN), stringToPlays(data[5]));
                strategy.put(new KeyPair(value, Value.SIX), stringToPlays(data[6]));
                strategy.put(new KeyPair(value, Value.FIVE), stringToPlays(data[7]));
                strategy.put(new KeyPair(value, Value.FOUR), stringToPlays(data[8]));
                strategy.put(new KeyPair(value, Value.THREE), stringToPlays(data[9]));
                strategy.put(new KeyPair(value, Value.TWO), stringToPlays(data[10]));
            }

        }

    }

    private Plays stringToPlays(String play) {
        switch (play) {
            case "H": return Plays.HIT;
            case "D": return Plays.DOUBLE_DOWN;
            case "S": return Plays.STAND;
            case "SP": return Plays.SPLIT;
            default:
                System.out.println("ERROR");
                return Plays.STAND;
        }
    }
}
