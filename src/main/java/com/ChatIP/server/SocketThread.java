package com.ChatIP.server;

import com.ChatIP.entity.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;

class SocketThread implements Runnable {

    private final Socket SOCKET;                                 // Сокет
    private static LinkedList<Socket> listSocket = new LinkedList<>(); // Список всех сокетов клиентов, подключенных к серверу
    private static int clientsColvo = 0;                         // Количество подключенных клиентов
    private Message message = null;                              // Сообщение
    private ObjectInputStream inputStream = null;                // Входящий потока

    SocketThread(Socket socket) { this.SOCKET = socket; }        // Конструктор

    // Получение количества клиентов
    static int getClientsQuantity() {
        return clientsColvo;
    }

    @Override
    public void run() { // старт серверного потока
        try {
            clientsColvo++;                                               // Увеличение количество клиентов
            listSocket.add(SOCKET);                                       // Добавление сокета в общий список
            inputStream = new ObjectInputStream(SOCKET.getInputStream()); // Создание постоянного одинночного входного потока
            ChatServer.enterMessage("Клиент подключен");             // Вывод сообщения, что клиент подключен
        } catch (IOException ex) {
            System.err.println(ex);
        }
        // Отправка новому клиенту истории чата
        ObjectOutputStream outputStream;
        try {
            for (Message m : ChatHistory.getList()) {                            // Перебор всех сообщейний из списка
                outputStream = new ObjectOutputStream(SOCKET.getOutputStream()); // Получение исходящего потока из сокета
                outputStream.writeObject(m);                                     // Отправка исходящего сообщения
                outputStream.flush();                                            // Проталкивание буфера
            }
        } catch (IOException ex) {
            System.err.println(ex);
        }
        // Работа постоянного приема входящих сообщений с постоянного входящего потока
        while (true) {
            try {
                message = (Message) inputStream.readObject();           // Прием сообщение с постоянного входящего потока
                ChatHistory.add(message);                               // Добавление сообщения в историю
                AddToMySQL.addMessageToMySQL(message);                  // Добавление сообщения в базу
                // Окончание работы потока
                if (message.getText().contains("END")) {                // Если во входящем сообщении есть END, отклють клиента
                    listSocket.remove(SOCKET);                          // Удалить из списка сокет клиента, который отключился от клиента
                    ChatServer.enterMessage("Клиент отключен");    // Клиент отключен
                    clientsColvo--;                                     // Уменьшение количество клиентов
                    return;
                }
            } catch (IOException | ClassNotFoundException e) {
                System.err.println(e);
            }

            // Рассыл входящего сообщения по всем клиентам
            try {
                for (Socket s : listSocket) {                                   // Отсылка сообщения всем сокетам/клиентам
                    outputStream = new ObjectOutputStream(s.getOutputStream()); // Создание из соета исходящего потока
                    outputStream.writeObject(message);                          // Отправить сообщение по исходящему потоку
                    outputStream.flush();                                       // Протолкнуть сообщение
                }
            } catch (IOException ex) {
                System.err.println(ex);
            }
        }
    }
}

