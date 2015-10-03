package ua.com.juja.sqlcmd.model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.*;

public class JDBCDatabaseManager implements DatabaseManager {

    Connection connection;

    public JDBCDatabaseManager() {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream propsFile = classLoader.getResourceAsStream("dataSource.properties");
        Properties connectionProps = new Properties();
        try {
            if (propsFile == null) {
                throw new FileNotFoundException("File dataSource.properties is not found in classpath.");
            }
            connectionProps.load(propsFile);
        } catch (IOException e) {
            throw new RuntimeException("Cant't get data base connection properties", e);
        }
        String dbms = connectionProps.getProperty("dbms");
        String serverName = connectionProps.getProperty("serverName");
        String portNumber = connectionProps.getProperty("portNumber");
        String dbName = connectionProps.getProperty("dbName");
        String connectionURI = "jdbc:" + dbms + "://" + serverName + ":" + portNumber + "/" + dbName;
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(connectionURI, connectionProps);
        } catch (SQLException e) {
            throw new RuntimeException("Can't get connection to data base", e);
        }
        this.connection = connection;
    }

    @Override
    public List<DataSet> getTableData(String tableName) {
        return null;
    }

    @Override
    public int getSize(String tableName) {
        return 0;
    }

    @Override
    public Set<String> getTableNames() {
        return null;
    }

    @Override
    public void connect(String database, String userName, String password) {

    }

    @Override
    public void clear(String tableName) {

    }

    @Override
    public void create(String tableName, DataSet input) {

    }

    @Override
    public void update(String tableName, int id, DataSet newValue) {

    }

    @Override
    public Set<String> getTableColumns(String tableName) {
        return null;
    }

    @Override
    public boolean isConnected() {
        return !(connection==null);
    }
}
