package herbott.retrofit.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserAccessTokenJsonModel {
    @SerializedName("access_token")
    public String accessToken;
    @SerializedName("refresh_token")
    public String refreshToken;
    @SerializedName("expires_in")
    public long expire;
    @SerializedName("token_type")
    public String tokenType;
    public List<String> scope;
}
