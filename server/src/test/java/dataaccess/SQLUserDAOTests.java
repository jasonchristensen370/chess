package dataaccess;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import model.UserData;


public class SQLUserDAOTests {
    @BeforeEach
    public void createDatabase() {
        try {
            DatabaseManager.createDatabase();
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void clearUserSuccess() {
        try {
            var myDAO = new SQLUserDAO();
            var newUser = new UserData("jason", "passwd","email@email.com");
            myDAO.createUser(newUser);
            myDAO.clearUser();
            assertNull(myDAO.getUser("jason"));
        } catch (DataAccessException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void getUserSuccess() {
        try {
            var myDAO = new SQLUserDAO();
            var newUser = new UserData("jason", "passwd","email@email.com");
            myDAO.createUser(newUser);
            var actual = myDAO.getUser(newUser.username());
            assertEquals(newUser, actual);
        } catch (DataAccessException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void getUserFail() {
        try {
            var myDAO = new SQLUserDAO();
            myDAO.clearUser();
            var newUser = new UserData("bobby", "passwd","email@email.com");
            myDAO.createUser(newUser);
            assertNull(myDAO.getUser("jason"));
        } catch (DataAccessException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void createUserSuccess() {

    }

    @Test
    public void createUserFail() {

    }
}
