package ua.com.tyomsky.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import ua.com.tyomsky.sqlcmd.model.DataSet;
import ua.com.tyomsky.sqlcmd.model.DataSetImpl;
import ua.com.tyomsky.sqlcmd.model.DatabaseManager;
import ua.com.tyomsky.sqlcmd.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class FindTest {

    private DatabaseManager manager;
    private View view;
    private Command command;

    @Before
    public void setup() {
        manager = mock(DatabaseManager.class);
        view = mock(View.class);
        command = new Find(manager, view);
    }

    @Test
    public void testFind() {
        // given
        final DataSet dataSet = new DataSetImpl();
        dataSet.put("id", 13);
        dataSet.put("name", "Stiven");
        dataSet.put("password", "*****");
        when(manager.getTableColumns(anyString())).thenReturn(new LinkedHashSet<>(Arrays.asList("id", "name", "password")));
        when(manager.getTableData(anyString())).thenReturn(new ArrayList<DataSet>(){{add(dataSet);}});
        // when
        command.process("find|user");
        // then
        verify(manager).getTableColumns(eq("user"));
        verify(manager).getTableData(eq("user"));
        ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
        verify(view, times(5)).write(argument.capture());

        List<String> allStrings = argument.getAllValues();

        assertEquals("[--------------------, " +
                "|id|name|password|, " +
                "--------------------, " +
                "|13|Stiven|*****|, " +
                "--------------------]", allStrings.toString());
    }


    @Test
    public void testCanProcessFindWithParametersString() {
        // when
        boolean canProcess = command.canProcess("find|user");

        // then
        assertTrue(canProcess);
    }

    @Test
    public void testCantProcessFindWithoutParametersString() {
        // when
        boolean canProcess = command.canProcess("find");

        // then
        assertFalse(canProcess);
    }

    @Test
    public void testValidationErrorWhenCountParametersIsLessThan2() {
        // when
        try {
            command.process("find");
            fail();
        } catch (IllegalArgumentException e) {
            // then
            assertEquals("Формат команды 'find|user', а ты ввел: find", e.getMessage());
        }
    }

    @Test
    public void testValidationErrorWhenCountParametersIsMoreThan2() {
        // when
        try {
            command.process("find|user|qwe");
            fail();
        } catch (IllegalArgumentException e) {
            // then
            assertEquals("Формат команды 'find|user', а ты ввел: find|user|qwe", e.getMessage());
        }
    }

    @Test
    public void testConnectionIsNeeded() {
        assertTrue(command.needsConnection());
    }

}