package ua.com.tyomsky.sqlcmd.controller.command;

import ua.com.tyomsky.sqlcmd.view.View;

public class Exit implements Command {

    private View view;

    public Exit(View view) {
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.equals("exit");
    }

    @Override
    public void process(String command) {
        view.write("До скорой встречи!");
        throw new ExitException();
    }

    @Override
    public boolean needsConnection() {
        return false;
    }

    @Override
    public String format() {
        return "exit";
    }

    @Override
    public String description() {
        return "для выхода из программы";
    }
}
