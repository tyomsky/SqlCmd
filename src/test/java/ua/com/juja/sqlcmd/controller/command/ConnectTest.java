package ua.com.juja.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ConnectTest {
    private DatabaseManager manager;
    private View view;
    private Command command;

    @Before
    public void setup() {
        manager = mock(DatabaseManager.class);
        view = mock(View.class);
        command = new Connect(manager, view);
    }

    @Test
    public void testConnect() {
        // given
        when(manager.isConnected()).thenReturn(true);
        // when
        command.process("connect|database|userName|password");

        // then
        verify(manager).connect("database", "userName", "password");
        verify(view).write("Успех!");
    }


    @Test
    public void testCanProcessConnectWithParametersString() {
        // when
        boolean canProcess = command.canProcess("connect|database|userName|password");

        // then
        assertTrue(canProcess);
    }

    @Test
    public void testCantProcessConnectWithoutParametersString() {
        // when
        boolean canProcess = command.canProcess("connect");

        // then
        assertFalse(canProcess);
    }

    @Test
    public void testValidationErrorWhenCountParametersIsLessThan4() {
        // when
        try {
            command.process("connect");
            fail();
        } catch (IllegalArgumentException e) {
            // then
            assertEquals("Неверно количество параметров разделенных знаком '|', ожидается 4, но есть: 1", e.getMessage());
        }
    }

    @Test
    public void testValidationErrorWhenCountParametersIsMoreThan4() {
        // when
        try {
            command.process("connect|database|userName|password|asd");
            fail();
        } catch (IllegalArgumentException e) {
            // then
            assertEquals("Неверно количество параметров разделенных знаком '|', ожидается 4, но есть: 5", e.getMessage());
        }
    }
}