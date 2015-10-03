package ua.com.juja.sqlcmd.model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.*;

public class JDBCDatabaseManager implements DatabaseManager {

    Connection connection;

    public JDBCDatabaseManager() {

    }

    @Override
    public List<DataSet> getTableData(String tableName) {
        checkIsConnected();
        List<DataSet> result = new ArrayList<>();
        Set<String> tableColumns = getTableColumns(tableName);
        String tableColumnsForQuery = tableColumns.toString();
        tableColumnsForQuery = tableColumnsForQuery.substring(1, tableColumnsForQuery.length()-1); //without brackets
        String query = "SELECT "+tableColumnsForQuery+" FROM " + tableName;
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                DataSet dataSet = new DataSetImpl();
                for (String columnName : tableColumns) {
                    dataSet.put(columnName, resultSet.getObject(columnName));
                }
                result.add(dataSet);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Cant prepare statement",e);
        }

        return result;
    }

    private void checkIsConnected() {
        if (!isConnected()){
            throw new RuntimeException("Connection to data base has been lost");
        }
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
        if (isConnected()) {
            return;
        }
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
        String connectionURI = "jdbc:" + dbms + "://" + serverName + ":" + portNumber + "/" + database;
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(connectionURI, userName, password);
        } catch (SQLException e) {
            throw new RuntimeException("Can't get connection to data base", e);
        }
        this.connection = connection;
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
        checkIsConnected();
        Set<String> columnNames = new HashSet<>();
        String query = "SELECT column_name FROM information_schema.columns " +
                        "WHERE table_schema = 'public' " +
                        "AND table_name   = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, tableName);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                columnNames.add(resultSet.getString("column_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return columnNames;
    }

    @Override
    public boolean isConnected() {
        return !(connection==null);
    }
}
