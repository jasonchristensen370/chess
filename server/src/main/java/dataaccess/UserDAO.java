package dataaccess;

import model.UserData;

public interface UserDAO {
    void clearUser();
    UserData createUser(UserData u) throws DataAccessException;
    UserData getUser(String username) throws DataAccessException;
}
