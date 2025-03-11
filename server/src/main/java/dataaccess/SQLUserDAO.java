package dataaccess;

import model.UserData;
import java.sql.SQLException;

public class SQLUserDAO implements UserDAO {
    public void clearUser() throws DataAccessException {
        var statement = "DELETE FROM userData";
        try {
            try (var conn = DatabaseManager.getConnection();
                var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public UserData createUser(UserData u) throws DataAccessException {
        String insertStatement = "INSERT INTO userData VALUES (?, ?, ?)";
        try {
            try (var conn = DatabaseManager.getConnection();
                 var preparedStatement = conn.prepareStatement(insertStatement)) {
                preparedStatement.setString(1, u.username());
                preparedStatement.setString(2, u.password());
                preparedStatement.setString(3, u.email());
                preparedStatement.executeUpdate();
                return u;
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public UserData getUser(String username) throws DataAccessException {
        String selectStatement = "SELECT username, password, email FROM userData WHERE username=?";
        try {
            try (var conn = DatabaseManager.getConnection();
                var preparedSelect = conn.prepareStatement(selectStatement)) {
                preparedSelect.setString(1, username);
                var rs = preparedSelect.executeQuery();
                if (rs.next()) {
                    String user = rs.getString("username");
                    String password = rs.getString("password");
                    String email = rs.getString("email");
                    return new UserData(user, password, email);
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
