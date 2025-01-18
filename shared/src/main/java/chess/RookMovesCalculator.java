package chess;

import java.util.ArrayList;
import java.util.Collection;

public class RookMovesCalculator  implements PieceMovesCalculator {
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {

        Collection<ChessMove> validMoves = new ArrayList<>();
        int max_moves = 7;
        int row = position.getRow();
        int col = position.getColumn();
        ChessGame.TeamColor my_color = board.getPiece(position).getTeamColor();

        /* Check Moves to the Left */
        for (int i=1; i <= max_moves; i++) {
            ChessPosition test_move = new ChessPosition(row, col-i);
            int test_row = test_move.getRow();
            int test_col = test_move.getColumn();
            if (test_row <= 8 && test_col <= 8 && test_row >= 1 && test_col >= 1) {
                if (board.getPiece(test_move) == null) {
                    validMoves.add(new ChessMove(position, test_move, null));
                } else if (board.getPiece(test_move).getTeamColor() != my_color) {
                    validMoves.add(new ChessMove(position, test_move, null));
                    break;
                } else { /* Is my_color. Don't add and stop in this direction */
                    break;
                }
            } else {
                break;
            }
        }
        /* Check Moves to the Right */
        for (int i=1; i <= max_moves; i++) {
            ChessPosition test_move = new ChessPosition(row, col+i);
            int test_row = test_move.getRow();
            int test_col = test_move.getColumn();
            if (test_row <= 8 && test_col <= 8 && test_row >= 1 && test_col >= 1) {
                if (board.getPiece(test_move) == null) {
                    validMoves.add(new ChessMove(position, test_move, null));
                } else if (board.getPiece(test_move).getTeamColor() != my_color) {
                    validMoves.add(new ChessMove(position, test_move, null));
                    break;
                } else { /* Is my_color. Don't add and stop in this direction */
                    break;
                }
            } else {
                break;
            }
        }
        /* Check Moves Up */
        for (int i=1; i <= max_moves; i++) {
            ChessPosition test_move = new ChessPosition(row+i, col);
            int test_row = test_move.getRow();
            int test_col = test_move.getColumn();
            if (test_row <= 8 && test_col <= 8 && test_row >= 1 && test_col >= 1) {
                if (board.getPiece(test_move) == null) {
                    validMoves.add(new ChessMove(position, test_move, null));
                } else if (board.getPiece(test_move).getTeamColor() != my_color) {
                    validMoves.add(new ChessMove(position, test_move, null));
                    break;
                } else { /* Is my_color. Don't add and stop in this direction */
                    break;
                }
            } else {
                break;
            }
        }
        /* Check Moves Down */
        for (int i=1; i <= max_moves; i++) {
            ChessPosition test_move = new ChessPosition(row-i, col);
            int test_row = test_move.getRow();
            int test_col = test_move.getColumn();
            if (test_row <= 8 && test_col <= 8 && test_row >= 1 && test_col >= 1) {
                if (board.getPiece(test_move) == null) {
                    validMoves.add(new ChessMove(position, test_move, null));
                } else if (board.getPiece(test_move).getTeamColor() != my_color) {
                    validMoves.add(new ChessMove(position, test_move, null));
                    break;
                } else { /* Is my_color. Don't add and stop in this direction */
                    break;
                }
            } else {
                break;
            }
        }
        return validMoves;
    }
}
