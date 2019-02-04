package herbott.retrofit;

import herbott.retrofit.api.TwitchHelixAPI;
import herbott.retrofit.api.TwitchOauth2API;
import herbott.retrofit.api.VkApi;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiManager {
    private static final String BASE_HELIX_URL = "https://api.twitch.tv/helix/";
    private static final String BASE_OAUTH2_URL = "https://id.twitch.tv/oauth2/";
    private static final String BASE_VK_API_URL = "https://api.vk.com/method/";

    private static ApiManager manager = new ApiManager();

    private TwitchHelixAPI helixApi = null;
    private TwitchOauth2API oauth2Api = null;
    private VkApi vkApi = null;

    private ApiManager() {
    }

    public static ApiManager getApiManager() {
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

    public VkApi getVkApi() {
        if (vkApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_VK_API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            vkApi = retrofit.create(VkApi.class);
        }
        return vkApi;
    }
}
