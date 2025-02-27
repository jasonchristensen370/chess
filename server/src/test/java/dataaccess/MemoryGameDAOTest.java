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
        var expected = new GameData(0, "", "", "gamename", new ChessGame());
        var actual = myDAO.createGame("gamename");
        assertEquals(expected, actual);
    }

    @Test
    void getGame() {
        GameDAO myDAO = new MemoryGameDAO();
        assertDoesNotThrow( () -> {
            myDAO.createGame("gamename");
            var expected = new GameData(0, "", "", "gamename", new ChessGame());
            var actual = myDAO.getGame(0);
            assertEquals(expected, actual);
        });
    }

    @Test
    void listGames() {
        GameDAO myDAO = new MemoryGameDAO();
        myDAO.createGame("game0");
        myDAO.createGame("game1");
        myDAO.createGame("game2");
        myDAO.createGame("game3");
        var expected = new ArrayList<>();
        for (int i=0; i<4; i++) {
            expected.add(new GameData(i, "", "", "game"+i, new ChessGame()));
        }
        assertEquals(expected, myDAO.listGames());
    }

    @Test
    void updateGame() {
        GameDAO myDAO = new MemoryGameDAO();
        myDAO.createGame("game0");
        myDAO.createGame("game1");
        assertDoesNotThrow( () -> {
            myDAO.updateGame("black", 1, "bob");
            var expected = new GameData(1, "", "bob", "game1", new ChessGame());
            var actual = myDAO.getGame(1);
            assertEquals(expected, actual);
        });
    }
}