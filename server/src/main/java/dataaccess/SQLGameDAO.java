package dataaccess;

import model.GameData;

import java.util.ArrayList;

public class SQLGameDAO implements GameDAO{
    public void clearGame() {

    }
    public GameData createGame(String gameName) {
        return new GameData(1, null, null, null, null);
    }
    public GameData getGame(int gameID) {
        return new GameData(1, null, null, null, null);
    }
    public ArrayList<GameData> listGames() {
        return new ArrayList<>();
    }
    public void updateGame(String playerColor, int gameID, String username) {

    }
}
