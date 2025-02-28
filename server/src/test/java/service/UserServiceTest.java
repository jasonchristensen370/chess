package service;

import model.RegisterRequest;
import model.RegisterResult;
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
}
