package dataaccess;

import model.AuthData;

import java.sql.SQLException;

public class SQLAuthDAO extends SQLDAO implements AuthDAO {
    public void clearAuth() throws DataAccessException {
        var statement = "DELETE FROM authData";
        try {
            try (var conn = DatabaseManager.getConnection();
                 var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
    public AuthData createAuth(String username) throws DataAccessException {
        return new AuthData(null, null);
    }
    public AuthData getAuth(String authToken) throws DataAccessException {
        return new AuthData(null, null);
    }
    public void deleteAuth(String authToken) throws DataAccessException {

    }
}
