package herbott.retrofit.api;

import herbott.Main;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface VkApi {

    // guid=stream&

    @GET("wall.post?from_group=1&signed=0&v=5.92&access_token=" + Main.VK_ACCESS_TOKEN)
    Call<ResponseBody> postStreamNotice(@Query("owner_id") int ownerId, @Query("from_group") int fromGroup,
                                        @Query("message") String message);
}
