package dataaccess;

import model.UserData;

public class SQLUserDAO implements UserDAO {
    public void clearUser() {

    }
    public UserData createUser(UserData u) {
        return new UserData(null, null, null);
    }
    public UserData getUser(String username) {
        return new UserData(null, null, null);
    }
}
