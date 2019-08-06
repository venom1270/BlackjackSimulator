package si.casino;

import si.casino.card.Card;
import si.casino.players.HumanPlayer;
import si.casino.players.Player;
import si.casino.players.RandomPlayer;
import si.casino.players.StrategyPlayer;
import si.casino.plays.Plays;

import java.util.ArrayList;
import java.util.List;


// TODO: BLACKJACK ONLY ON FFIRST TWO CARDS --DONE
//  TODO: SPLIT DOESN't COUNT --DONE
//   PLAYER AND DEALER BLACKJACK - PUSH --DONE

public class Game {

    private Table table;

    private long wins;
    private long losses;
    private long pushes;
    private long gamesPlayed;
    private long splits;

    Player player;
    private double chips;

    private final boolean DEBUG = false;
    private Rules rules;


    public Game() {
        //player = new HumanPlayer();
        //player = new RandomPlayer();
    }

    public void run() {

        rules = new Rules();

        long startTime = System.currentTimeMillis();

        final double STARTING_CHIPS = 1000;
        chips = STARTING_CHIPS;
        List<Integer> validInts = new ArrayList<>();
        for (int i = 1; i <= 6; i++) {
            validInts.add(i);
        }
        System.out.println("Choose number of decks: (1-6)");
        table = new Table(InputManager.getValidInputInt(validInts), rules);

        System.out.println("Choose player type: (1-Human, 2-Simulator_Random, 3-Simulator_Strategy)");
        validInts.clear();
        validInts.add(1);
        validInts.add(2);
        validInts.add(3);
        int playerType = InputManager.getValidInputInt(validInts);
        if (playerType == 1) {
            player = new HumanPlayer();
        } else if (playerType == 2) {
            player = new RandomPlayer();
        } else if (playerType == 3) {
            //player = new StrategyPlayer("strategies/baseline_strategy.csv");
            //String strategyFile = "baseline_strategy.csv";
            String strategyFile = "one_deck_no_ds.csv";
            player = new StrategyPlayer("strats/play/" + strategyFile, "strats/betting/" + strategyFile);
        }

        long numberOfGames = 0;
        System.out.println("Choose number of games: ");
        numberOfGames = InputManager.getValidInputLong(null);

        System.out.println("Starting with " + this.chips + " chips.");

        final String DELIMITER = "********************";

        int previousGameOutcome = 0;
        int previousBet = 1;

        for (long gameIndex = 0; gameIndex < numberOfGames; gameIndex++) {

            if (DEBUG)
                System.out.println(DELIMITER + " GAME " + (gameIndex+1) + " " + DELIMITER);
            table.reset();

            int currentBet = player.getBet(previousGameOutcome, previousBet);
            /*if (currentBet > 128) {
                currentBet = 128;
            }*/
            table.setBet(currentBet);
            previousBet = currentBet;
            if (DEBUG)
                System.out.println("Player bets " + currentBet);
            //this.chips -= currentBet;
            previousGameOutcome = 0;

            table.deal();
            if (DEBUG) {
                System.out.println("Dealer's face up card: " + table.seeDealerCard());
                System.out.println("Your hand: " + table.getPlayerHand().toStringVerbose());
            }
            if (!table.getDealerHand().isBlackjack()) {
                playPlayerHand();

                if (DEBUG)
                    System.out.println("Dealer's hand: " + table.getDealerHand().toStringVerbose());

                playDealerHand();

                if (DEBUG) {
                    System.out.println("Final dealer hand: " + table.getDealerHand().toStringVerbose());

                    System.out.println(DELIMITER);
                }


                // CALCULATE SCORE
                if (table.isPlayerSplit()) {
                    int handCount = 1;
                    if (DEBUG)
                        System.out.println("Split player hands: ");
                    for (Hand hand : table.getSplitHands()) {
                        if (DEBUG)
                            System.out.println("  Hand " + handCount + ": " + hand.toStringVerbose());
                        calculateScore(hand);
                        handCount++;
                    }
                } else {
                    if (DEBUG)
                        System.out.println("Your hand: " + table.getPlayerHand().toStringVerbose());
                    previousGameOutcome += calculateScore(table.getPlayerHand());
                }
            } else {
                if (DEBUG)
                    System.out.println("DEALER HAS BLACKJACK! " + table.getDealerHand().toStringVerbose());
                if (table.getPlayerHand().isBlackjack()) {
                    previousGameOutcome = 0;
                    if (DEBUG)
                        System.out.println("PUSH");
                    pushes++;
                } else {
                    previousGameOutcome = -1;
                    if (DEBUG)
                        System.out.println("YOU LOSE");
                    losses++;
                    this.chips -= table.getPlayerHand().getBetAmount();
                }
            }

            if (previousGameOutcome > 2) previousGameOutcome = 2;
            else if (previousGameOutcome < -2) previousGameOutcome = -2;

            gamesPlayed++;

            if (DEBUG) {

                System.out.println(DELIMITER);
                System.out.println("Wins: " + wins);
                System.out.println("Loses: " + losses);
                System.out.println("Pushes: " + pushes);
                System.out.println("Splits: " + splits);
                System.out.println("Games played: " + gamesPlayed);
                System.out.println("Chips: " + this.chips);
                System.out.println();

            }

            /*System.out.println("Keep playing? (y)");
            if (InputManager.getValidInput(null) != 'y') {
                keepPlaying = false;
            }*/

        }

        System.out.println(DELIMITER);
        System.out.println("Wins: " + wins);
        System.out.println("Loses: " + losses);
        System.out.println("Pushes: " + pushes);
        System.out.println("Splits: " + splits);
        System.out.println("Games played: " + gamesPlayed);
        System.out.println("Chips: " + this.chips);
        System.out.println();

        float winRate = (float) wins / (wins+losses+splits);
        float lossRate = (float) losses / (wins+losses+splits);
        System.out.println("Winrate: " + winRate*100 + "%");
        System.out.println("House winrate: " + lossRate*100 + "%");
        System.out.println("Average loss per game (in chips): " + (STARTING_CHIPS-chips)/(float)numberOfGames);
        System.out.println("The end!");

        long endTime = System.currentTimeMillis();
        System.out.println("Execution time: " + (endTime-startTime) / 1000.0 + " seconds.");



    }

    private void playPlayerHand() {

        Card faceUpCard = table.getDealerHand().getCards().get(1);

        while (table.getPlayerHand().getPossiblePlays().size() != 0) {

            playerMakeMove(table.getPlayerHand(), faceUpCard);

            if (table.isPlayerSplit()) {
                int handCount = 1;
                for (Hand hand : table.getSplitHands()) {
                    if (DEBUG)
                        System.out.println("HAND " + handCount + ": " + hand.toStringVerbose());
                    handCount++;
                }

                // Stop normal play iteration, go to split handling.
                break;
            } else {
                if (DEBUG)
                    System.out.println("Player's hand: " + table.getPlayerHand().toStringVerbose());
            }

        }

        if (table.isPlayerSplit()) {
            // Split handling
            int handCount = 1;
            for (Hand hand : table.getSplitHands()) {
                if (DEBUG)
                    System.out.println("SPLIT | HAND " + handCount + ": " + hand.toStringVerbose());
                while (hand.getPossiblePlays().size() != 0) {
                    playerMakeMove(hand, faceUpCard);
                    if (DEBUG)
                        System.out.println("Player's hand " + handCount + ": " + hand.toStringVerbose());
                }
                handCount++;
            }
        }

        // Print hands
        if (table.isPlayerSplit()) {
            int handCount = 1;
            for (Hand hand : table.getSplitHands()) {
                if (DEBUG)
                    System.out.println("HAND " + handCount + ": " + hand.toStringVerbose());
                handCount++;
            }
        }

    }

    private void playerMakeMove(Hand playerHand, Card faceUpCard) {
        Plays chosenPlay = player.makePlay(playerHand, faceUpCard);

        switch (chosenPlay) {
            case HIT:
                if (DEBUG)
                    System.out.println("Player hits!");
                table.hit(playerHand);
                break;
            case STAND:
                if (DEBUG)
                    System.out.println("Player stands!");
                table.stand(playerHand);
                break;
            case SPLIT:
                if (DEBUG)
                    System.out.println("Player splits!");
                splits++;
                table.split(playerHand);
                break;
            case DOUBLE_DOWN:
                if (DEBUG)
                    System.out.println("Player doubles down!");
                table.doubleDown(playerHand);
                break;
        }

        //System.out.println("Dealer's face up card: " + table.seeDealerCard());
        //System.out.println("Your hand: " + playerHand.toStringVerbose());
    }

    private void playDealerHand() {
        // Choose against which hand to play (this is for splits and multiple players)
        Hand oponnentHand = null;
        if (table.isPlayerSplit()) {
            for (Hand hand : table.getSplitHands()) {
                if (!hand.isBust()) {
                    if (oponnentHand == null) {
                        // At least one hand is not bust - play aginst it - nothing to lose
                        oponnentHand = hand;
                    } else if (oponnentHand.getValue() < hand.getValue() && hand.getValue() < 19){
                        // Chooses stronger hand to play against BUT ONLY IF value is lower than 19 (arbitrary value)
                        oponnentHand = hand;
                    }

                }
            }
        } else {
            oponnentHand = table.getPlayerHand();
        }

        // DEALER HITS UNTIL AT LEAST 17
        // TRY TO BEAT OPONNENT HAND
        while (table.getDealerHand().getValue() < 17) {
            table.hit(table.getDealerHand());
            if (DEBUG)
                System.out.println("Dealer hits! (Less than 17.) Current dealer's hand: " + table.getDealerHand().toStringVerbose());
        }

        // STAND ON 17
        if (table.getDealerHand().getValue() == 17) {
            if (rules.DEALER_STANDS_ON_SOFT_17) {
                table.stand(table.getDealerHand());
                if (DEBUG)
                    System.out.println("Dealer stands! (17)");
            } else {
                table.hit(table.getDealerHand());
                if (DEBUG)
                    System.out.println("Dealer hits! (17)");
            }
        } else if (!table.getDealerHand().isBust()){ // STAND IF NOT BUST
            table.stand(table.getDealerHand());
            if (DEBUG)
                System.out.println("Dealer stands!");
        }

        /*

        //if (oponnentHand != null && !oponnentHand.isBust()) {
        if (oponnentHand != null) {
            while (!table.getDealerHand().isBust() && table.getDealerHand().getPossiblePlays().size() > 0) {

                // Dealer must stand on 17
                if (table.getDealerHand().getValue() == 17) {
                    table.stand(table.getDealerHand());
                    System.out.println("Dealer stands! (17)");
                    break;
                }

                if (oponnentHand.isBust()) {
                    if (table.getDealerHand().getValue() < 17) table.hit(table.getDealerHand());
                    else table.stand(table.getDealerHand());
                    continue;
                }

                if (table.getDealerHand().getValue() < oponnentHand.getValue()) {
                    table.hit(table.getDealerHand());
                    System.out.println("Dealer hits! Current dealer's hand: " + table.getDealerHand().toStringVerbose());
                } else if (table.getDealerHand().getValue() == oponnentHand.getValue()) {
                    if (table.getDealerHand().getValue() < 17) {
                        // Dealer has to hit if he has less than 17
                        table.hit(table.getDealerHand());
                        System.out.println("Dealer hits! (Less than 17.) Current dealer's hand: " + table.getDealerHand().toStringVerbose());
                    } else {
                        table.stand(table.getDealerHand());
                        System.out.println("Dealer stands!");
                    }
                } else {
                    if (table.getDealerHand().getValue() < 17) {
                        // Dealer has to hit if he has less than 17
                        table.hit(table.getDealerHand());
                        System.out.println("Dealer hits (Less than 17.) Current dealer's hand: " + table.getDealerHand().toStringVerbose());
                    } else {
                        table.stand(table.getDealerHand());
                        System.out.println("Dealer stands!");
                    }

                }
            }
        } else {
            table.stand(table.getDealerHand());
            System.out.println("Dealer stands");
        }
        */
    }

    private int calculateScore(Hand playerHand) {

        /*if (playerHand.isBlackjack()) {
            System.out.println("YOU WON!");
            wins++;
            this.chips += (playerHand.getBetAmount() * (3/2));
            return 1;
        }*/

        if (playerHand.isBust() || (!table.getDealerHand().isBust() && table.getDealerHand().getValue() > playerHand.getValue())) {
            if (DEBUG)
                System.out.println("YOU LOST");
            losses++;
            this.chips -= playerHand.getBetAmount();
            if (playerHand.isDoubleDown()) {
                return -2;
            } else {
                return -1;
            }
        } else if (table.getDealerHand().isBust() || table.getDealerHand().getValue() < playerHand.getValue() || playerHand.isBlackjack()) {
            if (DEBUG)
                System.out.println("YOU WON!");
            wins++;
            if (playerHand.isBlackjack()) {
                this.chips += (playerHand.getBetAmount() * (3.0/2.0));
            } else {
                this.chips += playerHand.getBetAmount();
            }

            if (playerHand.isDoubleDown()) {
                return 2;
            } else {
                return 1;
            }
        } else if (table.getDealerHand().getValue() == playerHand.getValue()) {
            if (DEBUG)
                System.out.println("PUSH!");
            pushes++;
            return 0;
        } else {
            if (DEBUG)
                System.out.println("********** SCORING ERROR **************");
            return 0;
        }
    }


}
