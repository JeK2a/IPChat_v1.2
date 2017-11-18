import entity.Message;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Date;

public class ChatClientApplet extends Applet implements ActionListener {

    private static TextField textEnter = new TextField(50);
    private static TextArea textArea = new TextArea("", 15, 50);
    private ObjectOutputStream out;
    private String name = "";
    private String whoIm;

    static void addMessage(Message message) {
        textArea.append(String.valueOf(message) + "\n");
        textArea.setCaretPosition(textArea.getText().length());
    }

    @Override
    public void init() {
        textArea.setEditable(false); // сделать неактивной для редактирования область вывода
        textArea.append("Enter you name:");
        add(textArea); // добавить область вывода
        add(textEnter); // добавить область ввода
        textEnter.addActionListener(this);  // добавить реакцию на нажатие enter
        textEnter.requestFocus(); // Установить фокус на панель ввода

        try {
            //new Settings();  // подключить первональные настройки

            InetAddress address = InetAddress.getByName(Settings.getServerPc()); // получение адреса сервера в сети
            Socket socket = new Socket(address, Settings.getPort()); // открытия соета для связи с сервером

            whoIm = InetAddress.getLocalHost().getHostName() + " - " + InetAddress.getLocalHost().getHostAddress();

            out = new ObjectOutputStream(socket.getOutputStream()); // создание потока для отправки сообщение на сервер
            new Thread(new ClientInWeb(socket)).start();  // Создание потока для входящих сообщений с сервера

            System.out.println(Settings.getServerPc()); // вывод на экран название ПК сервера
            System.out.println(Settings.getPort()); // вывод на экран порт ПК сервера
            System.out.println("address = " + address); // вывод на экран адреса
            System.out.println("socket = " + socket); // вывод на экран сокета
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
            out.writeObject(new Message(new Date(), name, "END", whoIm, "offline")); // отправка на сервер данных, что клиент отключился
            out.flush(); // проталкивание буфера
        } catch (IOException e1) {
            System.err.println(e1);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        if (!textEnter.getText().equals("")) {
            if (name.equals("")) {
                name = textEnter.getText();
            } else {
                try {
                    String status = "online";
                    out.writeObject(new Message(new Date(), name, textEnter.getText(), whoIm, status)); // отправка сообщения на сервер
                    out.flush(); // проталкивание буфера вывода
                    textEnter.setText("");  // обнуление строки для ввода текста
                } catch (IOException e2) {
                    System.err.println(e2);
                }
            }
            textEnter.setText("");
        }
    }
}