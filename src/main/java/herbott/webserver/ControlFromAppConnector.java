package herbott.webserver;

import herbott.webserver.servlets.Hi;
import herbott.webserver.servlets.OauthServlet;
import herbott.webserver.servlets.TakeActiveServlet;
import herbott.webserver.servlets.WebHookCallback;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class ControlFromAppConnector extends Thread {

    @Override
    public void run() {
        int port = Integer.valueOf(System.getenv("PORT"));
        Server server = new Server(port);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);
        context.addServlet(new ServletHolder(new Hi()), "/*");
        context.addServlet(new ServletHolder(new WebHookCallback()), "/callback");
        context.addServlet(new ServletHolder(new OauthServlet()), "/oauth");
        context.addServlet(new ServletHolder(new TakeActiveServlet()), "/takeactive");
        try {
            server.start();
            server.join();
        } catch (Exception io) {
            io.printStackTrace();
        }
    }
}
