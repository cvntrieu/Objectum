package lma.objectum.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import lma.objectum.Models.BookInAPI;

import java.io.ByteArrayInputStream;

/**
 * Controller for the book cell in the list view.
 * This class is responsible for displaying information about a book, such as the title, authors, publisher, and cover image.
 */
public class BookCellController {

    @FXML
    private ImageView coverImageView;

    @FXML
    private Label titleLabel;

    @FXML
    private Label authorsLabel;

    @FXML
    private Label publisherLabel;

    /**
     * Sets the book information for the cell.
     * Updates the title, authors, publisher, and cover image in the UI.
     *
     * @param book The BookInAPI instance containing information about the book.
     */
    public void setBook(BookInAPI book) {
        titleLabel.setText(book.getTitle());
        authorsLabel.setText(book.getAuthors());
        publisherLabel.setText(book.getPublisher());

        if (book.getCoverImage() != null) {
            Image image = new Image(new ByteArrayInputStream(book.getCoverImage()));
            coverImageView.setImage(image);
        }
    }
}
