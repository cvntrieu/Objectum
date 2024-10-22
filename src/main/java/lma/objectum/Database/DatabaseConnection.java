package lma.objectum.Database;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {

    public Connection databaseLink;

    public Connection getConnection() {

        String databaseName = "";
        String databaseUser = "";
        String databasePassword = "";
        String url = "";

        try {

            databaseLink = DriverManager.getConnection(url, databaseUser, databasePassword);

        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
        return databaseLink;
    }
}
