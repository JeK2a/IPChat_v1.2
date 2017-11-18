import entity.Message;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

class AddToMySQL {

    private static final String url = "jdbc:mysql://localhost:3306";
    private static final String user = "root";
    private static final String password = "nokia3510";

    static void addMessageToMySQL(Message message) {
        Date date = message.getDate();
        String name = message.getName();
        String text = message.getText();
        String namePCAndIP = message.getNamePCAndIP();
        String status = message.getStatus();
        Connection connection = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(url, user, password); // opening database connection to MySQL server

            if (connection == null) {
                System.out.println("Нет соединения с БД!");
                System.exit(0);
            }

            Statement statement = connection.createStatement(); // getting Statement object to execute query

            String query = "INSERT INTO myshema.message (date, name, text, namePCAndIP, status) \n" +
                           " VALUES (\'" + new SimpleDateFormat("yyyyMMddHHmmss").format(date) +
                           "\', \'" + name + "\', \'" + text + "\', \'" + namePCAndIP + "\', \'" + status + "\');";
            statement.executeUpdate(query);
        } catch(SQLException | ClassNotFoundException e){
             System.err.println(e);
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println(e);
            }
        }
}
}