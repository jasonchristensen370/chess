package client;

import dataaccess.DataAccessException;
import dataaccess.SQLAuthDAO;
import dataaccess.SQLGameDAO;
import dataaccess.SQLUserDAO;
import exception.ResponseException;
import net.ServerFacade;
import org.junit.jupiter.api.*;
import server.Server;
import servicemodel.*;


import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {
    static ServerFacade facade;

    private static Server server;

    @BeforeAll
    public static void init() {
        server = new Server();
        int port = server.run(8081);
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
}
