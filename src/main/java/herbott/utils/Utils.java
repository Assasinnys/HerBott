package herbott.utils;

import herbott.Main;
import okhttp3.ResponseBody;
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
}
