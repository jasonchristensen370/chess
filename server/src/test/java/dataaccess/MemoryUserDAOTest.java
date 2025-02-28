package dataaccess;

import model.UserData;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class MemoryUserDAOTest {

    @Test
    public void clearUserSuccess() throws NoSuchFieldException, IllegalAccessException {
        Map<String, UserData> expectedDB = new HashMap<>();
        Map<String, UserData> testDB = new HashMap<>();
        testDB.put("jason", new UserData("jason", "mypass", "email@gmail.com"));
        UserDAO myDAO = new MemoryUserDAO();

        Field field = MemoryUserDAO.class.getDeclaredField("database");
        field.setAccessible(true);

        field.set(myDAO, testDB);
        var actualDB = field.get(myDAO);
        assertNotEquals(expectedDB, actualDB);

        myDAO.clearUser();

        actualDB = field.get(myDAO);
        assertEquals(expectedDB, actualDB);
    }

    @Test
    public void createUserInDatabase() throws NoSuchFieldException, IllegalAccessException {
        Map<String, UserData> expectedDB = new HashMap<>();
        UserDAO myDAO = new MemoryUserDAO();

        Field field = MemoryUserDAO.class.getDeclaredField("database");
        field.setAccessible(true);

        expectedDB.put("jason", new UserData("jason", "mypass", "email@gmail.com"));

        var actualDB = field.get(myDAO);
        assertNotEquals(expectedDB, actualDB);
        myDAO.createUser(new UserData("jason", "mypass", "email@gmail.com"));
        actualDB = field.get(myDAO);
        assertEquals(expectedDB, actualDB);
    }

    @Test
    public void getUserWhenUserThere() {
        UserDAO myDAO = new MemoryUserDAO();
        UserData user = new UserData("jason","password","email@email.com");
        var expected = myDAO.createUser(user);
        var actual = myDAO.getUser("jason");
        assertEquals(expected, actual);
    }
}
