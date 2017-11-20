package com.ChatIP.client;

import com.ChatIP.Settings;
import com.ChatIP.entity.Message;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.Date;

public class ChatClientApplet extends Applet implements ActionListener {

    private static TextField textEnter = new TextField(50);
    private static TextArea textArea = new TextArea("", 15, 50);
    private ObjectOutputStream out;
    private String name = "";
    private String whoIm;

    static void addMessage(Message message) {
        textArea.append(String.valueOf(message) + "\n");        // Добавление сообщения в конец текстового поля
        textArea.setCaretPosition(textArea.getText().length()); // Перемещение в конец текстового поля
    }

    @Override
    public void init() {
        textArea.setEditable(false);           // сделать неактивной для редактирования область вывода
        textArea.append("Введите имя:");       //
        add(textArea);                         // добавить область вывода
        add(textEnter);                        // добавить область ввода
        textEnter.addActionListener(this);  // добавить реакцию на нажатие enter
        textEnter.requestFocus();              // Установить фокус на панель ввода

        try {
            InetAddress address = InetAddress.getByName(Settings.getServerPc()); // получение адреса сервера в сети
            Socket socket = new Socket(address, Settings.getPort());             // открытия соета для связи с сервером

            // Получение IP по имени компбютера
            whoIm = InetAddress.getLocalHost().getHostName() + " - " + InetAddress.getLocalHost().getHostAddress();

            out = new ObjectOutputStream(socket.getOutputStream()); // создание потока для отправки сообщение на сервер
            new Thread(new ClientInWeb(socket)).start();            // Создание потока для входящих сообщений с сервера

            System.out.println(Settings.getServerPc()); // вывод на экран название ПК сервера
            System.out.println(Settings.getPort());     // вывод на экран порт ПК сервера
            System.out.println("address = " + address); // вывод на экран адреса
            System.out.println("socket = " + socket);   // вывод на экран сокета
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    @Override
    public void start() {}

    @Override
    public void stop() {}

    @Override
    public void destroy() {
        try {
            // Отправка на сервер данных, что клиент отключился
            out.writeObject(new Message(new Timestamp(new Date().getTime()), name, "END", whoIm, "offline"));
            out.flush(); // проталкивание буфера
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint(); // Перерисовка окна
    }

    @Override
    public void paint(Graphics g) {
        if (!textEnter.getText().equals("")) {
            try {
                // Отправка сообщения на сервер
                out.writeObject(new Message(new Timestamp(new Date().getTime()), name, textEnter.getText(), whoIm, "online"));
                out.flush();            // проталкивание буфера вывода
            } catch (IOException e) {
                System.err.println(e);
            }
            textEnter.setText("");  // обнуление строки для ввода текста
        }
    }
}