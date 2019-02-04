package herbott.retrofit.model;

import com.google.gson.annotations.SerializedName;

public class VkOauthResponseModel {
    @SerializedName("access_token")
    public String accessToken;
    @SerializedName("expires_in")
    public int expiresIn;
    @SerializedName("user_id")
    public int userId;
}
