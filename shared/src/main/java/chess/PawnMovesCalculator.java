package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalculator  implements PieceMovesCalculator {
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> validMoves = new ArrayList<>();
        int start_row = position.getRow();
        int start_column = position.getColumn();
        ChessGame.TeamColor my_color = board.getPiece(position).getTeamColor();
        int direction = (my_color == ChessGame.TeamColor.BLACK) ? -1 : 1;

        ChessPosition[] test_moves_attack = {new ChessPosition(start_row+direction, start_column+1),
                                             new ChessPosition(start_row+direction, start_column-1)};

        /* Normal Moves */
        if ((start_row == 2 && my_color == ChessGame.TeamColor.WHITE) || (start_row == 7 && my_color == ChessGame.TeamColor.BLACK)) {
            var max_moves = 2;
            for (int i=1; i <= max_moves; i++) {
                ChessPosition test_move = new ChessPosition(start_row+i*direction, start_column);
                int test_row = test_move.getRow();
                int test_col = test_move.getColumn();
                if (test_row <= 8 && test_col <= 8 && test_row >= 1 && test_col >= 1) {
                    if (board.getPiece(test_move) == null) {
                        validMoves.add(new ChessMove(position, test_move, null));
                    } else { /* Is my_color. Don't add and stop in this direction */
                        break;
                    }
                } else {
                    break;
                }
            }
        } else {
            ChessPosition test_move = new ChessPosition(start_row+direction, start_column);
            int test_row = test_move.getRow();
            int test_col = test_move.getColumn();
            if (test_row <= 8 && test_col <= 8 && test_row >= 1 && test_col >= 1 && board.getPiece(test_move) == null) {
                if (test_row == 1 || test_row == 8) {
                    validMoves.add(new ChessMove(position, test_move, ChessPiece.PieceType.ROOK));
                    validMoves.add(new ChessMove(position, test_move, ChessPiece.PieceType.KNIGHT));
                    validMoves.add(new ChessMove(position, test_move, ChessPiece.PieceType.BISHOP));
                    validMoves.add(new ChessMove(position, test_move, ChessPiece.PieceType.QUEEN));
                } else {
                    validMoves.add(new ChessMove(position, test_move, null));
                }

            }
        }

        /* Attacking Moves */
        for (ChessPosition test_move : test_moves_attack) {
            int test_row = test_move.getRow();
            int test_col = test_move.getColumn();
            if (test_row <= 8 && test_col <= 8 && test_row >= 1 && test_col >= 1 && board.getPiece(test_move) != null && board.getPiece(test_move).getTeamColor() != my_color) {
                if (test_row == 1 || test_row == 8) {
                    validMoves.add(new ChessMove(position, test_move, ChessPiece.PieceType.ROOK));
                    validMoves.add(new ChessMove(position, test_move, ChessPiece.PieceType.KNIGHT));
                    validMoves.add(new ChessMove(position, test_move, ChessPiece.PieceType.BISHOP));
                    validMoves.add(new ChessMove(position, test_move, ChessPiece.PieceType.QUEEN));
                } else {
                    validMoves.add(new ChessMove(position, test_move, null));
                }
            }
        }

        return validMoves;
    }
}


/*
int count = 0;
        for (ChessPosition test_move : test_moves_normal) {
            int test_row = test_move.getRow();
            int test_col = test_move.getColumn();
            if (count == 0 && ((start_row != 2 && my_color == ChessGame.TeamColor.WHITE) || (start_row != 7 && my_color == ChessGame.TeamColor.BLACK))) {
                continue;
            }
            if (test_row <= 8 && test_col <= 8 && test_row >= 1 && test_col >= 1 && board.getPiece(test_move) == null) {
                if (test_row == 1 || test_row == 8) {
                    promo_piece = ChessPiece.PieceType.QUEEN;
                } else {
                    promo_piece = null;
                }
                validMoves.add(new ChessMove(position, test_move, promo_piece));
            }
            count++;
        }
        for (ChessPosition test_move : test_moves_attack) {
            int test_row = test_move.getRow();
            int test_col = test_move.getColumn();
            if (test_row <= 8 && test_col <= 8 && test_row >= 1 && test_col >= 1 && board.getPiece(test_move) != null && board.getPiece(test_move).getTeamColor() != my_color) {
                if (test_row == 1 || test_row == 8) {
                    promo_piece = ChessPiece.PieceType.QUEEN;
                } else {
                    promo_piece = null;
                }
                validMoves.add(new ChessMove(position, test_move, promo_piece));
            }
        }
 */