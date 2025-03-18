package ui;

public class InputChecker {

    public static boolean isNotValidMenuInput(String input, int max) {
        if (!isNumeric(input)) {
            return true;
        }
        int num = Integer.parseInt(input);
        return num < 1 || num > max;
    }

    public static boolean isNotValidStringInput(String input) {
        return input.isEmpty();
    }

    private static boolean isNumeric(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
