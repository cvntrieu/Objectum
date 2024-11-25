package lma.objectum.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection databaseLink;

    /**
     * Constructor for DatabaseConnection.
     */
    private DatabaseConnection() {
        String databaseName = "objectum";
        String databaseUser = "root";
        String databasePassword = "13082005";
        String url = "jdbc:mysql://localhost:/" + databaseName;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            databaseLink = DriverManager.getConnection(url, databaseUser, databasePassword);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the singleton instance of DatabaseConnection.
     *
     * @return DatabaseConnection instance
     */
    public static DatabaseConnection getInstance() throws SQLException {
        if (instance == null || instance.databaseLink == null || instance.databaseLink.isClosed()) {
            synchronized (DatabaseConnection.class) {
                if (instance == null || instance.databaseLink == null || instance.databaseLink.isClosed()) {
                    instance = new DatabaseConnection();
                }
            }
        }
        return instance;
    }

    /**
     * Returns the database connection.
     *
     * @return Connection
     */
    public Connection getConnection() {
        return databaseLink;
    }
}