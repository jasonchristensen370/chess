package chess;

import java.util.Collection;

public abstract class MoveHelper {
    public static boolean isValidBoardSpace(ChessPosition position) {
        int row = position.getRow();
        int col = position.getColumn();
        return row <= 8 && col <= 8 && row >= 1 && col >= 1;
    }

    public static boolean canMoveAndCapture(ChessBoard board, ChessPosition testMove, ChessGame.TeamColor myColor) {
        return (board.getPiece(testMove) == null || board.getPiece(testMove).getTeamColor() != myColor);
    }

    public static void addPromotionPieces(ChessPosition startPosition,
                                          ChessPosition endPosition,
                                          Collection<ChessMove> validMoves) {
        validMoves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.QUEEN));
        validMoves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.ROOK));
        validMoves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.KNIGHT));
        validMoves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.BISHOP));
    }

    /* Adds a move to ValidMoves and returns true if a piece can continue, false if it hits a piece or the edge. */
    public static boolean addDirectionalMove(ChessBoard board,
                                             ChessPosition testMove,
                                             ChessGame.TeamColor myColor,
                                             ChessPosition position,
                                             Collection<ChessMove> validMoves) {
        int testRow = testMove.getRow();
        int testCol = testMove.getColumn();
        if (testRow <= 8 && testCol <= 8 && testRow >= 1 && testCol >= 1) {
            if (board.getPiece(testMove) == null) {
                validMoves.add(new ChessMove(position, testMove, null));
            } else if (board.getPiece(testMove).getTeamColor() != myColor) {
                validMoves.add(new ChessMove(position, testMove, null));
                return false;
            } else { /* Is myColor. Don't add and stop in this direction */
                return false;
            }
            return true;
        } else {
            return false;
        }
    }
}
