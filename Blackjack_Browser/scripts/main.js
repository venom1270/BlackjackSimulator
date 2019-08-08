document.onload = function() {
    test();
}

var game = null;

function initGui() {
    var gui = {}
    gui.bHit = document.getElementById("bHit");
    gui.bStand = document.getElementById("bStand");
    gui.bDoubleDown = document.getElementById("bDoubleDown");
    gui.bSplit = document.getElementById("bSplit");
    gui.tbPlayerStatus = document.getElementById("tbPlayerStatus");
    gui.tbDealerStatus = document.getElementById("tbDealerStatus");
    gui.imgDealerFaceDownCard = document.getElementById("imgDealerFaceDownCard")
    gui.divDealerHand = document.getElementById("divDealerHand");
    gui.divPlayerHands = document.getElementById("divPlayerHands");
    gui.divPlayerHand0 = document.getElementById("divPlayerHand0");
    gui.statWins = document.getElementById("statWins");
    gui.statLosses = document.getElementById("statLosses");
    gui.statPushes = document.getElementById("statPushes");
    gui.statChips = document.getElementById("statChips");
    gui.inBet = document.getElementById("inBet");
    gui.bBet = document.getElementById("bBet");
    return gui;
}

function start() {

    if (game != null) {
        game.clear();
    }
    
    var numOfDecks = document.getElementById("inNumberOfDecks").value;
    console.log(numOfDecks);

    if (numOfDecks >= 1 && numOfDecks <= 8) {
        game = new Game();
        game.run(numOfDecks, initGui());
    } else {
        alert("Number of decks must be between 1 and 8.");
    }


}

function newRound() {
    if (game != null) {
        game.newRound();
    }
}

function hit() {
    game.makePlay(Play.HIT, game.table.playerHand);
    //checkTable();
}
function stand() {
    game.makePlay(Play.STAND, game.table.playerHand);
    //checkTable();
}
function doubleDown() {
    game.makePlay(Play.DOUBLE_DOWN, game.table.playerHand);
    //checkTable();
}
function split() {
    game.makePlay(Play.SPLIT, game.table.playerHand);
    //checkTable();
}