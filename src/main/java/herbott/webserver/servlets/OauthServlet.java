package herbott.webserver.servlets;

import herbott.Main;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class OauthServlet extends HttpServlet {

    private static final String CODE = "code";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Oauth GET request accepted!");
//        Map<String, String[]> params = req.getParameterMap();

        System.out.println("Request PARTS = " + req.getParts().toString());

//        if (params.containsKey(CODE)) {
//            String code = params.get(CODE)[0];
//            System.out.println("Code = " + code);
//            requestUserToken(code);
//        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Oauth POST request accepted!");
    }

    private void requestUserToken(String code) {
        System.out.println("Start to request access token...");
        String url = String.format("https://id.twitch.tv/oauth2/token" +
                "?client_id=%s&client_secret=%s&code=%s&grant_type=authorization_code" +
                "&redirect_uri=https://herbott.herokuapp.com/oauth", Main.CLIENT_ID, Main.CLIENT_SECRET,
                code);
    }
}
