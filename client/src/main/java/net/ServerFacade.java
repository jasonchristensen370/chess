package net;

import exception.ResponseException;
import servicemodel.*;

// Calls ClientCommunicator for http calls
public class ServerFacade {

    private final HttpCommunicator httpCom;

    public ServerFacade(int port) {
        httpCom = new HttpCommunicator("http://localhost:"+port);
    }

    public LoginResult login(LoginRequest req) throws ResponseException {
        var path = "/session";
        return httpCom.makeRequest("POST", path, req, LoginResult.class, null);
    }

    public RegisterResult register(RegisterRequest req) throws ResponseException {
        var path = "/user";
        return httpCom.makeRequest("POST", path, req, RegisterResult.class, null);
    }

    public LogoutResult logout(LogoutRequest req) throws ResponseException {
        var path = "/session";
        return httpCom.makeRequest("DELETE", path, req, LogoutResult.class, req.authToken());
    }

    public ListResult listGames(ListRequest req) throws ResponseException {
        var path = "/game";
        return httpCom.makeRequest("GET", path, req, ListResult.class, req.authToken());
    }

    public CreateGameResult createGame(CreateGameRequest req) throws ResponseException {
        var path = "/game";
        return httpCom.makeRequest("POST", path, req, CreateGameResult.class, req.authToken());
    }

    public JoinGameResult joinGame(JoinGameRequest req) throws ResponseException {
        var path = "/game";
        return httpCom.makeRequest("PUT", path, req, JoinGameResult.class, req.authToken());
    }
}
