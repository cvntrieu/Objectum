package lma.objectum.Models;

public class Book {
    private String isbn;
    private Long isbn_13;
    private String title;
    private String authors;
    private Double rating;
    private String date;
    private String publisher;
    private String imageUrl;

    // Constructors, getters and setters

    /**
     * Constructors.
     *
     * @param isbn isbn
     * @param isbn_13 isbn13
     * @param title title
     * @param authors authors
     * @param rating rating
     * @param date date
     * @param publisher publisher
     * @param imageUrl imageUrl
     */
    public Book(String isbn, Long isbn_13, String title, String authors,
                Double rating, String date, String publisher, String imageUrl) {

        this.isbn = isbn;
        this.isbn_13 = isbn_13;
        this.title = title;
        this.authors = authors;
        this.rating = rating;
        this.date = date;
        this.publisher = publisher;
        this.imageUrl = imageUrl;
    }

    /**
     * Default constructor.
     */
    public Book() {

    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Long getIsbn_13() {
        return isbn_13;
    }

    public void setIsbn_13(Long isbn_13) {
        this.isbn_13 = isbn_13;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
