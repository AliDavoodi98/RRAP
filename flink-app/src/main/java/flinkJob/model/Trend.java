package flinkJob.model;

public class Trend {
    private String title;
    private String postId;
    private long timestamp;
    private int upvotes;
    private double upvoteRate; // Upvotes per unit time, could be per minute or hour

    public Trend(String title, String postId, long timestamp, int upvotes, double upvoteRate) {
        this.title = title;
        this.postId = postId;
        this.timestamp = timestamp;
        this.upvotes = upvotes;
        this.upvoteRate = upvoteRate;
    }

    // Getters and setters
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

    public double getUpvoteRate() {
        return upvoteRate;
    }

    public void setUpvoteRate(double upvoteRate) {
        this.upvoteRate = upvoteRate;
    }

    @Override
    public String toString() {
        return "Trend{" +
                "title='" + title + '\'' +
                ", postId='" + postId + '\'' +
                ", timestamp=" + timestamp +
                ", upvotes=" + upvotes +
                ", upvoteRate=" + upvoteRate +
                '}';
    }
}
