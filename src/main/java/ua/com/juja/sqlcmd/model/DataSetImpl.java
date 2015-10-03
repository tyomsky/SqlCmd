package ua.com.juja.sqlcmd.model;

import java.util.*;

public class DataSetImpl implements DataSet {

    private Map data = new HashMap();

    @Override
    public void put(String name, Object value) {
        data.put(name, value);
    }

    @Override
    public List<Object> getValues() {
        return null;
    }

    @Override
    public Set<String> getNames() {
        return null;
    }

    @Override
    public Object get(String name) {
        return null;
    }

    @Override
    public void updateFrom(DataSet newValue) {

    }

}
