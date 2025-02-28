package dataaccess;

import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class MemoryUserDAO implements UserDAO {

    private final Map<String, UserData> database;

    public MemoryUserDAO() {
        database = new HashMap<>();
    }

    public void clearUser() {
        database.clear();
    }

    public UserData createUser(UserData u) {
        String username = u.username();
        database.put(username, u);
        return u;
    }

    public UserData getUser(String username) {
        return database.get(username);
    }
}
