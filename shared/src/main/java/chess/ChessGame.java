package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import static java.lang.Math.abs;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private ChessBoard board = new ChessBoard();
    private TeamColor teamTurn;
    private ChessPosition enPassantPosition;

    public ChessGame() {
        board.resetBoard();
        teamTurn = TeamColor.WHITE;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(board, chessGame.board) && teamTurn == chessGame.teamTurn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, teamTurn);
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = board.getPiece(startPosition);
        if (piece == null) {
            return null;
        }
        ChessPiece.PieceType pieceType = piece.getPieceType();
        ChessGame.TeamColor turn = piece.getTeamColor();
        Collection<ChessMove> possibleMoves = piece.pieceMoves(board, startPosition);
        Collection<ChessMove> validMoves = new ArrayList<>();
        // Loop through all possible moves and make sure it doesn't put the king in check.
        for (ChessMove move : possibleMoves) {
            // Clone the board
            ChessGame testGame = new ChessGame();
            ChessBoard testBoard = board.clone();
            ChessPiece testPiece = testBoard.getPiece(startPosition);
            // Make the move
            testBoard.addPiece(move.getEndPosition(), testPiece);
            testBoard.addPiece(move.getStartPosition(), null);
            testGame.setBoard(testBoard);
            // If not in check, add it to validMoves
            if (testGame.isInCheck(turn)) {
                continue;
            }
            validMoves.add(move);
        }
        // Castling Logic for King moves
        if (pieceType == ChessPiece.PieceType.KING) {
            int row = (turn == TeamColor.WHITE) ? 1 : 8;
            if (canCastle(turn, -1)) {
                validMoves.add(new ChessMove(startPosition, new ChessPosition(row,3), null));
            }
            if (canCastle(turn, 1)) {
                validMoves.add(new ChessMove(startPosition, new ChessPosition(row,7), null));
            }
        }
        // En Passant Logic for Pawn moves
        if (pieceType == ChessPiece.PieceType.PAWN && enPassantPosition != null) {
            int targetRow = enPassantPosition.getRow();
            int targetColumn = enPassantPosition.getColumn();
            int startRow = startPosition.getRow();
            int startColumn = startPosition.getColumn();
            int direction = (turn == TeamColor.WHITE) ? 1 : -1;
            if (abs(startColumn-targetColumn) == 1 && startRow == targetRow) {
                validMoves.add(new ChessMove(startPosition, new ChessPosition(targetRow+direction, targetColumn), null));
            }
        }
        return validMoves;
    }

    private boolean canCastle(TeamColor teamColor, int direction) {
        int row = (teamColor == TeamColor.WHITE) ? 1 : 8;
        ChessPosition startPosition = new ChessPosition(row,5);
        ChessPiece kingPiece = board.getPiece(startPosition);
        int rookCol = (direction == -1) ? 1 : 8;
        ChessPiece rook = board.getPiece(new ChessPosition(row, rookCol));
        if (kingPiece == null || rook == null) {
            return false;
        } else if (kingPiece.getPieceType() != ChessPiece.PieceType.KING || rook.getPieceType() != ChessPiece.PieceType.ROOK) {
            return false;
        } else if (kingPiece.hasMoved) {
            return false;
        }
        // Check empty spaces to the left
        int[] colParams = switch (direction) {
            case -1 -> new int[]{2, 4};
            case 1 -> new int[]{6, 7};
            default -> throw new IllegalArgumentException("Invalid direction: " + direction);
        };
        boolean canCastle = true;
        for (int col = colParams[0]; col <= colParams[1]; col++) {
            ChessPosition testPosition = new ChessPosition(row, col);
            if (board.getPiece(testPosition) != null || rook.hasMoved) {
                canCastle = false;
                break;
            } else {
                // Look for check restrictions
                ChessGame testGame = new ChessGame();
                ChessBoard testBoard = board.clone();
                ChessPiece testPiece = testBoard.getPiece(startPosition);
                testBoard.addPiece(testPosition, testPiece);
                testBoard.addPiece(startPosition, null);
                testGame.setBoard(testBoard);
                // If in check, can't castle
                if (testGame.isInCheck(teamColor)) {
                    canCastle = false;
                }
            }
        }
        return canCastle;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition startPosition = move.getStartPosition();
        ChessPosition endPosition = move.getEndPosition();
        ChessPiece.PieceType promoPiece = move.getPromotionPiece();
        ChessPiece piece = (promoPiece != null) ? new ChessPiece(teamTurn, promoPiece) : board.getPiece(startPosition);
        Collection<ChessMove> validMoves = validMoves(startPosition);
        // Check if the move is a valid move for the team whose turn it is
        if (validMoves != null && validMoves.contains(move) && piece.getTeamColor() == teamTurn) {
            int colDistance = endPosition.getColumn() - startPosition.getColumn();
            int rowDistance = endPosition.getRow() - startPosition.getRow();
            // Castling Move Logic
            if (piece.getPieceType() == ChessPiece.PieceType.KING && abs(colDistance) == 2) {
                ChessPosition rookStartPosition;
                ChessPosition rookEndPosition;
                if (colDistance < 0) {
                    // Castling Queen-side
                    rookStartPosition = new ChessPosition(startPosition.getRow(), 1);
                    rookEndPosition = new ChessPosition(endPosition.getRow(), 4);
                } else {
                    // Castling King-side
                    rookStartPosition = new ChessPosition(startPosition.getRow(), 8);
                    rookEndPosition = new ChessPosition(endPosition.getRow(), 6);
                }
                board.addPiece(rookEndPosition, board.getPiece(rookStartPosition));
                board.addPiece(rookStartPosition, null);
            }
            // En Passant Move Logic
            if (piece.getPieceType() == ChessPiece.PieceType.PAWN) {
                if (abs(rowDistance) == 2) {
                    enPassantPosition = endPosition;
                } else {
                    if (abs(colDistance) == 1 && abs(rowDistance) == 1 && board.getPiece(endPosition) == null) {
                        // Pawn is capturing en passant, remove pawn to be captured
                        board.addPiece(enPassantPosition, null);
                    }
                    // En passant not possible
                    enPassantPosition = null;
                }
            }
            // Move normally
            board.addPiece(endPosition, piece);
            board.addPiece(startPosition, null);
            piece.hasMoved = true;
            teamTurn = (teamTurn == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;
        } else {
            throw new InvalidMoveException("Invalid move: " + move);
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = board.findKing(teamColor);
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition position = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(position);
                if (piece == null || piece.getTeamColor() == teamColor) {
                    continue;
                }
                for (ChessMove move : piece.pieceMoves(board, position)) {
                    if (move.getEndPosition().equals(kingPosition)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        // Loop through all piece moves for teamColor
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition position = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(position);
                if (piece == null || piece.getTeamColor() != teamColor) {
                    continue;
                }
                for (ChessMove move : board.getPiece(position).pieceMoves(board, position)) {
                    ChessGame testGame = new ChessGame();
                    ChessBoard testBoard = board.clone();
                    testGame.setBoard(testBoard);
                    testBoard.addPiece(move.getEndPosition(), testBoard.getPiece(position));
                    testBoard.addPiece(move.getStartPosition(), null);
                    if (!testGame.isInCheck(teamColor)) {
                        return false;
                    }
                }

            }
        }
        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        ChessPosition kingPosition = board.findKing(teamColor);
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition position = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(position);
                if (piece != null && piece.getTeamColor() == teamColor) {
                    Collection<ChessMove> validMoves = validMoves(position);
                    if (!validMoves.isEmpty()) {
                        return false;
                    }
                }
            }
        }
        Collection<ChessMove> kingMoves = validMoves(kingPosition);
        return (kingMoves == null || kingMoves.isEmpty()) && !isInCheck(teamColor);
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

    public void printBoard() {
        board.printBoard();
    }
}
