package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.model.DataSetImpl;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

public class Create implements Command{

    DatabaseManager manager;
    View view;

    public Create(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("create|");
    }

    @Override
    public void process(String command) {
        String[] data = command.split("\\|");
        if (data.length < 4 && (data.length % 2 != 0)) {
            throw new IllegalArgumentException("Должно быть четное количество параметров в формате " +
                    "'create|tableName|column1|value1|column2|value2|...|columnN|valueN', " +
                    "а ты прислал: \'"+ command+"\'");
        }
        String tableName = data[1];
        DataSet dataSet = new DataSetImpl();
        for (int i = 2; i < data.length; i=i+2) {
            dataSet.put(data[i], transformToCompatibleType(data[i+1]));
        }
        manager.create(tableName, dataSet);
        view.write("Запись "+dataSet+" была успешно создана в таблице 'user'.");
    }

    @Override
    public boolean needsConnection() {
        return true;
    }

    private Object transformToCompatibleType(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return s;
        }
    }
}
