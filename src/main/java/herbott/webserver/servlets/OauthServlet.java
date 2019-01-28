package herbott.webserver.servlets;

import herbott.Main;
import herbott.Statistics;
import herbott.retrofit.ApiManager;
import herbott.retrofit.model.UserAccessTokenJsonModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class OauthServlet extends HttpServlet {

    private static final String CODE = "code";
    private static final String REDIRECT_URI = "https://herbott.herokuapp.com/oauth";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Oauth GET request accepted! Code accepted.");
        Map<String, String[]> params = req.getParameterMap();

        if (params.containsKey(CODE)) {
            String code = params.get(CODE)[0];
            System.out.println("Code = " + code);
            requestUserToken(code);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Oauth POST request accepted!");
    }

    private void requestUserToken(String code) {
        System.out.println("Start to request access token...");
        ApiManager.getApiManager().getOauth2Api().getUserAccessToken(Main.CLIENT_ID, Main.CLIENT_SECRET, code, REDIRECT_URI)
                .enqueue(new Callback<UserAccessTokenJsonModel>() {
                    @Override
                    public void onResponse(Call<UserAccessTokenJsonModel> call, Response<UserAccessTokenJsonModel> response) {
                        if (response.isSuccessful()) {
                            UserAccessTokenJsonModel json = response.body();
                            if (json != null) {
                                Statistics.getStats().addUserAccessToken(Main.CHANNEL, json.accessToken, json.refreshToken);
                            } else {
                                System.out.println("Error due reading json response.");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserAccessTokenJsonModel> call, Throwable t) {
                        System.out.println("Failure to connect to twitch.tv.");
                    }
                });
    }
}
