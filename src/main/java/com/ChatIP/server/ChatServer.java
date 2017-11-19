package com.ChatIP.server;

import com.ChatIP.Settings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.ServerSocket;

class ChatServer extends JFrame {
    private static JTextArea textArea; // Панель для вывода сообщения

    private ChatServer(String s) {
        super(s);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // Выходить из программы при закрытии основного окна
        this.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) { }

            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }

            @Override
            public void windowClosed(WindowEvent e) { }

            @Override
            public void windowIconified(WindowEvent e) { }

            @Override
            public void windowDeiconified(WindowEvent e) { }

            @Override
            public void windowActivated(WindowEvent e) { }

            @Override
            public void windowDeactivated(WindowEvent e) { }
        });      // Добавить событи для взаимодействия с основным окном

        textArea = new JTextArea(20, 30);         // Панель для вывода сообщения
        textArea.setEditable(false);                             // Сделать панель для вывода текста активной

        JScrollPane scrollPane = new JScrollPane(textArea);                                  // Панель прокрутки
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);     // Вертикальная прокрутка
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); // Горизонтальная прокрутка
        setPreferredSize(new Dimension(450, 450));                             // Установить размер панели прокрутки
        add(scrollPane);                                                                     // Добавить на окно панель прокрутки

        setVisible(true); // Сделать окно видимым
        pack();           //  Сжать окно до минимума
    }

    // Вывод сообщения
    public static void enterMessage(String text) {
        System.out.println(text);     // На панель
        textArea.append(text + "\n"); // В коммандную строку
    }

    public static void main(String[] args) {
        new ChatServer("IPChatServer V1.2");

        try (ServerSocket serverSocket = new ServerSocket(Settings.getPort())) {
            enterMessage("Сервер запущен");

            while(true) {
                if (SocketThread.getClientsQuantity() <= Settings.getSizeMaxClients()) {   // Если не привышено максималь
                    new Thread(new SocketThread(serverSocket.accept())).start();           // Созлание нового потока на сервере
                } else {
                    enterMessage("Превышено максимальное количество пользователей!"); // Вывести информационное сообщение
                }
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}