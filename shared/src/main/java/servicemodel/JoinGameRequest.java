package servicemodel;

public record JoinGameRequest(String authToken, String playerColor, Integer gameID) {
}
