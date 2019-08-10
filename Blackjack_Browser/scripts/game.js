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

    currentPlayerHandIndex = 0;

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

        this.currentPlayerHandIndex = 0;

        this.clear();

        this.table.reset();

        this.updateStatsGUI();

        //this.table.playerHand.betAmount = this.gui.inBet.value;
        this.table.setBet(this.gui.inBet.value);
        this.enableBetting(false);

        // Main loop
        this.table.deal();
        this.updateGUIInitial();
        this.updateGui();

        this.selectHandGui(0);

        let pHand = this.getPlayerHand();
        if (this.getPlayerHand().blackjack || this.table.dealerHand.blackjack) {
            pHand.possiblePlays.splice(0, pHand.possiblePlays.length);
            this.updateGui();
            this.playDealerHand();
            setTimeout(() => this.calculateScore(), 1000);
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
        this.drawCard(this.gui.divPlayerHandArray[0], this.getPlayerHand().cards[0]);
        this.drawCard(this.gui.divPlayerHandArray[0], this.getPlayerHand().cards[1]);
    }

    hideAllButtons() {
        this.gui.bHit.style.display = "none";
        this.gui.bStand.style.display = "none";
        this.gui.bDoubleDown.style.display = "none";
        this.gui.bSplit.style.display = "none";
    }
    
    updateGui() {

        
        let pHand = this.getPlayerHand();
    
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
                case Play.SPLIT: g.bSplit.style.display = "inline"; break;
            }
        });

        // TODO: Maybe make nicer??
        if (pHand.possiblePlays.length == 0) {
            this.deselectHandGui(this.currentPlayerHandIndex);
        }
    }

    selectHandGui(index) {
        this.gui.divPlayerHandArray[index].className += " currentHand";
    }
    deselectHandGui(index) {
        this.gui.divPlayerHandArray[index].className = this.gui.divPlayerHandArray[index].className.replace(" currentHand", "");
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

        let dHand = this.table.dealerHand;
        let pHands = this.table.playerHands;

        pHands.forEach(function(pHand, i) {
            if (pHand.blackjack && dHand.blackjack) {
                this.statsPush(pHand, i);
            } else if (dHand.blackjack) {
                this.statsLose(pHand, i);
            } else if (pHand.blackjack) {
                this.statsWin(pHand, i);
            } else {
                if (pHand.bust || (!dHand.bust && dHand.value > pHand.value)) {
                    this.statsLose(pHand, i);
                } else if (dHand.bust || pHand.value > dHand.value) {
                    this.statsWin(pHand, i);
                } else if (dHand.value == pHand.value) {
                    this.statsPush(pHand, i);
                } else {
                    alert("Scoring error!");
                    console.log("Scoring error!");
                }
            }
        }.bind(this));

        

        this.updateStatsGUI();
        this.enableBetting(true);
        
    }

    statsWin(hand, i) {
        if (hand.blackjack) {
            this.drawScoreText(this.gui.divPlayerHandArray[i], "BLACKJACK");
            this.chips += hand.betAmount * 3.0/2.0;
        } else {
            this.drawScoreText(this.gui.divPlayerHandArray[i], "WIN");
            this.chips += Number(hand.betAmount);
        }
        this.wins++;
    }
    statsLose(hand, i) {
        if (this.table.dealerHand.blackjack) {
            this.drawScoreText(this.gui.divDealerHand, "BLACKJACK");
        } 
        this.drawScoreText(this.gui.divPlayerHandArray[i], "LOSS");
        this.losses++;
        this.chips -= hand.betAmount;
    }
    statsPush(hand, i) {
        this.drawScoreText(this.gui.divPlayerHandArray[i], "PUSH");
        this.pushes++; 
    }

    drawScoreText(div, text) {
        let scoreText = document.createElement("span");
        scoreText.className = "scoreText";
        scoreText.innerHTML = text;
        div.appendChild(scoreText);
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
                case Play.HIT: this.table.hit(hand); this.handleHitGui(hand); break;
                case Play.STAND: this.table.stand(hand); break;
                case Play.DOUBLE_DOWN: this.table.doubleDown(hand); this.drawCard(this.gui.divPlayerHandArray[this.currentPlayerHandIndex], hand.cards[hand.cards.length-1]); break;
                case Play.SPLIT: this.table.split(hand); this.handleSplitGui(); break;
            }

            this.updateGui();

            
            let pHand = this.getPlayerHand();
            // Check if no more moves: iterate through hands until you find one with valid moves or play dealer.
            if (pHand.possiblePlays.length == 0) {
                while (pHand != null && pHand.possiblePlays.length == 0) {
                    // Increase index
                    this.currentPlayerHandIndex++;
                    pHand = this.getPlayerHand();
                    // If player doesn't have any more hands, play dealer
                    // Otherwise update GUI with increased value if currentPlayerHandIndex
                    
                }
                if (this.table.playerHands.length <= this.currentPlayerHandIndex) {
                    this.playDealerHand();
                    setTimeout(() => this.calculateScore(), 1000);
                } else {
                    this.selectHandGui(this.currentPlayerHandIndex);
                    console.log("QWEQWE");
                    this.updateGui();
                }
            }

            

            


        } else {
            alert("Invalid play!");
        }
    }

    handleHitGui(hand) {
        this.drawCard(this.gui.divPlayerHandArray[this.currentPlayerHandIndex], hand.cards[hand.cards.length-1]);
    }

    clear() {
        this.hideAllButtons();
        this.gui.divPlayerHands.innerHTML = "<div id=\"divPlayerHand0\" class=\"cardSpace\"></div>";
        this.gui.divPlayerHandArray[0] = document.getElementById("divPlayerHand0");
        this.gui.divDealerHand.innerHTML = "<img id=\"imgDealerFaceDownCard\" style=\"display: none;\" src=\"images/cards/blue_back.png\" class=\"card\" />";

        this.gui.statWins.innerHTML = "0";
        this.gui.statLosses.innerHTML = "0";
        this.gui.statPushes.innerHTML = "0";
        this.gui.statChips.innerHTML = "0";

        this.gui.tbPlayerStatus.value = "";
        this.gui.tbDealerStatus.value = "";
    }

    clearPlayerHands() {
        this.gui.divPlayerHandArray.forEach(function(el) {
            if (el != null ) {
                el.innerHTML = "";
            }
        });
    }


    // Useful: https://stackoverflow.com/questions/24383519/why-do-i-lose-reference-to-an-element
    generateHand(id) {
        let idString = "divPlayerHand" + id;
        // Append div container
        let divNewHand = document.createElement("div");
        divNewHand.className = "cardSpace";
        divNewHand.id = idString;
        this.gui.divPlayerHands.appendChild(divNewHand);
        // Save it's reference
        this.gui.divPlayerHandArray[id] = document.getElementById(idString);
    }

    handleSplitGui() {
        // Generate new hand
        this.generateHand(this.table.playerHands.length-1);

        // Get references to both hands
        let splitHand1 = this.gui.divPlayerHandArray[this.currentPlayerHandIndex];
        let splitHand2 = this.gui.divPlayerHandArray[this.table.playerHands.length-1];

        let cardsHand1 = this.table.playerHands[this.currentPlayerHandIndex].cards;
        let cardsHand2 = this.table.playerHands[this.table.playerHands.length-1].cards;

        // Clear old hand
        splitHand1.innerHTML = "";

        // Draw cards to both hands ("update")
        this.drawCard(splitHand1, cardsHand1[0]);
        this.drawCard(splitHand1, cardsHand1[1]);
        this.drawCard(splitHand2, cardsHand2[0]);
        this.drawCard(splitHand2, cardsHand2[1]);

    }

    getPlayerHand() {
        if (this.currentPlayerHandIndex < this.table.playerHands.length) {
            return this.table.playerHands[Math.min(this.currentPlayerHandIndex, this.table.playerHands.length-1)];
        } else {
            return null;
        }
    }

    get table() {
        return this.table;
    }
    get currentPlayerHandIndex() {
        return this.currentPlayerHandIndex;
    }
}