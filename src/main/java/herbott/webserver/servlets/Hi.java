package herbott.webserver.servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Hi extends HttpServlet {

    private String index = "<!DOCTYPE html>\n" +
            "<html>\n" +
            "<meta charset=\"UTF-8\">" +
            "  <head>\n" +
            "    <title>Заголовок</title>\n" +
            "  </head>\n" +
            "  <body>\n" +
            "    <h1> ААААААААААААААААААААААААБ</h1>\n" +
            "  </body>\n" +
            "</html>";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("GET request accepted! ☺");
        resp.getOutputStream().print(index);
        resp.getOutputStream().flush();
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}