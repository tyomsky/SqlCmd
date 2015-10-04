package ua.com.tyomsky.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import ua.com.tyomsky.sqlcmd.model.DatabaseManager;
import ua.com.tyomsky.sqlcmd.view.View;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TablesTest {

    private DatabaseManager manager;
    private View view;
    private Command command;

    @Before
    public void setup() {
        manager = mock(DatabaseManager.class);
        view = mock(View.class);
        command = new Tables(manager, view);
    }

    @Test
    public void testClearTable() {
        // given
        Set<String> tableNames = new LinkedHashSet<>(Arrays.asList("user", "test"));
        when(manager.getTableNames()).thenReturn(tableNames);
        // when
        command.process("tables");

        // then
        verify(manager).getTableNames();
        verify(view).write(tableNames.toString());
    }


    @Test
    public void testCanProcessClearWithParametersString() {
        // when
        boolean canProcess = command.canProcess("tables");

        // then
        assertTrue(canProcess);
    }

    @Test
    public void testCantProcessClearWithoutParametersString() {
        // when
        boolean canProcess = command.canProcess("tabls");

        // then
        assertFalse(canProcess);
    }

    @Test
    public void testConnectionIsNeeded() {
        assertTrue(command.needsConnection());
    }

}