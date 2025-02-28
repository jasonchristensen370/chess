package dataaccess;

import chess.ChessGame;
import model.GameData;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class MemoryGameDAOTest {

    @Test
    void clearGame() {
        GameDAO myDAO = new MemoryGameDAO();
        myDAO.createGame("gamename");
        assertEquals(1, myDAO.listGames().size());
        myDAO.clearGame();
        assertEquals(0, myDAO.listGames().size());
    }

    @Test
    void createGame() {
        GameDAO myDAO = new MemoryGameDAO();
        var expected = new GameData(1, null, null, "gamename", new ChessGame());
        var actual = myDAO.createGame("gamename");
        assertEquals(expected, actual);
    }

    @Test
    void getGame() {
        GameDAO myDAO = new MemoryGameDAO();
        assertDoesNotThrow( () -> {
            myDAO.createGame("gamename");
            var expected = new GameData(1, null, null, "gamename", new ChessGame());
            var actual = myDAO.getGame(1);
            assertEquals(expected, actual);
        });
    }

    @Test
    void listGames() {
        GameDAO myDAO = new MemoryGameDAO();
        myDAO.createGame("game1");
        myDAO.createGame("game2");
        myDAO.createGame("game3");
        myDAO.createGame("game4");
        var expected = new ArrayList<>();
        for (int i=1; i<5; i++) {
            expected.add(new GameData(i, null, null, "game"+i, null));
        }
        assertEquals(expected, myDAO.listGames());
    }

    @Test
    void updateGame() {
        GameDAO myDAO = new MemoryGameDAO();
        myDAO.createGame("game1");
        myDAO.createGame("game2");
        myDAO.updateGame("black", 1, "bob");
        var expected = new GameData(1, null, "bob", "game1", new ChessGame());
        var actual = myDAO.getGame(1);
        assertEquals(expected, actual);

    }
}