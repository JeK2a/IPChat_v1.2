package com.ChatIP.server;

import com.ChatIP.entity.Message;

import java.sql.*;

public class AddToMySQL {

    private static final String url = "jdbc:mysql://localhost:3306";
    private static final String user = "root";
    private static final String password = "nokia3510";

    public static void addMessageToMySQL(Message message) {
        Timestamp timestamp = new Timestamp(message.getDate().getTime());
        String name = message.getName();
        String text = message.getText();
        String namePCAndIP = message.getNamePCAndIP();
        String status = message.getStatus();
        Connection connection = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(url, user, password); // JDBC подключение к MySQL

            if (connection == null) {                       // Если подключение к БД не установлено
                System.err.println("Нет соединения с БД!"); // Вывести ошибку
                System.exit(0);                      // И выйти из программы
            }

            Statement statement = connection.createStatement(); // getting Statement object to execute query

            // Создание запроса для добавление сообщения в базу
            String query = "INSERT INTO myshema.message (date, name, text, namePCAndIP, status) \n" +
                           "VALUES (\'" + timestamp + "\', \'" + name + "\', \'" + text +
                           "\', \'" + namePCAndIP + "\', \'" + status + "\');";
            statement.executeUpdate(query); // Выполнить запрос
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