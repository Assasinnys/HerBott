package herbott.retrofit.model;

import com.google.gson.annotations.SerializedName;

public class RefreshTokenJsonModel {
    @SerializedName("access_token")
    public String accessToken;
    @SerializedName("refresh_token")
    public String refreshToken;
    public String scope;
}
