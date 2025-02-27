package dataaccess;

import model.GameData;

import java.util.ArrayList;

public interface GameDAO {
    void clearGame();
    GameData createGame(String gameName);
    GameData getGame(int gameID);
    ArrayList<GameData> listGames();
    void updateGame(String playerColor, int gameID, String Username) throws DataAccessException;
}
