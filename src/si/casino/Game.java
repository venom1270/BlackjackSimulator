package si.casino;

import si.casino.card.Card;
import si.casino.players.HumanPlayer;
import si.casino.players.Player;
import si.casino.players.RandomPlayer;
import si.casino.plays.Plays;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private Table table;

    private int wins;
    private int losses;
    private int pushes;
    private int gamesPlayed;
    private int splits;

    Player player;


    public Game() {
        //player = new HumanPlayer();
        //player = new RandomPlayer();
    }

    public void run() {

        List<Integer> validInts = new ArrayList<>();
        for (int i = 1; i <= 6; i++) {
            validInts.add(i);
        }
        System.out.println("Choose number of decks: (1-6)");
        table = new Table(InputManager.getValidInputInt(validInts));

        System.out.println("Choose player type: (1-Human, 2-Simulator_Random)");
        validInts.clear();
        validInts.add(1);
        validInts.add(2);
        int playerType = InputManager.getValidInputInt(validInts);
        if (playerType == 1) {
            player = new HumanPlayer();
        } else {
            player = new RandomPlayer();
        }

        int numberOfGames = 0;
        System.out.println("Choose number of games: ");
        numberOfGames = InputManager.getValidInputInt(null);

        final String DELIMITER = "********************";

        for (int gameIndex = 0; gameIndex < numberOfGames; gameIndex++) {

            System.out.println(DELIMITER + " GAME " + (gameIndex+1) + " " + DELIMITER);
            table.reset();

            table.deal();
            System.out.println("Dealer's face up card: " + table.seeDealerCard());
            System.out.println("Your hand: " + table.getPlayerHand().toStringVerbose());

            if (!table.getDealerHand().isBlackjack()) {
                playPlayerHand();

                System.out.println("Dealer's hand: " + table.getDealerHand().toStringVerbose());

                playDealerHand();

                System.out.println("Final dealer hand: " + table.getDealerHand().toStringVerbose());

                System.out.println(DELIMITER);

                // CALCULATE SCORE
                if (table.isPlayerSplit()) {
                    int handCount = 1;
                    System.out.println("Split player hands: ");
                    for (Hand hand : table.getSplitHands()) {
                        System.out.println("  Hand " + handCount + ": " + hand.toStringVerbose());
                        calculateScore(hand);
                        handCount++;
                    }
                } else {
                    System.out.println("Your hand: " + table.getPlayerHand().toStringVerbose());
                    calculateScore(table.getPlayerHand());
                }
            } else {
                System.out.println("DEALER HAS BLACKJACK! " + table.getDealerHand().toStringVerbose());
                losses++;
            }

            gamesPlayed++;

            System.out.println(DELIMITER);
            System.out.println("Wins: " + wins);
            System.out.println("Loses: " + losses);
            System.out.println("Pushes: " + pushes);
            System.out.println("Splits: " + splits);
            System.out.println("Games played: " + gamesPlayed);

            /*System.out.println("Keep playing? (y)");
            if (InputManager.getValidInput(null) != 'y') {
                keepPlaying = false;
            }*/

        }

        float winRate = (float) wins / (wins+losses+splits);
        System.out.println("Winrate: " + winRate*100 + "%");
        System.out.println("The end!");

    }

    private void playPlayerHand() {

        Card faceUpCard = table.getDealerHand().getCards().get(1);

        while (table.getPlayerHand().getPossiblePlays().size() != 0) {

            playerMakeMove(table.getPlayerHand(), faceUpCard);

            if (table.isPlayerSplit()) {
                int handCount = 1;
                for (Hand hand : table.getSplitHands()) {
                    System.out.println("HAND " + handCount + ": " + hand.toStringVerbose());
                    handCount++;
                }

                // Stop normal play iteration, go to split handling.
                break;
            } else {
                System.out.println("Player's hand: " + table.getPlayerHand().toStringVerbose());
            }

        }

        if (table.isPlayerSplit()) {
            // Split handling
            int handCount = 1;
            for (Hand hand : table.getSplitHands()) {
                System.out.println("SPLIT | HAND " + handCount + ": " + hand.toStringVerbose());
                while (hand.getPossiblePlays().size() != 0) {
                    playerMakeMove(hand, faceUpCard);
                    System.out.println("Game's hand " + handCount + ": " + table.getPlayerHand().toStringVerbose());
                }
                handCount++;
            }
        }

        // Print hands
        if (table.isPlayerSplit()) {
            int handCount = 1;
            for (Hand hand : table.getSplitHands()) {
                System.out.println("HAND " + handCount + ": " + hand.toStringVerbose());
                handCount++;
            }
        }

    }

    private void playerMakeMove(Hand playerHand, Card faceUpCard) {
        Plays chosenPlay = player.makePlay(playerHand, faceUpCard);

        switch (chosenPlay) {
            case HIT:
                System.out.println("Player hits!");
                table.hit(playerHand);
                break;
            case STAND:
                System.out.println("Player stands!");
                table.stand(playerHand);
                break;
            case SPLIT:
                System.out.println("Player splits!");
                splits++;
                table.split(playerHand);
                break;
            case DOUBLE_DOWN:
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

        // DEALER
        if (oponnentHand != null && !oponnentHand.isBust()) {
            while (!table.getDealerHand().isBust() && table.getDealerHand().getPossiblePlays().size() > 0) {

                // Dealer must stand on 17
                if (table.getDealerHand().getValue() == 17) {
                    table.stand(table.getDealerHand());
                    System.out.println("Dealer stands! (17)");
                    break;
                }

                if (table.getDealerHand().getValue() < oponnentHand.getValue()) {
                    table.hit(table.getDealerHand());
                    System.out.println("Dealer hits! Current dealer's hand: " + table.getDealerHand().toStringVerbose());
                } else if (table.getDealerHand().getValue() == oponnentHand.getValue()) {
                    if (table.getDealerHand().getValue() < 16) {
                        table.hit(table.getDealerHand());
                        System.out.println("Dealer hits! Current dealer's hand: " + table.getDealerHand().toStringVerbose());
                    } else {
                        table.stand(table.getDealerHand());
                        System.out.println("Dealer stands!");
                    }
                } else {
                    table.stand(table.getDealerHand());
                    System.out.println("Dealer stands!");
                }
            }
        } else {
            table.stand(table.getDealerHand());
            System.out.println("Dealer stands");
        }
    }

    private void calculateScore(Hand playerHand) {
        if (playerHand.isBust() || (!table.getDealerHand().isBust() && table.getDealerHand().getValue() > playerHand.getValue())) {
            System.out.println("YOU LOST");
            losses++;
        } else if (table.getDealerHand().isBust() || table.getDealerHand().getValue() < playerHand.getValue() || playerHand.isBlackjack()) {
            wins++;
            System.out.println("YOU WON!");
        } else if (table.getDealerHand().getValue() == playerHand.getValue()) {
            System.out.println("PUSH!");
            pushes++;
        } else {
            System.out.println("********** SCORING ERROR **************");
        }
    }


}
