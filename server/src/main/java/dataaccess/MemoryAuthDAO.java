package dataaccess;

import model.AuthData;

import java.util.HashMap;
import java.util.UUID;
import java.util.Map;

public class MemoryAuthDAO implements AuthDAO {

    private Map<String, AuthData> database;

    public MemoryAuthDAO() {
        database = new HashMap<>();
    }

    public void clearAuth() {
        database.clear();
    }
    public AuthData createAuth(String username) {
        String newAuthToken = UUID.randomUUID().toString();
        AuthData newAuthData = new AuthData(newAuthToken, username);
        database.put(newAuthToken, newAuthData);
        return newAuthData;
    }
    public AuthData getAuth(String authToken) throws DataAccessException {
        if (!database.containsKey(authToken)) {
            throw new DataAccessException("Invalid authToken");
        }
        return database.get(authToken);
    }
    public void deleteAuth(String authToken) throws DataAccessException {
        if (!database.containsKey(authToken)) {
            throw new DataAccessException("Cannot delete non-existent authToken");
        }
        database.remove(authToken);
    }
}
