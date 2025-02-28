package dataaccess;

import model.UserData;

public interface UserDAO {
    void clearUser();
    UserData createUser(UserData u);
    UserData getUser(String username);
}
