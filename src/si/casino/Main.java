package si.casino;

import si.casino.card.Card;
import si.casino.card.Suit;
import si.casino.card.Value;

public class Main {

    public static void main(String[] args) {

        Player player = new Player();
        player.run();


        /*
        int h = 0, c = 0, s = 0, d = 0;
        for (int i = 0; i < 10000000; i++) {
            game.reset();
            switch (game.getCard().getSuit()) {
                case CLUBS: c++; break;
                case HEARTS: h++; break;
                case SPADES: s++; break;
                case DIAMONDS: d++; break;
                default:
                    System.out.println("WTF?");
            }
        }

        System.out.printf("%d\n%d\n%d\n%d\n", h, c, s, d);

        */

        /*

        Hand testHand = new Hand();
        testHand.add(new Card(Value.ACE, Suit.HEARTS));
        testHand.add(new Card(Value.TWO, Suit.SPADES));
        testHand.add(new Card(Value.ACE, Suit.SPADES));
        testHand.add(new Card(Value.ACE, Suit.SPADES));
        testHand.add(new Card(Value.KING, Suit.SPADES));
        System.out.println(testHand);

        */

    }
}
