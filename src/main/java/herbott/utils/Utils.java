package herbott.utils;

import herbott.Main;
import okhttp3.ResponseBody;
import retrofit2.Response;

import java.util.HashMap;
import java.util.Map;

import static herbott.retrofit.ApiManager.getApiManager;

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
        Main.isActive = true;
        if (!Main.wakeUpTimer.isAlive()) {
            Main.wakeUpTimer = new WakeUpTimer();
            Main.wakeUpTimer.start();
        }
    }

    public static void disactiveBot() {
        Main.isActive = false;
        if (Main.wakeUpTimer.isAlive()) {
            Main.wakeUpTimer.interrupt();
        }
    }
}
