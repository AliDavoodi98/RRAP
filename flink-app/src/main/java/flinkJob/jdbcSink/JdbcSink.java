package flinkJob.jdbcSink;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

import flinkJob.model.Trend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.Date;

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

        Class.forName("com.mysql.jdbc.Driver");

        String dbUser = dotenv.get("DB_USER");
        String dbPassword = dotenv.get("DB_PASSWORD");
        String trendDB = dotenv.get("DB_NAME");
        String dbInstance = dotenv.get("DB_INSTANCE");

        // Initialize the connection to the database
        String url = "jdbc:mysql://" + dbInstance + ":3306/" + trendDB;

        connection = DriverManager.getConnection(url, dbUser, dbPassword);
        insertStatement = connection
                .prepareStatement("INSERT INTO trends2 (title, upvotes, timestamp) VALUES (?, ?, ?)");
    }

    @Override
    public void invoke(Trend value, Context context) throws Exception {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC")); // Ensure it is in UTC time zone

        // Convert the timestamp from milliseconds to a formatted date string
        String formattedTimestamp = formatter.format(new Date(value.getTimestamp()));

        // Set parameters based on the trend object
        insertStatement.setString(1, value.getTitle());
        insertStatement.setInt(2, value.getUpvotes());
        insertStatement.setString(3, formattedTimestamp);

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
