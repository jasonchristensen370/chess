package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;

public class MemoryGameDAO implements GameDAO {
    private final int gameIDOffset;
    private final ArrayList<GameData> database;

    public MemoryGameDAO() {
        database = new ArrayList<>();
        gameIDOffset = 1;
    }

    public void clearGame() {
        database.clear();
    }
    public GameData createGame(String gameName) {
        int gameID = database.size()+gameIDOffset;
        ChessGame game = new ChessGame();
        GameData newGameData = new GameData(gameID, "", "", gameName, game);
        database.add(newGameData);
        return newGameData;
    }
    public GameData getGame(int gameID) {
        return database.get(gameID-gameIDOffset);
    }
    public ArrayList<GameData> listGames() {
        ArrayList<GameData> newList = new ArrayList<>();
        for (GameData data : database) {
            newList.add(new GameData(data.gameID(), data.whiteUsername(), data.blackUsername(), data.gameName(), null));
        }
        return newList;
    }
    public void updateGame(String playerColor, int gameID, String username) {
        GameData gameInfo = database.get(gameID-gameIDOffset);
        String whiteUsername = gameInfo.whiteUsername();
        String blackUsername = gameInfo.blackUsername();
        String gameName = gameInfo.gameName();
        if (playerColor.equalsIgnoreCase("BLACK")) {
            database.set(gameID-gameIDOffset, new GameData(gameID, whiteUsername, username, gameName, gameInfo.game()));
        } else {
            database.set(gameID-gameIDOffset, new GameData(gameID, username, blackUsername, gameName, gameInfo.game()));
        }
    }
}
