package dataaccess;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MemoryAuthDAOTest {

    @Test
    void clearAuth() {
        AuthDAO myDAO = new MemoryAuthDAO();
        assertThrows(DataAccessException.class, () -> {
            var expected = myDAO.createAuth("username");
            myDAO.clearAuth();
            myDAO.getAuth(expected.authToken());
        });
    }

    @Test
    void createAuth() {
        AuthDAO myDAO = new MemoryAuthDAO();
        assertDoesNotThrow(() -> {
            var actual = myDAO.createAuth("username");
            assertEquals("username", actual.username());
        });
    }

    @Test
    void getAuth() {
        AuthDAO myDAO = new MemoryAuthDAO();
        assertDoesNotThrow(() -> {
            var expected = myDAO.createAuth("username");
            var actual = myDAO.getAuth(expected.authToken());
            assertEquals(expected, actual);
        });
    }

    @Test
    void deleteAuth() {
        AuthDAO myDAO = new MemoryAuthDAO();
        assertThrows(DataAccessException.class, () -> {
            var expected = myDAO.createAuth("username");
            myDAO.deleteAuth(expected.authToken());
            myDAO.getAuth(expected.authToken());
        });
    }
}