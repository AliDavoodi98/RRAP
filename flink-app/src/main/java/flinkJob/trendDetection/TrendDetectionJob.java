package flinkJob.trendDetection;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
//import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
//import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.util.Collector;

import java.net.Socket;
import java.util.Properties;
import flinkJob.aggregator.TrendAggregator;
import flinkJob.jdbcSink.JdbcSink;
import flinkJob.model.Post;
import flinkJob.model.Trend;
import flinkJob.timeStreamSink.TimeStreamSink;
import flinkJob.util.JsonParser;

public class TrendDetectionJob {

    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // Set up the Kafka source

        KafkaSource<String> consumer = KafkaSource.<String>builder()
                .setBootstrapServers("localhost:9092")
                .setTopics("worldnews")
                .setGroupId("flink-reddit-consumer")
                .setStartingOffsets(OffsetsInitializer.earliest())
                .setValueOnlyDeserializer(new SimpleStringSchema())
                .build();

        // Define the data processing pipeline
        DataStream<String> stream = env.fromSource(consumer,
                WatermarkStrategy.noWatermarks(), "Kafka Source");

        DataStream<Trend> trends = stream
                .map(json -> JsonParser.parseJsonPost(json)) // Parse JSON to Post objects
                .keyBy(Post::getPostId)
                .window(TumblingProcessingTimeWindows.of(Time.minutes(5)))
                .aggregate(new TrendAggregator())
                .flatMap(new FlatMapFunction<Trend, Trend>() {
                    @Override
                    public void flatMap(Trend trend, Collector<Trend> out) throws Exception {
                        if (trend.getUpvotes() > 100) {
                            out.collect(trend);
                        }
                    }
                });

        // Print the detected trends
        trends.print();
        trends.addSink(new JdbcSink());

        // Execute the Flink job
        env.execute("Worldnews Subreddit Trend Detection");
    }

}
