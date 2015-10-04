package ua.com.tyomsky.sqlcmd.model;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.commons.lang3.StringUtils;

import javax.sql.DataSource;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class JDBCDatabaseManager implements DatabaseManager {

    DataSource dataSource;

    public JDBCDatabaseManager() {

    }

    @Override
    public List<DataSet> getTableData(String tableName) {
        List<DataSet> tableData;
        QueryRunner queryRunner = new QueryRunner(dataSource);
        final Set<String> tableColumns = getTableColumns(tableName);
        ResultSetHandler<List<DataSet>> rsh = new ResultSetHandler<List<DataSet>>() {
            @Override
            public List<DataSet> handle(ResultSet resultSet) throws SQLException {
                List<DataSet> result = new LinkedList<>();
                while (resultSet.next()) {
                    DataSet dataSet = new DataSetImpl();
                    for (String column : tableColumns) {
                        dataSet.put(column, resultSet.getObject(column));
                    }
                    result.add(dataSet);
                }
                return result;
            }
        };
        String query = prepareSelectQuery(tableName, tableColumns.toArray());
        try {
            tableData = queryRunner.query(query, rsh);
        } catch (SQLException e) {
            throw new RuntimeException("Cant prepare statement", e);
        }
        return tableData;
    }

    private String prepareSelectQuery(String tableName, Object[] tableColumns) {
        String tableColumnsForQuery = StringUtils.join(tableColumns, ",");
        tableColumnsForQuery = "".equals(tableColumnsForQuery) ? "*" : tableColumnsForQuery;
        return "SELECT " + tableColumnsForQuery + " FROM public." + tableName;
    }

    @Override
    public int getSize(String tableName) {
        long size;
        ResultSetHandler<Long> rsh2 = new ScalarHandler<>("COUNT");
        QueryRunner queryRunner = new QueryRunner(dataSource);
        try {
            size = queryRunner.query("SELECT COUNT(*) AS COUNT FROM public." + tableName, rsh2);
        } catch (SQLException e) {
            throw new RuntimeException("Can't execute query", e);
        }
        return (int) size;
    }

    @Override
    public Set<String> getTableNames() {
        Set<String> tableNames = new LinkedHashSet<>();
        ResultSetHandler<List<String>> rsh = new ColumnListHandler<>("table_name");
        QueryRunner queryRunner = new QueryRunner(dataSource);
        try {
            List<String> result = queryRunner.query("SELECT table_name FROM information_schema.tables " +
                    "WHERE table_schema='public' AND table_type='BASE TABLE'", rsh);
            tableNames.addAll(result);
        } catch (SQLException e) {
            throw new RuntimeException("Can't execute query", e);
        }
        return tableNames;
    }

    @Override
    public void connect(String database, String userName, String password) {
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
        String connectionURL = "jdbc:" + dbms + "://" + serverName + ":" + portNumber + "/" + database;
        org.apache.tomcat.jdbc.pool.DataSource dataSource = new org.apache.tomcat.jdbc.pool.DataSource();
        dataSource.setDriverClassName(connectionProps.getProperty("driverName"));
        dataSource.setUrl(connectionURL);
        dataSource.setUsername(userName);
        dataSource.setPassword(password);
        dataSource.setInitialSize(1);
        dataSource.setMaxActive(1);
        dataSource.setMaxIdle(1);
        dataSource.setMinIdle(1);
        this.dataSource = dataSource;
    }

    @Override
    public void clear(String tableName) {
        QueryRunner queryRunner = new QueryRunner(dataSource);
        try {
            queryRunner.update("DELETE FROM public." + tableName);
        } catch (SQLException e) {
            throw new RuntimeException("Can't execute query", e);
        }
    }

    @Override
    public void create(String tableName, DataSet input) {
        QueryRunner queryRunner = new QueryRunner(dataSource);
        Object[] values = input.getValues().toArray();
        String columnNames = StringUtils.join(input.getNames(), ',');
        String placeHolders = transformIntoPlaceHoldersString(values);
        String query = "INSERT INTO public." + tableName + " " +
                "(" + columnNames + ")" + " " +
                "VALUES (" + placeHolders + ")";

        try {
            queryRunner.update(query, values);
        } catch (SQLException e) {
            throw new RuntimeException("Can't execute query", e);
        }
    }

    private String transformIntoPlaceHoldersString(Object[] values) {
        String[] placeHolders = new String[values.length];
        Arrays.fill(placeHolders, "?");
        return StringUtils.join(placeHolders, ',');
    }

    @Override
    public void update(String tableName, int id, DataSet newValue) {
        QueryRunner queryRunner = new QueryRunner(dataSource);
        String namedPlaceHolders = transformToNamedPlaceholders(newValue.getNames().toArray());
        String query = "UPDATE public." + tableName + " SET " + namedPlaceHolders + " WHERE id = ?";
        LinkedList<Object> queryParams = new LinkedList<>(newValue.getValues());
        queryParams.add(id);
        try {
            queryRunner.update(query, queryParams.toArray());
        } catch (SQLException e) {
            throw new RuntimeException("Can't execute query", e);
        }
    }

    private String transformToNamedPlaceholders(Object[] values) {
        String[] placeHolders = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            placeHolders[i] = values[i] + " = ?";
        }
        return StringUtils.join(placeHolders, ",");
    }

    @Override
    public Set<String> getTableColumns(String tableName) {
        Set<String> columnNames = new LinkedHashSet<>();
        ResultSetHandler<List<String>> rsh = new ColumnListHandler<>("column_name");
        QueryRunner queryRunner = new QueryRunner(dataSource);
        String query = "SELECT column_name FROM information_schema.columns " +
                "WHERE table_schema = 'public' " +
                "AND table_name   = ?";
        try {
            List<String> result = queryRunner.query(query, rsh, tableName);
            columnNames.addAll(result);
        } catch (SQLException e) {
            throw new RuntimeException("Can't execute query", e);
        }
        return columnNames;
    }

    @Override
    public boolean isConnected() {
        boolean isConnected;
        try (Connection connection = dataSource.getConnection()) {
            connection.prepareStatement("SELECT 1");
            isConnected = true;
        } catch (SQLException | NullPointerException e) {
            isConnected = false;
        }
        return isConnected;
    }

}
