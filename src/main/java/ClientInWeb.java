import entity.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

class ClientInWeb implements Runnable {

    private Socket socket;

    ClientInWeb(Socket socket) { this.socket = socket; }

    @Override
    public void run() {
        while (true) {
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                Message message = (Message) objectInputStream.readObject();
                ChatClientApplet.addMessage(message);
            } catch (IOException | ClassNotFoundException e) {
                System.err.println(e);
            }
        }
    }
}
