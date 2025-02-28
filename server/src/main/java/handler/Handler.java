package handler;

import com.google.gson.Gson;
import model.RegisterRequest;
import model.RegisterResult;
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

}
