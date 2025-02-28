package server;

import handler.Handler;
import spark.*;
import java.util.Map;

public class Server {
    private final Handler handler = new Handler();
    private final Map<String, Integer> failResponses = Map.of(
            "{\"message\":\"Error: bad request\"}", 400,
            "{\"message\":\"Error: unauthorized\"}", 401,
            "{\"message\":\"Error: already taken\"}", 403
    );

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", this::clear);
        Spark.post("/user", this::register);

        //This line initializes the server and can be removed once you have a functioning endpoint 
//        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    private Object clear(Request req, Response res) {
        String result = handler.clear();
        if (!result.contains("Error")) {
            res.status(200);
        } else {
            res.status(failResponses.getOrDefault(result, 500));
        }
        return result;
    }

    private Object register(Request req, Response res) {
        String body = req.body(); // Returns JSON as string
//        String authToken = req.headers("authorization");
        String result = handler.register(body);
        if (!result.contains("Error")) {
            res.status(200);
        } else {
            res.status(failResponses.getOrDefault(result, 500));
        }
        return result;
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
