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

    private int betAmount;

    private boolean enableSplit; // To limit to only one split

    private boolean bust;

    private List<Plays> possiblePlays;
    private Rules rules;

    public Hand(Rules rules) {
        this.cards = new ArrayList<>();
        this.possiblePlays = new ArrayList<>();
        this.possiblePlays.add(Plays.HIT);
        this.possiblePlays.add(Plays.STAND);
        this.possiblePlays.add(Plays.DOUBLE_DOWN);
        this.enableSplit = true;
        this.rules = rules;
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
        if (this.cards.size() == 2 && CardValue.getValue(this.cards.get(0)) == CardValue.getValue(this.cards.get(1))) {
            same = true;
            if (enableSplit) {
                // Only enable splitting if enableSplit is true - this is used to limit only one split per round
                possiblePlays.add(Plays.SPLIT);
            }
        } else {
            same = false;
            this.possiblePlays.remove(Plays.SPLIT);
        }

        // Remove double down play
        if (this.cards.size() > 2) {
            this.possiblePlays.remove(Plays.DOUBLE_DOWN);
        }
        // If doubling after split is not allowed and splitting not allowed (because we already split once)
        if (!rules.DOUBLE_AFTER_SPLIT && !this.enableSplit) {
            this.possiblePlays.remove(Plays.DOUBLE_DOWN);
        }


        this.value += CardValue.getValue(card);

        if (this.value > 21 && this.ace > 0) {
            this.value -= 10;
            this.ace--;
            if (this.value == 21) {
                // We use enableSplit to check if this is the original hand
                if (this.cards.size() == 2 && this.enableSplit) {
                    this.blackjack = true;
                }
                this.possiblePlays.clear();
            }
        } else if (this.value > 21) {
            bust = true;
            this.possiblePlays.clear();
        } else if (this.value == 21) {
            if (this.cards.size() == 2 && this.enableSplit) {
                this.blackjack = true;
            }
            this.possiblePlays.clear();
        }

        // If we split aces, we only get one additional card per hand
        if (!rules.HIT_SPLIT_ACES && this.cards.size() == 2 && !enableSplit && cards.get(0).getValue() == Value.ACE) {
            this.possiblePlays.clear();
        }

    }

    @Override
    public String toString() {
        if (this.same && this.cards.get(0).getValue() == Value.ACE) {
            return "A-A";
        }
        if (this.same) {
            return (this.value/2) + "-" + (this.value/2); //+ " (" + this.value + ")";
        }
        if (this.ace > 0) {
            return "A-" + (this.value-11); //+ " (" + this.value + ")";
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
        this.betAmount *= 2;
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

    public List<Plays> getPossiblePlays() {
        return possiblePlays;
    }

    public int getValue() {
        return this.value;
    }

    public boolean isBlackjack() {
        return blackjack;
    }

    public boolean isEnableSplit() {
        return enableSplit;
    }

    public void setEnableSplit(boolean enableSplit) {
        this.enableSplit = enableSplit;
    }

    public int getBetAmount() {
        return betAmount;
    }

    public void setBetAmount(int betAmount) {
        this.betAmount = betAmount;
    }

    public boolean isDoubleDown() {
        return doubleDown;
    }
}
