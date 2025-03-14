package net;

import servicemodel.*;

// Calls ClientCommunicator for http calls
public class ServerFacade {
    public LoginResult login(LoginRequest req) {
        return new LoginResult(null, null, "Error: Not Implemented");
    }

    public RegisterResult register(RegisterRequest req) {
        return new RegisterResult(null, null, "Error: Not Implemented");
    }

    public LogoutResult logout(LogoutRequest req) {
        return new LogoutResult("Error: Not Implemented");
    }

    public ListResult listGames(ListRequest req) {
        return new ListResult(null, "Error: Not Implemented");
    }

    public CreateGameResult createGame(CreateGameRequest req) {
        return new CreateGameResult(null, "Error: Not Implemented");
    }

    public JoinGameResult joinGame(JoinGameRequest req) {
        return new JoinGameResult("Error: Not Implemented");
    }
}
