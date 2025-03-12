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
        try {
            String statement = "SELECT * FROM gameData WHERE gameID=?";
            try(var conn = DatabaseManager.getConnection();
                var prepStatement = conn.prepareStatement(statement)) {
                prepStatement.setInt(1, gameID);
                var rs = prepStatement.executeQuery();
                if (rs.next()) {
                    int id = rs.getInt("gameID");
                    String whiteUser = rs.getString("whiteUsername");
                    String blackUser = rs.getString("blackUsername");
                    String gameName = rs.getString("gameName");
                    String jsonGame = rs.getString("game");
                    // Get ChessGame Object from json
                    var serializer = new Gson();
                    ChessGame game = serializer.fromJson(jsonGame, ChessGame.class);
                    return new GameData(id, whiteUser, blackUser, gameName, game);
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public ArrayList<GameData> listGames()  throws DataAccessException{
        return new ArrayList<>();
    }

    public void updateGame(String playerColor, int gameID, String username)  throws DataAccessException{

    }
}
