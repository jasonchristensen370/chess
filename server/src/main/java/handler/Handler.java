package handler;

import com.google.gson.Gson;
import model.*;
import service.GameService;
import service.UserService;

public class Handler {
    private final UserService userService;
    private final GameService gameService;

    public Handler() {
        userService = new UserService();
        gameService = new GameService();
    }

    public String clear() {
        userService.clear();
        gameService.clear();
        return "{}";
    }

    public String register(String body) {
        var serializer = new Gson();
        RegisterRequest req = serializer.fromJson(body, RegisterRequest.class);
        RegisterResult result = userService.register(req);
        return serializer.toJson(result);
    }

    public String login(String body) {
        var serializer = new Gson();
        LoginRequest req = serializer.fromJson(body, LoginRequest.class);
        LoginResult result = userService.login(req);
        return serializer.toJson(result);
    }

    public String logout(String authToken) {
        var serializer = new Gson();
        LogoutRequest req = new LogoutRequest(authToken);
        LogoutResult result = userService.logout(req);
        return serializer.toJson(result);
    }

    public String listGames(String body) {
        return "{\"message\": \"Error: Not Implemented\"}";
    }

    public String createGame(String body, String authToken) {
        return "{\"message\": \"Error: Not Implemented\"}";
    }

    public String joinGame(String body, String authToken) {
        return "{\"message\": \"Error: Not Implemented\"}";
    }

}
