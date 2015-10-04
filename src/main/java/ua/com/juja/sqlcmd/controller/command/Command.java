package ua.com.juja.sqlcmd.controller.command;

public interface Command {

    boolean canProcess(String command);

    void process(String command);

//    TODO выделить новым методом интерфейса Command формат команды и описание, которое выводит help
//    String format();
//    String description();
}
