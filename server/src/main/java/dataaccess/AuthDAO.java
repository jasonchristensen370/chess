package dataaccess;

import model.AuthData;

public interface AuthDAO {
    public void clear();
    public AuthData createAuth();
    public AuthData getAuth();
    public void deleteAuth();
}
