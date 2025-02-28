package dataaccess;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MemoryAuthDAOTest {

    @Test
    void createAuth() {
        AuthDAO myDAO = new MemoryAuthDAO();
        var actual = myDAO.createAuth("username");
        assertEquals("username", actual.username());
    }

    @Test
    void getAuth() {
        AuthDAO myDAO = new MemoryAuthDAO();
        var expected = myDAO.createAuth("username");
        var actual = myDAO.getAuth(expected.authToken());
        assertEquals(expected, actual);
    }
}