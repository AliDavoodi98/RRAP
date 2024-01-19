import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

import java.util.Properties;

public class KafkaFlinkJob {
    
    public static void main(String[] args) throws Exception {
        // Set up the execution environment
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // Configure Kafka consumer
        Properties props = new Properties();
        props.setProperty("bootstrap.servers", "localhost:9092");
        props.setProperty("group.id", "flink-group");

        // Set up the Kafka consumer
        FlinkKafkaConsumer<String> consumer = new FlinkKafkaConsumer<>(
                "worldnews", new SimpleStringSchema(), props);

        // Set the consumer to start from the earliest offset
        consumer.setStartFromEarliest();

        // Add Kafka consumer as a source to the execution environment
        DataStream<String> stream = env.addSource(consumer);

        // Process the data (simple transformation in this example)
        stream.map(value -> "Streamed message: " + value)
                .print();

        // Execute the Flink job
        env.execute("Flink Kafka Job");
    }
}
