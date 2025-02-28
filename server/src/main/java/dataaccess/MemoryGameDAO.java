package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;

public class MemoryGameDAO implements GameDAO {

    private final ArrayList<GameData> database;

    public MemoryGameDAO() {
        database = new ArrayList<>();
    }

    public void clearGame() {
        database.clear();
    }
    public GameData createGame(String gameName) {
        int gameID = database.size();
        ChessGame game = new ChessGame();
        GameData newGameData = new GameData(gameID, "", "", gameName, game);
        database.add(newGameData);
        return newGameData;
    }
    public GameData getGame(int gameID) {
        return database.get(gameID);
    }
    public ArrayList<GameData> listGames() {
        return database;
    }
    public void updateGame(String playerColor, int gameID, String username) {
        GameData gameInfo = database.get(gameID);
        String whiteUsername = gameInfo.whiteUsername();
        String blackUsername = gameInfo.blackUsername();
        String gameName = gameInfo.gameName();
        if (playerColor.equalsIgnoreCase("BLACK")) {
            database.set(gameID, new GameData(gameID, whiteUsername, username, gameName, gameInfo.game()));
        } else {
            database.set(gameID, new GameData(gameID, username, blackUsername, gameName, gameInfo.game()));
        }
    }
}
