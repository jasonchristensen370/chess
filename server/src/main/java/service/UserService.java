package service;

import dataaccess.*;
import model.AuthData;
import model.RegisterRequest;
import model.RegisterResult;
import model.UserData;

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

}
