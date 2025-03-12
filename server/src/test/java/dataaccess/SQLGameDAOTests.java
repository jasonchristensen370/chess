package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import model.GameData;

import static org.junit.jupiter.api.Assertions.*;

public class SQLGameDAOTests {
    private final GameDAO myDAO = new SQLGameDAO();

    @BeforeAll
    public static void createDatabase() {
        try {
            DatabaseManager.createDatabase();
        } catch (DataAccessException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @BeforeEach
//    @AfterEach
    public void clearGameData() {
        try {
            var statement = "DELETE FROM gameData";
            try(var conn = DatabaseManager.getConnection();
                var prepStatement = conn.prepareStatement(statement)) {
                prepStatement.executeUpdate();
            }
        } catch (DataAccessException | SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Test
    public void clearGameSuccess() {
        try {
            // Put gameData in database
            var statement = "INSERT INTO gameData (gameName, game) VALUES ('myGameName', 'insert_game_here')";
            try (var conn = DatabaseManager.getConnection();
                 var prepStatement = conn.prepareStatement(statement)) {
                prepStatement.executeUpdate();
            }
            myDAO.clearGame();
            statement = "SELECT * FROM gameData";
            try(var conn = DatabaseManager.getConnection();
                var prepStatement = conn.prepareStatement(statement)) {
                var rs = prepStatement.executeQuery();
                assertFalse(rs.next(), "Failed to remove gameData in table");
            }
        } catch (DataAccessException | SQLException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void createGameSuccess() {
        try {
            myDAO.createGame("game1");
            String statement = "SELECT * FROM gameData";
            try(var conn = DatabaseManager.getConnection();
                var prepStatement = conn.prepareStatement(statement)) {
                var rs = prepStatement.executeQuery();
                assertTrue(rs.next());
                assertEquals("game1", rs.getString("gameName"));
                assertTrue(rs.getString("game").length() > 10);
            }
        } catch (DataAccessException | SQLException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void createGameFail() {
        assertThrows(DataAccessException.class, () -> myDAO.createGame(null));
    }

    @Test
    public void getGameSuccess() {
        try {
            var s = new Gson();
            ChessGame game = new ChessGame();
            String jsonGame = s.toJson(game);
            // Put gameData in database
            var statement = "INSERT INTO gameData (gameID, gameName, game) VALUES (1, 'myGameName', ?)";
            try (var conn = DatabaseManager.getConnection();
                 var prepStatement = conn.prepareStatement(statement)) {
                prepStatement.setString(1, jsonGame);
                prepStatement.executeUpdate();
            }
            GameData actual = myDAO.getGame(1);
            assertEquals(1, actual.gameID());
            assertEquals("myGameName", actual.gameName());
        } catch (DataAccessException | SQLException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void getGameFail() {
        try {
            assertNull(myDAO.getGame(0));
        } catch (DataAccessException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void listGamesSuccess() {

    }

    @Test
    public void listGamesFail() {

    }

    @Test
    public void updateGameSuccess() {

    }

    @Test
    public void updateGameFail() {

    }
}
