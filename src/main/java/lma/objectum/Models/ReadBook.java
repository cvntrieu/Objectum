package lma.objectum.Models;

public class ReadBook {
    private String title;
    private String author;

    public ReadBook(String title, String author) {
        this.title = title;
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }
}

