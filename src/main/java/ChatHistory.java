import entity.Message;

import java.util.ArrayList;
import java.util.HashSet;

// История сообщений
class ChatHistory {
    private static HashSet<Message> list = new HashSet<>(); // Список сообщений

    // Добавление нового сообщения в историю
    public static void add(Message message) {
        if (list.size() >= Settings.getSizeHistory()) { // Если история заполнена, то
            list.remove(0);                         // Удаление самого старого сообщения
        }
        list.add(message);                              // Добавление нового сообщения в историю
    }

    // Получить список сообщений
    public static HashSet<Message> getList() {
        return list;
    }
}
