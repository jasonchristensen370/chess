package net;

import com.google.gson.Gson;
import exception.ResponseException;

import javax.websocket.*;

import ui.ServerMessageObserver;
import websocket.commands.UserGameCommand;
import websocket.commands.UserGameCommand.*;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.net.URI;

public class WebsocketCommunicator extends Endpoint {
    private final ServerMessageObserver observer;
    private final Gson gson;
    private Session session;

    public WebsocketCommunicator(String url, ServerMessageObserver observer) throws ResponseException {
        try {
            this.observer = observer;
            gson = new Gson();
            URI socketURI = new URI(url + "/ws");

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = (Session) container.connectToServer(this, socketURI);
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                public void onMessage(String message) {
//                    System.out.println(message);
                    var serverMessage = gson.fromJson(message, ServerMessage.class);
                    switch (serverMessage.getServerMessageType()) {
                        case ERROR -> observer.notify(gson.fromJson(message, ErrorMessage.class));
                        case LOAD_GAME -> observer.notify(gson.fromJson(message, LoadGameMessage.class));
                        case NOTIFICATION -> observer.notify(gson.fromJson(message, NotificationMessage.class));
                    }

                }
            });
        } catch (Exception e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void connect(String authToken, Integer gameID) throws IOException {
        var message = new UserGameCommand(CommandType.CONNECT, authToken, gameID);
        this.session.getBasicRemote().sendText(gson.toJson(message));
    }
}
