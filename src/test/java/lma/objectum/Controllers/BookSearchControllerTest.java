package lma.objectum.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.TextField;
import lma.objectum.Models.Book;
import lma.objectum.Utils.AuthorSearchStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;

class BookSearchControllerTest extends ApplicationTest {

    private BookSearchController controller;
    private ObservableList<Book> bookList;

    @BeforeEach
    public void setUp() {
        controller = new BookSearchController();

        controller.setTableView(new TableView<>());
        controller.setKeyWordTextField(new TextField());
        controller.setDynamicIsland(new AnchorPane());
        controller.setTitleLabel(new Label());
        controller.setAuthorLabel(new Label());
        controller.setRatingLabel(new Label());
        controller.setSearchCriteriaComboBox(new ComboBox<>());

        bookList = FXCollections.observableArrayList(
                new Book("123", 1234567890123L, "Effective Java", "Joshua Bloch", 4.9, "2018", "Addison-Wesley", "image1"),
                new Book("456", 1234567890124L, "Clean Code", "Robert C. Martin", 4.7, "2008", "Prentice Hall", "image2")
        );
        controller.setBookList(bookList);
    }

    @Test
    public void testInitialize() {
        assertNotNull(controller.getTableView());
        assertNotNull(controller.getKeyWordTextField());
    }

    @Test
    public void testHandleTableRowClick() {
        controller.getTableView().getSelectionModel().select(bookList.get(0));
        Book selectedBook = controller.getTableView().getSelectionModel().getSelectedItem();

        if (selectedBook != null) {
            controller.getTitleLabel().setText("Title: " + selectedBook.getTitle());
            controller.getAuthorLabel().setText("Author: " + selectedBook.getAuthors());
            controller.getRatingLabel().setText("Rating: " + selectedBook.getRating());
            controller.getDynamicIsland().setVisible(true);
        }

        assertEquals("Title: Effective Java", controller.getTitleLabel().getText());
        assertEquals("Author: Joshua Bloch", controller.getAuthorLabel().getText());
        assertEquals("Rating: 4.9", controller.getRatingLabel().getText());
        assertTrue(controller.getDynamicIsland().isVisible());
    }

    @Test
    public void testSearchCriteriaComboBoxChangesStrategy() {
        controller.getSearchCriteriaComboBox().getItems().addAll("Title", "Author", "ISBN", "Year");

        controller.getSearchCriteriaComboBox().getSelectionModel().select("Author");
        controller.updateSearchStrategy();

        assertTrue(controller.getSearchContext().getStrategy() instanceof AuthorSearchStrategy);
    }

    @Test
    public void testEmptySelectionUpdatesUI() {
        controller.getTableView().getSelectionModel().clearSelection();
        assertEquals("", controller.getTitleLabel().getText());
        assertEquals("", controller.getAuthorLabel().getText());
        assertEquals("", controller.getRatingLabel().getText());
        assertTrue(controller.getDynamicIsland().isVisible());
    }
}
