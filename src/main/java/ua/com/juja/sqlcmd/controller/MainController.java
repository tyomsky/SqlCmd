package ua.com.juja.sqlcmd.controller;

import ua.com.juja.sqlcmd.controller.command.*;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

import java.util.AbstractList;
import java.util.LinkedList;
import java.util.List;

public class MainController {

    View view;
    DatabaseManager databaseManager;
    Command[] commands;

    public MainController(View view, DatabaseManager databaseManager, Command[] commands) {
        this.view = view;
        this.databaseManager = databaseManager;
        this.commands = commands;
    }

    public void run() {

    }

}
