package handler;

import com.google.gson.Gson;
import model.*;
import service.Service;

public class Handler {
    private final Service service;

    public Handler() {
        service = new Service();
    }

    public String clear() {
        service.clear();
        return "{}";
    }

    public String register(String body) {
        var serializer = new Gson();
        RegisterRequest req = serializer.fromJson(body, RegisterRequest.class);
        RegisterResult result = service.register(req);
        return serializer.toJson(result);
    }

    public String login(String body) {
        var serializer = new Gson();
        LoginRequest req = serializer.fromJson(body, LoginRequest.class);
        LoginResult result = service.login(req);
        return serializer.toJson(result);
    }

    public String logout(String authToken) {
        var serializer = new Gson();
        LogoutRequest req = new LogoutRequest(authToken);
        LogoutResult result = service.logout(req);
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
