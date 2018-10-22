package other;

import herbott.Main;
import herbott.utils.TimeParser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestRequests {
    public static void main(String[] args) throws Exception {
        BufferedReader reader = null;
        int userID = 227791191;
//        String urlString = "https://api.twitch.tv/helix/users/follows?from_id=" + userID;
        String url2 = "https://api.twitch.tv/helix/users?login=assasinnys";
        String url3 = "https://api.twitch.tv/helix/users/follows?from_id=" + Main.CREATOR_ID;
        try {
            URL url = new URL(url3);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Client-ID", "qkgjxy0g275eedgwehwby8irxfrxm1");
//            conn.setRequestProperty("Authorization", "Bearer oauth:ey9jn438m1cxaw9dencqqnx5jorq49");
            System.out.println("code: " + conn.getResponseCode());
            System.out.println(conn.getResponseMessage());
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), Charset.forName("UTF-8")));
            StringBuilder buffer = new StringBuilder();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);
            System.out.println(buffer);
            JSONObject json = new JSONObject(buffer.toString());
            JSONArray data = json.getJSONArray("data");
            System.out.println(data.toString());
            String s = "";
            for (int i = 0; i < data.length(); i++) {
                System.out.println(data.getString(i));
                if (data.getJSONObject(i).getString("to_name").equalsIgnoreCase(Main.CHANNEL)) {
                    s = data.getJSONObject(i).getString("followed_at");
                    System.out.println(">>>>>> ХОПА: " + s);
                }
            }
            s = s.replace("T", " ").replace("Z", "");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = sdf.parse(s);
            System.out.println(sdf.format(date));
            System.out.println(TimeParser.parse(System.currentTimeMillis() - date.getTime()));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Exception");
        } /*catch (JSONException j) {
            j.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }*/ finally {
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