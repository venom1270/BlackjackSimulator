class Table {

    deck = []; // Array of Cards
    currentCard = 0; // int
    numberOfDecks = 0; // int: number of decks at this table

    dealerHand = null; // Hand: dealer
    //playerHand = null; // Hand: player // TODO: add multiple

    playerSplit = false; // boolean
    playerHands = []; // Array of Hands if player splits

    rules = null; // Rules

    constructor(numberOfDecks, rules=null) {
        this.numberOfDecks = numberOfDecks;
        this.rules = rules;
        this.initializeDeck();
        this.reset();
    }

    initializeDeck() {
        this.deck = [];
        this.currentCard = 0;

        let suits = ["CLUBS", "HEARTS", "SPADES", "DIAMONDS"];
        let values = ["ACE", "TWO", "THREE", "FOUR", "FIVE", "SIX", "SEVEN", "EIGHT", "NINE", "TEN", "JACK", "QUEEN", "KING"];

        for (let i = 0; i < this.numberOfDecks; i++) {
            for (let s = 0; s < 4; s++) {
                for (let v = 0; v < 13; v++) {
                    this.deck.push(new Card(Value[values[v]], Suit[suits[s]]));
                }
            }
        }
    }

    reset() {
        this.currentCard = 0;
        this.dealerHand = new Hand(name="Dealer");
        this.playerHands = [];
        this.playerHands.push(new Hand(name="Player"));
        this.playerSplit = false;
        this.shuffleDeck();
    }

    shuffleDeck() {
        shuffle(this.deck);
    }

    getCard() {
        return this.deck[this.currentCard++];
    }

    deal() {
        this.playerHands[0].add(this.getCard());
        //this.playerHands[0].add(new Card(Value.TEN, Suit.SPADES));
        this.dealerHand.add(this.getCard());
        this.playerHands[0].add(this.getCard());
        //this.playerHands[0].add(new Card(Value.TEN, Suit.SPADES));
        this.dealerHand.add(this.getCard());
    }

    seeDealerCard() {
        if (this.dealerHand != null && this.dealerHand.lenght >= 2) {
            return this.dealerHand[1];
        } else {
            console.log("WARNING -- see delaer card fail");
            return null;
        }
    }

    // TODO: maybe we don't need this?
    hit(hand) {
        hand.add(this.getCard());
    }
    stand(hand) {
        hand.stand();
    }
    doubleDown(hand) {
        console.log(hand);
        hand.doubleDown(this.getCard());
    }
    split(hand) {
        this.playerSplit = true;
        let hand1 = new Hand();
        hand1.add(hand.cards[0]);
        //hand1.enableSplit = false;
        hand1.fromSplit = true;
        let hand2 = new Hand();
        hand2.add(hand.cards[1]);
        //hand2.enableSplit = false;
        hand2.fromSplit = true;

        hand1.add(this.getCard());
        hand2.add(this.getCard());

        hand1.betAmount = hand.betAmount;
        hand2.betAmount = hand.betAmount;

        //remove(this.playerHands, hand);
        let i = this.playerHands.indexOf(hand);
        this.playerHands[i] = hand1;
        //this.playerHands.splice(i+1, 0, hand2); // This would be nicer (split hands together) but adding to the back of array will suffice too.
        this.playerHands.push(hand2);
    }

    setBet(bet) {
        this.playerHands[0].betAmount = bet;
    }

    get playerHands() {
        return this.playerHands;
    }
    get dealerHand() {
        return this.dealerHand;
    }

}