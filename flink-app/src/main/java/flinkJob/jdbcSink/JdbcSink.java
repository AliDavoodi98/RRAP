package flinkJob.jdbcSink;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

import flinkJob.model.Trend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import io.github.cdimascio.dotenv.Dotenv;

public class JdbcSink extends RichSinkFunction<Trend> {

    private Connection connection;
    private PreparedStatement insertStatement;

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);

        Dotenv dotenv = Dotenv.configure()
                .directory("/home/ubuntu/RRAP/RRAP") // Specify the directory of the .env file
                .filename(".env") // Specify the .env file name if different
                .load();

        String dbUser = dotenv.get("DB_USER"); // Replace with your variable name
        String dbPassword = dotenv.get("DB_PASSWORD"); // Replace with your variable name
        String trendDB = dotenv.get("trendDB"); // Replace with your variable name
        String dbInstance = dotenv.get("my-trend-db-instance"); // Replace with your variable name
        // Now you can use myVar as needed

        // Initialize the connection to the database
        String url = "jdbc:mysql://" + dbInstance + ":3306/" + trendDB;

        connection = DriverManager.getConnection(url, dbUser, dbPassword);
        insertStatement = connection
                .prepareStatement("INSERT INTO trends (title, upvotes, timestamp) VALUES (?, ?, ?)");
    }

    @Override
    public void invoke(Trend value, Context context) throws Exception {
        // Set parameters based on the trend object
        insertStatement.setString(1, value.getTitle());
        insertStatement.setInt(2, value.getUpvotes());
        insertStatement.setTimestamp(3, new java.sql.Timestamp(value.getTimestamp()));

        // Execute the update
        insertStatement.executeUpdate();
    }

    @Override
    public void close() throws Exception {
        // Clean up resources
        try {
            if (insertStatement != null) {
                insertStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (Exception e) {
            // Log exception
        }
        super.close();
    }
}
