package dataaccess;

import model.AuthData;

import java.util.HashMap;
import java.util.UUID;
import java.util.Map;

public class MemoryAuthDAO implements AuthDAO {

    private Map<String, AuthData> database = new HashMap<>();

    public void clearAuth() {
        database.clear();
    }
    public AuthData createAuth(String username) {
        String newAuthToken = UUID.randomUUID().toString();
        AuthData newAuthData = new AuthData(newAuthToken, username);
        database.put(newAuthToken, newAuthData);
        return newAuthData;
    }
    public AuthData getAuth(String authToken) {
        return database.get(authToken);
    }
    public void deleteAuth(String authToken) {
        database.remove(authToken);
    }
}
