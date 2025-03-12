package service;

import dataaccess.DataAccessException;
import dataaccess.SQLAuthDAO;
import dataaccess.SQLGameDAO;
import dataaccess.SQLUserDAO;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import servicemodel.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceTests {

    @BeforeEach
    public void clearDatabase() {
        try {
            new SQLUserDAO().clearUser();
            new SQLGameDAO().clearGame();
            new SQLAuthDAO().clearAuth();
        } catch (DataAccessException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Test
    public void registerSuccess() {
        Service userService = new Service();
        RegisterRequest req = new RegisterRequest("jason", "secretpassword", "me@gmail.com");
        var expected = new RegisterResult("jason", "", null);
        RegisterResult actual = userService.register(req);
        assertEquals(expected.username(), actual.username());
        assertEquals(expected.message(), actual.message());
    }

    @Test
    public void registerFail() {
        Service userService = new Service();
        userService.register(new RegisterRequest("jason", "secretpassword", "me@gmail.com"));
        RegisterRequest req = new RegisterRequest("jason", "differentpassword", "idk@emails.com");

        var expected = new RegisterResult(null, null, "Error: already taken");
        RegisterResult actual = userService.register(req);

        assertEquals(expected, actual);
    }

    @Test
    public void clearSuccess() {
        Service service = new Service();
        RegisterRequest request = new RegisterRequest("jason", "secretpassword", "me@gmail.com");
        service.register(request);
        service.clear();
        var res = service.register(request);
        assertNull(res.message());
    }

    @Test
    public void loginSuccess() {
        Service service = new Service();
        service.register(new RegisterRequest("jason", "mypassword", "myemail@gmail.com"));
        LoginRequest request = new LoginRequest("jason", "mypassword");
        LoginResult expected = new LoginResult("jason", "", null);
        LoginResult actual = service.login(request);
        assertEquals(expected.username(), actual.username());
        assertNull(actual.message());
    }

    @Test
    public void loginFail() {
        Service service = new Service();
        LoginRequest request = new LoginRequest("jason", "mypassword");
        LoginResult expected = new LoginResult(null, null, "Error: unauthorized");
        LoginResult actual = service.login(request);
        assertEquals(expected, actual);
    }

    @Test
    public void logoutSuccess() {
        Service service = new Service();
        var regResult = service.register(new RegisterRequest("jason", "mypass", "email@email.com"));
        LogoutRequest req = new LogoutRequest(regResult.authToken());
        LogoutResult res = service.logout(req);
        assertNull(res.message());
        LogoutResult res2 = service.logout(req);
        assertEquals("Error: unauthorized", res2.message());
    }

    @Test
    public void logoutFail() {
        Service service = new Service();
//        var regResult = service.register(new RegisterRequest("jason", "mypass", "email@email.com"));
        LogoutRequest req = new LogoutRequest("fakeAuthToken");
        LogoutResult res = service.logout(req);
        assertEquals("Error: unauthorized", res.message());
    }

    @Test
    public void listGamesSuccess() {
        Service service = new Service();
        RegisterResult regResult = service.register(new RegisterRequest("username", "password", "email"));
        String authToken = regResult.authToken();

        CreateGameRequest createGameRequest = new CreateGameRequest(authToken, "newGame");
        service.createGame(createGameRequest);

        var gameList = new ArrayList<GameData>();
        gameList.add(new GameData(1, null, null, "newGame", null));
        var expected = new ListResult(gameList, null);

        ListRequest listRequest = new ListRequest(authToken);
        var actual = service.listGames(listRequest);

        assertEquals(expected.games().size(), actual.games().size());
    }

    @Test public void listGamesFail() {
        Service service = new Service();
        ListRequest req = new ListRequest("authToken");
        ListResult res = service.listGames(req);
        assertEquals("Error: unauthorized", res.message());
    }

    @Test public void createGameSuccess() {
        Service service = new Service();
        RegisterResult regResult = service.register(new RegisterRequest("username", "password", "email"));
        String authToken = regResult.authToken();

        CreateGameRequest createGameRequest = new CreateGameRequest(authToken, "game name");
        var expected = new CreateGameResult(1, null);
        var actual = service.createGame(createGameRequest);

        assertEquals(expected.message(), actual.message());
    }

    @Test public void createGameFail() {
        Service service = new Service();
        CreateGameRequest createGameRequest = new CreateGameRequest("bad auth token", "game name");
        var expected = new CreateGameResult(null, "Error: unauthorized");
        var actual = service.createGame(createGameRequest);
        assertEquals(expected, actual);

        RegisterResult regResult = service.register(new RegisterRequest("username", "password", "email"));
        String authToken = regResult.authToken();
        createGameRequest = new CreateGameRequest(authToken, null);
        expected = new CreateGameResult(null, "Error: bad request");
        actual = service.createGame(createGameRequest);
        assertEquals(expected, actual);
    }

    @Test public void joinGameSuccess() {
        Service service = new Service();
        RegisterResult regResult = service.register(new RegisterRequest("username", "password", "email"));
        String authToken = regResult.authToken();

        CreateGameRequest createGameRequest = new CreateGameRequest(authToken, "game name");
        var createGameResult = service.createGame(createGameRequest);

        var joinRequest = new JoinGameRequest(authToken, "BLACK", createGameResult.gameID());
        var expected = new JoinGameResult(null);
        var actual = service.joinGame(joinRequest);
        assertEquals(expected, actual);
        ListRequest listRequest = new ListRequest(authToken);
        GameData game = service.listGames(listRequest).games().getFirst();
        GameData expectedGame = new GameData(createGameResult.gameID(), null, "username", "game name", null);

        assertEquals(game, expectedGame);
    }

    @Test public void joinGameFail() {
        Service service = new Service();
        RegisterResult regResult = service.register(new RegisterRequest("username", "password", "email"));
        String authToken = regResult.authToken();

        CreateGameRequest createGameRequest = new CreateGameRequest(authToken, "game name");
        var createGameResult = service.createGame(createGameRequest);

        var joinRequest = new JoinGameRequest(authToken, "BLACK", createGameResult.gameID());
        service.joinGame(joinRequest);
        var expected = new JoinGameResult("Error: already taken");
        var actual = service.joinGame(joinRequest);

        assertEquals(expected, actual);
    }
}
