package other;

import herbott.Main;
import herbott.retrofit.api.VkOauth2Api;
import herbott.retrofit.model.VkOauthResponseModel;
import okhttp3.ResponseBody;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Test {

    public static void main(String[] args) throws Exception {
        System.out.println("START");
        getVkAccessToken();
    }

    private static void getVkAccessToken() throws Exception {
        Scanner scanner = new Scanner(System.in);
        String code = scanner.nextLine();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://oauth.vk.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Response<VkOauthResponseModel> bodyR = retrofit.create(VkOauth2Api.class)
                .getAccessTokenFromCode("6844607", "1jvwRyq8LoYQchLq26nm", "https://herbott.herokuapp.com", code).execute();
        System.out.println(bodyR.code());
        VkOauthResponseModel body = bodyR.body();

        System.out.println("Access token = " + body.accessToken + "\n" + "expires in = " + body.expiresIn);
    }

    private static void sendSubscribeRequest() throws Exception {
        System.out.println("send sub request");
        HttpURLConnection connection = (HttpURLConnection) new URL("https://api.twitch.tv/helix/webhooks/hub").openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Client-ID", Main.CLIENT_ID);
        connection.setDoOutput(true);
        OutputStream outputStream = connection.getOutputStream();
        Map<String, String> params = new HashMap<>();
        params.put("hub.callback", "https://herbott.herokuapp.com/callback");
        params.put("hub.mode", "subscribe");
        params.put("hub.topic", "https://api.twitch.tv/helix/streams?user_id=" + Main.CHANNEL_ID);
        params.put("hub.lease_seconds", "864000");
        JSONObject object = new JSONObject(params);
        System.out.println("json = " + object.toString());
        outputStream.write(object.toString().getBytes("UTF-8"));
        outputStream.flush();
        System.out.println("response = " + connection.getResponseCode());
        connection.disconnect();
    }

    private static void sendReq(String url) throws Exception {
        System.out.println("Starting send req ...");
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("POST");
        System.out.println("Result: " + connection.getResponseCode() + " " + connection.getResponseMessage());
        connection.disconnect();
    }

    private static void oauth() throws Exception {
        String url = "https://id.twitch.tv/oauth2/authorize?client_id=" + Main.CLIENT_ID +
                "&redirect_uri=http://localhost&scope=viewing_activity_read&response_type=code";
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");
        System.out.println("Result: " + connection.getResponseCode() + " " + connection.getResponseMessage());
    }

    private static void oauthVsev() throws Exception {
        System.out.println("Trying to connect ...");
        String url = "https://api.lufthansa.com/v1/oauth/token";
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setDoOutput(true);
        String params = "client_id=dz7zd4bxtquxnpw7f67hma8u&client_secret=R6BSbEu4tu&grant_type=client_credentials";
        connection.getOutputStream().write(params.getBytes("UTF-8"));
        connection.getOutputStream().flush();
        System.out.println("Response = " + connection.getResponseCode() + " " + connection.getResponseMessage());
        StringBuilder json = new StringBuilder();
        int i;
        char[] b = new char[1024];
        InputStreamReader reader = new InputStreamReader(connection.getInputStream());
        while ((i = reader.read(b)) != -1) {
            json.append(b, 0, i);
        }
        connection.disconnect();
        System.out.println("json = " + json.toString());
    }

    private static void appAccessToken() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://id.twitch.tv/oauth2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);
        try {
            Response<ResponseBody> res = api.getAppToken(Main.CLIENT_ID, Main.CLIENT_SECRET).execute();
            if (res.isSuccessful()) {
                System.out.println("successful");
                System.out.println(res.body().string());
            } else {
                System.out.println("failure " + res.errorBody().string());
            }
        } catch (IOException t) {
            t.printStackTrace();
        }
    }

    interface Api {
        @POST("token?grant_type=client_credentials")
        Call<ResponseBody> getAppToken(@Query("client_id") String id, @Query("client_secret") String secret);
    }
}
