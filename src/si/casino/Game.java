package si.casino;

import si.casino.card.Card;
import si.casino.card.Suit;
import si.casino.card.Value;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game {

    private List<Card> deck;
    private int currentCard;
    private int numberOfDecks;

    private Hand dealerHand;
    private Hand playerHand;

    private boolean playerSplit;
    private List<Hand> splitHands;

    public Game(int numberOfDecks) {
        this.numberOfDecks = numberOfDecks;
        initializeDeck();
        reset();
    }

    private void initializeDeck() {
        deck = new ArrayList<>();
        this.currentCard = 0;

        for (int i = 0; i < this.numberOfDecks; i++) {
            for (int s = 0; s < 4; s++) {
                for (int v = 0; v < 13; v++) {
                    this.deck.add(new Card(Value.values()[v], Suit.values()[s]));
                }
            }
        }
    }

    private void shuffleDeck() {
        Collections.shuffle(deck);
    }

    public void reset() {
        this.currentCard = 0;
        this.dealerHand = new Hand();
        this.playerHand = new Hand();
        this.playerSplit = false;
        this.splitHands = new ArrayList<>();
        shuffleDeck();
    }

    public Card getCard() {
        return deck.get(this.currentCard++);
    }

    public void deal() {
        playerHand.add(getCard());
        dealerHand.add(getCard());
        playerHand.add(getCard());
        dealerHand.add(getCard());
    }

    public Card seeDealerCard() {
        if (this.dealerHand != null && this.dealerHand.getSize() >= 2) {
            return dealerHand.getCards().get(1);
        } else {
            return null;
        }
    }

    public Hand getPlayerHand() {
        return this.playerHand;
    }

    public Hand getDealerHand() {
        return this.dealerHand;
    }

    public void hit(Hand hand) {
        hand.add(this.getCard());
    }

    public void stand(Hand hand) {
        hand.stand();
    }

    public void doubleDown(Hand hand) {
        hand.doubleDown(this.getCard());
    }

    public void split(Hand hand) {

        // TODO: support for more than 1 split?

        this.playerSplit = true;
        Hand hand1 = new Hand();
        hand1.add(hand.getCards().get(0));
        Hand hand2 = new Hand();
        hand2.add(hand.getCards().get(1));

        hand1.add(getCard());
        hand2.add(getCard());

        this.splitHands.add(hand1);
        this.splitHands.add(hand2);
    }

    public boolean isPlayerSplit() {
        return playerSplit;
    }

    public List<Hand> getSplitHands() {
        return splitHands;
    }
}
