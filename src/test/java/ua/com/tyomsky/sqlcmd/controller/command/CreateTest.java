package ua.com.tyomsky.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import ua.com.tyomsky.sqlcmd.model.DataSet;
import ua.com.tyomsky.sqlcmd.model.DataSetImpl;
import ua.com.tyomsky.sqlcmd.model.DatabaseManager;
import ua.com.tyomsky.sqlcmd.view.View;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class CreateTest {

    private DatabaseManager manager;
    private View view;
    private Command command;

    @Before
    public void setup() {
        manager = mock(DatabaseManager.class);
        view = mock(View.class);
        command = new Create(manager, view);
    }

    @Test
    public void testCreate() {
        // given
        // when
        command.process("create|user|id|13|name|Stiven|password|*****");

        // then
        DataSet dataSet = new DataSetImpl();
        dataSet.put("id", 13);
        dataSet.put("name", "Stiven");
        dataSet.put("password", "*****");

        ArgumentCaptor<DataSetImpl> argument = ArgumentCaptor.forClass(DataSetImpl.class);
        verify(manager).create(eq("user"), argument.capture());
        assertEquals(argument.getValue().getNames().toString(), dataSet.getNames().toString());

        verify(view).write("Запись "+dataSet+" была успешно создана в таблице 'user'.");
    }


    @Test
    public void testCanProcessCreateWithParametersString() {
        // when
        boolean canProcess = command.canProcess("create|user|id|13|name|Stiven|password|*****");

        // then
        assertTrue(canProcess);
    }

    @Test
    public void testCantProcessCreateWithoutParametersString() {
        // when
        boolean canProcess = command.canProcess("create");

        // then
        assertFalse(canProcess);
    }

    @Test
    public void testValidationErrorWhenCountParametersIsOdd() {
        // when
        try {
            command.process("create|user|id|");
            fail();
        } catch (IllegalArgumentException e) {
            // then
            assertEquals("Должно быть четное количество параметров в формате " +
                    "'create|tableName|column1|value1|column2|value2|...|columnN|valueN', " +
                    "а ты прислал: \'create|user|id|\'", e.getMessage());
        }
    }

    @Test
    public void testConnectionIsNeeded() {
        assertTrue(command.needsConnection());
    }

}