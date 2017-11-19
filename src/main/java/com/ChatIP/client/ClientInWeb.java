package com.ChatIP.client;

import com.ChatIP.entity.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

class ClientInWeb implements Runnable {

    private Socket socket; // Сетевой сокет для пересылки сообщений

    ClientInWeb(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() { // Запуск потока
        while (true) {  // Работать постоянно
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream()); // Создание входящего потока из сокета
                Message message = (Message) objectInputStream.readObject();                           // Получение входящего сообщения
                ChatClientApplet.addMessage(message);                                                 // Добавление полученного сообщения на основно окно
            } catch (IOException | ClassNotFoundException e) {
                System.err.println(e);
            }
        }
    }
}
