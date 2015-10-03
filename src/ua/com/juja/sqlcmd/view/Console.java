package ua.com.juja.sqlcmd.view;

import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Created by oleksandr.baglai on 25.08.2015.
 */
public class Console implements View {

    @Override
    public void write(String message) {
        System.out.println(message);
    }

    @Override
    public String read() {
        try {
            Scanner scanner = new Scanner(System.in);
            return scanner.nextLine();
        } catch (NoSuchElementException e) {
            return null;
        }
    }
}
