package ua.com.tyomsky.sqlcmd.controller.command;

import ua.com.tyomsky.sqlcmd.model.DatabaseManager;
import ua.com.tyomsky.sqlcmd.view.View;

public class Tables implements Command {

    private DatabaseManager manager;
    private View view;

    public Tables(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return "tables".equals(command);
    }

    @Override
    public void process(String command) {
        view.write(manager.getTableNames().toString());
    }

    @Override
    public boolean needsConnection() {
        return true;
    }

    @Override
    public String format() {
        return "tables";
    }

    @Override
    public String description() {
        return "для получения списка всех таблиц базы, к которой подключились";
    }

}
