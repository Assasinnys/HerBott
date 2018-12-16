package other;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class Test {
    public static void main(String[] args) throws Exception {
        HttpURLConnection connection = (HttpURLConnection) new URL("https://herbott.herokuapp.com/callback").openConnection();
        System.out.println(connection.getResponseCode());
    }
}
