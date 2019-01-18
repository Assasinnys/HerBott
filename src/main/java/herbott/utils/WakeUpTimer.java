package herbott.utils;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import static herbott.Main.isActive;

public class WakeUpTimer extends Thread {

    private static final String url = "https://herbott.herokuapp.com/takeactive";

    @Override
    public void run() {
        do {
            try {
                TimeUnit.MINUTES.sleep(5);
                makeWakeUpRequest();
            } catch (InterruptedException interrupt) {
                System.out.println("Wake up timer interrupted.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } while (isActive);
    }

    private void makeWakeUpRequest() throws Exception {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");
        System.out.println(connection.getResponseMessage() + " wake up!");
        connection.disconnect();
    }
}
