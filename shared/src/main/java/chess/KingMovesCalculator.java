package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KingMovesCalculator implements PieceMovesCalculator {

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> validMoves = new ArrayList<>();
        int startRow = position.getRow();
        int startColumn = position.getColumn();
        ChessGame.TeamColor myColor = board.getPiece(position).getTeamColor();

        ChessPosition[] testMoves = {new ChessPosition(startRow-1, startColumn-1),
                                      new ChessPosition(startRow-1, startColumn),
                                      new ChessPosition(startRow-1, startColumn+1),
                                      new ChessPosition(startRow, startColumn-1),
                                      new ChessPosition(startRow, startColumn+1),
                                      new ChessPosition(startRow+1, startColumn-1),
                                      new ChessPosition(startRow+1, startColumn),
                                      new ChessPosition(startRow+1, startColumn+1)};

        for (ChessPosition testMove : testMoves) {
            if (MoveHelper.isValidBoardSpace(testMove) && MoveHelper.canMoveAndCapture(board, testMove, myColor)) {
                validMoves.add(new ChessMove(position, testMove, null));
            }
        }
        return validMoves;
    }
}
