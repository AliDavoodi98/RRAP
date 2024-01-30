package flinkJob.aggregator;

import java.util.HashMap;
import java.util.Map;
import flinkJob.model.Trend;
import flinkJob.model.Post;

public class TrendAccumulator {
    // Map to keep track of the count and last timestamp of upvotes for each post
    private Map<String, PostStats> postStatsMap;

    public TrendAccumulator() {
        this.postStatsMap = new HashMap<>();
    }

    public Map<String, PostStats> getPostStatsMap() {
        return postStatsMap;
    }

    public void add(Post post) {
        // Update the stats for the given post
        PostStats stats = postStatsMap.getOrDefault(post.getPostId(), new PostStats());
        stats.setLastUpvoteCount(post.getUpvotes());
        stats.setLastTimestamp(post.getTimestamp());
        postStatsMap.put(post.getPostId(), stats);
    }

    public Trend detectTrend(long windowStartTime) {
        // Logic to determine if there is a trend
        for (Map.Entry<String, PostStats> entry : postStatsMap.entrySet()) {
            PostStats stats = entry.getValue();
            double upvoteRate = calculateUpvoteRate(stats, windowStartTime);

            // If the upvote rate is above a certain threshold, we consider it a trend
            if (upvoteRate > getTrendThreshold()) {
                return new Trend(
                        stats.getTitle(),
                        entry.getKey(),
                        stats.getLastTimestamp(),
                        stats.getLastUpvoteCount(),
                        upvoteRate);
            }
        }
        return null; // No trend detected
    }

    private double calculateUpvoteRate(PostStats stats, long windowStartTime) {
        long timeDiff = stats.getLastTimestamp() - windowStartTime;
        if (timeDiff <= 0) {
            return 0;
        }
        // Assuming 'timeDiff' is in seconds and we want the rate per minute
        double ratePerMinute = (double) stats.getLastUpvoteCount() / (timeDiff / 60.0);
        return ratePerMinute;
    }

    private double getTrendThreshold() {
        // Define your threshold for what constitutes a "trend"
        return 10.0; // Example threshold
    }

    // Helper class to store stats for a post
    public static class PostStats {
        private String title;
        private int lastUpvoteCount;
        private long lastTimestamp;

        public PostStats() {
            // Initialize your variables here
        }

        // Getters and Setters
        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getLastUpvoteCount() {
            return lastUpvoteCount;
        }

        public void setLastUpvoteCount(int lastUpvoteCount) {
            this.lastUpvoteCount = lastUpvoteCount;
        }

        public long getLastTimestamp() {
            return lastTimestamp;
        }

        public void setLastTimestamp(long lastTimestamp) {
            this.lastTimestamp = lastTimestamp;
        }

    }
}
