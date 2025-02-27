package dataaccess;

import model.UserData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MemoryUserDAOTest {

    @Test
    public void clearUserSuccess() {
        UserDAO myDAO = new MemoryUserDAO();
        try {
            myDAO.createUser("bob", "joe", "email@blah.com");
            myDAO.clearUser();
        } catch (DataAccessException e) {
            fail(e.getMessage());
        }
        assertThrows(DataAccessException.class, () -> {
            myDAO.getUser("bob");
        });
    }

    @Test
    public void createUserInDatabase() {
        UserDAO myDAO = new MemoryUserDAO();

        String username = "bobbyboy";
        String password = "ILoveCats";
        String email = "nada@gmail.com";
        try {
            var expected = new UserData(username, password, email);
            var actual = myDAO.createUser(username, password, email);
            assertEquals(expected, actual);
        } catch (DataAccessException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void createUserInDatabaseUsernameTaken() {
        UserDAO myDAO = new MemoryUserDAO();

        String username = "bobbyboy";
        String password = "ILoveCats";
        String email = "nada@gmail.com";
        try {
            myDAO.createUser(username, password, email);
        } catch (DataAccessException e) {
            fail(e.getMessage());
        }
        assertThrows(DataAccessException.class, () -> {
            myDAO.createUser(username, "password", "email@blah.com");
        });
    }

    @Test
    public void getUserWhenUserThere() {
        UserDAO myDAO = new MemoryUserDAO();
        try {
            var expected = myDAO.createUser("bobbyboy", "ILoveCats", "nada@gmail.com");
            var actual = myDAO.getUser("bobbyboy");
            assertEquals(expected, actual);
        } catch (DataAccessException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void getUserWhenUserNotThere() {
        UserDAO myDAO = new MemoryUserDAO();
        assertThrows(DataAccessException.class, () -> {
            myDAO.getUser("username");
        });
    }
}
