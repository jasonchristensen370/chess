package server.websocket;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.SQLAuthDAO;
import dataaccess.SQLGameDAO;
import model.GameData;
import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.*;


import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@WebSocket
public class WebSocketHandler {
    private final ConcurrentHashMap<Integer, ConnectionManager> connections = new ConcurrentHashMap<>();
    private final GameDAO gameDAO = new SQLGameDAO();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        try {
            UserGameCommand command = Serializer.fromJsonUserGameCommand(message);

            // Throws a custom UnauthorizedException. Yours may work differently.
            String username = getUsername(command.getAuthToken());

            switch (command.getCommandType()) {
                case CONNECT -> connect(session, username, command);
                case MAKE_MOVE -> makeMove(session, username, (MakeMoveCommand) command);
                case LEAVE -> leaveGame(session, username, command);
                case RESIGN -> resign(session, username, command);
            }
        } catch (UnauthorizedException ex) {
            // Serializes and sends the error message
            sendMessage(session.getRemote(), new ErrorMessage("Error: unauthorized"));
        } catch (Exception ex) {
            ex.printStackTrace();
            sendMessage(session.getRemote(), new ErrorMessage("Error: " + ex.getMessage()));
        }

    }

    private void sendMessage(RemoteEndpoint remote, ServerMessage message) throws IOException {
        String messageString = Serializer.toJsonServerMessage(message);
        remote.sendString(messageString);
    }

    private String getUsername(String authToken) throws UnauthorizedException {
        try {
            var authDAO = new SQLAuthDAO();
            var authData = authDAO.getAuth(authToken);
            if (authData == null) {
                throw new UnauthorizedException("Error: unauthorized");
            } else {
                return authData.username();
            }
        } catch (DataAccessException e) {
            throw new UnauthorizedException(e.getMessage());
        }
    }

    private void connect(Session session, String username, UserGameCommand command) throws IOException {
        // Add Connection
        var gameID = command.getGameID();
        connections.putIfAbsent(gameID, new ConnectionManager());
        connections.get(gameID).add(username, session);
        // Send load game message to root client, and notification to all other clients.
        try {
            var gameData = gameDAO.getGame(gameID);
            String playerColor = getPlayerColor(username, gameData);
            sendMessage(session.getRemote(), new LoadGameMessage(gameData.game()));
            connections.get(gameID).broadcast(username, new NotificationMessage(username+" has joined the game as "+playerColor));
        } catch (DataAccessException e) {
            sendMessage(session.getRemote(), new ErrorMessage("Error accessing data"));
        }

    }

    private String getPlayerColor(String username, GameData gameData) {
        var whiteUser = gameData.whiteUsername();
        var blackUser = gameData.blackUsername();
        if (whiteUser != null && whiteUser.equals(username)) {
            return "white";
        } else if (blackUser != null && blackUser.equals(username)) {
            return "black";
        } else {
            return "observer";
        }
    }

    private void makeMove(Session session, String username, MakeMoveCommand command) {

    }

    private void leaveGame(Session session, String username, UserGameCommand command) {

    }

    private void resign(Session session, String username, UserGameCommand command) {

    }
}
