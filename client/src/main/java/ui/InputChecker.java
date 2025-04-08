package ui;

import java.util.ArrayList;
import java.util.Arrays;

public class InputChecker {

    public static boolean isNotValidMenuInput(String input, int max) {
        if (!isNumeric(input)) {
            return true;
        }
        int num = Integer.parseInt(input);
        return num < 1 || num > max;
    }

    public static boolean isNotValidMoveInput(String input) {
        int len = input.length();
        if (len < 4 || (len > 4 && len < 10) || len > 12) {
            return true;
        }
        if (len == 4) { // No Promo Piece
            return isNotValidPosition(input.substring(0, 2)) || isNotValidPosition(input.substring(2, 4));
        } else { // Promo Piece
            ArrayList<String> validPieces = new ArrayList<>(Arrays.asList("QUEEN","ROOK","BISHOP","KNIGHT"));
            if (!validPieces.contains(input.substring(6).toUpperCase())) {
                return true;
            }
            return !input.startsWith("->", 4);
        }
    }
    private static boolean isNotValidPosition(String pos) {
        if (!Character.isAlphabetic(pos.charAt(0)) || !Character.isDigit(pos.charAt(1))) {
            return true;
        }
        int col = Character.toUpperCase(pos.charAt(0)) - 'A' + 1;
        int row = Character.getNumericValue(pos.charAt(1));
        return col > 8 || col < 1 || row > 8 || row < 1;
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
