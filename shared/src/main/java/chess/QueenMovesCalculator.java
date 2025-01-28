package chess;

import java.util.ArrayList;
import java.util.Collection;

public class QueenMovesCalculator implements PieceMovesCalculator {
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {

        Collection<ChessMove> validMoves = new ArrayList<>();
        int max_moves = 7;
        int row = position.getRow();
        int col = position.getColumn();
        ChessGame.TeamColor my_color = board.getPiece(position).getTeamColor();

        /* Check Diagonal Moves Down and Left */
        for (int i=1; i <= max_moves; i++) {
            ChessPosition test_move = new ChessPosition(row-i, col-i);
            if (!MoveHelper.addDirectionalMove(board, test_move, my_color, position, validMoves)) {
                break;
            }
        }
        /* Check Diagonal Moves Up and Left */
        for (int i=1; i <= max_moves; i++) {
            ChessPosition test_move = new ChessPosition(row+i, col-i);
            if (!MoveHelper.addDirectionalMove(board, test_move, my_color, position, validMoves)) {
                break;
            }
        }
        /* Check Diagonal Moves Up and Right */
        for (int i=1; i <= max_moves; i++) {
            ChessPosition test_move = new ChessPosition(row+i, col+i);
            if (!MoveHelper.addDirectionalMove(board, test_move, my_color, position, validMoves)) {
                break;
            }
        }
        /* Check Diagonal Moves Down and Right */
        for (int i=1; i <= max_moves; i++) {
            ChessPosition test_move = new ChessPosition(row-i, col+i);
            if (!MoveHelper.addDirectionalMove(board, test_move, my_color, position, validMoves)) {
                break;
            }
        }
        /* Check Moves to the Left */
        for (int i=1; i <= max_moves; i++) {
            ChessPosition test_move = new ChessPosition(row, col-i);
            if (!MoveHelper.addDirectionalMove(board, test_move, my_color, position, validMoves)) {
                break;
            }
        }
        /* Check Moves to the Right */
        for (int i=1; i <= max_moves; i++) {
            ChessPosition test_move = new ChessPosition(row, col+i);
            if (!MoveHelper.addDirectionalMove(board, test_move, my_color, position, validMoves)) {
                break;
            }
        }
        /* Check Moves Up */
        for (int i=1; i <= max_moves; i++) {
            ChessPosition test_move = new ChessPosition(row+i, col);
            if (!MoveHelper.addDirectionalMove(board, test_move, my_color, position, validMoves)) {
                break;
            }
        }
        /* Check Moves Down */
        for (int i=1; i <= max_moves; i++) {
            ChessPosition test_move = new ChessPosition(row-i, col);
            if (!MoveHelper.addDirectionalMove(board, test_move, my_color, position, validMoves)) {
                break;
            }
        }
        return validMoves;
    }
}
