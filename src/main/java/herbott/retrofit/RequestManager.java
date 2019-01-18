package herbott.retrofit;

import herbott.retrofit.api.TwitchHelixAPI;
import herbott.retrofit.api.TwitchOauth2API;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RequestManager {
    private static final String BASE_HELIX_URL = "https://api.twitch.tv/helix/";
    private static final String BASE_OAUTH2_URL = "https://id.twitch.tv/oauth2/";

    private static RequestManager manager = new RequestManager();

    private TwitchHelixAPI helixApi = null;
    private TwitchOauth2API oauth2Api = null;

    private RequestManager() {
    }

    public static RequestManager getRequestManager() {
        return manager;
    }

    public TwitchHelixAPI getHelixApi() {
        if (helixApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_HELIX_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            helixApi = retrofit.create(TwitchHelixAPI.class);
        }
        return helixApi;
    }

    public TwitchOauth2API getOauth2Api() {
        if (oauth2Api == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_OAUTH2_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            oauth2Api = retrofit.create(TwitchOauth2API.class);
        }
        return oauth2Api;
    }
}
