const Value = {
    ACE: "ACE",
    TWO: "TWO",
    THREE: "THREE",
    FOUR: "FOUR",
    FIVE: "FIVE",
    SIX: "SIX",
    SEVEN: "SEVEN",
    EIGHT: "EIGHT",
    NINE: "NINE",
    TEN: "TEN",
    JACK: "JACK",
    QUEEN: "QUEEN",
    KING: "KING"
}

const Suit = {
    CLUBS: "CLUBS",
    HEARTS: "HEARTS",
    SPADES: "SPADES",
    DIAMONDS: "DIAMONDS"
}

function CardValue(value) {
    switch(value) {
        case Value.ACE:
            return 11;
        case Value.KING: case Value.QUEEN: case Value.JACK: case Value.TEN:
            return 10;
        case Value.NINE:
            return 9;
        case Value.EIGHT:
            return 8;
        case Value.SEVEN:
            return 7;
        case Value.SIX:
            return 6;
        case Value.FIVE:
            return 5;
        case Value.FOUR:
            return 4;
        case Value.THREE:
            return 3;
        case Value.TWO:
            return 2;
    }
}

class Card {
    
    constructor(value, suit) {
        this.value = value;
        this.suit = suit;

        // Concatentate image path
        let imagePath = "images/cards/";
        if ([Value.ACE, Value.KING, Value.QUEEN, Value.JACK].includes(value)) {
            imagePath += value.charAt(0);
        } else {
            imagePath += CardValue(value);
        }
        imagePath += suit.charAt(0) +  ".png";
        this.image = imagePath;
    }

    toString() {
        return this.value + " OF " + this.suit;
    }

}