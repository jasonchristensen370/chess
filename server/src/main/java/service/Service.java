package service;

import dataaccess.*;
import model.AuthData;
import model.*;
import servicemodel.*;

import java.util.ArrayList;
import java.util.List;

public class Service {
    private final AuthDAO authDAO;
    private final UserDAO userDAO;
    private final GameDAO gameDAO;

    public Service() {
        authDAO = new SQLAuthDAO();
        userDAO = new SQLUserDAO();
        gameDAO = new SQLGameDAO();
    }

    public void clear() {
        try {
            authDAO.clearAuth();
            userDAO.clearUser();
            gameDAO.clearGame();
        } catch (DataAccessException _) {
        }
    }

    public RegisterResult register(RegisterRequest req) {
        try {
            if (req.email() == null || req.username() == null || req.password() == null) {
                return new RegisterResult(null, null, "Error: bad request");
            } else if (userDAO.getUser(req.username()) != null) {
                return new RegisterResult(null, null, "Error: already taken");
            }
            UserData newUserData = new UserData(req.username(), req.password(), req.email());
            userDAO.createUser(newUserData);
            AuthData authData = authDAO.createAuth(req.username());
            return new RegisterResult(authData.username(), authData.authToken(), null);
        } catch (DataAccessException e) {
            return new RegisterResult(null, null, e.getMessage());
        }
    }

    public LoginResult login(LoginRequest req) {
        try {
            UserData userData = userDAO.getUser(req.username());
            if (userData == null || !userData.password().equals(req.password())) {
                return new LoginResult(null, null, "Error: unauthorized");
            }
            AuthData authData = authDAO.createAuth(req.username());
            return new LoginResult(authData.username(), authData.authToken(), null);
        } catch (DataAccessException e) {
            return new LoginResult(null, null, e.getMessage());
        }
    }

    public LogoutResult logout(LogoutRequest req) {
        AuthData authData = authDAO.getAuth(req.authToken());
        if (authData == null) {
            return new LogoutResult("Error: unauthorized");
        }
        authDAO.deleteAuth(authData.authToken());
        return new LogoutResult(null);
    }

    public ListResult listGames(ListRequest req) {
        AuthData authData = authDAO.getAuth(req.authToken());
        if (authData == null) {
            return new ListResult(null, "Error: unauthorized");
        }
        ArrayList<GameData> games = gameDAO.listGames();
        return new ListResult(games, null);
    }

    public CreateGameResult createGame(CreateGameRequest req) {
        AuthData authData = authDAO.getAuth(req.authToken());
        if (req.gameName() == null) {
            return new CreateGameResult(null, "Error: bad request");
        } else if (authData == null) {
            return new CreateGameResult(null, "Error: unauthorized");
        }
        GameData gameData = gameDAO.createGame(req.gameName());
        return new CreateGameResult(gameData.gameID(), null);
    }

    // Join Game Request Members: (String authToken, String playerColor, Integer gameID)
    public JoinGameResult joinGame(JoinGameRequest req) {
        if (req.gameID() == null || req.playerColor() == null || req.authToken() == null) {
            return new JoinGameResult("Error: bad request");
        }
        ArrayList<String> allowedColors = new ArrayList<>(List.of("WHITE","BLACK"));
        AuthData authData = authDAO.getAuth(req.authToken());
        if (authData == null) {
            return new JoinGameResult("Error: unauthorized");
        }
        GameData gameData = gameDAO.getGame(req.gameID());
        if (gameData == null || !allowedColors.contains(req.playerColor())) {
            return new JoinGameResult("Error: bad request");
        }
        if (playerColorTaken(req.playerColor(), gameData)) {
            return new JoinGameResult("Error: already taken");
        }
        gameDAO.updateGame(req.playerColor(), req.gameID(), authData.username());
        return new JoinGameResult(null);
    }

    private boolean playerColorTaken(String playerColor, GameData gameData) {
        boolean whiteTaken = gameData.whiteUsername() != null;
        boolean blackTaken = gameData.blackUsername() != null;
        return playerColor.equals("WHITE") ? whiteTaken : blackTaken;
    }

}
