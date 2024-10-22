
package lma.objectum.Database;

import java.sql.*;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

public class DatabaseConnection {

    public Connection databaseLink;

    public Connection getConnection() {

        String databaseName = "";
        String databaseUser = "";
        String databasePassword = "";
        String url = "jdbc..." + databaseName;

        try{
            Class.forName("...Driver");
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
        return databaseLink;
    }
}
