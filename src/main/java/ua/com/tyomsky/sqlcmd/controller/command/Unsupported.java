package ua.com.tyomsky.sqlcmd.controller.command;

import ua.com.tyomsky.sqlcmd.view.View;

public class Unsupported implements Command {

    View view;

    public Unsupported(View view) {
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return true;
    }

    @Override
    public void process(String command) {
        view.write("Несуществующая команда: " + command);
    }

    @Override
    public boolean needsConnection() {
        return true;
    }

    @Override
    public String format() {
        return "";
    }

    @Override
    public String description() {
        return "Несуществующая команда";
    }
}