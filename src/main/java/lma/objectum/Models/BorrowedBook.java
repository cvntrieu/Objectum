package lma.objectum.Models;

public class BorrowedBook {
    private String title;
    private String author;
    private String dueDate;

    public BorrowedBook(String title, String author, String dueDate) {
        this.title = title;
        this.author = author;
        this.dueDate = dueDate;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getDueDate() {
        return dueDate;
    }
}
