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

    private Scanner keyboard;

    public Player() {
        game = new Game(1);
        keyboard = new Scanner(System.in);
    }

    public void run() {

        boolean keepPlaying = true;
        final String DELIMITER = "********************";



        while (keepPlaying) {

            System.out.println(DELIMITER);
            game.reset();

            game.deal();
            System.out.println("Dealer's face up card: " + game.seeDealerCard());
            System.out.println("Your hand: " + game.getPlayerHand() + " | " + game.getPlayerHand().toStringVerbose());

            // PLAYER
            while (game.getPlayerHand().getPossiblePlays().size() != 0) {
                List<Character> validChars = new ArrayList<>();

                for (Plays play : game.getPlayerHand().getPossiblePlays()) {
                    switch (play) {
                        case HIT: System.out.println("(H)it"); validChars.add('h'); break;
                        case STAND: System.out.println("(S)tand"); validChars.add('s'); break;
                        //case SPLIT: System.out.println("s(P)lit"); validChars.add('p'); break;
                        case DOUBLE_DOWN: System.out.println("(D)ouble down"); validChars.add('d'); break;
                    }
                }
                System.out.print("> ");

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
                        game.hit(game.getPlayerHand());
                        break;
                    case STAND:
                        game.stand(game.getPlayerHand());
                        break;
                    case SPLIT:
                        game.split(game.getPlayerHand());
                        break;
                    case DOUBLE_DOWN:
                        game.doubleDown(game.getPlayerHand());
                        break;
                }

                System.out.println("Dealer's face up card: " + game.seeDealerCard());
                System.out.println("Your hand: " + game.getPlayerHand() + " | " + game.getPlayerHand().toStringVerbose());

            }

            System.out.println("Dealer's hand: " + game.getDealerHand().toStringVerbose());

            // DEALER
            if (!game.getPlayerHand().isBust()) {
                while (!game.getDealerHand().isBust() && game.getDealerHand().getPossiblePlays().size() > 0) {

                    // Dealer must stand on 17
                    if (game.getDealerHand().getValue() == 17) {
                        game.stand(game.getDealerHand());
                        System.out.println("Dealer stands! (Mandatory)");
                        break;
                    }

                    if (game.getDealerHand().getValue() < game.getPlayerHand().getValue()) {
                        game.hit(game.getDealerHand());
                        System.out.println("Dealer hits! Current dealer's hand: " + game.getDealerHand());
                    } else if (game.getDealerHand().getValue() == game.getPlayerHand().getValue()) {
                        if (game.getDealerHand().getValue() < 16) {
                            game.hit(game.getDealerHand());
                            System.out.println("Dealer hits! Current dealer's hand: " + game.getDealerHand());
                        } else {
                            game.stand(game.getDealerHand());
                            System.out.println("Dealer stands!");
                        }
                    } else {
                        game.stand(game.getDealerHand());
                        System.out.println("Dealer stands!");
                    }
                }
            }

            System.out.println("Final dealer hand: " + game.getDealerHand() + " | " + game.getDealerHand().toStringVerbose());


            // CALCULATE SCORE
            if (game.getPlayerHand().isBust() || (!game.getDealerHand().isBust() && game.getDealerHand().getValue() > game.getPlayerHand().getValue())) {
                System.out.println("YOU LOST");
                losses++;
            } else if (game.getDealerHand().isBust() || game.getDealerHand().getValue() < game.getPlayerHand().getValue() || game.getPlayerHand().isBlackjack()) {
                wins++;
                System.out.println("YOU WON!");
            } else if (game.getDealerHand().getValue() == game.getPlayerHand().getValue()) {
                System.out.println("PUSH!");
                pushes++;
            } else {
                System.out.println("********** SCORING ERROR **************");
            }
            gamesPlayed++;

            System.out.println(DELIMITER);
            System.out.println("Wins: " + wins);
            System.out.println("Loses: " + losses);
            System.out.println("Pushes: " + pushes);
            System.out.println("Games played: " + gamesPlayed);
            System.out.println(DELIMITER);

            System.out.println("Keep playing? (y)");
            System.out.println("> ");
            if (getValidInput(null) != 'y') {
                keepPlaying = false;
            }

        }

        System.out.println("BYE!");

    }

    private char getValidInput(List<Character> validChars) {
        while (true) {
            try {
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



}
