package dataaccess;

import model.AuthData;

import java.sql.SQLException;
import java.util.UUID;

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
        // Create the token
        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken, username);

        // Add it to the database
        String insertStatement = "INSERT INTO authData VALUES (?, ?)";
        try {
            try (var conn = DatabaseManager.getConnection();
                 var preparedStatement = conn.prepareStatement(insertStatement)) {
                preparedStatement.setString(1, authToken);
                preparedStatement.setString(2, username);
                preparedStatement.executeUpdate();
                return authData;
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
    public AuthData getAuth(String authToken) throws DataAccessException {
        String selectStatement = "SELECT authToken, username FROM authData WHERE authToken=?";
        try {
            try (var conn = DatabaseManager.getConnection();
                 var preparedSelect = conn.prepareStatement(selectStatement)) {
                preparedSelect.setString(1, authToken);
                var rs = preparedSelect.executeQuery();
                if (rs.next()) {
                    String auth = rs.getString("authToken");
                    String username = rs.getString("username");
                    return new AuthData(auth, username);
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
    public void deleteAuth(String authToken) throws DataAccessException {
        String removeStatement = "DELETE FROM authData WHERE authToken=?";
        try {
            try (var conn = DatabaseManager.getConnection();
                 var preparedDelete = conn.prepareStatement(removeStatement)) {
                preparedDelete.setString(1, authToken);
                preparedDelete.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
