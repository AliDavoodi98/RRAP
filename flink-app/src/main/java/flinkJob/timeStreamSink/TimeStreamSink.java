package flinkJob.timeStreamSink;

import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.apache.flink.configuration.Configuration;
import flinkJob.model.Trend;
import software.amazon.awssdk.services.timestreamwrite.TimestreamWriteClient;
import software.amazon.awssdk.services.timestreamwrite.model.*;

public class TimeStreamSink extends RichSinkFunction<Trend> {
    private transient TimestreamWriteClient writeClient;

    @Override
    public void open(Configuration parameters) throws Exception {
        // Initialize the Timestream client
        this.writeClient = TimestreamWriteClient.create();
    }

    @Override
    public void invoke(Trend value, Context context) throws Exception {
        // Convert Trend object to a Timestream record
        WriteRecordsRequest.Builder writeRecordsRequestBuilder = WriteRecordsRequest.builder()
                .databaseName("rrapDB") // Replace with your actual database name
                .tableName("postAnalyzer"); // Replace with your actual table name

        // Create a record
        Record record = Record.builder()
                .measureName("upvotes")
                .measureValue(String.valueOf(value.getUpvotes()))
                .measureValueType("BIGINT")
                .time(String.valueOf(value.getTimestamp()))
                .timeUnit("MILLISECONDS") // Specify the time unit, e.g., SECONDS, MILLISECONDS, etc.
                .dimensions(Dimension.builder().name("title").value(value.getTitle()).build())
                .build();

        // Add record to the request
        writeRecordsRequestBuilder.records(record);

        // Write the record to Timestream
        writeClient.writeRecords(writeRecordsRequestBuilder.build());
    }

    @Override
    public void close() throws Exception {
        // Close the Timestream client
        if (writeClient != null) {
            writeClient.close();
        }
    }
}
