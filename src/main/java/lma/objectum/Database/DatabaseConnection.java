
package lma.objectum.Database;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import lma.objectum.Models.BookInAPI;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class DatabaseConnection {

    private static DatabaseConnection instance;
    private Connection databaseLink;

    /**
     * Constructor for DatabaseConnection.
     */
    private DatabaseConnection() {

        String databaseName = "test_db";
        String databaseUser = "root";
        String databasePassword = "trieu50023080tt";
        String url = "jdbc:mysql://localhost:3306/" + databaseName;

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

        if (instance == null || instance.databaseLink == null || isClosed(instance.databaseLink)) {
            synchronized (DatabaseConnection.class) {
                if (instance == null || instance.databaseLink == null || isClosed(instance.databaseLink)) {
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
        if (databaseLink == null || isClosed(instance.databaseLink)) {
            throw new IllegalStateException("Database connection is not initialized or closed");
        }
        return databaseLink;
    }

    /**
     * Saves a book to the database.
     *
     * @param title Title of the book
     * @param authors Authors of the book
     * @param publisher Publisher of the book
     * @param publishedDate Published date of the book
     * @param pageCount Number of pages in the book
     * @param categories Categories of the book
     * @param language Language of the book
     * @param averageRating Average rating of the book
     * @param ratingsCount Number of ratings
     * @param printType Print type of the book
     * @param previewLink Preview link of the book
     * @param description Description of the book
     * @param coverImage Cover image of the book
     */
    public void saveBook(String title, String authors, String publisher, String publishedDate, int pageCount,
                         String categories, String language, double averageRating, int ratingsCount,
                         String printType, String previewLink, String description, byte[] coverImage) {

        String insertQuery = "INSERT INTO booksInAPI (title, authors, publisher, publishedDate, pageCount, categories, language, " +
                "averageRating, ratingsCount, printType, previewLink, description, coverImage) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = databaseLink.prepareStatement(insertQuery)) {

            stmt.setString(1, title);
            stmt.setString(2, authors);
            stmt.setString(3, publisher);
            stmt.setString(4, publishedDate);
            stmt.setInt(5, pageCount);
            stmt.setString(6, categories);
            stmt.setString(7, language);
            stmt.setDouble(8, averageRating);
            stmt.setInt(9, ratingsCount);
            stmt.setString(10, printType);
            stmt.setString(11, previewLink);
            stmt.setString(12, description);
            stmt.setBytes(13, coverImage);
            stmt.executeUpdate();
            // logger.info("Book saved to database: {}", title);

        } catch (SQLIntegrityConstraintViolationException e) {
            // logger.warn("Duplicate entry detected for book: {}", title, e);
            showAlert("Database Error", "Duplicate entry for book: " + title);
        } catch (SQLException e) {
           // logger.error("Error while saving book to database", e);
            showAlert("Database Error", "Unable to save book to database. Error details: " + e.getMessage());
        }
    }

    /**
     * Deletes old books from the database, keeping only the specified number of recent books.
     *
     * @param limit Number of latest books to keep
     */
    public void deleteOldBooks(int limit) {

        String deleteQuery = "DELETE FROM booksInAPI WHERE id NOT IN (SELECT id FROM booksInAPI ORDER BY id DESC LIMIT ?)";
        try (PreparedStatement stmt = databaseLink.prepareStatement(deleteQuery)) {
            stmt.setInt(1, limit);
            int rowsAffected = stmt.executeUpdate();
            // logger.info("Old books deleted, rows affected: {}", rowsAffected);
        } catch (SQLException e) {
            // logger.error("Error while deleting old books", e);
        }
    }

    /**
     * Fetches the latest books from the database.
     *
     * @param limit Number of latest books to retrieve
     * @return List of BookInAPI objects
     */
    public List<BookInAPI> getLatestBooks(int limit) {

        List<BookInAPI> books = new ArrayList<>();
        String selectQuery = "SELECT title, authors, publisher, coverImage FROM booksInAPI ORDER BY id DESC LIMIT ?";
        try (PreparedStatement stmt = databaseLink.prepareStatement(selectQuery)) {
            stmt.setInt(1, limit);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String title = rs.getString("title");
                    String authors = rs.getString("authors");
                    String publisher = rs.getString("publisher");
                    byte[] coverImage = rs.getBytes("coverImage");
                    books.add(new BookInAPI(title, authors, publisher, coverImage));
                }
            }
        } catch (SQLException e) {
          //  logger.error("Error while fetching latest books", e);
        }
        return books;
    }

    /**
     * Displays an alert dialog with a given title and content.
     *
     * @param title Title of the alert
     * @param content Content of the alert
     */
    private void showAlert(String title, String content) {

        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(content);
            alert.showAndWait();
        });
    }

    /**
     * Checks if the database connection is closed.
     *
     * @param connection Connection to be checked
     * @return true if the connection is closed, false otherwise
     */
    private static boolean isClosed(Connection connection) {
        
        try {
            return connection == null || connection.isClosed();
        } catch (SQLException e) {
            return true;
        }
    }
}

