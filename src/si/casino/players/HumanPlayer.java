package si.casino.players;

import si.casino.Hand;
import si.casino.InputManager;
import si.casino.card.Card;
import si.casino.plays.Plays;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class HumanPlayer implements Player{

    private Scanner keyboard;

    public HumanPlayer() {
        keyboard = new Scanner(System.in);
    }

    @Override
    public Plays makePlay(Hand playerHand, Card faceUpCard) {
        List<Character> validChars = new ArrayList<>();

        for (Plays play : playerHand.getPossiblePlays()) {
            switch (play) {
                case HIT: System.out.println("(H)it"); validChars.add('h'); break;
                case STAND: System.out.println("(S)tand"); validChars.add('s'); break;
                case SPLIT: System.out.println("s(P)lit"); validChars.add('p'); break;
                case DOUBLE_DOWN: System.out.println("(D)ouble down"); validChars.add('d'); break;
            }
        }

        Plays chosenPlay = Plays.STAND;
        switch (InputManager.getValidInput(validChars)) {
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

        return chosenPlay;

    }

    @Override
    public int getBet(int previousGame, int previousBet) {
        System.out.println("Bet amount:");
        return InputManager.getValidInputInt(null);
    }


}
