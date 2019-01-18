package herbott.retrofit.api;

import herbott.Main;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

import java.util.Map;

public interface TwitchHelixAPI {
    @POST("webhooks/hub")
    @Headers({"Content-Type: application/json", "Client-ID: " + Main.CLIENT_ID})
    Call<ResponseBody> subStreamNotice(@Body Map<String, String> params);
}
