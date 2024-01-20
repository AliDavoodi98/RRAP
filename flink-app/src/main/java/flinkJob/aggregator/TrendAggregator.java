package flinkJob.aggregator;

import flinkJob.model.Trend;

import java.util.Map.Entry;

import org.apache.flink.api.common.functions.AggregateFunction;

import flinkJob.aggregator.TrendAccumulator.PostStats;
import flinkJob.model.Post;

public class TrendAggregator implements AggregateFunction<Post, TrendAccumulator, Trend> {

    @Override
    public TrendAccumulator createAccumulator() {
        return new TrendAccumulator();
    }

    @Override
    public TrendAccumulator add(Post value, TrendAccumulator accumulator) {
        // Update the statistics for the post in the accumulator
        TrendAccumulator.PostStats stats = accumulator.getPostStatsMap().getOrDefault(value.getPostId(),
                new TrendAccumulator.PostStats());
        stats.setTitle(value.getTitle());
        stats.setLastUpvoteCount(value.getUpvotes());
        stats.setLastTimestamp(value.getTimestamp());
        accumulator.getPostStatsMap().put(value.getPostId(), stats);
        return accumulator;
    }

    @Override
    public Trend getResult(TrendAccumulator accumulator) {
        // Logic to determine if there is a trend
        for (Entry<String, PostStats> entry : accumulator.getPostStatsMap().entrySet()) {
            String postId = entry.getKey();
            TrendAccumulator.PostStats stats = entry.getValue();

            // For simplicity, let's say a post is trending if it has more than a threshold
            // of upvotes
            if (stats.getLastUpvoteCount() > getTrendThreshold()) {
                return new Trend(
                        stats.getTitle(),
                        postId,
                        stats.getLastTimestamp(),
                        stats.getLastUpvoteCount(),
                        calculateUpvoteRate(stats));
            }
        }
        return null; // No trend detected
    }

    @Override
    public TrendAccumulator merge(TrendAccumulator a, TrendAccumulator b) {
        // Merge the statistics from two accumulators
        for (Entry<String, PostStats> entry : b.getPostStatsMap().entrySet()) {
            a.getPostStatsMap().merge(entry.getKey(), entry.getValue(), this::mergePostStats);
        }
        return a;
    }

    private TrendAccumulator.PostStats mergePostStats(TrendAccumulator.PostStats a, TrendAccumulator.PostStats b) {
        // Logic to merge two PostStats objects. For example, take the latest upvote
        // count and timestamp
        if (a.getLastTimestamp() > b.getLastTimestamp()) {
            return a;
        } else {
            return b;
        }
    }

    private int getTrendThreshold() {
        // Define your threshold for what constitutes a "trend"
        return 1000; // Example threshold
    }

    private double calculateUpvoteRate(TrendAccumulator.PostStats stats) {
        // Implement your logic to calculate the rate of upvotes
        // For example, upvotes per minute/hour since the post was created
        // This is a placeholder implementation
        return (double) stats.getLastUpvoteCount()
                / ((System.currentTimeMillis() - stats.getLastTimestamp()) / 60000.0);
    }
}
