import entity.Message;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.Date;

public class ChatClientWin {

    private static JTextArea textArea;     // Текстовое поле для отбажения текстовых сообщений
    private static JTextField textEnter;   // Текстовое поле для ввода ссообщения
    private static String name = "anonim"; // Стандартное имя

    // Добавление сообщения в конец текстового поля
    static void addMessage(Message message) {
        textArea.append(String.valueOf(message) + "\n");                      // Добавление сообщения в конец текстового поля
        textArea.setCaretPosition(ChatClientWin.textArea.getText().length()); // Перемещение в конец текстового поля
    }

    // Диалоговое окно для ввода имени пользователя чата
    private static class EnterNameDialog extends JDialog {
        EnterNameDialog(JFrame parent) {  // Конструктор
            super(parent, "Enter name dialog", true);
            setLayout(new FlowLayout());
            add(new JLabel("Enter your name"), BorderLayout.WEST);
            JTextField enterDialog = new JTextField("",15);
            add(enterDialog, BorderLayout.CENTER);

            JButton okB = new JButton("ok");
            okB.addActionListener(e -> {
                if (!enterDialog.getText().equals("")) {
                    name = enterDialog.getText();
                    dispose();
                }
            });
            add(okB, BorderLayout.EAST);
            pack();
            setVisible(true);
        }
    }

    private static class MainFrame extends JFrame {
        private ObjectOutputStream outputStream;
        private String status = "online";
        private String whoIm = "";

        MainFrame() {
            super("IPChatClient V1.2");
            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

            try {
                InetAddress address = InetAddress.getByName(Settings.getServerPc()); // получение адреса сервера в сети
                Socket socket = new Socket(address, Settings.getPort()); // открытия соета для связи с сервером

                whoIm = InetAddress.getLocalHost().getHostName() + " - " + InetAddress.getLocalHost().getHostAddress();

                outputStream = new ObjectOutputStream(socket.getOutputStream()); // создание потока для отправки сообщение на сервер
                new Thread(new ClientInWin(socket)).start();  // Создание потока для входящих сообщений с сервера

                System.out.println(Settings.getServerPc()); // вывод на экран название ПК сервера
                System.out.println(Settings.getPort()); // вывод на экран порт ПК сервера
                System.out.println("address = " + address); // вывод на экран адреса
                System.out.println("socket = " + socket); // вывод на экран сокета
            } catch (IOException e) {
                System.err.println(e);
            }

            // Создание графической части клиента
            textArea = new JTextArea(20, 30);
            textArea.setEditable(false);

            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            setPreferredSize(new Dimension(450, 450));
            add(scrollPane, BorderLayout.NORTH);

            textEnter = new JTextField(50);
            add(textEnter, BorderLayout.SOUTH);

            setVisible(true);

            textEnter.addActionListener(e -> {
                if (!textEnter.getText().equals("")) {
                    try {
                        outputStream.writeObject(new Message(new Timestamp(new Date().getTime()), name, textEnter.getText(), whoIm, status)); // отправка сообщения на сервер
                        outputStream.flush(); // проталкивание буфера вывода
                        textEnter.setText("");  // обнуление строки для ввода текста
                    } catch (IOException e2) {
                        System.err.println(e2);
                    }
                }
            });
            pack();
            textEnter.requestFocus(true);

            this.addWindowListener(new WindowListener() {
                @Override
                public void windowOpened(WindowEvent e) { }

                @Override
                public void windowClosing(WindowEvent e) {
                    try {
                        outputStream.writeObject(new Message(new Timestamp(new Date().getTime()), name, "END", whoIm, "offline")); // отправка на сервер данных, что клиент отключился
                        outputStream.flush(); // проталкивание буфера
                    } catch (IOException e1) {
                        System.err.println(e1);
                    }
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
            });
        }
    }

    public static void main(String[] args) {
        new EnterNameDialog(new MainFrame());
    }
}
