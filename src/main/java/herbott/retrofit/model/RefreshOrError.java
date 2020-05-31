package herbott.retrofit.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RefreshOrError extends Error {
    @SerializedName("access_token")
    public String accessToken;
    @SerializedName("refresh_token")
    public String refreshToken;
    public List<String> scope;
}
