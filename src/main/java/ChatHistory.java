import entity.Message;

import java.util.ArrayList;

// История сообщений
class ChatHistory {
    private static ArrayList<Message> list = new ArrayList<>(); // Список сообщений

    // Добавление нового сообщения в историю
    public static void add(Message message) {
        if (list.size() >= Settings.getSizeHistory()) { // Если история заполнена, то
            list.remove(0);                       // Удаление самого старого сообщения
        }
        list.add(message);                               // Добавление нового сообщения в историю
    }

    // Получить список сообщений
    public static ArrayList<Message> getList() {
        return list;
    }
}
