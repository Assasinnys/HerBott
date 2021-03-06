package herbott.utils;

import herbott.Main;
import herbott.Statistics;
import herbott.retrofit.ApiManager;
import herbott.retrofit.model.RefreshToken;
import herbott.retrofit.model.UserAccessTokenJsonModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static herbott.retrofit.ApiManager.getApiManager;
import static herbott.Main.wakeUpTimer;

public class Utils {
    public static void sendSubscribeRequest() throws Exception {
        System.out.println("send sub request");
        Map<String, String> params = new HashMap<>();
        params.put("hub.callback", "https://herbott.herokuapp.com/callback");
        params.put("hub.mode", "subscribe");
        params.put("hub.topic", "https://api.twitch.tv/helix/streams?user_id=" + Main.CHANNEL_ID);
        params.put("hub.lease_seconds", "864000");
        try {
            Response<ResponseBody> response = getApiManager()
                    .getHelixApi()
                    .subStreamNotice(Main.BEARER, params)
                    .execute();
            System.out.println("response = " + response.code() + " " + response.message());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("request new bearer token");
            refreshToken(Main.CHANNEL);
            sendSubscribeRequest();
        }

    }

    public static void activeBot() {
        System.out.println("activeBot()");
        Main.isActive = true;
        if (wakeUpTimer == null) {
            System.out.println("active null");
            startWakeUpTimer();
        } else if (!wakeUpTimer.isAlive()) {
            System.out.println("(active isAlive()) wake up timer online = " + wakeUpTimer.isAlive());
            startWakeUpTimer();
        }
    }

    public static void inactiveBot() {
        System.out.println("inactiveBot()");
        if (!Main.isActive) {
            return;
        }
        Main.isActive = false;
        if (wakeUpTimer != null) {
            if (wakeUpTimer.isAlive()) {
                System.out.println("(inactive) wake up timer online = " + wakeUpTimer.isAlive());
                wakeUpTimer.interrupt();
            }
        }
    }

    private static void startWakeUpTimer() {
        wakeUpTimer = new WakeUpTimer();
        wakeUpTimer.start();
    }

    public static void refreshToken(String nick) {
        try {
            System.out.println("start refresh command");
            String refreshToken = Statistics.getStats().getRefreshToken(nick);
            System.out.println("refresh token = " + refreshToken);
            Map<String, String> params = new HashMap<>();
            params.put("grant_type", "refresh_token");
            params.put("refresh_token", refreshToken);
            params.put("client_id", Main.CLIENT_ID);
            params.put("client_secret", Main.CLIENT_SECRET);

            Response<RefreshToken> tokenResponse = getApiManager().getOauth2Api().refreshUserAccessToken(params).execute();
            System.out.println("response: "+tokenResponse.code()+" "+tokenResponse.message());

            if (tokenResponse.isSuccessful()) {
                System.out.println("Refresh successful.");
                if (tokenResponse.body() != null) {
                    Statistics.getStats().addUserAccessToken(nick, tokenResponse.body().accessToken, tokenResponse.body().refreshToken);
                    Main.BEARER = "Bearer " + tokenResponse.body().accessToken;
                    System.out.println("body not null :D");
                } else {
                    System.out.println("Body is null :(");
                }
            } else {
                System.out.println("Refresh failed.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean createWallPost(String message) {
        System.out.println("start post");
        try {
            Response<ResponseBody> response = ApiManager.getApiManager().getVkApi().postStreamNotice(Main.VK_GROUP_ID, 1,
                    message).execute();
            System.out.println("wall.post code = " + response.code());
            if (response.isSuccessful() && response.body() != null) {
                System.out.println("wall.post body = " + Objects.requireNonNull(response.body()).string());
                return true;
            } else {
                System.out.println("wall.post error");
            }
        } catch (IOException io) {
            io.printStackTrace();
            System.out.println("wall.post exception");
        }
        return false;
    }
}