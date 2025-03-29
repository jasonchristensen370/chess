package ui;

import chess.*;
import chess.ChessGame.TeamColor;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;

import static ui.EscapeSequences.*;

// Draws the chess board
public class ChessBoardGraphics {
    private static final String TERMINAL_COLOR = RESET_BG_COLOR;
    private static final String SET_BG_BORDER = SET_BG_COLOR_BLACK+SET_TEXT_COLOR_WHITE+SET_TEXT_BOLD;
    private static final String WHITE_SQUARE_COLOR = SET_BG_COLOR_LIGHT_GREY;
    private static final String BLACK_SQUARE_COLOR = SET_BG_COLOR_DARK_GREEN;
    private static final String WHITE_HIGHLIGHT = SET_BG_COLOR_GREEN;
    private static final String BLACK_HIGHLIGHT = SET_BG_COLOR_SEA_GREEN;
    private static final String PIECE_HIGHLIGHT = SET_BG_COLOR_BLUE;

    private static ChessBoard board;
    private static ChessPosition highPos;
    private static ArrayList<ChessPosition> validMoves;

    // Throw this away later, client will call methods in this class.
    public static void main(String[] args) {
        var highlight = new ChessPosition(2, 4);
        var game = new ChessGame();
        drawChessBoard(game, TeamColor.WHITE, highlight);
        drawChessBoard(game, TeamColor.BLACK, null);
    }

    public static void drawChessBoard(ChessGame game, TeamColor color, ChessPosition highlightPosition) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        board = game.getBoard();
        validMoves = new ArrayList<>();
        highPos = highlightPosition;
        getValidMoves();
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
        highPos = null;
        validMoves = null;
    }

    private static void getValidMoves() {
        if (highPos != null) {
            Collection<ChessMove> validMovements = board.getPiece(highPos).pieceMoves(board, highPos);
            for (ChessMove move : validMovements) {
                validMoves.add(move.getEndPosition());
            }
        }
    }

    private static void drawHeader(PrintStream out, TeamColor color) {
        out.print(SET_BG_BORDER);
        if (color == TeamColor.WHITE) {
            out.println(EMPTY+" a  b  c  d  e  f  g  h "+EMPTY+TERMINAL_COLOR);
        } else {
            out.println(EMPTY+" h  g  f  e  d  c  b  a "+EMPTY+TERMINAL_COLOR);
        }
    }

    private static void drawRow(PrintStream out, int row, TeamColor color) {
        out.print(SET_BG_BORDER+" "+row+" ");
        boolean countdown = color != TeamColor.WHITE;
        int start = countdown ? 8 : 1;
        int end = countdown ? 1 : 8;
        int step = countdown ? -1 : 1;
        for (int col=start; countdown ? col>=end : col<=end;  col += step) {
            ChessPosition pos = new ChessPosition(row, col);
            String whiteColor = validMoves.contains(pos) ? WHITE_HIGHLIGHT : WHITE_SQUARE_COLOR;
            String blackColor = validMoves.contains(pos) ? BLACK_HIGHLIGHT : BLACK_SQUARE_COLOR;
            if (row % 2 == 0) {
                if (col % 2 == 1) {
                    drawSquare(out, pos, whiteColor);
                } else {
                    drawSquare(out, pos, blackColor);
                }
            } else {
                if (col % 2 == 1) {
                    drawSquare(out, pos, blackColor);
                } else {
                    drawSquare(out, pos, whiteColor);
                }
            }
        }
        out.println(SET_BG_BORDER+" "+row+" "+TERMINAL_COLOR);
    }

    private static void drawSquare(PrintStream out, ChessPosition pos, String squareColor) {
        ChessPiece piece = board.getPiece(pos);
        String pieceString = getPieceString(piece);
        String textColor = getPieceTextColor(piece);
        if (pos.equals(highPos)) {
            squareColor = PIECE_HIGHLIGHT;
        }
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
