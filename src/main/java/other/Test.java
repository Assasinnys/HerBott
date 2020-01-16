package other;

import herbott.JsonUtils;
import herbott.Main;
import herbott.retrofit.ApiManager;
import herbott.retrofit.api.VkOauth2Api;
import herbott.retrofit.model.VkOauthResponseModel;
import okhttp3.*;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.*;
import retrofit2.http.Headers;

import java.io.*;
import java.net.*;
import java.util.*;

public class Test {

    public static void main(String[] args) throws Exception {
        System.out.println("START");
//        change2();
//        changeServer();
//        change3();
//        chatSocket();
        System.out.println("DONE!");
    }

    private static void getStreamInfo() throws Exception {
        Response<ResponseBody> response = ApiManager.getApiManager().getHelixApi().getStreamInfo(Main.CHANNEL_ID).execute();
        System.out.println("response code = " + response.code());
        if (response.body() != null && response.isSuccessful()) {
            System.out.println("response body = " + Objects.requireNonNull(response.body()).string());
        } else {
            System.out.println("Error");
        }
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

    private static String getUserId(String nickname) throws Exception {
        String url = "https://api.twitch.tv/helix/users?login=" + nickname;
        JSONObject json = new JSONObject(JsonUtils.readUrlAuth(url));
        return json.getJSONArray("data").getJSONObject(0).getString("id");
    }

    private static void change3() throws Exception {
//        URL url = new URL("http://109.248.202.101:7688/ServerAdmin/current/chat+frame+data");
        URL url = new URL("http://80.77.175.107:181/ServerAdmin/current/chat+frame+data?ajax=1");

        String encoding = Base64.getEncoder().encodeToString(("admin:157947").getBytes());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//        conn.setRequestMethod("GET");
        conn.setDoOutput(true);
        conn.setRequestProperty("Authorization", "Basic " + encoding);
//        conn.getOutputStream().write("ajax=1".getBytes());
//        conn.getOutputStream().flush();
        System.out.println(conn.getResponseCode() + " " + conn.getResponseMessage());
        InputStream content = conn.getInputStream();
        BufferedReader in =
                new BufferedReader(new InputStreamReader(content));
        String line;

        while (true) {
            System.out.println(in.readLine());
            System.out.println("---");
            Thread.sleep(1000);
        }

//        while ((line = in.readLine()) != null) {
//            System.out.println(line);
//        }
    }

    private static void chatSocket() throws Exception {
        Socket socket = new Socket(InetAddress.getByName("http://109.248.202.101/ServerAdmin/current/chat+frame+data"), 7688);
        Scanner scanner = new Scanner(socket.getInputStream());
        while (true) {
            System.out.println("Line: " + scanner.nextLine());
        }
    }

    interface Api {
        @POST("token?grant_type=client_credentials")
        Call<ResponseBody> getAppToken(@Query("client_id") String id, @Query("client_secret") String secret);
    }

    /*&map=KF-Airship&GameLength=1&Difficulty=1&MaxPlayers=6&game=KFGameContent.KFGameInfo_Survival*/

    private static void changeServer() {
        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://109.248.202.101:7688/ServerAdmin/current/")
                .baseUrl("http://80.77.175.107:181/ServerAdmin/current/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        KillingFloor2 kf = retrofit.create(KillingFloor2.class);
        try {
            Response<ResponseBody> res = kf.change(login, "KFGameContent.KFGameInfo_Survival",
                    "KF-Airship", "0", "?GameLength=1?Difficulty=1?MaxPlayers=6?multihome=80.77.175.107", "change").execute();
            System.out.println(res.headers().toString());
            System.out.println("response_code = " + res.code() + " " + res.message());

//            Response<ResponseBody> res = kf.get(login, "1").execute();
//
//            System.out.println(res.headers().toString());
//            System.out.println(res.raw());
//            System.out.println("response_code = " + res.code() + " " + res.message());
////            System.out.println(res.body().bytes().toString());
//            System.out.println(res.body().string());
        } catch (IOException e) {
            e.getMessage();
        }
    }

    private static final String login = "Basic " + Base64.getEncoder().encodeToString(("admin:157947").getBytes());

    interface KillingFloor2 {
        @POST("change")
        @Headers("Content-type: application/x-www-form-urlencoded")
        Call<ResponseBody> change(@Header("Authorization") String head, @Query("gametype") String gameType, @Query("map") String map,
                                  @Query("mutatorGroupCount") String count, @Query("urlextra") String extra, @Query("action") String change);

        @POST("chat+frame+data")
//        @Headers("Content-type: application/x-www-form-urlencoded")
        Call<ResponseBody> getChat(@Header("Authorization") String head, @Query("ajax") String value, @Query("message") String message,
                                   @Query("teamsay") String id);

        @POST("chat+frame+data")
        @Headers({"Content-type: application/x-www-form-urlencoded"})
        Call<ResponseBody> get(@Header("Authorization") String head, @Query("ajax") String value);
    }
}
