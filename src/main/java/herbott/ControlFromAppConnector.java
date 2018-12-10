package herbott;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ControlFromAppConnector extends Thread {

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private volatile String ipAddress;

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(7755);
            ipAddress = serverSocket.getInetAddress().getHostAddress();
            System.out.println("Address socket = " + ipAddress);
            while (true) {
                clientSocket = serverSocket.accept();
                System.out.println("Connection complete!");
            }
        } catch (IOException io) {
            io.printStackTrace();
        }
    }
}
