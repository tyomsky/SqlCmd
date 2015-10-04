package ua.com.tyomsky.sqlcmd.controller;

import ua.com.tyomsky.sqlcmd.controller.command.*;
import ua.com.tyomsky.sqlcmd.model.DatabaseManager;
import ua.com.tyomsky.sqlcmd.model.JDBCDatabaseManager;
import ua.com.tyomsky.sqlcmd.view.Console;
import ua.com.tyomsky.sqlcmd.view.View;

public class Main {

    public static void main(String[] args) {
        View view = new Console();
        DatabaseManager manager = new JDBCDatabaseManager();

        MainController controller = new MainController(view, manager, new Command[]{
                new Help(view),
                new Exit(view),
                new Clear(manager, view),
                new Tables(manager, view),
                new Find(manager, view),
                new Connect(manager, view),
                new Create(manager, view),
                new Unsupported(view)
        });
        controller.run();
    }
}
