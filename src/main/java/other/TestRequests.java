package other;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

public class TestRequests {
    public static void main(String[] args) {
        BufferedReader reader = null;
        int userID = 227791191;
        String urlString = "https://api.twitch.tv/helix/users/follows?from_id=" + userID;
        String url2 = "https://api.twitch.tv/helix/users?login=assasinnys";
        try {
            URL url = new URL(url2);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            //conn.setRequestProperty("Client-ID", "qkgjxy0g275eedgwehwby8irxfrxm1");
            conn.setRequestProperty("Authorization", "Bearer oauth:ey9jn438m1cxaw9dencqqnx5jorq49");
            System.out.println("code: " + conn.getResponseCode());
            System.out.println(conn.getResponseMessage());
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), Charset.forName("UTF-8")));
            StringBuilder buffer = new StringBuilder();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);
            System.out.println(buffer);

        } catch (IOException e) {
            //e.printStackTrace();
            System.out.println("Exception");
        }
        finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}