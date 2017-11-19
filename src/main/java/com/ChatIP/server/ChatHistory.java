package com.ChatIP.server;

import com.ChatIP.Settings;
import com.ChatIP.entity.Message;

import java.util.LinkedList;

// История сообщений
class ChatHistory {
    private static LinkedList<Message> list = new LinkedList<>(); // Список сообщений

    // Добавление нового сообщения в историю
    public static void add(Message message) {
        if (list.size() >= Settings.getSizeHistory()) { // Если история заполнена, то
            list.remove(0);                       // удаление самого старого сообщения
        }
        list.add(message);                               // добавление нового сообщения в историю
    }

    // Получить список сообщений
    public static LinkedList<Message> getList() {
        return list;
    }
}
