package dataaccess;

import model.GameData;

import java.sql.SQLException;
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
        return new GameData(1, null, null, null, null);
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
