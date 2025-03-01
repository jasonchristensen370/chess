package servicemodel;

public record CreateGameRequest(String authToken, String gameName) {
}
