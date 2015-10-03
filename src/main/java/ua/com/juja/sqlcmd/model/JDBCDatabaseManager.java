package ua.com.juja.sqlcmd.model;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.lang3.StringUtils;

import javax.sql.DataSource;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.*;

public class JDBCDatabaseManager implements DatabaseManager {

    DataSource dataSource;

    public JDBCDatabaseManager() {

    }

    @Override
    public List<DataSet> getTableData(String tableName) {
        checkIsConnected();
        List<DataSet> result = new ArrayList<>();
        Set<String> tableColumns = getTableColumns(tableName);
        String tableColumnsForQuery = StringUtils.join(tableColumns.toArray(), ",");
        tableColumnsForQuery = "".equals(tableColumnsForQuery) ? "*" : tableColumnsForQuery;
        String query = "SELECT "+tableColumnsForQuery+" FROM public." + tableName;
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                DataSet dataSet = new DataSetImpl();
                for (String columnName : tableColumns) {
                    dataSet.put(columnName, resultSet.getObject(columnName));
                }
                result.add(dataSet);
            }
            connection.commit();
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
        String connectionURL = "jdbc:" + dbms + "://" + serverName + ":" + portNumber + "/" + database;
        org.apache.tomcat.jdbc.pool.DataSource dataSource = new org.apache.tomcat.jdbc.pool.DataSource();
        dataSource.setDriverClassName(connectionProps.getProperty("driverName"));
        dataSource.setUrl(connectionURL);
        dataSource.setUsername(userName);
        dataSource.setPassword(password);
        dataSource.setDefaultAutoCommit(false);
        this.dataSource = dataSource;
    }

    @Override
    public void clear(String tableName) {
        checkIsConnected();
        String query = "DELETE FROM public." + tableName;
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void create(String tableName, DataSet input) {
        checkIsConnected();
        Set<String> tableColumns = getTableColumns(tableName);
        String tableColumnsForQuery = StringUtils.join(tableColumns.toArray(), ",");
        String valuesForQuery = StringUtils.repeat("?, ", tableColumns.size());
        valuesForQuery = valuesForQuery.substring(0, valuesForQuery.length()-2); // last ','
        String query = "INSERT INTO public."+tableName+" " +
                        "("+tableColumnsForQuery+")" + " " +
                        "VALUES ("+valuesForQuery+")";
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            int placeHolderIndex = 1;
            for (String column : tableColumns) {
                statement.setObject(placeHolderIndex, input.get(column));
                placeHolderIndex++;
            }
            statement.execute();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(String tableName, int id, DataSet newValue) {
        try (Connection connection = dataSource.getConnection()) {
            StringBuilder queryKeyValue = new StringBuilder();
            for (String column : newValue.getNames()) {
                queryKeyValue.append(column);
                queryKeyValue.append(" = ");
                queryKeyValue.append("?,");
            }
            queryKeyValue.deleteCharAt(queryKeyValue.length()-1); // for last ','
            String query = "UPDATE public."+tableName+" SET "+queryKeyValue+" WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            int placeHolderIndex = 1;
            for (Object value : newValue.getValues()) {
                statement.setObject(placeHolderIndex, value);
                placeHolderIndex++;
            }
            statement.setInt(placeHolderIndex, id);
            statement.execute();
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException("cant execute query", e);
        }
    }

    @Override
    public Set<String> getTableColumns(String tableName) {
        checkIsConnected();
        Set<String> columnNames = new LinkedHashSet<>();
        String query = "SELECT column_name FROM information_schema.columns " +
                        "WHERE table_schema = 'public' " +
                        "AND table_name   = ?";
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, tableName);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                columnNames.add(resultSet.getString("column_name"));
            }
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return columnNames;
    }

    @Override
    public boolean isConnected() {
        return dataSource!=null;
    }

}
