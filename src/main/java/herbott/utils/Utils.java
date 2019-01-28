package herbott.utils;

import herbott.Main;
import herbott.Statistics;
import herbott.retrofit.ApiManager;
import herbott.retrofit.model.RefreshTokenJsonModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.HashMap;
import java.util.Map;

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
        Response<ResponseBody> response = getApiManager()
                .getHelixApi()
                .subStreamNotice(params)
                .execute();
        System.out.println("response = " + response.code() + " " + response.message());
    }

    public static void activeBot() {
        System.out.println("activeBot()");
        Main.isActive = true;
        if (wakeUpTimer == null) {
            System.out.println("active null");
            startWakeUpTimer();
        }
        else if (!wakeUpTimer.isAlive()) {
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

    public static boolean refreshToken(String nick) {
        String refreshToken = Statistics.getStats().getRefreshToken(nick);
        if (!refreshToken.equalsIgnoreCase("")) {
            System.out.println(refreshToken);
            ApiManager.getApiManager().getOauth2Api().refreshUserAccessToken(refreshToken, Main.CLIENT_ID, Main.CLIENT_SECRET)
                    .enqueue(new Callback<RefreshTokenJsonModel>() {
                        @Override
                        public void onResponse(Call<RefreshTokenJsonModel> call, Response<RefreshTokenJsonModel> response) {
                            if (response.isSuccessful()) {
                                RefreshTokenJsonModel json = response.body();
                                if (json != null) {
                                    Statistics.getStats().addUserAccessToken(nick, json.accessToken, json.refreshToken);
                                    System.out.println("Token refreshed.");
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<RefreshTokenJsonModel> call, Throwable t) {
                            System.out.println("Error due refreshing access token.");
                        }
                    });
            return true;
        } else {
            return false;
        }
    }
}
