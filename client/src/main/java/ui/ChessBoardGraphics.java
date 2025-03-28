package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessGame.TeamColor;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

// Draws the chess board
public class ChessBoardGraphics {
    private static final String TERMINAL_COLOR = RESET_BG_COLOR;
    private static final String SET_BG_BORDER = SET_BG_COLOR_BLACK+SET_TEXT_COLOR_WHITE+SET_TEXT_BOLD;
    private static final String WHITE_SQUARE_COLOR = SET_BG_COLOR_LIGHT_GREY;
    private static final String BLACK_SQUARE_COLOR = SET_BG_COLOR_DARK_GREEN;

    private static ChessBoard board;

    // Throw this away later, client will call methods in this class.
    public static void main(String[] args) {
        drawChessBoard(new ChessGame(), TeamColor.BLACK);
    }

    public static void drawChessBoard(ChessGame game, TeamColor color) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        board = game.getBoard();
        drawHeader(out, color);

        boolean countdown = color == TeamColor.WHITE;
        int start = countdown ? 8 : 1;
        int end = countdown ? 1 : 8;
        int step = countdown ? -1 : 1;
        for (int row=start; countdown ? row>=end : row<=end;  row += step) {
            drawRow(out, row, color);
        }

        drawHeader(out, color);
        resetTextSettings(out);
    }

    public static void drawHeader(PrintStream out, TeamColor color) {
        out.print(SET_BG_BORDER);
        if (color == TeamColor.WHITE) {
            out.println(EMPTY+" a  b  c  d  e  f  g  h "+EMPTY+TERMINAL_COLOR);
        } else {
            out.println(EMPTY+" h  g  f  e  d  c  b  a "+EMPTY+TERMINAL_COLOR);
        }
    }

    public static void drawRow(PrintStream out, int row, TeamColor color) {
        out.print(SET_BG_BORDER+" "+row+" ");
        boolean countdown = color != TeamColor.WHITE;
        int start = countdown ? 8 : 1;
        int end = countdown ? 1 : 8;
        int step = countdown ? -1 : 1;
        for (int col=start; countdown ? col>=end : col<=end;  col += step) {
            ChessPosition pos = new ChessPosition(row, col);
            if (row % 2 == 0) {
                if (col % 2 == 1) {
                    drawSquare(out, pos, WHITE_SQUARE_COLOR);
                } else {
                    drawSquare(out, pos, BLACK_SQUARE_COLOR);
                }
            } else {
                if (col % 2 == 1) {
                    drawSquare(out, pos, BLACK_SQUARE_COLOR);
                } else {
                    drawSquare(out, pos, WHITE_SQUARE_COLOR);
                }
            }
        }
        out.println(SET_BG_BORDER+" "+row+" "+TERMINAL_COLOR);
    }

    public static void drawSquare(PrintStream out, ChessPosition pos, String squareColor) {
        ChessPiece piece = board.getPiece(pos);
        String pieceString = getPieceString(piece);
        String textColor = getPieceTextColor(piece);
        out.print(squareColor+textColor+pieceString);
    }

    private static String getPieceString(ChessPiece piece) {
        if (piece == null) {
            return EMPTY;
        }
        ChessPiece.PieceType type = piece.getPieceType();
        ChessGame.TeamColor color = piece.getTeamColor();
        return switch (type) {
            case KING -> getPieceByColor(color, WHITE_KING, BLACK_KING);
            case QUEEN -> getPieceByColor(color, WHITE_QUEEN, BLACK_QUEEN);
            case ROOK -> getPieceByColor(color, WHITE_ROOK, BLACK_ROOK);
            case BISHOP -> getPieceByColor(color, WHITE_BISHOP, BLACK_BISHOP);
            case KNIGHT -> getPieceByColor(color, WHITE_KNIGHT, BLACK_KNIGHT);
            case PAWN -> getPieceByColor(color, WHITE_PAWN, BLACK_PAWN);
        };
    }

    private static String getPieceByColor(ChessGame.TeamColor color, String whitePiece, String blackPiece) {
        return color == ChessGame.TeamColor.WHITE ? whitePiece : blackPiece;
    }

    private static String getPieceTextColor(ChessPiece piece) {
        if (piece == null) {
            return "";
        }
        return piece.getTeamColor()==ChessGame.TeamColor.WHITE ? SET_TEXT_COLOR_WHITE : SET_TEXT_COLOR_BLACK;
    }

    private static void resetTextSettings(PrintStream out) {
        out.print(RESET_TEXT_BOLD_FAINT+RESET_BG_COLOR+RESET_TEXT_COLOR);
    }
}
