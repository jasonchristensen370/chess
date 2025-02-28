package service;

import dataaccess.*;

public class GameService {
    private final GameDAO gameDAO;

    public GameService() {
        gameDAO = new MemoryGameDAO();
    }

    public void clear() {
        gameDAO.clearGame();
    }
}
