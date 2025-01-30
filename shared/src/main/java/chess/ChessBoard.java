package chess;


import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard implements Cloneable {

    private ChessPiece[][] board;

    public ChessBoard() {
        board = new ChessPiece[8][8];
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }

    @Override
    public ChessBoard clone() {
        try {
            ChessBoard clone = (ChessBoard) super.clone();
            clone.board = new ChessPiece[8][8];
            for (int r=1; r<=8; r++) {
                for (int c=1; c<=8; c++) {
                    ChessPosition position = new ChessPosition(r, c);
                    if (getPiece(position) == null) {
                        continue;
                    }
                    ChessPiece piece = new ChessPiece(getPiece(position).getTeamColor(), getPiece(position).getPieceType());
                    clone.addPiece(position, piece);
                }
            }
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        board[position.getRow()-1][position.getColumn()-1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return board[position.getRow()-1][position.getColumn()-1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        int row;

        for (ChessGame.TeamColor teamColor : ChessGame.TeamColor.values()) {
            row = (teamColor == ChessGame.TeamColor.BLACK) ? 7 : 0;

            board[row][0] = new ChessPiece(teamColor, ChessPiece.PieceType.ROOK);
            board[row][1] = new ChessPiece(teamColor, ChessPiece.PieceType.KNIGHT);
            board[row][2] = new ChessPiece(teamColor, ChessPiece.PieceType.BISHOP);
            board[row][3] = new ChessPiece(teamColor, ChessPiece.PieceType.QUEEN);
            board[row][4] = new ChessPiece(teamColor, ChessPiece.PieceType.KING);
            board[row][5] = new ChessPiece(teamColor, ChessPiece.PieceType.BISHOP);
            board[row][6] = new ChessPiece(teamColor, ChessPiece.PieceType.KNIGHT);
            board[row][7] = new ChessPiece(teamColor, ChessPiece.PieceType.ROOK);

            row = (teamColor == ChessGame.TeamColor.BLACK) ? 6 : 1;

            for (int col = 0; col < 8; col++) {
                board[row][col] = new ChessPiece(teamColor, ChessPiece.PieceType.PAWN);
            }
        }
    }

    public void printBoard() {
        for (int row = 7; row >= 0; row--) {
            System.out.print((row + 1) + " |");
            for (int col = 0; col < 8; col++) {
                if (board[row][col] != null) {
                    System.out.print(board[row][col] + "|");
                } else {
                    System.out.print(" |");
                }
            }
            System.out.println();
        }
        System.out.println("   1 2 3 4 5 6 7 8");
    }
}
