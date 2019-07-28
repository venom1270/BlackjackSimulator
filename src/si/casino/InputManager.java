package si.casino;

import java.util.List;
import java.util.Scanner;

public class InputManager {

    private static final Scanner keyboard = new Scanner(System.in);

    public static int getValidInputInt(List<Integer> validInts) {
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


    public static char getValidInput(List<Character> validChars) {
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

}
