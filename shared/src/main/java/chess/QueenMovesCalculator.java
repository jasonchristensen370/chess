package chess;

import java.util.ArrayList;
import java.util.Collection;

public class QueenMovesCalculator implements PieceMovesCalculator {
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {

        PieceMovesCalculator rookCalc = new RookMovesCalculator();
        PieceMovesCalculator bishopCalc = new BishopMovesCalculator();
        Collection<ChessMove> validMoves = new ArrayList<>(rookCalc.pieceMoves(board, position));
        validMoves.addAll(bishopCalc.pieceMoves(board, position));
        return validMoves;
    }
}
