package ua.com.juja.sqlcmd.controller.command;

public class Create implements Command{

    

    @Override
    public boolean canProcess(String command) {
        return false;
    }

    @Override
    public void process(String command) {

    }
}
