package herbott.webserver.servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class OauthServlet extends HttpServlet {

    private static final String CODE = "code";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Oauth GET request accepted!");
        Map<String, String[]> params = req.getParameterMap();
        System.out.println("Code = " + params.get(CODE)[0]);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Oauth POST request accepted!");
    }
}
