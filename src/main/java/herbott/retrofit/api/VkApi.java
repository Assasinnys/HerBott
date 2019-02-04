package herbott.retrofit.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface VkApi {

    @GET("wall.post?from_group=1&signed=0&guid=stream")
    Call<ResponseBody> postStreamNotice(@Query("owner_id") int ownerId, @Query("from_group") int fromGroup,
                                        @Query("message") String message);
}
