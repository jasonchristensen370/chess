package websocket.messages;

public class NotificationMessage extends ServerMessage {
    private final String message;

    public NotificationMessage(ServerMessageType messageType, String message) {
        super(messageType);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
