package net;

import exception.ResponseException;
import servicemodel.*;
import ui.ServerMessageObserver;

import java.io.IOException;

// Calls ClientCommunicator for http calls
public class ServerFacade {

    private final HttpCommunicator httpCom;
    private final WebsocketCommunicator websocketCom;
    private final ServerMessageObserver observer;
    private String authToken = null;

    public ServerFacade(int port, ServerMessageObserver observer) throws ResponseException {
        httpCom = new HttpCommunicator("http://localhost:"+port);
        this.observer = observer;
        websocketCom = new WebsocketCommunicator("ws://localhost:"+port, observer);
    }

    public LoginResult login(LoginRequest req) throws ResponseException {
        var path = "/session";
        var res = httpCom.makeRequest("POST", path, req, LoginResult.class, null);
        authToken = res.authToken();
        return res;
    }

    public RegisterResult register(RegisterRequest req) throws ResponseException {
        var path = "/user";
        var res = httpCom.makeRequest("POST", path, req, RegisterResult.class, null);
        authToken = res.authToken();
        return res;
    }

    public LogoutResult logout(LogoutRequest req) throws ResponseException {
        var path = "/session";
        var res = httpCom.makeRequest("DELETE", path, req, LogoutResult.class, req.authToken());
        authToken = null;
        return res;
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
        JoinGameResult res = httpCom.makeRequest("PUT", path, req, JoinGameResult.class, req.authToken());
        try {
            websocketCom.connect(authToken, req.gameID());
        } catch (IOException e) {
            throw new ResponseException(500, e.getMessage());
        }
        return res;
    }

    public void observeGame(JoinGameRequest req) throws ResponseException {
        try {
            websocketCom.connect(authToken, req.gameID());
        } catch (IOException e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

    public void leaveGame(JoinGameRequest req) throws ResponseException {
        try {
            websocketCom.leave(authToken, req.gameID());
        } catch (IOException e) {
            throw new ResponseException(500, e.getMessage());
        }
    }
}
