package ua.com.tyomsky.sqlcmd.model;

import java.util.*;

public class DataSetImpl implements DataSet {

    private Map<String, Object> data = new LinkedHashMap<>();

    public DataSetImpl() {
    }

    @Override
    public void put(String name, Object value) {
        data.put(name, value);
    }

    @Override
    public List<Object> getValues() {
        return new LinkedList<>(data.values());
    }

    @Override
    public Set<String> getNames() {
        return data.keySet();
    }

    @Override
    public Object get(String name) {
        return data.get(name);
    }

    @Override
    public void updateFrom(DataSet newValue) {
        for (String key : newValue.getNames()) {
            put(key, newValue.get(key));
        }
    }

    @Override
    public String toString() {
        return "{" +
                "names:" + getNames() + ", " +
                "values:" + getValues() +
                "}";
    }
}
