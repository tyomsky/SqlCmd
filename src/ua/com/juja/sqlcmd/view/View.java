package ua.com.juja.sqlcmd.view;

/**
 * Created by oleksandr.baglai on 25.08.2015.
 */
public interface View {

    void write(String message);

    String read();
}
