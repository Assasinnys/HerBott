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
    Call<ResponseBody> subStreamNotice(@Header("Authorization") String bearer, @Body Map<String, String> params);

    @GET("users/follows")
    @Headers({"Client-ID: " + Main.CLIENT_ID})
    Call<FollowJsonModel> getFollowData(@Header("Authorization") String bearer,  @Query("from_id") String userId, @Query("first") int numOfObjects);

    @GET("streams")
    @Headers({"Client-ID: " + Main.CLIENT_ID})
    Call<ResponseBody> getStreamInfo(@Query("user_id") String userId);
}
