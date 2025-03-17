package client;

import dataaccess.DataAccessException;
import dataaccess.SQLAuthDAO;
import dataaccess.SQLGameDAO;
import dataaccess.SQLUserDAO;
import exception.ResponseException;
import model.UserData;
import net.ServerFacade;
import org.junit.jupiter.api.*;
import server.Server;
import servicemodel.*;
import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {
    static ServerFacade facade;

    private static Server server;

    @BeforeAll
    public static void init() {
        server = new Server();
        int port = server.run(8080);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
    }

    @BeforeEach
    public void clearDatabase() {
        try {
            new SQLUserDAO().clearUser();
            new SQLAuthDAO().clearAuth();
            new SQLGameDAO().clearGame();
        } catch (DataAccessException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Test
    public void registerSuccess() {
        try {
            var req = new RegisterRequest("testUser", "testPass", "test@mail.com");
            var res = facade.register(req);
            assertTrue(res.authToken().length() > 10);
            assertEquals("testUser", res.username());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void registerFail() {
        var req = new RegisterRequest("username", null, "test@mail.com");
        assertThrows(ResponseException.class, () -> facade.register(req));
    }

    @Test
    public void loginSuccess() {
        try {
            new SQLUserDAO().createUser(new UserData("testUser", BCrypt.hashpw("testPass", BCrypt.gensalt()), "test@mail.com"));
            var res = facade.login(new LoginRequest("testUser", "testPass"));
            assertEquals("testUser", res.username());
            assertNull(res.message());
            assertTrue(res.authToken().length() > 10);
        } catch (DataAccessException e) {
            fail("DataAccessException "+e.getMessage());
        } catch (ResponseException e) {
            fail("ResponseException "+e.statusCode()+" "+e.getMessage());
        }
    }

    @Test
    public void loginFail() {
        try {
            facade.login(new LoginRequest("J", "a"));
            fail("Failed to throw exception when user doesn't exist.");
        } catch (ResponseException e) {
            assertEquals("Error: unauthorized", e.getMessage());
            assertEquals(401, e.statusCode());
        }
    }

    @Test
    public void logoutSuccess() {
        try {
            var dao = new SQLAuthDAO();
            new SQLUserDAO().createUser(new UserData("j", BCrypt.hashpw("j", BCrypt.gensalt()), "j"));
            var authData = dao.createAuth("j");
            var res = facade.logout(new LogoutRequest(authData.authToken()));
            assertNull(res.message());
            assertNull(dao.getAuth(authData.authToken()));
        } catch (DataAccessException e) {
            throw new RuntimeException(e.getMessage());
        } catch (ResponseException e) {
            fail(getResponseExceptionMessage(e));
        }
    }

    @Test
    public void logoutFail() {
        try {
            facade.logout(new LogoutRequest("J"));
            fail("Failed to throw exception when authToken is invalid.");
        } catch (ResponseException e) {
            assertEquals("Error: unauthorized", e.getMessage());
            assertEquals(401, e.statusCode());
        }
    }

    @Test
    public void listGamesSuccess() {
        try {
            var dao = new SQLAuthDAO();
            var authToken = dao.createAuth("j").authToken();
            var res = facade.listGames(new ListRequest(authToken));
            assertNull(res.message());
            assertEquals(new ArrayList<>(), res.games());
        } catch (DataAccessException e) {
            throw new RuntimeException(e.getMessage());
        } catch (ResponseException e) {
            fail(getResponseExceptionMessage(e));
        }
    }

    @Test
    public void listGamesFail() {
        try {
            facade.listGames(new ListRequest("J"));
            fail("Failed to throw exception when authToken is invalid.");
        } catch (ResponseException e) {
            assertEquals("Error: unauthorized", e.getMessage());
            assertEquals(401, e.statusCode());
        }
    }

    @Test
    public void createGameSuccess() {
        try {
            var dao = new SQLAuthDAO();
            var authData = dao.createAuth("j");
            var res = facade.createGame(new CreateGameRequest(authData.authToken(), "newGame"));
            assertNull(res.message());
            assertTrue(res.gameID() > 1);
        } catch (DataAccessException e) {
            throw new RuntimeException(e.getMessage());
        } catch (ResponseException e) {
            fail(getResponseExceptionMessage(e));
        }
    }

    @Test
    public void createGameFail() {
        try {
            var dao = new SQLAuthDAO();
            var authData = dao.createAuth("j");
            facade.createGame(new CreateGameRequest(authData.authToken(), null));
            fail("Failed to throw exception on bad request");
        } catch (ResponseException e) {
            assertEquals("Error: bad request", e.getMessage());
            assertEquals(400, e.statusCode());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Test
    public void joinGameSuccess() {
        try {
            var authDAO = new SQLAuthDAO();
            var authData = authDAO.createAuth("J");
            var gameDAO = new SQLGameDAO();
            var gameData = gameDAO.createGame("game 1");
            var res = facade.joinGame(new JoinGameRequest(authData.authToken(), "WHITE", gameData.gameID()));
            assertNull(res.message());
            var getGameData = gameDAO.getGame(gameData.gameID());
            assertEquals("J", getGameData.whiteUsername());
        } catch (ResponseException e) {
            fail(getResponseExceptionMessage(e));
        } catch (DataAccessException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Test
    public void joinGameFail() {
        try {
            facade.joinGame(new JoinGameRequest("J", "WHITE", null));
            fail("Failed to throw exception when gameID is null.");
        } catch (ResponseException e) {
            assertEquals("Error: bad request", e.getMessage());
            assertEquals(400, e.statusCode());
        }
    }

    private String getResponseExceptionMessage(ResponseException e) {
        return "ResponseException "+e.statusCode()+" "+e.getMessage();
    }
}
