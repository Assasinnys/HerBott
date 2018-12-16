package herbott.webserver.servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class WebHookCallback extends HttpServlet{

    public String challenge = null;
    private static final String HUB_CHALLENGE = "hub.challenge";
    private static final String HUB_MODE = "hub.mode";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, String[]> params = req.getParameterMap();
        if (params.containsKey(HUB_CHALLENGE)) {
            System.out.println("sub accepted");
            challenge = params.get(HUB_CHALLENGE)[0];
            resp.getOutputStream().print(challenge);
            resp.getOutputStream().flush();
            resp.setStatus(200);
        } else if (params.containsKey(HUB_MODE)) {
            if (params.get(HUB_MODE)[0].equalsIgnoreCase("denied")) {
                System.out.println("sub denied");
                resp.setStatus(200, "OK");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}
