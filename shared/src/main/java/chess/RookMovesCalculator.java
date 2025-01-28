package chess;

import java.util.ArrayList;
import java.util.Collection;

public class RookMovesCalculator  implements PieceMovesCalculator {
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {

        Collection<ChessMove> validMoves = new ArrayList<>();
        int maxMoves = 7;
        int row = position.getRow();
        int col = position.getColumn();
        ChessGame.TeamColor myColor = board.getPiece(position).getTeamColor();

        /* Check Moves to the Left */
        for (int i=1; i <= maxMoves; i++) {
            ChessPosition testMove = new ChessPosition(row, col-i);
            if (!MoveHelper.addDirectionalMove(board, testMove, myColor, position, validMoves)) {
                break;
            }
        }
        /* Check Moves to the Right */
        for (int i=1; i <= maxMoves; i++) {
            ChessPosition testMove = new ChessPosition(row, col+i);
            if (!MoveHelper.addDirectionalMove(board, testMove, myColor, position, validMoves)) {
                break;
            }
        }
        /* Check Moves Up */
        for (int i=1; i <= maxMoves; i++) {
            ChessPosition testMove = new ChessPosition(row+i, col);
            if (!MoveHelper.addDirectionalMove(board, testMove, myColor, position, validMoves)) {
                break;
            }
        }
        /* Check Moves Down */
        for (int i=1; i <= maxMoves; i++) {
            ChessPosition testMove = new ChessPosition(row-i, col);
            if (!MoveHelper.addDirectionalMove(board, testMove, myColor, position, validMoves)) {
                break;
            }
        }
        return validMoves;
    }
}
