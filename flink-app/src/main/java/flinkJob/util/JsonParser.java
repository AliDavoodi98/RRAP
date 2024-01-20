package flinkJob.util;

import org.json.JSONObject;
import flinkJob.model.Post;

public class JsonParser {

    public static Post parseJsonPost(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);

            String title = jsonObject.optString("title");
            String postId = jsonObject.optString("id");
            long timestamp = jsonObject.optLong("created_utc") * 1000; // Assuming it's a Unix timestamp
            int upvotes = jsonObject.optInt("upvotes");

            return new Post(title, postId, timestamp, upvotes);
        } catch (Exception e) {
            System.err.println("Error parsing JSON: " + e.getMessage());
            return null; // or handle the error as appropriate
        }
    }
}
