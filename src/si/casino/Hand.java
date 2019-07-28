package si.casino;

import si.casino.card.Card;
import si.casino.card.CardValue;
import si.casino.card.Value;
import si.casino.plays.Plays;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Hand {

    private List<Card> cards;
    private int value;
    private int ace;
    private boolean same;
    private boolean doubleDown;
    private boolean blackjack;

    private boolean bust;

    private Set<Plays> possiblePlays;

    public Hand() {
        this.cards = new ArrayList<>();
        this.possiblePlays = new HashSet<>();
        this.possiblePlays.add(Plays.HIT);
        this.possiblePlays.add(Plays.STAND);
        this.possiblePlays.add(Plays.DOUBLE_DOWN);
    }

    public void add(Card card) {
        this.cards.add(card);

        /*
        *
        * Če as in ni prej as: nastavi as = true;
        * Če je to druga karta in ista kot prva = same = true;
        *
        * Vedno povečaj value:
        *   prištej max. vrednost
        *   če bust in as, odštej 10
        *
        * */
        if (card.getValue() == Value.ACE) {
            this.ace++;
        }
        if (this.cards.size() == 2 && this.cards.get(0).getValue() == this.cards.get(1).getValue()) {
            same = true;
            possiblePlays.add(Plays.SPLIT);
        } else {
            same = false;
            this.possiblePlays.remove(Plays.SPLIT);
        }

        if (this.cards.size() > 2) {
            this.possiblePlays.remove(Plays.DOUBLE_DOWN);
        }

        this.value += CardValue.getValue(card);

        if (this.value > 21 && this.ace > 0) {
            this.value -= 10;
            this.ace--;
        } else if (this.value > 21) {
            bust = true;
            this.possiblePlays.clear();
        } else if (this.value == 21) {
            this.blackjack = true;
            this.possiblePlays.clear();
        }

    }

    @Override
    public String toString() {
        if (this.same && this.cards.get(0).getValue() == Value.ACE) {
            return "A-A (12)";
        }
        if (this.same) {
            return (this.value/2) + "-" + (this.value/2) + " (" + this.value + ")";
        }
        if (this.ace > 0) {
            return "A-" + (this.value-11) + " (" + this.value + ")";
        }
        return String.valueOf(this.value);
    }

    public String toStringVerbose() {
        String hand = "";

        if (this.bust) {
            hand += " BUST | ";
        }

        if (this.blackjack) {
            hand += " BLACKJACK | ";
        }

        hand += toString() + " | ";

        for (Card c : this.cards) {
            hand += c.toString() + ", ";
        }
        if (hand.length() < 3) {
            return "EMPTY";
        }
        return hand.substring(0, hand.length()-2);
    }

    public void doubleDown(Card card) {
        this.add(card);
        this.doubleDown = true;
        possiblePlays.clear();
    }

    public void stand() {
        possiblePlays.clear();
    }

    public int getSize() {
        return this.cards.size();
    }

    public List<Card> getCards() {
        return this.cards;
    }

    public boolean isBust() {
        return bust;
    }

    public Set<Plays> getPossiblePlays() {
        return possiblePlays;
    }

    public int getValue() {
        return this.value;
    }

    public boolean isBlackjack() {
        return blackjack;
    }

    public void setBlackjack(boolean blackjack) {
        this.blackjack = blackjack;
    }
}
