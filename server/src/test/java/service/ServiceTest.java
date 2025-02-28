package service;

import model.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceTest {
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
        ListRequest listRequest = new ListRequest("authToken");
        CreateGameRequest createGameRequest = new CreateGameRequest("authToken", "newGame");
        service.listGames(listRequest);

    }

    @Test public void listGamesFail() {
        Service service = new Service();
        ListRequest req = new ListRequest("authToken");
        ListResult res = service.listGames(req);
        assertEquals("Error: unauthorized", res.message());
    }

    @Test public void createGameSuccess() {

    }

    @Test public void createGameFail() {

    }

    @Test public void joinGameSuccess() {

    }

    @Test public void joinGameFail() {

    }
}
