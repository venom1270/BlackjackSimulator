class Game {
    
    table = null; // Table

    // Stats
    wins = 0; // int
    losses = 0; // int
    pushes = 0; // int
    gamesPlayed = 0; // int
    splits = 0; // int

    player = null // Player - human or simulator
    chips = 0.0; // double: number of chips ("money")

    rules = null; // rules
    gui = null;

    constructor() {

    }

    run (numOfDecks=1, gui) {

        this.gui = gui;
        this.chips = 1000.0;
        this.table = new Table(numOfDecks);

        this.gui.inBet.disabled = false;
        this.gui.bBet.disabled = false;
        
        this.enableBetting(true);

    }

    newRound() {

        if (this.gui.inBet.value <= 0 || this.chips < this.gui.inBet.value) {
            alert("Invalid bet! Check if you have enough chips.");
            return;
        }

        this.clear();

        this.table.reset();

        this.updateStatsGUI();

        this.table.playerHand.betAmount = this.gui.inBet.value;
        this.enableBetting(false);

        // Main loop
        this.table.deal();
        this.updateGUIInitial();
        this.updateGui();

        if (this.table.playerHand.blackjack || this.table.dealerHand.blackjack) {
            this.table.playerHand.possiblePlays.splice(0, this.table.playerHand.possiblePlays.length);
            this.updateGui();
            this.playDealerHand();
            this.calculateScore();
        }
    }

    enableBetting(b) {
        this.gui.inBet.disabled = !b;
        this.gui.bBet.disabled = !b;
    }

    hideAllButtons() {
        this.gui.bHit.style.display = "none";
        this.gui.bStand.style.display = "none";
        this.gui.bDoubleDown.style.display = "none";
        this.gui.bSplit.style.display = "none";
    }

    showDealerFaceDownCard(show) {
        this.gui.imgDealerFaceDownCard = document.getElementById("imgDealerFaceDownCard");
        // Not sure why this method only works for the first time... ?
        console.log(this.gui.imgDealerFaceDownCard == document.getElementById("imgDealerFaceDownCard"));
        if (show) {
            this.gui.imgDealerFaceDownCard.style.display = "inline";
        } else {
            this.gui.imgDealerFaceDownCard.style.display = "none";
        }
    }

    drawCard(div, card) {
        div.innerHTML += "<img src=\"" + card.image + "\" class=\"card\" />";
    }

    updateGUIInitial() {
        this.showDealerFaceDownCard(true);
        this.drawCard(this.gui.divDealerHand, this.table.dealerHand.cards[1]);
        this.drawCard(this.gui.divPlayerHand0, this.table.playerHand.cards[0]);
        this.drawCard(this.gui.divPlayerHand0, this.table.playerHand.cards[1]);
    }

    hideAllButtons() {
        this.gui.bHit.style.display = "none";
        this.gui.bStand.style.display = "none";
        this.gui.bDoubleDown.style.display = "none";
        this.gui.bSplit.style.display = "none";
    }
    
    updateGui() {
        
        // TODO: temporary: get correct player hand
        let pHand = this.table.playerHand;
        if (this.table.splitHands != null && this.table.splitHands.length > 0) {
            pHand = this.table.splitHands[0];
        }
    
        // Update output status
        this.gui.tbPlayerStatus.value = pHand.toString();

        let g = this.gui;
    
        // Enable buttons based on possible plays
        this.hideAllButtons();
        pHand.possiblePlays.forEach(function(el) {
            switch (el) {
                case Play.HIT: g.bHit.style.display = "inline"; break;
                case Play.STAND: g.bStand.style.display = "inline"; break;
                case Play.DOUBLE_DOWN: g.bDoubleDown.style.display = "inline"; break;
                //case Play.SPLIT: g.bSplit.style.display = "inline"; break; // TODO SPLIT
            }
        });
    }

    playDealerHand() {

        let dHand = this.table.dealerHand;
        //this.showDealerFaceDownCard(false);
        this.gui.divDealerHand.innerHTML = "";
        this.drawCard(this.gui.divDealerHand, dHand.cards[0]);
        this.drawCard(this.gui.divDealerHand, dHand.cards[1]);
        this.gui.tbDealerStatus.value = dHand.toString();
        while(dHand.value < 17) {
            this.table.hit(dHand);
            this.drawCard(this.gui.divDealerHand, dHand.cards[dHand.cards.length-1]);
            this.gui.tbDealerStatus.value = dHand.toString();
        }
        this.table.stand(dHand);

    }

    calculateScore() {
        let pHand = this.table.playerHand;
        let dHand = this.table.dealerHand;

        if (pHand.blackjack && dHand.blackjack) {
            this.statsPush();
        } else if (dHand.blackjack) {
            this.statsLose();
        } else if (pHand.blackjack) {
            this.statsWin();
        } else {
            if (pHand.bust || (!dHand.bust && dHand.value > pHand.value)) {
                this.statsLose();
            } else if (dHand.bust || pHand.value > dHand.value) {
                this.statsWin();
            } else if (dHand.value == pHand.value) {
                this.statsPush();
            } else {
                alert("Scoring error!");
                console.log("Scoring error!");
            }
        }

        this.updateStatsGUI();
        this.enableBetting(true);
        
    }

    statsWin() {
        let pHand = this.table.playerHand;
        if (pHand.blackjack) {
            alert("You won (BLACKJACK)!");
            this.chips += pHand.betAmount * 3.0/2.0;
        } else {
            alert("You won!");
            this.chips += Number(pHand.betAmount);
        }
        this.wins++;
    }
    statsLose() {
        let pHand = this.table.playerHand;
        if (this.table.dealerHand.blackjack) {
            alert("You lost (DEALER BLACKJACK).");
        } else {
            alert("You lost.");
        }
        this.losses++;
        this.chips -= pHand.betAmount;
    }
    statsPush() {
        alert("Push!");
        this.pushes++; 
    }

    updateStatsGUI() {
        this.gui.statWins.innerHTML = this.wins;
        this.gui.statLosses.innerHTML = this.losses;
        this.gui.statPushes.innerHTML = this.pushes;
        this.gui.statChips.innerHTML = this.chips;
    }

    makePlay(play, hand) {

        if (hand.possiblePlays.includes(play)) {

            switch (play) {
                case Play.HIT: this.table.hit(hand); this.drawCard(this.gui.divPlayerHand0, hand.cards[hand.cards.length-1]); break;
                case Play.STAND: this.table.stand(hand); break;
                case Play.DOUBLE_DOWN: this.table.doubleDown(hand); this.drawCard(this.gui.divPlayerHand0, hand.cards[hand.cards.length-1]); break;
                case Play.SPLIT: this.table.split(hand); break; // TODO draw
            }

            this.updateGui();

            // When player finished play dealer
            if (hand.possiblePlays.length == 0) {
                this.playDealerHand();
                //setTimeout(this.calculateScore, 1000);
                this.calculateScore();
            }


        } else {
            alert("Invalid play!");
        }
    }

    clear() {
        this.hideAllButtons();
        this.gui.divPlayerHands.innerHTML = "<div id=\"divPlayerHand0\" class=\"cardSpace\"></div>";
        this.gui.divPlayerHand0 = document.getElementById("divPlayerHand0");
        this.gui.divDealerHand.innerHTML = "<img id=\"imgDealerFaceDownCard\" style=\"display: none;\" src=\"images/cards/blue_back.png\" class=\"card\" />";

        this.gui.statWins.innerHTML = "0";
        this.gui.statLosses.innerHTML = "0";
        this.gui.statPushes.innerHTML = "0";
        this.gui.statChips.innerHTML = "0";

        this.gui.tbPlayerStatus.value = "";
        this.gui.tbDealerStatus.value = "";
    }

    get table() {
        return this.table;
    }
}