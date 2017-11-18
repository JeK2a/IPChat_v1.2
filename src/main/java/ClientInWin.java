import entity.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

class ClientInWin implements Runnable {

    private Socket socket;

    ClientInWin(Socket socket) { this.socket = socket; }

    @Override
    public void run() {
        while (true) {
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                ChatClientWin.addMessage((Message) objectInputStream.readObject());
            } catch (IOException | ClassNotFoundException e) {
                System.err.println(e);
            }
        }
    }
}
