package herbott;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ControlFromAppConnector extends Thread {

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private volatile String ipAddress;

    @Override
    public void run() {
        Server server = new Server(Integer.valueOf(System.getenv("PORT")));
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);
        context.addServlet(new ServletHolder(new Hi()), "/*");
        try {
            server.start();
            server.join();
        } catch (Exception io) {
            io.printStackTrace();
        }
    }

    class Hi extends HttpServlet {
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            System.out.println("get request accepted! ☺");
        }
    }
}
