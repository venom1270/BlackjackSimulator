document.onload = function() {
    test();
}


table = null;
bHit = null;
bStand = null;
bDoubleDown = null;
bSplit = null;
tbStatus = null;

function init() {
    bHit = document.getElementById("bHit");
    bStand = document.getElementById("bStand");
    bDoubleDown = document.getElementById("bDoubleDown");
    bSplit = document.getElementById("bSplit");
    tbStatus = document.getElementById("tbStatus");
    hideAllButtons();
}

function start() {
    card = new Card(Value.FIVE, Suit.HEARTS);
    console.log(card);
    console.log(card.image);

    var h = new Hand();
    h.add(new Card(Value.FIVE, Suit.DIAMONDS));
    h.add(new Card(Value.ACE, Suit.HEARTS));

    console.log(h.toString());
    console.log(h);

    table = new Table(4);
    table.deal();
    console.log(table);
    
    checkTable();

    /*
    console.log(table.playerHand.toString());
    console.log("HITTING...");
    table.hit(table.playerHand);
    console.log(table.playerHand.toString());
    */
}

function hideAllButtons() {
    bHit.style.display = "none";
    bStand.style.display = "none";
    bDoubleDown.style.display = "none";
    bSplit.style.display = "none";
}

function checkTable() {

    if (table == null) {
        alert("Game has not started yet!");
        return;
    }

    // TODO: temporary: get correct player hand
    let pHand = table.playerHand;
    if (table.splitHands != null && table.splitHands.length > 0) {
        pHand = table.splitHands[0];
    }

    // Update output status
    tbStatus.value = pHand.toString();

    // Enable buttons based on possible plays
    hideAllButtons();
    pHand.possiblePlays.forEach(function(el) {
        switch (el) {
            case Play.HIT: bHit.style.display = "inline"; break;
            case Play.STAND: bStand.style.display = "inline"; break;
            case Play.DOUBLE_DOWN: bDoubleDown.style.display = "inline"; break;
            case Play.SPLIT: bSplit.style.display = "inline"; break;
        }
    });
}

function hit() {
    table.hit(table.playerHand);
    checkTable();
}
function stand() {
    table.stand(table.playerHand);
    checkTable();
}
function doubleDown() {
    table.doubleDown(table.playerHand);
    checkTable();
}
function split() {
    table.split(table.playerHand);
    checkTable();
}