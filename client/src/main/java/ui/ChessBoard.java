package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

// Draws the chess board
public class ChessBoard {
    private static final String TERMINAL_COLOR = SET_BG_COLOR_BLACK;
    private static final String SET_BG_BORDER = SET_BG_COLOR_BLACK+SET_TEXT_COLOR_WHITE+SET_TEXT_BOLD;

    // Throw this away later, client will call methods in this class.
    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        drawChessBoard(out);
    }

    public static void drawChessBoard(PrintStream out) {
        drawHeader(out);
        for (int row=8; row>0; row--) {
            drawRowWhite(out, row);
        }
        drawHeader(out);
    }

    public static void drawHeader(PrintStream out) {
        out.print(SET_BG_BORDER);
        out.println(EMPTY+" a  b  c  d  e  f  g  h "+EMPTY+TERMINAL_COLOR);
    }

    public static void drawRowWhite(PrintStream out, int row) {
        out.print(SET_BG_BORDER+" "+row+" ");
        for (int i=0; i<4; i++) {
            if (row % 2 == 0) {
                drawWhiteSquare(out);
                drawBlackSquare(out);
            } else {
                drawBlackSquare(out);
                drawWhiteSquare(out);
            }
        }
        out.println(SET_BG_BORDER+" "+row+" "+TERMINAL_COLOR);
    }

    public static void drawWhiteSquare(PrintStream out) {
        out.print(SET_BG_COLOR_LIGHT_GREY+EMPTY);
    }

    public static void drawBlackSquare(PrintStream out) {
        out.print(SET_BG_COLOR_DARK_GREEN+EMPTY);
    }
}
