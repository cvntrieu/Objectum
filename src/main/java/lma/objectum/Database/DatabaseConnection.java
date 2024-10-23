
package lma.objectum.Database;

import java.sql.*;

public class DatabaseConnection {
    public Connection databaseLink;

    /**
     * Establishes a connection to the database.
     *
     * @return Connection
     */
    public Connection getConnection() {
        String databaseName = "objectum";
        String databaseUser = "root";
        String databasePassword = "13082005";
        String url = "jdbc:mysql://localhost/" + databaseName;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            databaseLink = DriverManager.getConnection(url, databaseUser, databasePassword);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return databaseLink;
    }
}
