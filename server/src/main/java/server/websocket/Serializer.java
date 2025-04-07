package server.websocket;

import com.google.gson.Gson;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

public class Serializer {
    public static UserGameCommand fromJsonUserGameCommand(String message) {
        var gson = new Gson();
        var command = gson.fromJson(message, UserGameCommand.class);
        if (command.getCommandType() == UserGameCommand.CommandType.MAKE_MOVE) {
            return gson.fromJson(message, MakeMoveCommand.class);
        } else {
            return command;
        }
    }

    public static String toJsonServerMessage(ServerMessage message) {
        var gson = new Gson();
        return switch (message.getServerMessageType()) {
            case ERROR -> gson.toJson(message, ErrorMessage.class);
            case LOAD_GAME -> gson.toJson(message, LoadGameMessage.class);
            case NOTIFICATION -> gson.toJson(message, NotificationMessage.class);
        };
    }
}
