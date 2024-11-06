package lma.objectum.Database;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection databaseLink;

    /**
     * Constructor for DatabaseConnection.
     */
    private DatabaseConnection() {
        String databaseName = "test_db";
        String databaseUser = "root";
        String databasePassword = "080305";
        String url = "jdbc:mysql://localhost:3307/" + databaseName;

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
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            synchronized (DatabaseConnection.class) {
                if (instance == null) {
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
