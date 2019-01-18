package herbott.retrofit.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface TwitchOauth2API {
    @POST("token?grant_type=client_credentials")
    Call<ResponseBody> getAppToken(@Query("client_id") String id, @Query("client_secret") String secret);
}
