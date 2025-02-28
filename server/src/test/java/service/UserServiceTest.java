package service;

import model.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {
    @Test
    public void registerSuccess() {
        UserService userService = new UserService();
        RegisterRequest req = new RegisterRequest("jason", "secretpassword", "me@gmail.com");
        var expected = new RegisterResult("jason", "", null);
        RegisterResult actual = userService.register(req);
        assertEquals(expected.username(), actual.username());
        assertEquals(expected.message(), actual.message());
    }

    @Test
    public void registerFail() {
        UserService userService = new UserService();
        userService.register(new RegisterRequest("jason", "secretpassword", "me@gmail.com"));
        RegisterRequest req = new RegisterRequest("jason", "differentpassword", "idk@emails.com");

        var expected = new RegisterResult(null, null, "Error: already taken");
        RegisterResult actual = userService.register(req);

        assertEquals(expected, actual);
    }

    @Test
    public void clearSuccess() {
        UserService service = new UserService();
        RegisterRequest request = new RegisterRequest("jason", "secretpassword", "me@gmail.com");
        service.register(request);
        service.clear();
        var res = service.register(request);
        assertNull(res.message());
    }

    @Test
    public void loginSuccess() {
        UserService service = new UserService();
        service.register(new RegisterRequest("jason", "mypassword", "myemail@gmail.com"));
        LoginRequest request = new LoginRequest("jason", "mypassword");
        LoginResult expected = new LoginResult("jason", "", null);
        LoginResult actual = service.login(request);
        assertEquals(expected.username(), actual.username());
        assertNull(actual.message());
    }

    @Test
    public void loginFail() {
        UserService service = new UserService();
        LoginRequest request = new LoginRequest("jason", "mypassword");
        LoginResult expected = new LoginResult(null, null, "Error: unauthorized");
        LoginResult actual = service.login(request);
        assertEquals(expected, actual);
    }

    @Test
    public void logoutSuccess() {
        UserService service = new UserService();
        var regResult = service.register(new RegisterRequest("jason", "mypass", "email@email.com"));
        LogoutRequest req = new LogoutRequest(regResult.authToken());
        LogoutResult res = service.logout(req);
        assertNull(res.message());
        LogoutResult res2 = service.logout(req);
        assertEquals("Error: unauthorized", res2.message());
    }

    @Test
    public void logoutFail() {
        UserService service = new UserService();
//        var regResult = service.register(new RegisterRequest("jason", "mypass", "email@email.com"));
        LogoutRequest req = new LogoutRequest("fakeAuthToken");
        LogoutResult res = service.logout(req);
        assertEquals("Error: unauthorized", res.message());
    }
}
