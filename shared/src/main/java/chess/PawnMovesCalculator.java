package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalculator  implements PieceMovesCalculator {
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        Collection<ChessMove> validMoves = new ArrayList<>();

        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        ChessGame.TeamColor myColor = board.getPiece(myPosition).getTeamColor();
        int direction = (myColor == ChessGame.TeamColor.BLACK) ? -1 : 1;
        int finalRow = (myColor == ChessGame.TeamColor.BLACK) ? 1 : 8;

        ChessPosition[] attackMoves = { new ChessPosition(row+direction, col+1),
                                         new ChessPosition(row+direction, col-1)};

        // For attack moves
        for (ChessPosition testMove : attackMoves) {
            int testRow = testMove.getRow();
            if (MoveHelper.isValidBoardSpace(testMove) && board.getPiece(testMove) != null && board.getPiece(testMove).getTeamColor() != myColor) {
                if (testRow == finalRow) {
                    MoveHelper.addPromotionPieces(myPosition, testMove, validMoves);
                } else {
                    validMoves.add(new ChessMove(myPosition, testMove, null));
                }

            }
        }

        // For normal moves
        int maxMoves = ((row == 2 && myColor == ChessGame.TeamColor.WHITE) || (row == 7 && myColor == ChessGame.TeamColor.BLACK)) ? 2 : 1;
        for (int i=1; i<=maxMoves; i++) {
            ChessPosition testMove = new ChessPosition(row+direction*i, col);
            int testRow = testMove.getRow();
            if (MoveHelper.isValidBoardSpace(testMove)) {
                if (board.getPiece(testMove) == null) {
                    if (testRow == finalRow) {
                        MoveHelper.addPromotionPieces(myPosition, testMove, validMoves);
                    } else {
                        validMoves.add(new ChessMove(myPosition, testMove, null));
                    }
                } else {
                    break;
                }
            }
        }
        return validMoves;
    }
}
