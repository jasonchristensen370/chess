package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMovesCalculator  implements PieceMovesCalculator{
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> validMoves = new ArrayList<>();
        int start_row = position.getRow();
        int start_column = position.getColumn();
        ChessGame.TeamColor my_color = board.getPiece(position).getTeamColor();

        ChessPosition[] test_moves = {new ChessPosition(start_row+2, start_column-1),
                new ChessPosition(start_row+2, start_column+1),
                new ChessPosition(start_row-2, start_column-1),
                new ChessPosition(start_row-2, start_column+1),
                new ChessPosition(start_row+1, start_column-2),
                new ChessPosition(start_row+1, start_column+2),
                new ChessPosition(start_row-1, start_column-2),
                new ChessPosition(start_row-1, start_column+2)};

        for (ChessPosition test_move : test_moves) {
            if (MoveHelper.isValidBoardSpace(test_move) && MoveHelper.canMoveAndCapture(board, test_move, my_color)) {
                validMoves.add(new ChessMove(position, test_move, null));
            }
        }
        return validMoves;
    }
}
