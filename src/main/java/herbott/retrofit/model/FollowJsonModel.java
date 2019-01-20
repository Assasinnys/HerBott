package herbott.retrofit.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FollowJsonModel {
    public int total;
    public List<Data> data;
    public Pagination pagination;

    public static class Data {
        @SerializedName("from_id")
        public int fromId;
        @SerializedName("from_name")
        public String fromName;
        @SerializedName("to_id")
        public int toId;
        @SerializedName("to_name")
        public String toName;
        @SerializedName("followed_at")
        public String followedAt;
    }

    public static class Pagination {
        public String cursor;
    }
}
