package ua.com.juja.sqlcmd.model;

import java.sql.*;
import java.util.*;

public class JDBCDatabaseManager implements DatabaseManager {
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
        return false;
    }
    // TODO implement me
}
