package com.ChatIP;

import org.w3c.dom.*;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

// Получение настроек из XML файла
public class Settings {
    private static int port;           // Порт
    private static String serverPc;    // Имя серверного компьютера
    private static int sizeHistory;    // Получить максимального количества сообщении в истории
    private static int sizeMaxClients; // Получение максимального количества подключенных к серверу клиентов

    public static int getPort() {
        openFileXML();
        return port;
    }

    public static String getServerPc() {
        openFileXML();
        return serverPc;
    }

    public static int getSizeHistory() {
        openFileXML();
        return sizeHistory;
    }

    public static int getSizeMaxClients() {
        openFileXML();
        return sizeMaxClients;
    }

    // Получение настроек из XML файла
    private static void openFileXML() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document doc = null;
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            File file = new File("configChat.xml");
            doc = builder.parse(file);
//          URL url = new URL("http://jc3.ucoz.com/_ld/0/1_configChat.xml");
//          doc = builder.parse(url.openStream());
        } catch (SAXException | ParserConfigurationException | IOException ex) {
            System.err.println(ex);
        }

        assert doc != null;
        Element root = doc.getDocumentElement();
        NodeList children = root.getChildNodes();

        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child instanceof Element) {
                Element childElement = (Element) child;
                Text textNode = (Text) childElement.getFirstChild();
                String text = textNode.getData().trim();

                switch (childElement.getTagName()) {
                    case "server_pc": serverPc = text; break;
                    case "port": port = Integer.parseInt(text); break;
                    case "size_history": sizeHistory = Integer.parseInt(text); break;
                    case "size_max_clients": sizeMaxClients = Integer.parseInt(text); break;
                }
            }
        }
    }
}
