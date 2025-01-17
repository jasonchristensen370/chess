package chess;

import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private ChessGame.TeamColor pieceColor;
    private ChessPiece.PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    public String toString() {
        switch (pieceColor) {
            case WHITE:
                switch (type) {
                    case KING:
                        return "K";
                    case QUEEN:
                        return "Q";
                    case BISHOP:
                        return "B";
                    case KNIGHT:
                        return "N";
                    case ROOK:
                        return "R";
                    case PAWN:
                        return "P";
                    default:
                        return "";
                }
            case BLACK:
                switch (type) {
                    case KING:
                        return "k";
                    case QUEEN:
                        return "q";
                    case BISHOP:
                        return "b";
                    case KNIGHT:
                        return "n";
                    case ROOK:
                        return "r";
                    case PAWN:
                        return "p";
                    default:
                        return "";
                }
            default:
                return "Error";
        }

    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        switch (board.getPiece(myPosition).getPieceType()) {
            case KING:
                PieceMovesCalculator calc1 = new KingMovesCalculator();
                return calc1.pieceMoves(board, myPosition);
            case QUEEN:
                PieceMovesCalculator calc2 = new QueenMovesCalculator();
                return calc2.pieceMoves(board, myPosition);
            case BISHOP:
                PieceMovesCalculator calc3 = new BishopMovesCalculator();
                return calc3.pieceMoves(board, myPosition);
            case KNIGHT:
                PieceMovesCalculator calc4 = new KnightMovesCalculator();
                return calc4.pieceMoves(board, myPosition);
            case ROOK:
                PieceMovesCalculator calc5 = new RookMovesCalculator();
                return calc5.pieceMoves(board, myPosition);
            case PAWN:
                PieceMovesCalculator calc6 = new PawnMovesCalculator();
                return calc6.pieceMoves(board, myPosition);
            default:
                return null;
        }
    }
}
