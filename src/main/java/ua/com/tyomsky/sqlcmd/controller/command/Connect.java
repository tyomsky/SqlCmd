package ua.com.tyomsky.sqlcmd.controller.command;

import ua.com.tyomsky.sqlcmd.model.DatabaseManager;
import ua.com.tyomsky.sqlcmd.view.View;

public class Connect implements Command {

    private DatabaseManager manager;
    private View view;

    public Connect(DatabaseManager manager, View view) {
        this.view = view;
        this.manager = manager;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("connect|");
    }

    @Override
    public void process(String command) {
        String[] data = command.split("\\|");
        if (data.length != 4) {
            throw new IllegalArgumentException("Неверно количество параметров разделенных знаком '|', ожидается 4, но есть: " + data.length);
        }
        manager.connect(data[1], data[2], data[3]);
        if (manager.isConnected()) {
            view.write("Успех!");
        }
    }

    @Override
    public boolean needsConnection() {
        return false;
    }

    @Override
    public String format() {
        return "connect|databaseName|userName|password";
    }

    @Override
    public String description() {
        return "для подключения к базе данных, с которой будем работать";
    }
}
