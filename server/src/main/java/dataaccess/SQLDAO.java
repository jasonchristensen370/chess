package dataaccess;

public class SQLDAO {
    public SQLDAO() {
        try {
            DatabaseManager.createDatabase();
        } catch (DataAccessException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
