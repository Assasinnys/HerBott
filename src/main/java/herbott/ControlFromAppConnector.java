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
            clientSocket = new Socket();
            String hostAddress = clientSocket.getLocalAddress().toString();
            System.out.println("hostAddress = " + hostAddress);
            ipAddress = serverSocket.getInetAddress().getHostAddress();
            System.out.println("Address socket = " + ipAddress);
            String hostName = serverSocket.getInetAddress().getHostName();
            System.out.println("HostName = " + hostName);
            String canonicalHostName = serverSocket.getInetAddress().getCanonicalHostName();
            System.out.println("canonicalHostName = " + canonicalHostName);
            while (true) {
                clientSocket = serverSocket.accept();
                System.out.println("Connection complete!");
            }
        } catch (IOException io) {
            io.printStackTrace();
        }
    }
}
