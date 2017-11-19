import com.ChatIP.Settings;
import com.ChatIP.entity.Message;
import org.junit.jupiter.api.Test;
import com.ChatIP.server.AddToMySQL;

import java.sql.*;
import java.util.Date;

class MyTest {

    @Test
    void testMultiply() {
        String s = new String("Hello!");

        char[] cs = s.toCharArray();

        for (char c : cs) {
            System.out.println(c);
        }
    }

    @Test
    void testMessage() throws Exception {
        try {
            System.out.println(new Message(new Timestamp(new Date().getTime()), "JeK2a", "text", "PCPI", "status"));
            System.out.print("TestMessage Все ок!");
        } catch (Exception e) {
            System.err.println("TestMessage Нифига не ок!");
        }
    }

    @Test
    void testSettings() throws Exception {
        Settings.getPort();
        Settings.getServerPc();
        Settings.getSizeHistory();
        Settings.getSizeMaxClients();
        System.out.println("TestSettings Все ок!");
    }

    private static final String url = "jdbc:mysql://localhost:3306";
    private static final String user = "root";
    private static final String password = "nokia3510";

    @Test
    void testAddToMySQL() throws Exception {

        AddToMySQL.addMessageToMySQL(new Message(new Timestamp(new Date().getTime()), "JeK2aTest", "testText", "testPC", "test"));
        System.out.println("TestAddToMySQL 1 Все ок!");

        for (int i = 0; i<10000; i++) {
            AddToMySQL.addMessageToMySQL(new Message(new Timestamp(new Date().getTime()), "JeK2aTest", "text", "testPC", "test"));
        }
        System.out.println("TestAddToMySQL 3 Все ок!");

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, user, password); // Подключение к MySQL базе данных

            if (connection == null) {
                System.out.println("Нет соединения с БД!");
                System.exit(0);
            }

            Statement statement = connection.createStatement(); // getting Statement object to execute query

            // Создание запроса для добавление сообщения в базу
            String query = "INSERT INTO myshema.message (date, name, text, namePCAndIP, status) \n" +
                           " VALUES (\'" + new Timestamp(new Date().getTime()) + "\', \'" + "JeK2aTest" +
                           "\', \'" + "test" + "\', \'" + "test" + "\', \'" + "test" + "\');";
            statement.executeUpdate(query);  // Выполнение запроса
            System.out.println("TestAddToMySQL 2 Все ок!");
        } catch(SQLException | ClassNotFoundException ex){
            System.err.println(ex);
        }
    }
}
