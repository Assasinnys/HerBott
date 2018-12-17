package other;

import herbott.Main;
import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Test {
    public static void main(String[] args) throws Exception {
        sendSubscribeRequest();
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
}
