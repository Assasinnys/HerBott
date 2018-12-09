package herbott;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ControlFromAppConnector extends Thread {

    private ServerSocket serverSocket;
    private Socket clientSocket;

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(7755);
            clientSocket = serverSocket.accept();
            System.out.println("Connection complete!");
        } catch (IOException io) {
            io.printStackTrace();
        }
    }
}
