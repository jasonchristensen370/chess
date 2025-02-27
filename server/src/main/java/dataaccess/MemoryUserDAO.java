package dataaccess;

import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class MemoryUserDAO implements UserDAO {

    private Map<String, UserData> database = new HashMap<>();

    public void clearUser() {
        database.clear();
    }
    public UserData createUser(String username, String password, String email) {
        UserData newUser = new UserData(username, password, email);
        database.put(username, newUser);
        return newUser;
    }
    public UserData getUser(String username) {
        return database.get(username);
    }
}
