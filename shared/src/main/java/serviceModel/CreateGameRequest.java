package serviceModel;

public record CreateGameRequest(String authToken, String gameName) {
}
