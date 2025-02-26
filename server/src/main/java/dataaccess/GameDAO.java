package dataaccess;

import model.GameData;

import java.util.ArrayList;

public interface GameDAO {
    public void clear();
    public GameData createGame();
    public GameData getGame();
    public ArrayList<GameData> listGames();
    public void updateGame();
}
