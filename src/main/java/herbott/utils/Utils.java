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

    public static /*boolean*/void refreshToken(String nick) {
        /*String refreshToken = Statistics.getStats().getRefreshToken(nick);
        if (!refreshToken.equalsIgnoreCase("")) {
            System.out.println(refreshToken);
            try {
                Map<String, String> params = new HashMap<>();
                params.put("grant_type", "refresh_token");
                params.put("refresh_token", refreshToken);
                params.put("client_id", Main.CLIENT_ID);
                params.put("client_secret", Main.CLIENT_SECRET);

                Response<RefreshToken> response = ApiManager.getApiManager()
                        .getOauth2Api().refreshUserAccessToken(params).execute();

                if (response.isSuccessful()) {
                    System.out.println("response successful");
                    RefreshToken json = response.body();
                    if (json != null) {
                        Statistics.getStats().addUserAccessToken(nick, json.accessToken, json.refreshToken);
                        System.out.println("Token refreshed.");
                    } else {
                        System.out.println("response is null");
                    }
                } else {
                    System.out.println("response not successful");
                }
                return true;
            } catch (IOException i) {
                i.printStackTrace();
            }
        }
        return false;*/
        try {
            System.out.println("start refresh command");
            String refreshToken = Statistics.getStats().getRefreshToken(nick);
            System.out.println("refresh token = " + refreshToken);
            Map<String, String> params = new HashMap<>();
            params.put("grant_type", "refresh_token");
            params.put("refresh_token", refreshToken);
            params.put("client_id", Main.CLIENT_ID);
            params.put("client_secret", Main.CLIENT_SECRET);
            ApiManager.getApiManager().getOauth2Api().refreshUserAccessToken(params).enqueue(new Callback<RefreshToken>() {
                @Override
                public void onResponse(Call<RefreshToken> call, Response<RefreshToken> response) {
                    System.out.println("response: "+response.code()+" "+response.message());
                    if (response.isSuccessful()) {
                        System.out.println("Refresh successful.");
                        if (response.body() != null) {
                            Statistics.getStats().addUserAccessToken(nick, response.body().accessToken, response.body().refreshToken);
                            Main.BEARER = "Bearer " + response.body().accessToken;
                            System.out.println("body not null :D");
                        } else {
                            System.out.println("Body is null :(");
                        }
                    } else {
                        System.out.println("Refresh failed.");
                    }
                }

                @Override
                public void onFailure(Call<RefreshToken> call, Throwable t) {
                    System.out.println("Failure to connect twitch.tv (refresh)");
                }
            });
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