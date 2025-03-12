package dataaccess;

import model.GameData;

import java.util.ArrayList;

public interface GameDAO {
    void clearGame() throws DataAccessException;
    GameData createGame(String gameName) throws DataAccessException;
    GameData getGame(int gameID) throws DataAccessException;
    ArrayList<GameData> listGames() throws DataAccessException;
    void updateGame(String playerColor, int gameID, String username) throws DataAccessException;
}
