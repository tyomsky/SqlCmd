package ua.com.tyomsky.sqlcmd.integration;


import org.junit.Test;
import ua.com.tyomsky.sqlcmd.controller.Main;

import static org.junit.Assert.assertEquals;

public class IntegrationTest {

    private ConsoleMock console = new ConsoleMock();

    @Test
    public void testHelp() {
        // given
        console.addIn("help");
        console.addIn("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Привет юзер!\r\n" +
                "Введи, пожалуйста имя базы данных, имя пользователя и пароль в формате: connect|database|userName|password\r\n" +
                // help
                "Существующие команды:\r\n" +
                "\tconnect|databaseName|userName|password\r\n" +
                "\t\tдля подключения к базе данных, с которой будем работать\r\n" +
                "\ttables\r\n" +
                "\t\tдля получения списка всех таблиц базы, к которой подключились\r\n" +
                "\tclear|tableName\r\n" +
                "\t\tдля очистки всей таблицы\r\n" +
                "\tcreate|tableName|column1|value1|column2|value2|...|columnN|valueN\r\n" +
                "\t\tдля создания записи в таблице\r\n" +
                "\tfind|tableName\r\n" +
                "\t\tдля получения содержимого таблицы 'tableName'\r\n" +
                "\thelp\r\n" +
                "\t\tдля вывода этого списка на экран\r\n" +
                "\texit\r\n" +
                "\t\tдля выхода из программы\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                // exit
                "До скорой встречи!\r\n", console.getOut());
    }

    @Test
    public void testExit() {
        // given
        console.addIn("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Привет юзер!\r\n" +
                "Введи, пожалуйста имя базы данных, имя пользователя и пароль в формате: connect|database|userName|password\r\n" +
                // exit
                "До скорой встречи!\r\n", console.getOut());
    }

    @Test
    public void testListWithoutConnect() {
        // given
        console.addIn("tables");
        console.addIn("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Привет юзер!\r\n" +
                "Введи, пожалуйста имя базы данных, имя пользователя и пароль в формате: connect|database|userName|password\r\n" +
                // tables
                "Вы не можете пользоваться командой 'tables' пока не подключитесь с помощью комманды connect|databaseName|userName|password\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                // exit
                "До скорой встречи!\r\n", console.getOut());
    }

    @Test
    public void testFindWithoutConnect() {
        // given
        console.addIn("find|user");
        console.addIn("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Привет юзер!\r\n" +
                "Введи, пожалуйста имя базы данных, имя пользователя и пароль в формате: connect|database|userName|password\r\n" +
                // find|user
                "Вы не можете пользоваться командой 'find|user' пока не подключитесь с помощью комманды connect|databaseName|userName|password\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                // exit
                "До скорой встречи!\r\n", console.getOut());
    }

    @Test
    public void testUnsupported() {
        // given
        console.addIn("unsupported");
        console.addIn("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Привет юзер!\r\n" +
                "Введи, пожалуйста имя базы данных, имя пользователя и пароль в формате: connect|database|userName|password\r\n" +
                // unsupported
                "Вы не можете пользоваться командой 'unsupported' пока не подключитесь с помощью комманды connect|databaseName|userName|password\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                // exit
                "До скорой встречи!\r\n", console.getOut());
    }

    @Test
    public void testUnsupportedAfterConnect() {
        // given
        console.addIn("connect|sqlcmd|postgres|postgres");
        console.addIn("unsupported");
        console.addIn("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Привет юзер!\r\n" +
                "Введи, пожалуйста имя базы данных, имя пользователя и пароль в формате: connect|database|userName|password\r\n" +
                // connect
                "Успех!\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                // unsupported
                "Несуществующая команда: unsupported\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                // exit
                "До скорой встречи!\r\n", console.getOut());
    }

    @Test
    public void testListAfterConnect() {
        // given
        console.addIn("connect|sqlcmd|postgres|postgres");
        console.addIn("tables");
        console.addIn("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Привет юзер!\r\n" +
                "Введи, пожалуйста имя базы данных, имя пользователя и пароль в формате: connect|database|userName|password\r\n" +
                // connect
                "Успех!\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                // tables
                "[user, test]\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                // exit
                "До скорой встречи!\r\n", console.getOut());
    }

    @Test
    public void testFindAfterConnect() {
        // given
        console.addIn("connect|sqlcmd|postgres|postgres");
        console.addIn("clear|user");
        console.addIn("find|user");
        console.addIn("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Привет юзер!\r\n" +
                "Введи, пожалуйста имя базы данных, имя пользователя и пароль в формате: connect|database|userName|password\r\n" +
                // connect
                "Успех!\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                // clear
                "Таблица user была успешно очищена.\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                // find|user
                "--------------------\r\n" +
                "|name|password|id|\r\n" +
                "--------------------\r\n" +
                "--------------------\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                // exit
                "До скорой встречи!\r\n", console.getOut());
    }

    @Test
    public void testConnectAfterConnect() {
        // given
        console.addIn("connect|sqlcmd|postgres|postgres");
        console.addIn("tables");
        console.addIn("connect|test|postgres|postgres");
        console.addIn("tables");
        console.addIn("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Привет юзер!\r\n" +
                "Введи, пожалуйста имя базы данных, имя пользователя и пароль в формате: connect|database|userName|password\r\n" +
                // connect sqlcmd
                "Успех!\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                // tables
                "[user, test]\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                // connect test
                "Успех!\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                // tables
                "[qwe]\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                // exit
                "До скорой встречи!\r\n", console.getOut());
    }

    @Test
    public void testConnectWithError() {
        // given
        console.addIn("connect|sqlcmd");
        console.addIn("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Привет юзер!\r\n" +
                "Введи, пожалуйста имя базы данных, имя пользователя и пароль в формате: connect|database|userName|password\r\n" +
                // connect sqlcmd
                "Неудача! по причине: Неверно количество параметров разделенных знаком '|', ожидается 4, но есть: 2\r\n" +
                "Повтори попытку.\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                // exit
                "До скорой встречи!\r\n", console.getOut());
    }

    @Test
    public void testFindAfterConnect_withData() {
        // given
//        databaseManager.connect("sqlcmd", "postgres", "postgres");
//
//        databaseManager.clear("user");
//
//        DataSet user1 = new DataSet();
//        user1.put("id", 13);
//        user1.put("name", "Stiven");
//        user1.put("password", "*****");
//        databaseManager.create("user", user1);
//
//        DataSet user2 = new DataSet();
//        user2.put("id", 14);
//        user2.put("name", "Eva");
//        user2.put("password", "+++++");
//        databaseManager.create("user", user2);

        console.addIn("connect|sqlcmd|postgres|postgres");
        console.addIn("clear|user");
        console.addIn("create|user|id|13|name|Stiven|password|*****");
        console.addIn("create|user|id|14|name|Eva|password|+++++");
        console.addIn("find|user");
        console.addIn("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Привет юзер!\r\n" +
                "Введи, пожалуйста имя базы данных, имя пользователя и пароль в формате: connect|database|userName|password\r\n" +
                // connect
                "Успех!\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                // clear|user
                "Таблица user была успешно очищена.\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                // create|user|id|13|name|Stiven|password|*****
                "Запись {names:[id, name, password], values:[13, Stiven, *****]} была успешно создана в таблице 'user'.\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                // create|user|id|14|name|Eva|password|+++++
                "Запись {names:[id, name, password], values:[14, Eva, +++++]} была успешно создана в таблице 'user'.\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                // find|user
                "--------------------\r\n" +
                "|name|password|id|\r\n" +
                "--------------------\r\n" +
                "|Stiven|*****|13|\r\n" +
                "|Eva|+++++|14|\r\n" +
                "--------------------\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                // exit
                "До скорой встречи!\r\n", console.getOut());
    }

    @Test
    public void testClearWithError() {
        // given
        console.addIn("connect|sqlcmd|postgres|postgres");
        console.addIn("clear|sadfasd|fsf|fdsf");
        console.addIn("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Привет юзер!\r\n" +
                "Введи, пожалуйста имя базы данных, имя пользователя и пароль в формате: connect|database|userName|password\r\n" +
                // connect
                "Успех!\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                // clear|sadfasd|fsf|fdsf
                "Неудача! по причине: Формат команды 'clear|tableName', а ты ввел: clear|sadfasd|fsf|fdsf\r\n" +
                "Повтори попытку.\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                // exit
                "До скорой встречи!\r\n", console.getOut());
    }

    @Test
    public void testCreateWithErrors() {
        // given
        console.addIn("connect|sqlcmd|postgres|postgres");
        console.addIn("create|user|error");
        console.addIn("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Привет юзер!\r\n" +
                "Введи, пожалуйста имя базы данных, имя пользователя и пароль в формате: connect|database|userName|password\r\n" +
                // connect
                "Успех!\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                // create|user|error
                "Неудача! по причине: Должно быть четное количество параметров в формате 'create|tableName|column1|value1|column2|value2|...|columnN|valueN', а ты прислал: 'create|user|error'\r\n" +
                "Повтори попытку.\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                // exit
                "До скорой встречи!\r\n", console.getOut());
    }
}
