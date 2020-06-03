package herbott.webserver.servlets;

import herbott.JsonUtils;
import herbott.Main;
import herbott.utils.Utils;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class StreamNoticeCallback extends HttpServlet{

    private static final int HTTP_OK = 200;
    private static final int HTTP_BAD_REQUEST = 400;
    private static final String HUB_CHALLENGE = "hub.challenge";
    private static final String HUB_MODE = "hub.mode";
    private static final String DATA = "data";

    // verify subscription stream notice
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, String[]> params = req.getParameterMap();
        System.out.println("GET request accepted! /StreamNoticeCallback accepted");

        if (params.containsKey(HUB_MODE)) {
            String hubMode = params.get(HUB_MODE)[0];
            if (hubMode.equalsIgnoreCase("denied")) {
                System.out.println("sub denied");
                resp.setStatus(HTTP_OK);
            } else if (hubMode.equalsIgnoreCase("subscribe")) {
                if (params.containsKey(HUB_CHALLENGE)) {
                    System.out.println("sub accepted");
                    String challenge = params.get(HUB_CHALLENGE)[0];
                    resp.getOutputStream().print(challenge);
                    resp.getOutputStream().flush();
                    resp.setStatus(HTTP_OK);
                } else {
                    resp.setStatus(HTTP_BAD_REQUEST);
                }
            }
        } else {
            System.out.println("request not supported!");
        }
    }

    // getting stream notice webhooks
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("POST request accepted! /StreamNoticeCallback post");
        try {
            JSONObject object = JsonUtils.parseJsonFromStreamNotice(req.getReader());
            JSONArray data = object.getJSONArray(DATA);
            if (data.isNull(0)) {
                System.out.println("Stream offline!");
                Main.bot.sendIRC().message("#" + Main.CHANNEL, "Stream offline!");
                Utils.inactiveBot();
                Main.bot.close();
            } else {
                System.out.println("Stream online!");
                Main.bot.sendIRC().message("#" + Main.CHANNEL, "Stream online!");
                Utils.activeBot();
//                String title = data.getJSONObject(0).getString("title");
//                Utils.createWallPost("Стрим начался.\nНазвание: " + title + "\n Ссылка на стрим: https://www.twitch.tv/roblife42");
                try {
                    Utils.refreshToken(Main.CHANNEL);
                    Utils.sendSubscribeRequest();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            resp.setStatus(HTTP_OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
