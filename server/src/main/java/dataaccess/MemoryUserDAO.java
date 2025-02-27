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

    public UserData createUser(UserData u) throws DataAccessException{
        String username = u.username();
        if (database.containsKey(username)) {
            throw new DataAccessException("Username already taken");
        }
        database.put(username, u);
        return u;
    }

    public UserData getUser(String username) throws DataAccessException {
        if (!database.containsKey(username)) {
            throw new DataAccessException("Username does not exist");
        }
        return database.get(username);
    }
}
