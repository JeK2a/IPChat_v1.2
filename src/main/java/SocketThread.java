import entity.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

class SocketThread implements Runnable {

    private final Socket SOCKET;
    private static ArrayList<Socket> listSocket = new ArrayList<>();
    private static int clientsColvo = 0;
    private Message message = null;
    private ObjectInputStream inputStream = null;

    SocketThread(Socket socket) { this.SOCKET = socket; } // Конструктор

    static int getClientsQuantity() { return clientsColvo; }

    @Override
    public void run() {
        // старт серверного потока
        try {
            clientsColvo++; // Увеличение количество клиентов
            listSocket.add(SOCKET); // Добавление сокета в общий список
            inputStream = new ObjectInputStream(SOCKET.getInputStream()); // Создание постоянного одинночного входного потока
            ChatServer.enterMessage("User connect...");
        } catch (IOException ex) {
            System.err.println(ex);
        }
        // отправка новому клиенту истории чата
        ObjectOutputStream outputStream;
        try {
            for (Message m : ChatHistory.getList()) { // Перебор всех сообщейний из списка
                outputStream = new ObjectOutputStream(SOCKET.getOutputStream()); // Получение исходящего потока из сокета
                outputStream.writeObject(m); // Отправка исходящего сообщения
                outputStream.flush(); // Проталкивание буфера
            }
        } catch (IOException ex) {
            System.err.println(ex);
        }
        // Работа постоянного приема входящих сообщений с постоянного входящего потока
        while (true) {
            try {
                message = (Message) inputStream.readObject(); // Прием сообщение с постоянного входящего потока
                ChatHistory.add(message); // Добавление сообщения в историю
                AddToMySQL.addMessageToMySQL(message); // Добавление сообщения в базу
                // Окончание работы потока
                if (message.getText().contains("END")) {
                    listSocket.remove(SOCKET);
                    ChatServer.enterMessage("User disconnect...");
                    clientsColvo--;
                    return;
                }
            } catch (IOException | ClassNotFoundException e) {
                System.err.println(e);
            }

            // Рассыл входящего сообщения по временным исходящим каналам
            try {
                for (Socket s : listSocket) { // отсылка сообщения всем сокетам\клиентам
                    outputStream = new ObjectOutputStream(s.getOutputStream());
                    outputStream.writeObject(message);
                    outputStream.flush();
                }
            } catch (IOException ex) {
                System.err.println(ex);
            }
        }
    }
}

