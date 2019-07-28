package si.casino;

import si.casino.plays.Plays;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Player {

    private Game game;

    private int wins;
    private int losses;
    private int pushes;
    private int gamesPlayed;
    private int splits;

    private Scanner keyboard;

    public Player() {
        keyboard = new Scanner(System.in);
    }

    public void run() {

        List<Integer> validInts = new ArrayList<>();
        for (int i = 1; i <= 6; i++) {
            validInts.add(i);
        }
        System.out.println("Choose number of decks: (1-6)");
        game = new Game(getValidInputInt(validInts));

        boolean keepPlaying = true;
        final String DELIMITER = "********************";

        while (keepPlaying) {

            System.out.println(DELIMITER);
            game.reset();

            game.deal();
            System.out.println("Dealer's face up card: " + game.seeDealerCard());
            System.out.println("Your hand: " + game.getPlayerHand().toStringVerbose());

            if (!game.getDealerHand().isBlackjack()) {
                playPlayerHand();

                System.out.println("Dealer's hand: " + game.getDealerHand().toStringVerbose());

                playDealerHand();

                System.out.println("Final dealer hand: " + game.getDealerHand().toStringVerbose());

                System.out.println(DELIMITER);

                // CALCULATE SCORE
                if (game.isPlayerSplit()) {
                    int handCount = 1;
                    System.out.println("Split player hands: ");
                    for (Hand hand : game.getSplitHands()) {
                        System.out.println("  Hand " + handCount + ": " + hand.toStringVerbose());
                        calculateScore(hand);
                        handCount++;
                    }
                } else {
                    System.out.println("Your hand: " + game.getPlayerHand().toStringVerbose());
                    calculateScore(game.getPlayerHand());
                }
            } else {
                System.out.println("DEALER HAS BLACKJACK! " + game.getDealerHand().toStringVerbose());
                losses++;
            }

            gamesPlayed++;

            System.out.println(DELIMITER);
            System.out.println("Wins: " + wins);
            System.out.println("Loses: " + losses);
            System.out.println("Pushes: " + pushes);
            System.out.println("Splits: " + splits);
            System.out.println("Games played: " + gamesPlayed);
            System.out.println(DELIMITER);

            System.out.println("Keep playing? (y)");
            if (getValidInput(null) != 'y') {
                keepPlaying = false;
            }

        }

        System.out.println("BYE!");

    }

    private void playPlayerHand() {


        while (game.getPlayerHand().getPossiblePlays().size() != 0) {

            playerMakeMove(game.getPlayerHand());

            if (game.isPlayerSplit()) {
                int handCount = 1;
                for (Hand hand : game.getSplitHands()) {
                    System.out.println("HAND " + handCount + ": " + hand.toStringVerbose());
                    handCount++;
                }

                // Stop normal play iteration, go to split handling.
                break;
            } else {
                System.out.println("Player's hand: " + game.getPlayerHand().toStringVerbose());
            }

        }

        if (game.isPlayerSplit()) {
            // Split handling
            int handCount = 1;
            for (Hand hand : game.getSplitHands()) {
                System.out.println("SPLIT | HAND " + handCount + ": " + hand.toStringVerbose());
                while (hand.getPossiblePlays().size() != 0) {
                    playerMakeMove(hand);
                    System.out.println("Player's hand " + handCount + ": " + game.getPlayerHand().toStringVerbose());
                }
                handCount++;
            }
        }

        // Print hands
        if (game.isPlayerSplit()) {
            int handCount = 1;
            for (Hand hand : game.getSplitHands()) {
                System.out.println("HAND " + handCount + ": " + hand.toStringVerbose());
                handCount++;
            }
        }

    }

    private void playerMakeMove(Hand playerHand) {
        List<Character> validChars = new ArrayList<>();

        for (Plays play : playerHand.getPossiblePlays()) {
            if (play == Plays.SPLIT && game.isPlayerSplit()) continue;
            switch (play) {
                case HIT: System.out.println("(H)it"); validChars.add('h'); break;
                case STAND: System.out.println("(S)tand"); validChars.add('s'); break;
                case SPLIT: System.out.println("s(P)lit"); validChars.add('p'); break;
                case DOUBLE_DOWN: System.out.println("(D)ouble down"); validChars.add('d'); break;
            }
        }

        Plays chosenPlay = Plays.STAND;
        switch (getValidInput(validChars)) {
            case 'h':
                chosenPlay = Plays.HIT;
                break;
            case 's':
                chosenPlay = Plays.STAND;
                break;
            case 'p':
                chosenPlay = Plays.SPLIT;
                break;
            case 'd':
                chosenPlay = Plays.DOUBLE_DOWN;
                break;
            default:
                chosenPlay = Plays.STAND;
        }

        switch (chosenPlay) {
            case HIT:
                game.hit(playerHand);
                break;
            case STAND:
                game.stand(playerHand);
                break;
            case SPLIT:
                splits++;
                game.split(playerHand);
                break;
            case DOUBLE_DOWN:
                game.doubleDown(playerHand);
                break;
        }

        //System.out.println("Dealer's face up card: " + game.seeDealerCard());
        //System.out.println("Your hand: " + playerHand.toStringVerbose());
    }

    private void playDealerHand() {
        // Choose against which hand to play (this is for splits and multiple players)
        Hand oponnentHand = null;
        if (game.isPlayerSplit()) {
            for (Hand hand : game.getSplitHands()) {
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
            oponnentHand = game.getPlayerHand();
        }

        // DEALER
        if (oponnentHand != null && !oponnentHand.isBust()) {
            while (!game.getDealerHand().isBust() && game.getDealerHand().getPossiblePlays().size() > 0) {

                // Dealer must stand on 17
                if (game.getDealerHand().getValue() == 17) {
                    game.stand(game.getDealerHand());
                    System.out.println("Dealer stands! (17)");
                    break;
                }

                if (game.getDealerHand().getValue() < oponnentHand.getValue()) {
                    game.hit(game.getDealerHand());
                    System.out.println("Dealer hits! Current dealer's hand: " + game.getDealerHand().toStringVerbose());
                } else if (game.getDealerHand().getValue() == oponnentHand.getValue()) {
                    if (game.getDealerHand().getValue() < 16) {
                        game.hit(game.getDealerHand());
                        System.out.println("Dealer hits! Current dealer's hand: " + game.getDealerHand().toStringVerbose());
                    } else {
                        game.stand(game.getDealerHand());
                        System.out.println("Dealer stands!");
                    }
                } else {
                    game.stand(game.getDealerHand());
                    System.out.println("Dealer stands!");
                }
            }
        } else {
            game.stand(game.getDealerHand());
            System.out.println("Dealer stands");
        }
    }

    private void calculateScore(Hand playerHand) {
        if (playerHand.isBust() || (!game.getDealerHand().isBust() && game.getDealerHand().getValue() > playerHand.getValue())) {
            System.out.println("YOU LOST");
            losses++;
        } else if (game.getDealerHand().isBust() || game.getDealerHand().getValue() < playerHand.getValue() || playerHand.isBlackjack()) {
            wins++;
            System.out.println("YOU WON!");
        } else if (game.getDealerHand().getValue() == playerHand.getValue()) {
            System.out.println("PUSH!");
            pushes++;
        } else {
            System.out.println("********** SCORING ERROR **************");
        }
    }

    private char getValidInput(List<Character> validChars) {
        while (true) {
            try {
                System.out.print("> ");
                char input = keyboard.nextLine().toLowerCase().charAt(0);
                if (validChars == null || validChars.contains(input)) {
                    return input;
                } else {
                    throw new Exception();
                }
            } catch (Exception e) {
                System.out.println("Invalid input!");
            }
        }
    }

    private int getValidInputInt(List<Integer> validInts) {
        while (true) {
            try {
                System.out.print("> ");
                int input = keyboard.nextInt();
                if (validInts == null || validInts.contains(input)) {
                    keyboard.nextLine();
                    return input;
                } else {
                    throw new Exception();
                }
            } catch (Exception e) {
                System.out.println("Invalid input!");
            }
        }
    }



}
