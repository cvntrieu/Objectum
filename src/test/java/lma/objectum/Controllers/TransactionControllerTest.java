package lma.objectum.Controllers;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import lma.objectum.Models.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.testfx.framework.junit5.ApplicationTest;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(org.testfx.framework.junit5.ApplicationExtension.class)
class TransactionControllerTest extends ApplicationTest {

    private TransactionController transactionController;

    @BeforeEach
    void setUp() {
        transactionController = new TransactionController();

        // Mock JavaFX components
        transactionController.setISBN13_borrow(new TextField());
        transactionController.setISBN13_return(new TextField());
        transactionController.setBorrowButton(new Button());
        transactionController.setReturnButton(new Button());
        transactionController.setQrImageView(new ImageView());
    }

    @Test
    void testPrefillData() {
        long isbn13 = 1234567890123L;
        transactionController.prefillData(isbn13);
        assertEquals(String.valueOf(isbn13), transactionController.getISBN13_borrow().getText());
    }

    @Test
    void testBorrowBook_withEmptyISBN13_shouldShowError() {
        Platform.runLater(() -> {
            transactionController.getISBN13_borrow().setText("");
            TransactionController spyController = Mockito.spy(transactionController);
            spyController.borrowBook();
            verify(spyController).showCustomAlert(eq("Error"),
                    eq("Please enter a valid ISBN13."), eq(false));
        });

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testBorrowBook_withInvalidISBN13_shouldShowError() {
        Platform.runLater(() -> {
            transactionController.getISBN13_borrow().setText("INVALID_ISBN");
            TransactionController spyController = Mockito.spy(transactionController);
            spyController.borrowBook();
            verify(spyController).showCustomAlert(eq("Error"),
                    eq("Invalid ISBN13 format."), eq(false));
        });

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testScanQRCode_withValidBook_shouldGenerateQRCode() throws Exception {
        Book mockBook = new Book();
        mockBook.setIsbn("123-4567890123");
        mockBook.setIsbn_13(Long.valueOf("1234567890123"));
        mockBook.setTitle("Test Book");
        mockBook.setAuthors("Test Author");
        mockBook.setRating(4.5);
        mockBook.setDate("2023-01-01");
        mockBook.setPublisher("Test Publisher");

        TransactionController spyController = Mockito.spy(transactionController);
        ImageView mockImageView = spyController.getQrImageView();
        spyController.setQrImageView(mockImageView);
        spyController.scanQRCode(mockBook);
        assertNotNull(spyController.getQrImageView().getImage());
    }

    @Test
    void testReturnBook_withEmptyISBN13_shouldShowError() {
        Platform.runLater(() -> {
            transactionController.getISBN13_return().setText("");
            TransactionController spyController = Mockito.spy(transactionController);
            spyController.returnBook();
            verify(spyController).showCustomAlert(eq("Error"),
                    eq("Please enter a valid ISBN13."), eq(false));
        });

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testReturnBook_withInvalidISBN13_shouldShowError() {
        Platform.runLater(() -> {
            transactionController.getISBN13_return().setText("INVALID_ISBN");
            TransactionController spyController = Mockito.spy(transactionController);
            spyController.returnBook();
            verify(spyController).showCustomAlert(eq("Error"),
                    eq("Invalid ISBN13 format."), eq(false));
        });

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testReturnBook_withValidISBN13_shouldSucceed() throws Exception {
        Platform.runLater(() -> {
            transactionController.getISBN13_return().setText("1234567890123");
            SessionManager sessionManager = Mockito.mock(SessionManager.class);
            when(sessionManager.getCurrentUserId()).thenReturn(1);

            TransactionController spyController = Mockito.spy(transactionController);
            try {
                doReturn(1).when(spyController).getTransactionId(anyLong(), anyInt());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                doReturn(new java.sql.Date(System.currentTimeMillis())).when(spyController).getDueDate(anyInt());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            spyController.returnBook();

            verify(spyController).showCustomAlert(eq("Success"), anyString(), eq(true));
        });

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
