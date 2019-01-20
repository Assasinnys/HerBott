package herbott.retrofit.api;

import herbott.Main;
import herbott.retrofit.model.FollowJsonModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.Map;

public interface TwitchHelixAPI {
    @POST("webhooks/hub")
    @Headers({"Content-Type: application/json", "Client-ID: " + Main.CLIENT_ID})
    Call<ResponseBody> subStreamNotice(@Body Map<String, String> params);

    @GET("users/follows")
    @Headers({"Client-ID: " + Main.CLIENT_ID})
    Call<FollowJsonModel> getFollowData(@Query("from_id") String userId);
}
