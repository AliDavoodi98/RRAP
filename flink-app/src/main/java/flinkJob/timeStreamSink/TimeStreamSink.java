package flinkJob.timeStreamSink;

import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.apache.flink.configuration.Configuration;
import flinkJob.model.Trend;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.timestreamwrite.TimestreamWriteClient;
import software.amazon.awssdk.services.timestreamwrite.model.*;

public class TimeStreamSink extends RichSinkFunction<Trend> {
    private transient TimestreamWriteClient writeClient;

    @Override
    public void open(Configuration parameters) throws Exception {
        // Initialize the Timestream client
        this.writeClient = TimestreamWriteClient.builder()
                .credentialsProvider(DefaultCredentialsProvider.builder().build())
                .region(Region.of("us-east-2")) // e.g., Region.US_EAST_1
                .build();
        try {
            ListTablesResponse listTablesResponse = writeClient.listTables(ListTablesRequest.builder()
                    .databaseName("rrapDB")
                    .build());

            // Log the response or tables for testing purposes
            System.out.println("Tables: " + listTablesResponse.tables());
        } catch (TimestreamWriteException e) {
            System.err.println("Error listing Timestream tables: " + e.awsErrorDetails().errorMessage());
            throw e;
        }
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
        // writeClient.writeRecords(writeRecordsRequestBuilder.build());
        try {
            writeClient.writeRecords(writeRecordsRequestBuilder.build());
        } catch (RejectedRecordsException e) {
            System.out.println("Rejected records: " + e.rejectedRecords());
            // Optionally, print more details about each rejected record
            for (RejectedRecord rejectedRecord : e.rejectedRecords()) {
                System.out.println("Record index: " + rejectedRecord.recordIndex());
                System.out.println("Reason: " + rejectedRecord.reason());
            }
            throw e; // Rethrow the exception if you want to handle it further up the stack
        } catch (TimestreamWriteException e) {
            // Handle other Timestream write exceptions
            System.err.println("Error writing to Timestream: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void close() throws Exception {
        // Close the Timestream client
        if (writeClient != null) {
            writeClient.close();
        }
    }
}
