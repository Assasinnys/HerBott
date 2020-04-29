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

        @Override
        public String toString() {
            return new StringBuilder("from_id = ").append(fromId).append("\n")
                    .append("from_name = ").append(fromName).append("\n")
                    .append("to_id = ").append(toId).append("\n")
                    .append("to_name = ").append(toName).append("\n")
                    .append("followed_at = ").append(followedAt).append("\n").toString();
        }
    }

    public static class Pagination {
        public String cursor;
    }
}
