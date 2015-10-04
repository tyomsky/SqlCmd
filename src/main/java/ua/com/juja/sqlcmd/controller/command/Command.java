package ua.com.juja.sqlcmd.controller.command;

public interface Command {

    boolean canProcess(String command);

    void process(String command);

    boolean needsConnection();

    String format();

    String description();

}
