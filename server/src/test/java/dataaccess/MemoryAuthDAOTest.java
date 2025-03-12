package dataaccess;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MemoryAuthDAOTest {

    @Test
    void createAuth() {
        try {
            AuthDAO myDAO = new MemoryAuthDAO();
            var actual = myDAO.createAuth("username");
            assertEquals("username", actual.username());
        } catch (DataAccessException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Test
    void getAuth() {
        try {
            AuthDAO myDAO = new MemoryAuthDAO();
            var expected = myDAO.createAuth("username");
            var actual = myDAO.getAuth(expected.authToken());
            assertEquals(expected, actual);
        } catch (DataAccessException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}