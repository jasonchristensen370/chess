package net;

import com.google.gson.Gson;
import exception.ResponseException;
import servicemodel.*;
import java.net.*;
import java.io.*;

// Calls ClientCommunicator for http calls
public class ServerFacade {

    public LoginResult login(LoginRequest req) throws ResponseException {
        var path = "/session";
        return makeRequest("POST", path, req, LoginResult.class);
//        return new LoginResult(null, null, "Error: Not Implemented");
    }

    public RegisterResult register(RegisterRequest req) {
        var path = "/user";
        return new RegisterResult(null, null, "Error: Not Implemented");
    }

    public LogoutResult logout(LogoutRequest req) {
        var path = "/session";
        return new LogoutResult("Error: Not Implemented");
    }

    public ListResult listGames(ListRequest req) {
        var path = "/game";
        return new ListResult(null, "Error: Not Implemented");
    }

    public CreateGameResult createGame(CreateGameRequest req) {
        var path = "/game";
        return new CreateGameResult(null, "Error: Not Implemented");
    }

    public JoinGameResult joinGame(JoinGameRequest req) {
        var path = "/game";
        return new JoinGameResult("Error: Not Implemented");
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws ResponseException {
        try {
            String serverUrl = "http://localhost:8080";
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);

        } catch (Exception e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!(status / 100 == 2)) {
            try (InputStream respErr = http.getErrorStream()) {
                if (respErr != null) {
                    throw ResponseException.fromJson(respErr);
                }
            }
            throw new ResponseException(status, "other failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }
}
