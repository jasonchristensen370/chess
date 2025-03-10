package dataaccess;

import model.AuthData;

public class SQLAuthDAO extends SQLDAO implements AuthDAO {
    public void clearAuth() {

    }
    public AuthData createAuth(String username) {
        return new AuthData(null, null);
    }
    public AuthData getAuth(String authToken) {
        return new AuthData(null, null);
    }
    public void deleteAuth(String authToken) {

    }
}
