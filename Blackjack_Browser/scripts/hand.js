class Hand {

    name = ""; // string: name of hand (i.e.: Player)

    cards = []; // Array of cards in hand
    value = 0; // int: value of hand
    ace = 0; // int: count of aces in hand that count 11

    same = false; // boolean: same cards? split flag
    hasDoubleDown = false; // boolean: flag if player has doubled down
    blackjack = false; // boolean: does player have blackjack?

    betAmount = 0; // float: current bet


    fromSplit = false; // boolean: for blackjack check
    enableSplit = true; // boolean: split flag

    bust = false; // boolean: bust flag

    possiblePlays = []; // list of possible plays (type Plays)
    rules = null; // type Rule: current ruleset

    constructor(name="", rules = null) {
        this.name = name;
        this.cards = [];
        this.possiblePlays = [];
        this.possiblePlays.push(Play.HIT);
        this.possiblePlays.push(Play.STAND);
        this.possiblePlays.push(Play.DOUBLE_DOWN);
        this.enableSplit = true;
        this.rules = rules;
    }

    add(card) {
        this.cards.push(card);

        if (card.value == Value.ACE) {
            this.ace++;
        }
        if (this.cards.length == 2 && CardValue(this.cards[0].value) == CardValue(this.cards[1].value)) {
            this.same = true;
            // If cards are the same and splitting is allowed, add SPLIT as possible play.
            if (this.enableSplit) {
                this.possiblePlays.push(Play.SPLIT);
            }
        } else {
            this.same = false;
            let index = this.possiblePlays.indexOf
            remove(this.possiblePlays, Play.SPLIT);
        }

        // Remove DOUBLE_DOWN play
        if (this.cards.length > 2) {
            remove(this.possiblePlays, Play.DOUBLE_DOWN);
        }
        // If doubling after split is not allowed and splitting not allowed (because we already split once)
        /* TODO
        if (!rules.DOUBLE_AFTER_SPLIT && !this.enableSplit) {
            this.possiblePlays.remove(Plays.DOUBLE_DOWN);
        }
        */

        this.value += CardValue(card.value);

        if (this.value > 21 && this.ace > 0) {
            this.value -= 10;
            this.ace--;
            if (this.value == 21) {
                // We use enableSplit to check if this is the original hand -- is this case even possible??? TODO
                if (this.cards.length == 2 && !this.fromSplit) {
                    this.blackjack = true;
                }
                // Clear array
                this.possiblePlays.splice(0, this.possiblePlays.length);
            }
        } else if (this.value > 21) {
            this.bust = true;
            // Clear array
            this.possiblePlays.splice(0, this.possiblePlays.length);
        } else if (this.value === 21) {
            if (this.cards.length == 2 && !this.fromSplit) {
                this.blackjack = true;
            }
            // Clear array
            this.possiblePlays.splice(0, this.possiblePlays.length);
        }

         // If we split aces, we only get one additional card per hand
         /* TODO
         if (!rules.HIT_SPLIT_ACES && this.cards.size() == 2 && !enableSplit && cards.get(0).getValue() == Value.ACE) {
            this.possiblePlays.clear();
        }
         */
    }

    toValueString() {
        if (this.same && this.cards[0] == Value.ACE) {
            return "A-A";
        }
        if (this.same) {
            return (this.value/2) + "-" + (this.value/2);
        }
        if (this.ace > 0) {
            return "A-" + (this.value-11);
        }
        return String(this.value);
    };

    toString() {
        let s = "";
        if (this.bust) {
            s += " BUST | ";
        }
        if (this.blackjack) {
            s += " BLACKJACK | ";
        }
        s += this.toValueString() + " | ";

        this.cards.forEach(function(el) {
            s += el.toString() + ", ";
        });
        if (s.length < 3) {
            return "EMPTY";
        }
        return s.substring(0, s.length-2);
    };

    doubleDown(card) {
        this.add(card);
        this.hasDoubleDown = true;
        this.betAmount *= 2;
        // Clear array
        this.possiblePlays.splice(0, this.possiblePlays.length);
    };

    stand() {
        // Clear array
        this.possiblePlays.splice(0, this.possiblePlays.length);
    }


    get cards() {
        return this.cards;
    }

    set enableSplit(enableSplit) {
        this.enableSplit = enableSplit;
    }

    set betAmount(betAmount) {
        this.betAmount = betAmount;
    }
    set fromSplit(fromSplit) {
        this.fromSplit = fromSplit;
    }
    get betAmount() {
        return this.betAmount;
    }
    get name() {
        return this.name;
    }
    get possiblePlays() {
        return this.possiblePlays;
    }
    get bust() {
        return this.bust;
    }
    get betAmount() {
        return this.betAmount;
    }
    get blackjack() {
        return this.blackjack;
    }

}