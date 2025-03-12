package dataaccess;

import model.AuthData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.fail;

public class SQLAuthDAOTests {
    @BeforeAll
    public static void createDatabase() {
        try {
            DatabaseManager.createDatabase();
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    @AfterEach
    @BeforeEach
    public void clearTable() {
        try {
            var statement = "DELETE FROM authData";
            try(var conn = DatabaseManager.getConnection();
                var prepStatement = conn.prepareStatement(statement)) {
                prepStatement.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Test
    public void clearAuthSuccess() {
        try {
            var authDAO = new SQLAuthDAO();
            authDAO.clearAuth();
            var statement = "SELECT * FROM authData";
            try(var conn = DatabaseManager.getConnection();
                var prepStatement = conn.prepareStatement(statement)) {
                var rs = prepStatement.executeQuery();
                assertFalse(rs.next());
            }
        } catch (DataAccessException | SQLException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void createAuthSuccess() {
        try {
            var myDAO = new SQLAuthDAO();
            myDAO.createAuth("testUser1");

            String statement = "SELECT * FROM authData";
            try (var conn = DatabaseManager.getConnection();
                 var prepStatement = conn.prepareStatement(statement)) {
                var rs = prepStatement.executeQuery();
                assertTrue(rs.next());
                var user = rs.getString("username");
                assertEquals("testUser1", user);
            }
        } catch (DataAccessException | SQLException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void createAuthWithNullUsernameFail() {
        assertThrows(DataAccessException.class, () -> {
            var myDAO = new SQLAuthDAO();
            myDAO.createAuth(null);
        });
    }

    @Test
    public void getAuthSuccess() {
        try {
            // Put authData in database
            var statement = "INSERT INTO authData VALUES ('aasldkqwerty', 'bobbyJoe')";
            try(var conn = DatabaseManager.getConnection();
                var prepStatement = conn.prepareStatement(statement)) {
                prepStatement.executeUpdate();
            }
            // Test getAuth function
            var myDAO = new SQLAuthDAO();
            var actual = myDAO.getAuth("aasldkqwerty");
            var expected = new AuthData("aasldkqwerty", "bobbyJoe");
            assertEquals(expected, actual);

        } catch (DataAccessException | SQLException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void getAuthFail() {
        try {
            var myDAO = new SQLAuthDAO();
            assertNull(myDAO.getAuth("tokenNotThere"));
        } catch (DataAccessException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void deleteAuthSuccess() {
        try {
            // Put authData in database
            var statement = "INSERT INTO authData VALUES ('aasldkqwerty', 'bobbyJoe')";
            try (var conn = DatabaseManager.getConnection();
                 var prepStatement = conn.prepareStatement(statement)) {
                prepStatement.executeUpdate();
            }
            var myDAO = new SQLAuthDAO();
            myDAO.deleteAuth("aasldkqwerty");

            statement = "SELECT * FROM authData";
            try(var conn = DatabaseManager.getConnection();
                var prepStatement = conn.prepareStatement(statement)) {
                var rs = prepStatement.executeQuery();
                assertFalse(rs.next());
            }

        } catch (DataAccessException | SQLException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void deleteAuthFail() {
        try {
            // Put authData in database
            var statement = "INSERT INTO authData VALUES ('aasldkqwerty', 'bobbyJoe')";
            try (var conn = DatabaseManager.getConnection();
                 var prepStatement = conn.prepareStatement(statement)) {
                prepStatement.executeUpdate();
            }
            // Delete it
            var myDAO = new SQLAuthDAO();
            myDAO.deleteAuth("notValidAuthToken");

            // Make sure it isn't there
            statement = "SELECT * FROM authData";
            try (var conn = DatabaseManager.getConnection();
                 var prepStatement = conn.prepareStatement(statement)) {
                var rs = prepStatement.executeQuery();
                assertTrue(rs.next());
            }
        } catch (DataAccessException | SQLException e) {
            fail(e.getMessage());
        }
    }
}
