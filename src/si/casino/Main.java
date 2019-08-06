package si.casino;

import si.casino.card.Card;
import si.casino.card.Suit;
import si.casino.card.Value;
import si.casino.gui.BlackjackForm;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        Game game = new Game();
        game.run();

        //BlackjackForm form = new BlackjackForm();
        //form.run();


        /*
        //THREE OF HEARTS, TWO OF SPADES, ACE OF HEARTS, FOUR OF CLUBS, ACE OF DIAMONDS
        Hand h = new Hand();

        h.add(new Card(Value.THREE, Suit.HEARTS));
        System.out.println(h.toStringVerbose());
        System.out.println(h.isBlackjack());

        h.add(new Card(Value.TWO, Suit.HEARTS));
        System.out.println(h.toStringVerbose());
        System.out.println(h.isBlackjack());

        h.add(new Card(Value.ACE, Suit.HEARTS));
        System.out.println(h.toStringVerbose());
        System.out.println(h.isBlackjack());

        h.add(new Card(Value.FOUR, Suit.HEARTS));
        System.out.println(h.toStringVerbose());
        System.out.println(h.isBlackjack());

        h.add(new Card(Value.ACE, Suit.HEARTS));
        System.out.println(h.toStringVerbose());
        System.out.println(h.isBlackjack());

        */
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
