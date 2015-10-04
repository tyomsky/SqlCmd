package ua.com.juja.sqlcmd.controller.command;

import org.apache.commons.lang3.StringUtils;
import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

import java.util.List;
import java.util.Set;

public class Find implements Command {

    private DatabaseManager manager;
    private View view;

    public Find(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("find|");
    }

    @Override
    public void process(String command) {
        String[] data = command.split("\\|");
        if (data.length != 2) {
            throw new IllegalArgumentException("Формат команды 'find|user', а ты ввел: " + command);
        }
        Set<String> tableColumns = manager.getTableColumns(data[1]);
        List<DataSet> dataSetList = manager.getTableData(data[1]);
        view.write("--------------------");
        view.write("|"+ StringUtils.join(tableColumns, "|")+"|");
        view.write("--------------------");
        for (DataSet dataSet : dataSetList) {
            view.write("|"+ StringUtils.join(dataSet.getValues(), "|")+"|");
        }
        view.write("--------------------");

    }
}
