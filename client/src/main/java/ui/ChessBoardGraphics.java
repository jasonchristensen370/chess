package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

// Draws the chess board
public class ChessBoardGraphics {
    private static final String TERMINAL_COLOR = SET_BG_COLOR_BLACK;
    private static final String SET_BG_BORDER = SET_BG_COLOR_BLACK+SET_TEXT_COLOR_WHITE+SET_TEXT_BOLD;

    private static ChessBoard board;

    // Throw this away later, client will call methods in this class.
    public static void main(String[] args) {
        drawChessBoard(new ChessGame());
    }

    public static void drawChessBoard(ChessGame game) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        board = game.getBoard();
        drawHeader(out);
        for (int row=8; row>0; row--) {
            drawRow(out, row);
        }
        drawHeader(out);
        resetTextSettings(out);
    }

    public static void drawHeader(PrintStream out) {
        out.print(SET_BG_BORDER);
        out.println(EMPTY+" a  b  c  d  e  f  g  h "+EMPTY+TERMINAL_COLOR);
    }

    public static void drawRow(PrintStream out, int row) {
        out.print(SET_BG_BORDER+" "+row+" ");
        for (int col=1; col<9; col++) {
            ChessPosition pos = new ChessPosition(row, col);
            if (row % 2 == 0) {
                if (col % 2 == 1) {
                    drawWhiteSquare(out, pos);
                } else {
                    drawBlackSquare(out, pos);
                }
            } else {
                if (col % 2 == 1) {
                    drawBlackSquare(out, pos);
                } else {
                    drawWhiteSquare(out, pos);
                }
            }
        }
        out.println(SET_BG_BORDER+" "+row+" "+TERMINAL_COLOR);
    }

    public static void drawWhiteSquare(PrintStream out, ChessPosition pos) {
        ChessPiece piece = board.getPiece(pos);
        String pieceString = getPieceString(piece);
        String textColor = getPieceTextColor(piece);
        out.print(SET_BG_COLOR_LIGHT_GREY+textColor+pieceString);
    }

    public static void drawBlackSquare(PrintStream out, ChessPosition pos) {
        ChessPiece piece = board.getPiece(pos);
        String pieceString = getPieceString(piece);
        String textColor = getPieceTextColor(piece);
        out.print(SET_BG_COLOR_DARK_GREEN+textColor+pieceString);
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
