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
            fail("ResponseException "+e.statusCode()+" "+e.getMessage());
        }
    }

    @Test
    public void logoutFail() {
        try {
            facade.logout(new LogoutRequest("J"));
            fail("Failed to throw exception when user doesn't exist.");
        } catch (ResponseException e) {
            assertEquals("Error: unauthorized", e.getMessage());
            assertEquals(401, e.statusCode());
        }
    }
}
