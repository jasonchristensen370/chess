package server;

import handler.Handler;
import spark.*;
import java.util.Map;

public class Server {
    private final Handler handler = new Handler();

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", this::clear);
        Spark.post("/user", this::register);
        Spark.post("/session", this::login);
        Spark.delete("/session", this::logout);
        Spark.get("/game", this::listGames);
        Spark.post("/game", this::createGame);
        Spark.put("/game", this::joinGame);

        Spark.awaitInitialization();
        return Spark.port();
    }

    private Object clear(Request req, Response res) {
        String result = handler.clear();
        res.status(getStatusCode(result));
        return result;
    }

    private Object register(Request req, Response res) {
        String body = req.body(); // Returns JSON as string
        String result = handler.register(body);
        res.status(getStatusCode(result));
        return result;
    }

    private Object login(Request req, Response res) {
        String body = req.body();
        String result = handler.login(body);
        res.status(getStatusCode(result));
        return result;
    }

    private Object logout(Request req, Response res) {
        String authToken = req.headers("authorization");
        String result = handler.logout(authToken);
        res.status(getStatusCode(result));
        return result;
    }

    private Object listGames(Request req, Response res) {
        String authToken = req.headers("authorization");
        String result = handler.listGames(authToken);
        res.status(getStatusCode(result));
        return result;
    }

    private Object createGame(Request req, Response res) {
        String authToken = req.headers("authorization");
        String body = req.body();
        String result = handler.createGame(body, authToken);
        res.status(getStatusCode(result));
        return result;
    }

    private Object joinGame(Request req, Response res) {
        String authToken = req.headers("authorization");
        String body = req.body();
        String result = handler.joinGame(body, authToken);
        res.status(getStatusCode(result));
        return result;
    }

    private int getStatusCode(String result) {
        Map<String, Integer> failResponses = Map.of(
                "{\"message\":\"Error: bad request\"}", 400,
                "{\"message\":\"Error: unauthorized\"}", 401,
                "{\"message\":\"Error: already taken\"}", 403
        );
        if (!result.contains("Error")) {
            return 200;
        } else {
            return failResponses.getOrDefault(result, 500);
        }
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
