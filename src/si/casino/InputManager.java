package si.casino;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class InputManager {

    private static final Scanner keyboard = new Scanner(System.in);

    public static int getValidInputInt(List<Integer> validInts) {
        while (true) {
            try {
                System.out.print("> ");
                int input = keyboard.nextInt();
                if ((validInts == null || validInts.contains(input)) && input > 0) {
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

    public static long getValidInputLong(List<Long> validLongs) {
        while (true) {
            try {
                System.out.print("> ");
                long input = keyboard.nextLong();
                if ((validLongs == null || validLongs.contains(input)) && input > 0) {
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

    public static List<String> readLines(String path) {
        try {
            List<String> lines = Files.lines(Paths.get(path)).collect(Collectors.toList());
            return lines;
        } catch (Exception e) {
            System.out.println("Error while reading file file! " + e.getMessage());
        }
        return null;
    }

}
