package dataaccess;

import model.UserData;

public interface UserDAO {
    public void clear();
    public UserData createUser();
    public UserData getUser(String username);
}
