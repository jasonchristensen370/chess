package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class SQLGameDAO implements GameDAO{
    public void clearGame() throws DataAccessException {
        try {
            var statement = "DELETE FROM gameData";
            try(var conn = DatabaseManager.getConnection();
                var prepStatement = conn.prepareStatement(statement)) {
                prepStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
    public GameData createGame(String gameName)  throws DataAccessException{
        try {
            var serializer = new Gson();
            ChessGame game = new ChessGame();
            String jsonGameString = serializer.toJson(game);

            String statement = "INSERT INTO gameData (gameName, game) VALUES (?, ?)";
            try(var conn = DatabaseManager.getConnection();
                var prepStatement = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
                prepStatement.setString(1, gameName);
                prepStatement.setString(2, jsonGameString);
                prepStatement.executeUpdate();
                var resultSet = prepStatement.getGeneratedKeys();
                int gameID = 0;
                if (resultSet.next()) {
                    gameID = resultSet.getInt(1);
                }
                return new GameData(gameID, null, null, gameName, game);
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public GameData getGame(int gameID)  throws DataAccessException{
        return new GameData(1, null, null, null, null);
    }

    public ArrayList<GameData> listGames()  throws DataAccessException{
        return new ArrayList<>();
    }

    public void updateGame(String playerColor, int gameID, String username)  throws DataAccessException{

    }
}
