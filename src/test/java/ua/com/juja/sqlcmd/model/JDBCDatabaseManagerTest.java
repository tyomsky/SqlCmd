package ua.com.juja.sqlcmd.model;

/**
 * Created by oleksandr.baglai on 25.08.2015.
 */
public class JDBCDatabaseManagerTest extends DatabaseManagerTest {

    @Override
    public DatabaseManager getDatabaseManager() {
        return new JDBCDatabaseManager();
    }
}
