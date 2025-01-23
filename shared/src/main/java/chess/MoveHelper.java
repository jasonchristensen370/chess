package chess;

public abstract class MoveHelper {
    public static boolean isValidBoardSpace(ChessPosition position) {
        int row = position.getRow();
        int col = position.getColumn();
        return row <= 8 && col <= 8 && row >= 1 && col >= 1;
    }

    public static boolean canMoveAndCapture(ChessBoard board, ChessPosition testMove, ChessGame.TeamColor myColor) {
        return (board.getPiece(testMove) == null || board.getPiece(testMove).getTeamColor() != myColor);
    }
}
