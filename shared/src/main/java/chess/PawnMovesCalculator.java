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

        ChessPosition[] attack_moves = { new ChessPosition(row+direction, col+1),
                                         new ChessPosition(row+direction, col-1)};

        // For attack moves
        for (ChessPosition test_move : attack_moves) {
            int test_row = test_move.getRow();
            int test_col = test_move.getColumn();
            if (test_row <= 8 && test_row >= 1 && test_col <= 8 && test_col >= 1 && board.getPiece(test_move) != null && board.getPiece(test_move).getTeamColor() != myColor) {
                if (test_row == finalRow) {
                    validMoves.add(new ChessMove(myPosition, test_move, ChessPiece.PieceType.QUEEN));
                    validMoves.add(new ChessMove(myPosition, test_move, ChessPiece.PieceType.ROOK));
                    validMoves.add(new ChessMove(myPosition, test_move, ChessPiece.PieceType.KNIGHT));
                    validMoves.add(new ChessMove(myPosition, test_move, ChessPiece.PieceType.BISHOP));
                } else {
                    validMoves.add(new ChessMove(myPosition, test_move, null));
                }

            }
        }

        // For normal moves
        int maxMoves = ((row == 2 && myColor == ChessGame.TeamColor.WHITE) || (row == 7 && myColor == ChessGame.TeamColor.BLACK)) ? 2 : 1;
        for (int i=1; i<=maxMoves; i++) {
            ChessPosition test_move = new ChessPosition(row+direction*i, col);
            int test_row = test_move.getRow();
            int test_col = test_move.getColumn();
            if (test_row <= 8 && test_row >= 1 && test_col <= 8 && test_col >= 1) {
                if (board.getPiece(test_move) == null) {
                    if (test_row == finalRow) {
                        validMoves.add(new ChessMove(myPosition, test_move, ChessPiece.PieceType.QUEEN));
                        validMoves.add(new ChessMove(myPosition, test_move, ChessPiece.PieceType.ROOK));
                        validMoves.add(new ChessMove(myPosition, test_move, ChessPiece.PieceType.KNIGHT));
                        validMoves.add(new ChessMove(myPosition, test_move, ChessPiece.PieceType.BISHOP));
                    } else {
                        validMoves.add(new ChessMove(myPosition, test_move, null));
                    }
                } else {
                    break;
                }
            }
        }
        return validMoves;
    }
}
