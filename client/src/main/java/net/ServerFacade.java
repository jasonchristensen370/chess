package net;

import com.google.gson.Gson;
import exception.ResponseException;
import servicemodel.*;
import java.net.*;
import java.io.*;

// Calls ClientCommunicator for http calls
public class ServerFacade {
    private final String serverUrl;

    public ServerFacade(int port) {
        serverUrl = "http://localhost:"+port;
    }

    public LoginResult login(LoginRequest req) throws ResponseException {
        var path = "/session";
        return makeRequest("POST", path, req, LoginResult.class, null);
    }

    public RegisterResult register(RegisterRequest req) throws ResponseException {
        var path = "/user";
        return makeRequest("POST", path, req, RegisterResult.class, null);
    }

    public LogoutResult logout(LogoutRequest req) throws ResponseException {
        var path = "/session";
        return makeRequest("DELETE", path, req, LogoutResult.class, req.authToken());
    }

    public ListResult listGames(ListRequest req) throws ResponseException {
        var path = "/game";
        return makeRequest("GET", path, req, ListResult.class, req.authToken());
    }

    public CreateGameResult createGame(CreateGameRequest req) throws ResponseException {
        var path = "/game";
        return makeRequest("POST", path, req, CreateGameResult.class, req.authToken());
    }

    public JoinGameResult joinGame(JoinGameRequest req) throws ResponseException {
        var path = "/game";
        return makeRequest("PUT", path, req, JoinGameResult.class, req.authToken());
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass, String authToken) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            writeBody(request, http, authToken);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);

        } catch (ResponseException e) {
            throw new ResponseException(e.statusCode(), e.getMessage());
        } catch (Exception e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

    private static void writeBody(Object request, HttpURLConnection http, String authToken) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            if (authToken != null) {
                http.setRequestProperty("authorization", authToken);
            }
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
