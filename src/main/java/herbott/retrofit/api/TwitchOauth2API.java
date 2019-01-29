package herbott.retrofit.api;

import herbott.retrofit.model.UserAccessTokenJsonModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.Map;

public interface TwitchOauth2API {
    @POST("token?grant_type=client_credentials")
    Call<ResponseBody> getAppAccessToken(@Query("client_id") String id, @Query("client_secret") String secret);

    @POST("token?grant_type=authorization_code")
    Call<UserAccessTokenJsonModel> getUserAccessToken(@Query("client_id") String clientId, @Query("client_secret") String secret,
                                                      @Query("code") String code, @Query("redirect_uri") String uri);

    @FormUrlEncoded
    @POST("token")
    Call<UserAccessTokenJsonModel> refreshUserAccessToken(@FieldMap Map<String, String> params);
}