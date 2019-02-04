package herbott.retrofit.api;

import herbott.retrofit.model.VkOauthResponseModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface VkOauth2Api {

    @GET("access_token")
    Call<VkOauthResponseModel> getAccessTokenFromCode(@Query("client_id") String clientId, @Query("client_secret") String secret,
                                                      @Query("redirect_url") String rUrl, @Query("code") String code);
}