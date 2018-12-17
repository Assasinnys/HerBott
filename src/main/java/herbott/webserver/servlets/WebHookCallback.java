package herbott.webserver.servlets;

import herbott.JSONParser;
import herbott.Main;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class WebHookCallback extends HttpServlet{

    private String challenge = null;
    private static final String HUB_CHALLENGE = "hub.challenge";
    private static final String HUB_MODE = "hub.mode";
    private static final String DATA = "data";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, String[]> params = req.getParameterMap();
        System.out.println("get callback accepted");
        if (params.containsKey(HUB_CHALLENGE)) {
            System.out.println("sub accepted");
            challenge = params.get(HUB_CHALLENGE)[0];
            resp.getOutputStream().print(challenge);
            resp.getOutputStream().flush();
            resp.setStatus(200);
        } else if (params.containsKey(HUB_MODE)) {
            if (params.get(HUB_MODE)[0].equalsIgnoreCase("denied")) {
                System.out.println("sub denied");
                resp.setStatus(200);
            }
        } else {
            try {
                JSONObject object = JSONParser.parseJsonFromStreamNotice(req.getReader());
                JSONArray data = object.getJSONArray(DATA);
                if (data.isNull(0)) {
                    Main.bot.sendIRC().message("#" + Main.CHANNEL, "Stream offline!");
                } else {
                    Main.bot.sendIRC().message("#" + Main.CHANNEL, "Stream started!");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}
