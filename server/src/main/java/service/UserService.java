package service;

import dataaccess.*;
import model.AuthData;
import model.*;

public class UserService {
    private final AuthDAO authDAO;
    private final UserDAO userDAO;

    public UserService() {
        authDAO = new MemoryAuthDAO();
        userDAO = new MemoryUserDAO();
    }

    public void clear() {
        authDAO.clearAuth();
        userDAO.clearUser();
    }

    public RegisterResult register(RegisterRequest req) {
        if (userDAO.getUser(req.username()) != null) {
            return new RegisterResult(null, null, "Error: already taken");
        }
        UserData newUserData = new UserData(req.username(), req.password(), req.email());
        userDAO.createUser(newUserData);
        AuthData authData = authDAO.createAuth(req.username());
        return new RegisterResult(authData.username(), authData.authToken(), null);
    }

    public LoginResult login(LoginRequest req) {
        UserData userData = userDAO.getUser(req.username());
        if (userData == null) {
            return new LoginResult(null, null, "Error: unauthorized");
        }
        AuthData authData = authDAO.createAuth(req.username());
        return new LoginResult(authData.username(), authData.authToken(), null);
    }

    public LogoutResult logout(LogoutRequest req) {
        AuthData authData = authDAO.getAuth(req.authToken());
        if (authData == null) {
            return new LogoutResult("Error: unauthorized");
        }
        authDAO.deleteAuth(authData.authToken());
        return new LogoutResult(null);
    }

}
