package flinkJob.model;

public class Post {
    private String title;
    private String postId;
    private long timestamp;
    private int upvotes;

    // Constructor
    public Post(String title, String postId, long timestamp, int upvotes) {
        this.title = title;
        this.postId = postId;
        this.timestamp = timestamp;
        this.upvotes = upvotes;
    }

    // Getters and setters for each field
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(int upvotes) {
        this.upvotes = upvotes;
    }

    @Override
    public String toString() {
        return "Post{" +
                "title='" + title + '\'' +
                ", postId='" + postId + '\'' +
                ", timestamp=" + timestamp +
                ", upvotes=" + upvotes +
                '}';
    }
}
